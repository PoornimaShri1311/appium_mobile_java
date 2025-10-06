package com.company.framework.base;

import com.aventstack.extentreports.ExtentTest;
import com.company.framework.interfaces.reporting.IReportingManager;
import com.company.framework.managers.AppLifecycleManager;
import com.company.framework.managers.DependencyManager;
import com.company.framework.managers.ExtentReportingManager;
import com.company.framework.utils.TestReportingUtils;
import io.appium.java_client.AppiumDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class BaseTestMobile {
    protected static final Logger logger = LogManager.getLogger(BaseTestMobile.class);
    protected AppiumDriver driver;
    private static IReportingManager reportingManager;
    private static String getTimestampedReportPath() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        return "reports/ExtentReport_" + timestamp + ".html";
    }
    
    /**
     * Get the current report path with timestamp for external reference
     */
    public static String getCurrentReportPath() {
        return getTimestampedReportPath();
    }
    protected ExtentTest test;

    protected AppLifecycleManager appLifecycle;

    @BeforeSuite
    public void setupSuite() {
        logger.info("🚀 Starting Mobile Test Suite...");
        String reportPath = getTimestampedReportPath();
        logger.info("📊 ExtentReport will be saved to: {}", reportPath);
        reportingManager = new ExtentReportingManager();
        reportingManager.initializeReport(reportPath);
    }

    @BeforeClass
    public void setupClass() {
        driver = DependencyManager.getInstance().getDriverManager().initializeAndGetDriver();
        
        // Get app configuration for lifecycle management
        String appPackage = DependencyManager.getInstance().getConfigurationManager()
                .getProperty("appPackage", "com.netbiscuits.bild.android");
        String appActivity = DependencyManager.getInstance().getConfigurationManager()
                .getProperty("appActivity", "de.bild.android.app.MainActivity");
        
        appLifecycle = new AppLifecycleManager(driver, appPackage, appActivity);
        appLifecycle.ensureAppIsRunning();
    }

    @BeforeMethod
    public void setupTest(Method method) {
        String testName = method.getName();
        String description = getTestDescription(method);
        test = reportingManager.createTest(testName, description);
    }

    @AfterMethod
    public void teardownTest(ITestResult result) {
        if (test == null) return;
        String methodName = result.getMethod().getMethodName();

        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                try {
                    // Capture screenshot for passed tests as well
                    String passScreenshot = reportingManager.captureScreenshot(driver, methodName, "PASS");
                    logger.info("Pass screenshot captured: {}", passScreenshot);
                    if (passScreenshot != null) {
                        test.addScreenCaptureFromPath(passScreenshot);
                        logger.info("Pass screenshot attached to report: {}", passScreenshot);
                    }
                } catch (Exception e) {
                    logger.warn("Failed to capture pass screenshot: {}", e.getMessage());
                }
                reportingManager.logPass(test, "✅ Test passed successfully");
                break;

            case ITestResult.FAILURE:
                try {
                    String failScreenshot = reportingManager.captureScreenshot(driver, methodName, "FAILURE");
                    logger.info("Failure screenshot captured: {}", failScreenshot);
                    if (failScreenshot != null) {
                        test.addScreenCaptureFromPath(failScreenshot);
                        logger.info("Failure screenshot attached to report: {}", failScreenshot);
                    } else {
                        // Fallback to base64 screenshot
                        try {
                            String base64Screenshot = TestReportingUtils.captureScreenshotAsBase64(driver);
                            test.addScreenCaptureFromBase64String(base64Screenshot);
                            logger.info("Base64 screenshot attached to report");
                        } catch (Exception e2) {
                            logger.error("Failed to attach base64 screenshot: {}", e2.getMessage());
                        }
                    }
                } catch (Exception e) {
                    logger.error("Failed to capture or attach failure screenshot: {}", e.getMessage());
                }
                reportingManager.logFail(test, "❌ " + result.getThrowable());
                break;

            case ITestResult.SKIP:
                reportingManager.logSkip(test, "⏭️ Test skipped");
                break;
        }
    }
    @AfterClass
    public void teardownClass() {
        DependencyManager.getInstance().getDriverManager().quitDriver();
    }

    @AfterSuite
    public void teardownSuite() {
        reportingManager.flush();
    }
    private String getTestDescription(Method method) {
        Test testAnnotation = method.getAnnotation(Test.class);
        return (testAnnotation != null && !testAnnotation.description().isEmpty())
                ? testAnnotation.description()
                : "Test method: " + method.getName();
    }
    
    /**
     * Log test step with ExtentReports integration
     */
    protected void logTestStep(String message) {
        if (test != null) {
            test.info(message);
        }
        logger.info(message);
    }
    
    /**
     * Assert with logging integration
     */
    protected void assertWithLogging(boolean condition, String message) {
        if (condition) {
            logTestStep("✅ Assertion passed: " + message);
        } else {
            logTestStep("❌ Assertion failed: " + message);
            throw new AssertionError(message);
        }
    }
}
