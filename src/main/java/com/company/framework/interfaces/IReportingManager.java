package com.company.framework.interfaces;

import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;

/**
 * IReportingManager - Interface to decouple reporting logic
 *
 * Responsibilities:
 * - Define a contract for reporting actions (init, create test, log, screenshot, flush)
 * - Enables flexibility to plug in any reporting tool (Extent, Allure, etc.)
 */
public interface IReportingManager {

    void initializeReport(String reportPath);

    ExtentTest createTest(String testName, String description);

    void logPass(ExtentTest test, String message);

    void logFail(ExtentTest test, String message);

    void logSkip(ExtentTest test, String message);

    String captureScreenshot(WebDriver driver, String testName, String status);

    void flush();
}
