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
        // Initialize ExtentReports
        extent = ExtentManager.getExtentReports();
        
        // Start Appium server if auto-start is enabled
        try {
            AppiumServerManager.startAppiumServer();
        } catch (Exception e) {
            System.err.println("Failed to start Appium server: " + e.getMessage());
            throw new RuntimeException("Cannot proceed with tests - Appium server startup failed", e);
        }
    }

    @BeforeMethod
    public void setUp(Method method) {
        DriverUtils.initializeDriver();
        test = extent.createTest(method.getName());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        DriverUtils.quitDriver();
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
