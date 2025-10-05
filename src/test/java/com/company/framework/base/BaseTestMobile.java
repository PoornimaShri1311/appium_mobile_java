package com.company.framework.base;

import com.company.framework.managers.DependencyFactory;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.appium.java_client.AppiumDriver;
import org.testng.annotations.*;

/**
 * BaseTestMobile - Base class for all mobile test classes
 * 
 * Responsibilities:
 * - Driver initialization and cleanup
 * - ExtentReports setup and teardown
 * - Common test setup/teardown logic
 * - Shared utilities for all test classes
 * 
 * Benefits:
 * - Eliminates code duplication across test classes
 * - Centralized driver management
 * - Consistent reporting setup
 * - Easy maintenance and updates
 */
public class BaseTestMobile {

    protected AppiumDriver driver;
    protected ExtentTest test;
    private static ExtentReports extent;
    private static final String REPORT_PATH = "reports/ExtentReport.html";
    
    /**
     * Suite-level setup - Initialize ExtentReports
     */
    @BeforeSuite
    public void setupSuite() {
        System.out.println("🚀 Starting Mobile Test Suite...");
        
        // Initialize ExtentReports
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_PATH);
        sparkReporter.config().setDocumentTitle("BILD Mobile Test Report");
        sparkReporter.config().setReportName("Mobile Automation Results");
        
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Framework", "Appium + TestNG + Java");
        extent.setSystemInfo("Platform", "Android");
        extent.setSystemInfo("App", "BILD Android");
        
        System.out.println("📊 ExtentReports initialized: " + REPORT_PATH);
    }
    
    /**
     * Class-level setup - Initialize driver once per test class
     */
    @BeforeClass
    public void setupClass() {
        try {
            System.out.println("🔧 Initializing Appium driver for: " + this.getClass().getSimpleName());
            
            // Initialize the driver through DependencyFactory
            DependencyFactory.getInstance().getDriverManager().initializeDriver();
            driver = DependencyFactory.getInstance().getDriverManager().getDriver();
            
            System.out.println("✅ Driver initialized successfully for: " + this.getClass().getSimpleName());
            
            // Explicitly verify and launch the BILD app
            ensureBildAppIsLaunched();
            
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize driver: " + e.getMessage());
            throw new RuntimeException("Driver initialization failed", e);
        }
    }
    
    /**
     * Ensure BILD app is actually launched and running
     */
    private void ensureBildAppIsLaunched() {
        try {
            System.out.println("🔍 Verifying BILD app launch...");
            
            // Check current app focus
            String pageSource = driver.getPageSource();
            
            // If BILD app is not detected, explicitly launch it
            if (!pageSource.contains("com.netbiscuits.bild.android")) {
                System.out.println("⚠️ BILD app not detected, launching explicitly...");
                
                // Launch BILD app using ADB command through ProcessBuilder
                ProcessBuilder processBuilder = new ProcessBuilder("adb", "shell", "am", "start", "-n", "com.netbiscuits.bild.android/de.bild.android.app.MainActivity");
                processBuilder.start();
                
                // Wait for app to launch
                Thread.sleep(5000);
                
                // Verify launch
                pageSource = driver.getPageSource();
                if (pageSource.contains("com.netbiscuits.bild.android")) {
                    System.out.println("✅ BILD app launched successfully!");
                } else {
                    System.out.println("⚠️ App launch verification inconclusive, continuing with tests...");
                }
            } else {
                System.out.println("✅ BILD app already running");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ App launch verification failed: " + e.getMessage());
            // Don't fail setup - continue with tests
        }
    }
    
    /**
     * Method-level setup - Create test report entry
     */
    @BeforeMethod
    public void setupTest(java.lang.reflect.Method method) {
        try {
            // Create ExtentTest for current test method
            String testName = method.getName();
            String testDescription = getTestDescription(method);
            
            test = extent.createTest(testName, testDescription);
            test.info("Starting test: " + testName);
            
            // Ensure app is in known state before each test
            waitForPageToLoad();
            
            System.out.println("🧪 Test started: " + testName);
            
        } catch (Exception e) {
            System.err.println("❌ Test setup failed: " + e.getMessage());
            if (test != null) {
                test.fail("Test setup failed: " + e.getMessage());
            }
        }
    }
    
    /**
     * Method-level teardown - Update test status
     */
    @AfterMethod
    public void teardownTest(org.testng.ITestResult result) {
        try {
            if (test != null) {
                switch (result.getStatus()) {
                    case org.testng.ITestResult.SUCCESS:
                        test.pass("Test passed successfully");
                        System.out.println("✅ Test passed: " + result.getMethod().getMethodName());
                        break;
                    case org.testng.ITestResult.FAILURE:
                        test.fail("Test failed: " + result.getThrowable());
                        System.out.println("❌ Test failed: " + result.getMethod().getMethodName());
                        break;
                    case org.testng.ITestResult.SKIP:
                        test.skip("Test skipped: " + result.getThrowable());
                        System.out.println("⏭️ Test skipped: " + result.getMethod().getMethodName());
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Test teardown failed: " + e.getMessage());
        }
    }
    
    /**
     * Class-level teardown - Clean up driver
     */
    @AfterClass
    public void teardownClass() {
        try {
            if (DependencyFactory.getInstance().getDriverManager() != null) {
                DependencyFactory.getInstance().getDriverManager().quitDriver();
                System.out.println("✅ Driver cleanup completed for: " + this.getClass().getSimpleName());
            }
        } catch (Exception e) {
            System.err.println("❌ Error during driver cleanup: " + e.getMessage());
        }
    }
    
    /**
     * Suite-level teardown - Finalize reports
     */
    @AfterSuite
    public void teardownSuite() {
        try {
            if (extent != null) {
                extent.flush();
                System.out.println("📊 Test report generated: " + REPORT_PATH);
            }
            System.out.println("🏁 Mobile Test Suite completed");
        } catch (Exception e) {
            System.err.println("❌ Suite teardown failed: " + e.getMessage());
        }
    }
    
    /**
     * Wait for page to load - delegates to MobileTestUtils
     */
    protected void waitForPageToLoad() {
        try {
            if (driver != null) {
                String pageSource = driver.getPageSource();
                if (pageSource != null && pageSource.length() > 1000) {
                    System.out.println("✅ App content loaded (" + pageSource.length() + " chars)");
                } else {
                    System.out.println("⚠️ Waiting for app content to load...");
                    Thread.sleep(2000);
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Warning during page load check: " + e.getMessage());
        }
    }
    
    /**
     * Get test description from TestNG annotation
     */
    private String getTestDescription(java.lang.reflect.Method method) {
        Test testAnnotation = method.getAnnotation(Test.class);
        if (testAnnotation != null && !testAnnotation.description().isEmpty()) {
            return testAnnotation.description();
        }
        return "Test method: " + method.getName();
    }
    
    /**
     * Helper method for logging test steps
     */
    protected void logTestStep(String stepDescription) {
        System.out.println("🔹 " + stepDescription);
        if (test != null) {
            test.info(stepDescription);
        }
    }
    
    /**
     * Helper method for assertions with logging
     */
    protected void assertWithLogging(boolean condition, String message) {
        try {
            org.testng.Assert.assertTrue(condition, message);
            System.out.println("✅ Assertion passed: " + message);
            if (test != null) {
                test.pass("Assertion passed: " + message);
            }
        } catch (AssertionError e) {
            System.out.println("❌ Assertion failed: " + message);
            if (test != null) {
                test.fail("Assertion failed: " + message);
            }
            throw e;
        }
    }
}