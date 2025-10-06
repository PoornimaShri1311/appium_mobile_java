package com.company.framework.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.company.framework.interfaces.IReportingManager;
import com.company.framework.utils.TestReportingUtils;
import org.openqa.selenium.WebDriver;

/**
 * ReportingManager - Alternative ExtentReports implementation of IReportingManager
 * This is a backup/alternative implementation to ExtentReportingManager
 */
public class ReportingManager implements IReportingManager {
    private static ExtentReports extent;
    private ExtentTest currentTest;

    @Override
    public void initializeReport(String reportPath) {
        ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);
        reporter.config().setDocumentTitle("Automation Report");
        reporter.config().setReportName("Mobile Automation Results");

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

    @Override
    public void logSkip(ExtentTest test, String message) {
        if (test != null) {
            test.skip(message);
        }
    }

    @Override
    public String captureScreenshot(WebDriver driver, String testName, String status) {
        return TestReportingUtils.captureScreenshot(driver, testName + "_" + status);
    }

    @Override
    public void flush() {
        if (extent != null) {
            extent.flush();
        }
    }

    /**
     * Sets up system info for ExtentReports
     */
    private void addSystemInformation() {
        if (extent != null) {
            extent.setSystemInfo("Environment", System.getProperty("test.environment", "QA"));
            extent.setSystemInfo("Browser", System.getProperty("browser.name", "N/A"));
            extent.setSystemInfo("Platform", System.getProperty("os.name"));
        }
    }

    /**
     * Finalize and flush the report with system information
     */
    public void finalizeReport() {
        if (extent != null) {
            addSystemInformation();
            extent.flush();
        }
    }
}
