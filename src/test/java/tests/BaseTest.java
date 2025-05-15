package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import utils.DriverUtils;
import utils.ExtentManager;
import utils.ScreenshotUtils;

import java.io.IOException;
import java.lang.reflect.Method;

public class BaseTest {

    protected ExtentReports extent;
    protected ExtentTest test;

    @BeforeSuite
    public void beforeSuite() {
        extent = ExtentManager.getExtentReports();
    }

    @BeforeMethod
    public void setUp(Method method) {
        DriverUtils.initializeDriver();
        test = extent.createTest(method.getName());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        String screenshotPath = ScreenshotUtils.captureScreenshot(DriverUtils.getDriver(), result.getName());
        if (result.getStatus() == ITestResult.FAILURE) {
            test.fail(result.getThrowable());
            // Take screenshot and attach to Extent Report
            try {
                test.addScreenCaptureFromPath(screenshotPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.addScreenCaptureFromPath(screenshotPath);
            test.pass("Test passed");
        } else {
            test.skip("Test skipped");
        }

        DriverUtils.quitDriver();
    }


    @AfterSuite
    public void afterSuite() {
        extent.flush();
    }

}
