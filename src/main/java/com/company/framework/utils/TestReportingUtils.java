package com.company.framework.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.company.framework.config.FrameworkConfig;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TestReportingUtils - Consolidated utility for test reporting and screenshots
 * Combines ExtentReports management and screenshot capture functionality
 * 
 * Responsibilities:
 * - ExtentReports setup and configuration
 * - Screenshot capture and management
 * - Report file organization
 */
public class TestReportingUtils {
    
    private static ExtentReports extent;
        
    // ========================================
    // EXTENT REPORTS MANAGEMENT
    // ========================================
    
    /**
     * Get configured ExtentReports instance (singleton)
     */
    public synchronized static ExtentReports getExtentReports() {
        if (extent == null) {
            // Create timestamp for unique report names
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String reportPath = "reports/ExtentReport_" + timestamp + ".html";
            
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle(FrameworkConfig.getFrameworkName() + " - Test Report");
            sparkReporter.config().setReportName("Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            // Add system and framework information to the report
            extent.setSystemInfo("Framework", FrameworkConfig.getFrameworkName());
            extent.setSystemInfo("Framework Version", FrameworkConfig.getFrameworkVersion());
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("OS Version", System.getProperty("os.version"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("User Name", System.getProperty("user.name"));
            extent.setSystemInfo("Execution Date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        return extent;
    }
    
    /**
     * Flush and finalize the ExtentReports
     */
    public static void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }
    
    /**
     * Reset ExtentReports instance (for testing or cleanup)
     */
    public static void resetReports() {
        if (extent != null) {
            extent.flush();
            extent = null;
        }
    }
    
    // ========================================
    // SCREENSHOT MANAGEMENT
    // ========================================
    
    /**
     * Capture screenshot with automatic naming
     */
    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String fileName = screenshotName + "_" + System.currentTimeMillis() + ".png";
        String destPath = "screenshots/" + fileName;
        
        try {
            Files.createDirectories(Paths.get("screenshots")); // ensure folder exists
            Files.copy(src.toPath(), Paths.get(destPath));
            
            // Return relative path that works from reports directory
            return "../" + destPath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Capture screenshot with timestamp
     */
    public static String captureTimestampedScreenshot(WebDriver driver, String testName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return captureScreenshot(driver, testName + "_" + timestamp);
    }
    
    /**
     * Capture screenshot and return as base64 string for embedding in reports
     */
    public static String captureScreenshotAsBase64(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }
    
    /**
     * Capture screenshot and save with custom path
     */
    public static String captureScreenshotToPath(WebDriver driver, String fullPath) {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        
        try {
            // Ensure parent directory exists
            File destFile = new File(fullPath);
            Files.createDirectories(destFile.getParentFile().toPath());
            
            Files.copy(src.toPath(), destFile.toPath());
            return fullPath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // ========================================
    // UTILITY METHODS
    // ========================================
    
    /**
     * Clean up old report files (keep last N reports)
     */
    public static void cleanupOldReports(int keepLastN) {
        try {
            File reportsDir = new File("reports");
            if (!reportsDir.exists()) {
                return;
            }
            
            File[] reportFiles = reportsDir.listFiles((dir, name) -> 
                name.startsWith("ExtentReport_") && name.endsWith(".html"));
                
            if (reportFiles != null && reportFiles.length > keepLastN) {
                // Sort by last modified time (newest first)
                java.util.Arrays.sort(reportFiles, 
                    (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
                    
                // Delete old files
                for (int i = keepLastN; i < reportFiles.length; i++) {
                    reportFiles[i].delete();
                }
            }
        } catch (Exception e) {
            // Silently fail for cleanup operations
        }
    }
    
    /**
     * Clean up old screenshot files (keep last N days)
     */
    public static void cleanupOldScreenshots(int keepLastDays) {
        try {
            File screenshotsDir = new File("screenshots");
            if (!screenshotsDir.exists()) {
                return;
            }
            
            long cutoffTime = System.currentTimeMillis() - (keepLastDays * 24L * 60L * 60L * 1000L);
            
            File[] screenshotFiles = screenshotsDir.listFiles((dir, name) -> 
                name.endsWith(".png") || name.endsWith(".jpg"));
                
            if (screenshotFiles != null) {
                for (File file : screenshotFiles) {
                    if (file.lastModified() < cutoffTime) {
                        file.delete();
                    }
                }
            }
        } catch (Exception e) {
            // Silently fail for cleanup operations
        }
    }
    
    /**
     * Get report directory path
     */
    public static String getReportsDirectory() {
        return "reports";
    }
    
    /**
     * Get screenshots directory path
     */
    public static String getScreenshotsDirectory() {
        return "screenshots";
    }
}
