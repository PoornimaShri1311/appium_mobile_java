package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.AppiumServerManager;
import utils.DriverUtils;
import utils.ExtentManager;
import utils.FrameworkConfig;
import utils.ScreenshotUtils;

import java.lang.reflect.Method;

public class BaseTest {

    protected ExtentReports extent;
    protected ExtentTest test;

    @BeforeSuite
    public void beforeSuite() {
        // Initialize ExtentReports (idempotent - safe to call multiple times)
        extent = ExtentManager.getExtentReports();
        
        // Start Appium server if auto-start is enabled (idempotent check)
        try {
            // Check if server is already running before attempting to start
            if (!AppiumServerManager.isServerRunning()) {
                AppiumServerManager.startAppiumServer();
            } else {
                System.out.println("Appium server is already running, skipping startup");
            }
        } catch (Exception e) {
            System.err.println("Failed to start Appium server: " + e.getMessage());
            // Try to recover by checking if server is actually running
            if (AppiumServerManager.isServerRunning()) {
                System.out.println("Server appears to be running despite startup error, continuing...");
            } else {
                throw new RuntimeException("Cannot proceed with tests - Appium server startup failed", e);
            }
        }
    }

    @BeforeMethod
    public void setUp(Method method) {
        // Idempotent driver initialization - safe to call multiple times
        try {
            DriverUtils.initializeDriver();
            test = extent.createTest(method.getName());
        } catch (Exception e) {
            // If driver initialization fails, try to clean up and retry once
            try {
                DriverUtils.quitDriver();
                Thread.sleep(1000); // Brief pause for cleanup
                DriverUtils.initializeDriver();
                test = extent.createTest(method.getName());
            } catch (Exception retryException) {
                throw new RuntimeException("Failed to initialize driver after retry", retryException);
            }
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        try {
            // Only process results if driver and test are available
            if (DriverUtils.getDriver() != null && test != null) {
                if (result.getStatus() == ITestResult.FAILURE) {
                    test.fail(result.getThrowable());
                    if (FrameworkConfig.isScreenshotOnFailure()) {
                        String screenshotPath = ScreenshotUtils.captureScreenshot(DriverUtils.getDriver(), result.getName());
                        test.addScreenCaptureFromPath(screenshotPath);
                    }
                } else if (result.getStatus() == ITestResult.SUCCESS) {
                    test.pass("Test passed");
                    if (FrameworkConfig.isScreenshotOnSuccess()) {
                        String screenshotPath = ScreenshotUtils.captureScreenshot(DriverUtils.getDriver(), result.getName());
                        test.addScreenCaptureFromPath(screenshotPath);
                    }
                } else {
                    test.skip("Test skipped");
                    String screenshotPath = ScreenshotUtils.captureScreenshot(DriverUtils.getDriver(), result.getName());
                    test.addScreenCaptureFromPath(screenshotPath);
                }
            }
        } catch (Exception e) {
            System.err.println("Error during test result processing: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Idempotent driver cleanup - safe to call even if driver is null
            try {
                DriverUtils.quitDriver();
            } catch (Exception e) {
                System.err.println("Error during driver cleanup: " + e.getMessage());
            }
        }
    }


    @AfterSuite
    public void afterSuite() {
        // Flush ExtentReports
        extent.flush();
        
        // Stop Appium server if it was started by the framework
        try {
            AppiumServerManager.stopAppiumServer();
        } catch (Exception e) {
            System.err.println("Error stopping Appium server: " + e.getMessage());
        }
    }

}
