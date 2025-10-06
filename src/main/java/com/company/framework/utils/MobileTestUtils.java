package com.company.framework.utils;

import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * MobileTestUtils - Utility class for common mobile testing operations
 * 
 * Provides reusable methods for:
 * - Element waiting and interaction
 * - Page load validation
 * - Common mobile gestures
 * - Element detection and verification
 * 
 * Benefits:
 * - Centralized wait strategies
 * - Consistent element interaction patterns
 * - Reduced code duplication
 * - Easier maintenance
 * 
 * Note: Consolidated into existing utils package for better organization
 */
public class MobileTestUtils {
    
    private static final Logger logger = LogManager.getLogger(MobileTestUtils.class);
    private static final int DEFAULT_TIMEOUT_SECONDS = 10;
    private static final int SHORT_TIMEOUT_SECONDS = 5;
    
    /**
     * Wait for page to load with content validation
     */
    public static void waitForPageToLoad(AppiumDriver driver) {
        waitForPageToLoad(driver, DEFAULT_TIMEOUT_SECONDS);
    }
    
    /**
     * Wait for page to load with custom timeout
     */
    public static void waitForPageToLoad(AppiumDriver driver, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            
            // Wait for page source to be available with meaningful content
            wait.until(d -> {
                try {
                    String pageSource = d.getPageSource();
                    return pageSource != null && pageSource.length() > 1000;
                } catch (Exception e) {
                    return false;
                }
            });
            
            logger.info("✅ Page loaded successfully");
            
        } catch (Exception e) {
            logger.warn("⚠️ Warning during page load: {}", e.getMessage());
        }
    }
    
    /**
     * Wait for element to be clickable
     */
    public static WebElement waitForElementClickable(AppiumDriver driver, By locator) {
        return waitForElementClickable(driver, locator, DEFAULT_TIMEOUT_SECONDS);
    }
    
    /**
     * Wait for element to be clickable with custom timeout
     */
    public static WebElement waitForElementClickable(AppiumDriver driver, By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            logger.warn("⚠️ Element not clickable within {} seconds: {}", timeoutSeconds, locator);
            throw e;
        }
    }
    
    /**
     * Wait for element to be visible
     */
    public static WebElement waitForElementVisible(AppiumDriver driver, By locator) {
        return waitForElementVisible(driver, locator, DEFAULT_TIMEOUT_SECONDS);
    }
    
    /**
     * Wait for element to be visible with custom timeout
     */
    public static WebElement waitForElementVisible(AppiumDriver driver, By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.warn("⚠️ Element not visible within {} seconds: {}", timeoutSeconds, locator);
            throw e;
        }
    }
    
    /**
     * Safe click with wait and error handling
     */
    public static boolean safeClick(AppiumDriver driver, By locator) {
        return safeClick(driver, locator, DEFAULT_TIMEOUT_SECONDS);
    }
    
    /**
     * Safe click with custom timeout
     */
    public static boolean safeClick(AppiumDriver driver, By locator, int timeoutSeconds) {
        try {
            WebElement element = waitForElementClickable(driver, locator, timeoutSeconds);
            element.click();
            logger.info("✅ Successfully clicked element: {}", locator);
            return true;
        } catch (Exception e) {
            logger.warn("⚠️ Failed to click element: {} - {}", locator, e.getMessage());
            return false;
        }
    }
    
    /**
     * Safe text input with wait and error handling
     */
    public static boolean safeType(AppiumDriver driver, By locator, String text) {
        return safeType(driver, locator, text, DEFAULT_TIMEOUT_SECONDS);
    }
    
    /**
     * Safe text input with custom timeout
     */
    public static boolean safeType(AppiumDriver driver, By locator, String text, int timeoutSeconds) {
        try {
            WebElement element = waitForElementVisible(driver, locator, timeoutSeconds);
            element.clear();
            element.sendKeys(text);
            logger.info("✅ Successfully typed text into element: {}", locator);
            return true;
        } catch (Exception e) {
            logger.warn("⚠️ Failed to type into element: {} - {}", locator, e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if element exists without throwing exception
     */
    public static boolean isElementPresent(AppiumDriver driver, By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    /**
     * Check if element is displayed
     */
    public static boolean isElementDisplayed(AppiumDriver driver, By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    /**
     * Get element text safely
     */
    public static String getElementText(AppiumDriver driver, By locator) {
        try {
            WebElement element = waitForElementVisible(driver, locator, SHORT_TIMEOUT_SECONDS);
            return element.getText();
        } catch (Exception e) {
            logger.warn("⚠️ Failed to get text from element: {}", locator);
            return "";
        }
    }
    
    /**
     * Find elements with timeout
     */
    public static List<WebElement> findElementsWithTimeout(AppiumDriver driver, By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        } catch (TimeoutException e) {
            logger.warn("⚠️ Elements not found within {} seconds: {}", timeoutSeconds, locator);
            return List.of(); // Return empty list instead of throwing
        }
    }
    
    /**
     * Wait for app to be responsive (page source available)
     */
    public static boolean waitForAppResponsive(AppiumDriver driver) {
        return waitForAppResponsive(driver, DEFAULT_TIMEOUT_SECONDS);
    }
    
    /**
     * Wait for app to be responsive with custom timeout
     */
    public static boolean waitForAppResponsive(AppiumDriver driver, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            
            // Wait for page source to be available and non-empty
            wait.until(d -> {
                try {
                    String pageSource = d.getPageSource();
                    return pageSource != null && pageSource.length() > 100;
                } catch (Exception e) {
                    return false;
                }
            });
            
            logger.info("✅ App is responsive");
            return true;
        } catch (TimeoutException e) {
            logger.warn("⚠️ App did not become responsive within {} seconds", timeoutSeconds);
            return false;
        } catch (Exception e) {
            logger.warn("⚠️ Error checking app responsiveness: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Verify BILD app is running
     */
    public static boolean isBildAppRunning(AppiumDriver driver) {
        try {
            String pageSource = driver.getPageSource();
            if (pageSource != null) {
                // Check for multiple BILD app indicators
                return pageSource.contains("com.netbiscuits.bild.android") || 
                       pageSource.toLowerCase().contains("bild") ||
                       pageSource.contains("androidx.compose.ui.platform.ComposeView");
            }
            return false;
        } catch (Exception e) {
            logger.warn("⚠️ Error checking BILD app status: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Get current app package name
     */
    public static String getCurrentAppPackage(AppiumDriver driver) {
        try {
            String pageSource = driver.getPageSource();
            if (pageSource.contains("com.netbiscuits.bild.android")) {
                return "com.netbiscuits.bild.android";
            }
            return "unknown";
        } catch (Exception e) {
            logger.warn("⚠️ Error getting app package: {}", e.getMessage());
            return "error";
        }
    }
    
    /**
     * Take screenshot (placeholder for future implementation)
     */
    public static String takeScreenshot(AppiumDriver driver, String testName) {
        logger.info("📸 Screenshot placeholder for: {}", testName);
        return "screenshot_" + testName + "_" + System.currentTimeMillis() + ".png";
    }

    public static WebElement waitWithFallback(AppiumDriver driver, By[] locators, int timeout) {
    for (By locator : locators) {
        try {
            return waitForElementVisible(driver, locator, timeout);
        } catch (Exception ignored) { }
    }
    throw new NoSuchElementException("Element not found with any fallback locators");
}

public static void tapAtCoordinates(AppiumDriver driver, int x, int y, int durationMs) {
    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
    Sequence tap = new Sequence(finger, 1);

    tap.addAction(finger.createPointerMove(Duration.ZERO,
            PointerInput.Origin.viewport(), x, y));
    tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
    tap.addAction(new Pause(finger, Duration.ofMillis(durationMs)));
    tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

    driver.perform(Arrays.asList(tap));
}

}