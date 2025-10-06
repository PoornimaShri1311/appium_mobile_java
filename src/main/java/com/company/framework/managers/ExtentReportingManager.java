package com.company.framework.managers;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.company.framework.interfaces.reporting.IReportingManager;
import com.company.framework.utils.TestReportingUtils;
import org.openqa.selenium.WebDriver;

public class ExtentReportingManager implements IReportingManager {
    private static ExtentReports extent;
    private ExtentTest currentTest;

    @Override
    public void initializeReport(String reportPath) {
        ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);
        reporter.config().setDocumentTitle("Automation Report");
        reporter.config().setReportName("Execution Report");
        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    @Override
    public ExtentTest createTest(String testName, String description) {
        currentTest = extent.createTest(testName, description);
        return currentTest;
    }

    @Override
    public void logPass(ExtentTest test, String message) {
        if (test != null) {
            test.pass(message);
        }
    }

    @Override
    public void logFail(ExtentTest test, String message) {
        if (test != null) {
            test.fail(message);
        }
    }
    
    /**
     * Log failure with screenshot attachment
     */
    public void logFailWithScreenshot(ExtentTest test, String message, WebDriver driver, String testName) {
        if (test != null) {
            test.fail(message);
            attachScreenshotToTest(test, driver, testName + "_FAILURE");
        }
    }
    
    /**
     * Log pass with optional screenshot attachment
     */
    public void logPassWithScreenshot(ExtentTest test, String message, WebDriver driver, String testName) {
        if (test != null) {
            test.pass(message);
            attachScreenshotToTest(test, driver, testName + "_PASS");
        }
    }
    
    /**
     * Attach screenshot to test using both file path and base64 as fallback
     */
    private void attachScreenshotToTest(ExtentTest test, WebDriver driver, String screenshotName) {
        try {
            // Method 1: Try file path approach
            String screenshotPath = TestReportingUtils.captureScreenshot(driver, screenshotName);
            if (screenshotPath != null) {
                test.addScreenCaptureFromPath(screenshotPath);
                return;
            }
        } catch (Exception e) {
            // Method 2: Fallback to base64 encoding
            try {
                String base64Screenshot = TestReportingUtils.captureScreenshotAsBase64(driver);
                test.addScreenCaptureFromBase64String(base64Screenshot);
            } catch (Exception e2) {
                test.warning("Failed to attach screenshot: " + e2.getMessage());
            }
        }
    }

    @Override
    public void logSkip(ExtentTest test, String message) {
        if (test != null) {
            test.skip(message);
        }
    }

    public void log(ExtentTest test, Status status, String message) {
        switch (status) {
            case PASS:
                logPass(test, message);
                break;
            case FAIL:
                logFail(test, message);
                break;
            case SKIP:
                logSkip(test, message);
                break;
            case INFO:
                if (test != null) {
                    test.info(message);
                }
                break;
            case WARNING:
                if (test != null) {
                    test.warning(message);
                }
                break;
        }
    }

    @Override
    public String captureScreenshot(WebDriver driver, String testName, String status) {
        return TestReportingUtils.captureScreenshot(driver, testName + "_" + status);
    }

    @Override
    public void flush() { if (extent != null) extent.flush(); }
}
