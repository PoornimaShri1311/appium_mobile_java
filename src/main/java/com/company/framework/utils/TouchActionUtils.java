// package com.company.framework.utils;

// import io.appium.java_client.AppiumDriver;
// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;
// import org.openqa.selenium.By;
// import org.openqa.selenium.JavascriptExecutor;
// import org.openqa.selenium.Point;
// import org.openqa.selenium.WebElement;
// import org.openqa.selenium.interactions.Pause;
// import org.openqa.selenium.interactions.PointerInput;
// import org.openqa.selenium.interactions.Sequence;
// import org.openqa.selenium.support.ui.WebDriverWait;
// import org.openqa.selenium.support.ui.ExpectedConditions;

// import java.time.Duration;
// import java.util.Arrays;
// import java.util.Map;

// /**
//  * TouchActionUtils - Utility class for advanced touch interactions
//  * Provides reusable touch actions for mobile automation
//  */
// public class TouchActionUtils {
    
//     private static final Logger logger = LogManager.getLogger(TouchActionUtils.class);
//     private final AppiumDriver driver;
    
//     public TouchActionUtils(AppiumDriver driver) {
//         this.driver = driver;
//     }
    
//     /**
//      * Performs a tap action at specified coordinates
//      * @param x X coordinate
//      * @param y Y coordinate
//      * @param duration Duration to hold the tap (in milliseconds)
//      */
//     public void tapAtCoordinates(int x, int y, int duration) {
//         logger.info("Performing tap at coordinates: ({}, {}) with duration: {}ms", x, y, duration);
        
//         try {
//             final var finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
//             var tapPoint = new Point(x, y);
//             var tap = new Sequence(finger, 1);
            
//             tap.addAction(finger.createPointerMove(Duration.ofMillis(0),
//                 PointerInput.Origin.viewport(), tapPoint.x, tapPoint.y));
//             tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
//             tap.addAction(new Pause(finger, Duration.ofMillis(duration)));
//             tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            
//             driver.perform(Arrays.asList(tap));
//             logger.info("Tap action completed successfully");
            
//         } catch (Exception e) {
//             logger.error("Tap action failed: " + e.getMessage());
//             throw new RuntimeException("Failed to perform tap action", e);
//         }
//     }
    
//     /**
//      * Performs a quick tap action at specified coordinates (50ms duration)
//      * @param x X coordinate
//      * @param y Y coordinate
//      */
//     public void quickTap(int x, int y) {
//         tapAtCoordinates(x, y, 50);
//     }
    
//     /**
//      * Performs a long press at specified coordinates
//      * @param x X coordinate
//      * @param y Y coordinate
//      * @param duration Duration to hold (in milliseconds)
//      */
//     public void longPress(int x, int y, int duration) {
//         logger.info("Performing long press at coordinates: ({}, {}) for {}ms", x, y, duration);
//         tapAtCoordinates(x, y, duration);
//     }
    
//     /**
//      * Performs a double tap at specified coordinates
//      * @param x X coordinate
//      * @param y Y coordinate
//      */
//     public void doubleTap(int x, int y) {
//         logger.info("Performing double tap at coordinates: ({}, {})", x, y);
        
//         // Use PointerInput for proper double tap with precise timing
//         PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
//         Sequence tap1 = new Sequence(finger, 0);
//         Sequence tap2 = new Sequence(finger, 0);
        
//         // First tap
//         tap1.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
//         tap1.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
//         tap1.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        
//         // Second tap with slight delay (using pause instead of sleep)
//         tap2.addAction(new Pause(finger, Duration.ofMillis(100))); // Proper W3C pause
//         tap2.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
//         tap2.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
//         tap2.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        
//         driver.perform(Arrays.asList(tap1, tap2));
//     }
    
//     /**
//      * Performs a swipe gesture from start point to end point
//      * @param startX Start X coordinate
//      * @param startY Start Y coordinate
//      * @param endX End X coordinate
//      * @param endY End Y coordinate
//      * @param duration Duration of the swipe (in milliseconds)
//      */
//     public void swipe(int startX, int startY, int endX, int endY, int duration) {
//         logger.info("Performing swipe from ({}, {}) to ({}, {}) over {}ms", startX, startY, endX, endY, duration);
        
//         try {
//             final var finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
//             var swipe = new Sequence(finger, 1);
            
//             swipe.addAction(finger.createPointerMove(Duration.ofMillis(0),
//                 PointerInput.Origin.viewport(), startX, startY));
//             swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
//             swipe.addAction(finger.createPointerMove(Duration.ofMillis(duration),
//                 PointerInput.Origin.viewport(), endX, endY));
//             swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            
//             driver.perform(Arrays.asList(swipe));
//             logger.info("Swipe action completed successfully");
            
//         } catch (Exception e) {
//             logger.error("Swipe action failed: " + e.getMessage());
//             throw new RuntimeException("Failed to perform swipe action", e);
//         }
//     }

//     /**
//      * Scrolls down on mobile device using various scroll strategies
//      */
//     public void scrollDown() {
//         logger.info("Performing scroll down action");
        
//         // Get screen size once and make it accessible to all catch blocks
//         org.openqa.selenium.Dimension size = driver.manage().window().getSize();
        
//         try {
//             int startY = (int) (size.height * 0.8);
//             int endY = (int) (size.height * 0.2);
            
//             // Use mobile: swipeGesture for Android UiAutomator2
//             Map<String, Object> params = Map.of(
//                 "left", 0,
//                 "top", startY,
//                 "width", size.width,
//                 "height", endY - startY,
//                 "direction", "up",
//                 "percent", 0.75
//             );
//             ((JavascriptExecutor) driver).executeScript("mobile: swipeGesture", params);
//             logger.info("Scroll down completed using swipeGesture");
            
//         } catch (Exception e) {
//             logger.warn("swipeGesture failed, trying scrollGesture fallback: " + e.getMessage());
//             // Fallback: Use scrollGesture instead of deprecated mobile: swipe
//             try {
//                 Map<String, Object> scrollParams = Map.of(
//                     "left", 100,
//                     "top", 800,
//                     "width", size.width - 200,
//                     "height", 800,
//                     "direction", "down",
//                     "percent", 0.8
//                 );
//                 ((JavascriptExecutor) driver).executeScript("mobile: scrollGesture", scrollParams);
//                 logger.info("Scroll down completed using scrollGesture");
//             } catch (Exception ex) {
//                 logger.warn("scrollGesture failed, using W3C Actions: " + ex.getMessage());
//                 // Final fallback - use W3C Actions API
//                 try {
//                     PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
//                     Sequence swipe = new Sequence(finger, 1)
//                         .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), 500, 1500))
//                         .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
//                         .addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), 500, 800))
//                         .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                    
//                     driver.perform(Arrays.asList(swipe));
//                     logger.info("Scroll down completed using W3C Actions");
//                 } catch (Exception finalEx) {
//                     logger.error("All scroll methods failed: " + finalEx.getMessage());
//                     throw new RuntimeException("Unable to perform scroll action", finalEx);
//                 }
//             }
//         }
//     }

//     /**
//      * Scrolls to make an element visible using mobile-specific scrolling
//      * @param element The element to scroll to
//      */
//     public void scrollToElement(WebElement element) {
//         logger.info("Attempting to scroll to element");
        
//         try {
//             // First try to find if element is already visible
//             if (isElementVisible(element)) {
//                 logger.info("Element is already visible, no scroll needed");
//                 return;
//             }
            
//             logger.info("Element not visible, attempting to scroll to it");
            
//             // Try multiple scroll approaches
//             boolean elementFound = false;
            
//             // Approach 1: Scroll down multiple times to find the element
//             int maxScrolls = 5;
//             for (int i = 0; i < maxScrolls && !elementFound; i++) {
//                 try {
//                     scrollDown();
//                     Thread.sleep(1000); // Wait for scroll to complete
                    
//                     if (isElementVisible(element)) {
//                         logger.info("Element found after {} scroll(s)", i + 1);
//                         elementFound = true;
//                         break;
//                     }
//                 } catch (InterruptedException ie) {
//                     Thread.currentThread().interrupt();
//                 } catch (Exception scrollEx) {
//                     logger.warn("Scroll attempt {} failed: {}", i + 1, scrollEx.getMessage());
//                 }
//             }
            
//             // If still not found, try JavaScript scroll as fallback
//             if (!elementFound) {
//                 try {
//                     logger.info("Trying JavaScript scroll as fallback");
//                     ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
//                     Thread.sleep(1000);
                    
//                     if (isElementVisible(element)) {
//                         logger.info("Element found using JavaScript scroll");
//                     } else {
//                         logger.warn("Element still not visible after all scroll attempts");
//                     }
//                 } catch (Exception jsEx) {
//                     logger.error("JavaScript scroll also failed: {}", jsEx.getMessage());
//                 }
//             }
            
//         } catch (Exception e) {
//             logger.error("ScrollToElement encountered an error: {}", e.getMessage());
//             // Final fallback to basic scroll
//             try {
//                 ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
//             } catch (Exception finalEx) {
//                 logger.error("All scroll methods failed: {}", finalEx.getMessage());
//             }
//         }
//     }

//     /**
//      * Check if element is visible without waiting
//      * @param element The element to check
//      * @return true if visible, false otherwise
//      */
//     private boolean isElementVisible(WebElement element) {
//         try {
//             return element.isDisplayed();
//         } catch (Exception e) {
//             return false;
//         }
//     }

//     /**
//  * Performs a tap on a given element
//  * @param element WebElement to tap
//  */
// public void tapOnElement(WebElement element) {
//     logger.info("Tapping on element: {}", element);
//     try {
//         Point location = element.getLocation();
//         int x = location.getX() + (element.getSize().width / 2);
//         int y = location.getY() + (element.getSize().height / 2);
//         quickTap(x, y);
//     } catch (Exception e) {
//         logger.error("Failed to tap on element: {}", e.getMessage());
//         throw new RuntimeException("Tap on element failed", e);
//     }
// }
// /**
//  * Scrolls until an element matching given locator is visible
//  * @param locator By locator of the element
//  * @param maxScrolls Maximum number of scroll attempts
//  * @return WebElement if found, else null
//  */
// public WebElement scrollUntilVisibleAndClickable(By locator, int maxScrolls, int timeoutSec) {
//     logger.info("Scrolling until element {} is visible and clickable", locator);

//     for (int i = 0; i < maxScrolls; i++) {
//         try {
//             WebElement element = driver.findElement(locator);
//             if (element.isDisplayed()) {
//                 // Wait until clickable to avoid stale reference
//                 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
//                 return wait.until(ExpectedConditions.elementToBeClickable(locator));
//             }
//         } catch (Exception ignored) {
//             // not found yet
//         }
//         scrollDown();
//     }

//     logger.warn("Element {} not found after {} scroll attempts", locator, maxScrolls);
//     return null;
// }

// /**
//  * Performs drag and drop from one element to another
//  * @param sourceElement Element to drag
//  * @param targetElement Element to drop onto
//  */
// public void dragAndDrop(WebElement sourceElement, WebElement targetElement) {
//     logger.info("Performing drag and drop");
//     try {
//         Point start = sourceElement.getLocation();
//         Point end = targetElement.getLocation();

//         final var finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
//         var drag = new Sequence(finger, 1);

//         drag.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), start.x, start.y));
//         drag.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
//         drag.addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), end.x, end.y));
//         drag.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

//         driver.perform(Arrays.asList(drag));
//         logger.info("Drag and drop completed");
//     } catch (Exception e) {
//         logger.error("Drag and drop failed: {}", e.getMessage());
//         throw new RuntimeException("Drag and drop failed", e);
//     }
// }
// /**
//  * Performs a pinch zoom (in/out) on given element
//  * @param element The element to zoom on
//  * @param zoomIn true for zoom in, false for zoom out
//  */
// public void pinchZoom(WebElement element, boolean zoomIn) {
//     logger.info("Performing {} on element", zoomIn ? "zoom in" : "zoom out");
//     try {
//         Point center = element.getLocation();
//         int centerX = center.getX() + (element.getSize().width / 2);
//         int centerY = center.getY() + (element.getSize().height / 2);

//         int moveOffset = 200; // adjust as needed

//         // Two fingers
//         PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
//         PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

//         Sequence seq1 = new Sequence(finger1, 1);
//         Sequence seq2 = new Sequence(finger2, 1);

//         if (zoomIn) {
//             // Fingers start apart -> move closer
//             seq1.addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX - moveOffset, centerY));
//             seq2.addAction(finger2.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX + moveOffset, centerY));
//         } else {
//             // Fingers start close -> move apart
//             seq1.addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX - 50, centerY));
//             seq2.addAction(finger2.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX + 50, centerY));
//         }

//         seq1.addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
//         seq2.addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

//         if (zoomIn) {
//             seq1.addAction(finger1.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), centerX - 50, centerY));
//             seq2.addAction(finger2.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), centerX + 50, centerY));
//         } else {
//             seq1.addAction(finger1.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), centerX - moveOffset, centerY));
//             seq2.addAction(finger2.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), centerX + moveOffset, centerY));
//         }

//         seq1.addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
//         seq2.addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

//         driver.perform(Arrays.asList(seq1, seq2));
//         logger.info("Pinch/zoom completed successfully");
//     } catch (Exception e) {
//         logger.error("Pinch/zoom failed: {}", e.getMessage());
//         throw new RuntimeException("Pinch/zoom failed", e);
//     }
// }

// }

package com.company.framework.utils;

import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
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

    /**
 * Performs a tap on a given element
 * @param element WebElement to tap
 */
public void tapOnElement(WebElement element) {
    logger.info("Tapping on element: {}", element);
    try {
        Point location = element.getLocation();
        int x = location.getX() + (element.getSize().width / 2);
        int y = location.getY() + (element.getSize().height / 2);
        quickTap(x, y);
    } catch (Exception e) {
        logger.error("Failed to tap on element: {}", e.getMessage());
        throw new RuntimeException("Tap on element failed", e);
    }
}
/**
 * Scrolls until an element matching given locator is visible
 * @param locator By locator of the element
 * @param maxScrolls Maximum number of scroll attempts
 * @return WebElement if found, else null
 */
public WebElement scrollUntilVisibleAndClickable(By locator, int maxScrolls, int timeoutSec) {
    logger.info("Scrolling until element {} is visible and clickable", locator);

    for (int i = 0; i < maxScrolls; i++) {
        try {
            WebElement element = driver.findElement(locator);
            if (element.isDisplayed()) {
                // Wait until clickable to avoid stale reference
                return MobileTestUtils.waitForElementClickable(driver, locator, timeoutSec);
            }
        } catch (Exception ignored) {
            // not found yet
        }
        scrollDown();
    }

    logger.warn("Element {} not found after {} scroll attempts", locator, maxScrolls);
    return null;
}

/**
 * Performs drag and drop from one element to another
 * @param sourceElement Element to drag
 * @param targetElement Element to drop onto
 */
public void dragAndDrop(WebElement sourceElement, WebElement targetElement) {
    logger.info("Performing drag and drop");
    try {
        Point start = sourceElement.getLocation();
        Point end = targetElement.getLocation();

        final var finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        var drag = new Sequence(finger, 1);

        drag.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), start.x, start.y));
        drag.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        drag.addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), end.x, end.y));
        drag.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(drag));
        logger.info("Drag and drop completed");
    } catch (Exception e) {
        logger.error("Drag and drop failed: {}", e.getMessage());
        throw new RuntimeException("Drag and drop failed", e);
    }
}
/**
 * Performs a pinch zoom (in/out) on given element
 * @param element The element to zoom on
 * @param zoomIn true for zoom in, false for zoom out
 */
public void pinchZoom(WebElement element, boolean zoomIn) {
    logger.info("Performing {} on element", zoomIn ? "zoom in" : "zoom out");
    try {
        Point center = element.getLocation();
        int centerX = center.getX() + (element.getSize().width / 2);
        int centerY = center.getY() + (element.getSize().height / 2);

        int moveOffset = 200; // adjust as needed

        // Two fingers
        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

        Sequence seq1 = new Sequence(finger1, 1);
        Sequence seq2 = new Sequence(finger2, 1);

        if (zoomIn) {
            // Fingers start apart -> move closer
            seq1.addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX - moveOffset, centerY));
            seq2.addAction(finger2.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX + moveOffset, centerY));
        } else {
            // Fingers start close -> move apart
            seq1.addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX - 50, centerY));
            seq2.addAction(finger2.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX + 50, centerY));
        }

        seq1.addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        seq2.addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        if (zoomIn) {
            seq1.addAction(finger1.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), centerX - 50, centerY));
            seq2.addAction(finger2.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), centerX + 50, centerY));
        } else {
            seq1.addAction(finger1.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), centerX - moveOffset, centerY));
            seq2.addAction(finger2.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), centerX + moveOffset, centerY));
        }

        seq1.addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        seq2.addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(seq1, seq2));
        logger.info("Pinch/zoom completed successfully");
    } catch (Exception e) {
        logger.error("Pinch/zoom failed: {}", e.getMessage());
        throw new RuntimeException("Pinch/zoom failed", e);
    }
}

}