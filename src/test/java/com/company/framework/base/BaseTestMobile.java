package com.company.framework.base;

import com.aventstack.extentreports.ExtentTest;
import com.company.framework.interfaces.IReportingManager;
import com.company.framework.managers.AppLifecycleManager;
import com.company.framework.managers.DependencyManager;
import com.company.framework.managers.ExtentReportingManager;
import io.appium.java_client.AppiumDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public abstract class BaseTestMobile {
    protected AppiumDriver driver;
    private static IReportingManager reportingManager;
    private static final String REPORT_PATH = "reports/ExtentReport.html";
    protected ExtentTest test;

    protected AppLifecycleManager appLifecycle;

    @BeforeSuite
    public void setupSuite() {
        System.out.println("🚀 Starting Mobile Test Suite...");
        reportingManager = new ExtentReportingManager();
        reportingManager.initializeReport(REPORT_PATH);
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
                reportingManager.logPass(test, "✅ Test passed successfully");
                break;

            case ITestResult.FAILURE:
                String failScreenshot = reportingManager.captureScreenshot(driver, methodName, "FAILURE");
                test.addScreenCaptureFromPath(failScreenshot);
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
        System.out.println(message);
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
