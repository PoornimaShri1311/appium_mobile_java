package utils;

import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

/**
 * TouchActionUtils - Utility class for advanced touch interactions
 * Provides reusable touch actions for mobile automation
 */
public class TouchActionUtils {
    
    private static final Logger logger = LogManager.getLogger(TouchActionUtils.class);
    private final AppiumDriver driver;
    
    public TouchActionUtils(AppiumDriver driver) {
        this.driver = driver;
    }
    
    /**
     * Performs a tap action at specified coordinates
     * @param x X coordinate
     * @param y Y coordinate
     * @param duration Duration to hold the tap (in milliseconds)
     */
    public void tapAtCoordinates(int x, int y, int duration) {
        logger.info("Performing tap at coordinates: ({}, {}) with duration: {}ms", x, y, duration);
        
        try {
            final var finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            var tapPoint = new Point(x, y);
            var tap = new Sequence(finger, 1);
            
            tap.addAction(finger.createPointerMove(Duration.ofMillis(0),
                PointerInput.Origin.viewport(), tapPoint.x, tapPoint.y));
            tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(new Pause(finger, Duration.ofMillis(duration)));
            tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            
            driver.perform(Arrays.asList(tap));
            logger.info("Tap action completed successfully");
            
        } catch (Exception e) {
            logger.error("Tap action failed: " + e.getMessage());
            throw new RuntimeException("Failed to perform tap action", e);
        }
    }
    
    /**
     * Performs a quick tap action at specified coordinates (50ms duration)
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void quickTap(int x, int y) {
        tapAtCoordinates(x, y, 50);
    }
    
    /**
     * Performs a long press at specified coordinates
     * @param x X coordinate
     * @param y Y coordinate
     * @param duration Duration to hold (in milliseconds)
     */
    public void longPress(int x, int y, int duration) {
        logger.info("Performing long press at coordinates: ({}, {}) for {}ms", x, y, duration);
        tapAtCoordinates(x, y, duration);
    }
    
    /**
     * Performs a double tap at specified coordinates
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void doubleTap(int x, int y) {
        logger.info("Performing double tap at coordinates: ({}, {})", x, y);
        
        // Use PointerInput for proper double tap with precise timing
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap1 = new Sequence(finger, 0);
        Sequence tap2 = new Sequence(finger, 0);
        
        // First tap
        tap1.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap1.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap1.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        
        // Second tap with slight delay (using pause instead of sleep)
        tap2.addAction(new Pause(finger, Duration.ofMillis(100))); // Proper W3C pause
        tap2.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap2.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap2.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        
        driver.perform(Arrays.asList(tap1, tap2));
    }
    
    /**
     * Performs a swipe gesture from start point to end point
     * @param startX Start X coordinate
     * @param startY Start Y coordinate
     * @param endX End X coordinate
     * @param endY End Y coordinate
     * @param duration Duration of the swipe (in milliseconds)
     */
    public void swipe(int startX, int startY, int endX, int endY, int duration) {
        logger.info("Performing swipe from ({}, {}) to ({}, {}) over {}ms", startX, startY, endX, endY, duration);
        
        try {
            final var finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            var swipe = new Sequence(finger, 1);
            
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(0),
                PointerInput.Origin.viewport(), startX, startY));
            swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(duration),
                PointerInput.Origin.viewport(), endX, endY));
            swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            
            driver.perform(Arrays.asList(swipe));
            logger.info("Swipe action completed successfully");
            
        } catch (Exception e) {
            logger.error("Swipe action failed: " + e.getMessage());
            throw new RuntimeException("Failed to perform swipe action", e);
        }
    }

    /**
     * Scrolls down on mobile device using various scroll strategies
     */
    public void scrollDown() {
        logger.info("Performing scroll down action");
        
        try {
            // First try using touch actions with Dimension
            org.openqa.selenium.Dimension size = driver.manage().window().getSize();
            int startY = (int) (size.height * 0.8);
            int endY = (int) (size.height * 0.2);
            
            // Use mobile: swipeGesture for Android UiAutomator2
            Map<String, Object> params = Map.of(
                "left", 0,
                "top", startY,
                "width", size.width,
                "height", endY - startY,
                "direction", "up",
                "percent", 0.75
            );
            ((JavascriptExecutor) driver).executeScript("mobile: swipeGesture", params);
            logger.info("Scroll down completed using swipeGesture");
            
        } catch (Exception e) {
            logger.warn("swipeGesture failed, trying swipe fallback: " + e.getMessage());
            // Fallback: Try using direct coordinates
            try {
                Map<String, Object> swipeParams = Map.of(
                    "startX", 500,
                    "startY", 1500, 
                    "endX", 500,
                    "endY", 800,
                    "duration", 1000
                );
                ((JavascriptExecutor) driver).executeScript("mobile: swipe", swipeParams);
                logger.info("Scroll down completed using mobile: swipe");
            } catch (Exception ex) {
                logger.warn("mobile: swipe failed, trying scrollGesture: " + ex.getMessage());
                // Final fallback - use UiScrollable if available
                try {
                    ((JavascriptExecutor) driver).executeScript("mobile: scrollGesture", Map.of(
                        "left", 0, "top", 0, "width", 500, "height", 1000,
                        "direction", "down",
                        "percent", 3.0
                    ));
                    logger.info("Scroll down completed using scrollGesture");
                } catch (Exception finalEx) {
                    logger.warn("All mobile scroll methods failed, using basic web scroll: " + finalEx.getMessage());
                    // Most basic fallback
                    ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500);");
                }
            }
        }
    }

    /**
     * Scrolls to make an element visible using mobile-specific scrolling
     * @param element The element to scroll to
     */
    public void scrollToElement(WebElement element) {
        logger.info("Attempting to scroll to element");
        
        try {
            // First try to find if element is already visible
            if (isElementVisible(element)) {
                logger.info("Element is already visible, no scroll needed");
                return;
            }
            
            logger.info("Element not visible, attempting to scroll to it");
            
            // Try multiple scroll approaches
            boolean elementFound = false;
            
            // Approach 1: Scroll down multiple times to find the element
            int maxScrolls = 5;
            for (int i = 0; i < maxScrolls && !elementFound; i++) {
                try {
                    scrollDown();
                    Thread.sleep(1000); // Wait for scroll to complete
                    
                    if (isElementVisible(element)) {
                        logger.info("Element found after {} scroll(s)", i + 1);
                        elementFound = true;
                        break;
                    }
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                } catch (Exception scrollEx) {
                    logger.warn("Scroll attempt {} failed: {}", i + 1, scrollEx.getMessage());
                }
            }
            
            // If still not found, try JavaScript scroll as fallback
            if (!elementFound) {
                try {
                    logger.info("Trying JavaScript scroll as fallback");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                    Thread.sleep(1000);
                    
                    if (isElementVisible(element)) {
                        logger.info("Element found using JavaScript scroll");
                    } else {
                        logger.warn("Element still not visible after all scroll attempts");
                    }
                } catch (Exception jsEx) {
                    logger.error("JavaScript scroll also failed: {}", jsEx.getMessage());
                }
            }
            
        } catch (Exception e) {
            logger.error("ScrollToElement encountered an error: {}", e.getMessage());
            // Final fallback to basic scroll
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            } catch (Exception finalEx) {
                logger.error("All scroll methods failed: {}", finalEx.getMessage());
            }
        }
    }

    /**
     * Check if element is visible without waiting
     * @param element The element to check
     * @return true if visible, false otherwise
     */
    private boolean isElementVisible(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}