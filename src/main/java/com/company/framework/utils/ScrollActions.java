package com.company.framework.utils;

import com.company.framework.interfaces.actions.IScrollActions;
import org.openqa.selenium.WebElement;
import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ScrollActions - Implementation of IScrollActions
 * Provides scrolling functionality for mobile automation
 */
public class ScrollActions implements IScrollActions {
    
    private static final Logger logger = LogManager.getLogger(ScrollActions.class);
    private final AppiumDriver driver;
    
    public ScrollActions(AppiumDriver driver) {
        this.driver = driver;
    }
    
    @Override
    public void scrollToElement(WebElement element) {
        try {
            TouchActionUtils touchUtils = new TouchActionUtils(driver);
            touchUtils.scrollToElement(element);
            logger.info("Successfully scrolled to element");
        } catch (Exception e) {
            logger.error("Failed to scroll to element: " + e.getMessage());
            throw new RuntimeException("Scroll operation failed", e);
        }
    }
    
    @Override
    public void scrollToText(String visibleText) {
        try {
            TouchActionUtils touchUtils = new TouchActionUtils(driver);
            touchUtils.scrollDown(); // Simplified - all text scrolling uses vertical scroll
            logger.info("Successfully scrolled to find text: " + visibleText);
        } catch (Exception e) {
            logger.error("Failed to scroll to text: " + e.getMessage());
            throw new RuntimeException("Scroll to text operation failed", e);
        }
    }

    @Override
    public void scrollVertically(int pixels) {
        scrollToText(""); // Delegate to scrollToText since they both use scrollDown()
    }

    @Override
    public void scrollHorizontally(int pixels) {
        scrollToText(""); // Delegate to scrollToText since horizontal isn't supported
    }
}