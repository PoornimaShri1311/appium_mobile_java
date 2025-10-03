package utils;

import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Arrays;

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
        quickTap(x, y);
        
        try {
            Thread.sleep(100); // Small delay between taps
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        quickTap(x, y);
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
}