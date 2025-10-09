package com.company.framework.base;

import com.aventstack.extentreports.ExtentTest;
import com.company.framework.interfaces.reporting.IReportingManager;
import com.company.framework.managers.*;
import com.company.framework.utils.MobileDeviceUtils;
import com.company.framework.utils.TestReportingUtils;
import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class BaseTestMobile {

    protected static final Logger logger = LogManager.getLogger(BaseTestMobile.class);
    protected AppiumDriver driver;
    protected ExtentTest test;
    protected AppLifecycleManager appLifecycle;
    private static IReportingManager reportingManager;

    // ---------- Report Path ----------
    private static String timestampedReportPath() {
        return "reports/ExtentReport_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".html";
    }

    public static String getCurrentReportPath() { return timestampedReportPath(); }

    // ---------- Suite Setup ----------
    @BeforeSuite(alwaysRun = true)
    public void setupSuite() {
        String reportPath = timestampedReportPath();
        logger.info("🚀 Starting Mobile Test Suite | Report: {}", reportPath);
        // Appium starts in Github Actions already. So removing this part.
        // MobileDeviceUtils.startAppiumServer();
        // logger.info("📱 Appium server started.");
        reportingManager = new ExtentReportingManager();
        reportingManager.initializeReport(reportPath);
    }

    // ---------- Class Setup ----------
    @BeforeClass(alwaysRun = true)
    public void setupClass() {
        driver = DependencyManager.getInstance().getDriverManager().initializeAndGetDriver();

        var config = DependencyManager.getInstance().getConfigurationManager();
        appLifecycle = new AppLifecycleManager(
                driver,
                config.getProperty("appPackage", "com.netbiscuits.bild.android"),
                config.getProperty("appActivity", "de.bild.android.app.MainActivity")
        );
        appLifecycle.ensureAppIsRunning();
    }

    // ---------- Test Setup ----------
    @BeforeMethod(alwaysRun = true)
    public void setupTest(Method method) {
        String name = method.getName();
        String desc = getTestDescription(method);
        test = reportingManager.createTest(name, desc);
        logger.info("🧩 Starting test: {}", name);
    }

    // ---------- Test Teardown ----------
    @AfterMethod(alwaysRun = true)
    public void teardownTest(ITestResult result) {
        if (test == null) return;

        String method = result.getMethod().getMethodName();
        String status = "";

        try {
            String screenshotPath = reportingManager.captureScreenshot(driver, method,
                    result.getStatus() == ITestResult.SUCCESS ? "PASS" :
                    result.getStatus() == ITestResult.FAILURE ? "FAILURE" : "SKIP");

            if (screenshotPath != null) {
                test.addScreenCaptureFromPath(screenshotPath);
                logger.info("📸 Screenshot attached: {}", screenshotPath);
            } else if (result.getStatus() == ITestResult.FAILURE) {
                // fallback base64 screenshot
                String base64 = TestReportingUtils.captureScreenshotAsBase64(driver);
                test.addScreenCaptureFromBase64String(base64);
            }
        } catch (Exception e) {
            logger.warn("⚠️ Screenshot capture failed: {}", e.getMessage());
        }

        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                reportingManager.logPass(test, "✅ Test passed");
                status = "PASSED";
                break;
            case ITestResult.FAILURE:
                reportingManager.logFail(test, "❌ " + result.getThrowable());
                status = "FAILED";
                break;
            case ITestResult.SKIP:
                reportingManager.logSkip(test, "⏭️ Test skipped");
                status = "SKIPPED";
                break;
        }
        logger.info("🧾 Test {} {}", method, status);
    }

    // ---------- Class & Suite Teardown ----------
    @AfterClass(alwaysRun = true)
    public void teardownClass() {
        DependencyManager.getInstance().getDriverManager().quitDriver();
        logger.info("📱 Driver closed for class {}", getClass().getSimpleName());
    }

    @AfterSuite(alwaysRun = true)
    public void teardownSuite() {
        reportingManager.flush();
        logger.info("📊 Extent report flushed and suite completed.");
        MobileDeviceUtils.stopAppiumServer();
        logger.info("🛑 Appium server stopped.");
    }

    // ---------- Helpers ----------
    private String getTestDescription(Method method) {
        var testAnnotation = method.getAnnotation(org.testng.annotations.Test.class);
        return (testAnnotation != null && !testAnnotation.description().isEmpty())
                ? testAnnotation.description()
                : "Test method: " + method.getName();
    }

    protected void logTestStep(String message) {
        if (test != null) test.info(message);
        logger.info(message);
    }

    protected void assertWithLogging(boolean condition, String message) {
        logTestStep((condition ? "✅ Passed: " : "❌ Failed: ") + message);
        if (!condition) throw new AssertionError(message);
    }
}
