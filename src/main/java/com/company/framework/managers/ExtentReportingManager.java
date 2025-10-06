package com.company.framework.managers;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.company.framework.interfaces.IReportingManager;
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
