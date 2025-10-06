package com.company.framework.utils;

import com.company.framework.interfaces.actions.IPageActions;
import com.company.framework.interfaces.wait.IWaitStrategy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;

/**
 * PageActions - Implementation of IPageActions
 * Follows Single Responsibility Principle - focused on page interactions
 */
public class PageActions implements IPageActions {
    
    private static final Logger logger = LogManager.getLogger(PageActions.class);
    private final IWaitStrategy waitStrategy;
    
    public PageActions(IWaitStrategy waitStrategy) {
        this.waitStrategy = waitStrategy;
    }
    
    @Override
    public void click(WebElement element) {
        try {
            element.click();
            logger.info("Clicked on element successfully");
        } catch (Exception e) {
            logger.error("Failed to click element: " + e.getMessage());
            throw new RuntimeException("Click operation failed", e);
        }
    }
    
    @Override
    public void enterText(WebElement element, String text) {
        try {
            element.clear();
            element.sendKeys(text);
            logger.info("Entered text: " + text);
        } catch (Exception e) {
            logger.error("Failed to enter text: " + e.getMessage());
            throw new RuntimeException("Text entry failed", e);
        }
    }
    
    @Override
    public boolean isDisplayed(WebElement element) {
        try {
            boolean displayed = element.isDisplayed();
            logger.info("Element displayed: " + displayed);
            return displayed;
        } catch (Exception e) {
            logger.info("Element not displayed: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public void waitAndClick(WebElement element) {
        try {
            WebElement clickableElement = waitStrategy.waitForClickable(element);
            clickableElement.click();
            logger.info("Waited and clicked element successfully");
        } catch (Exception e) {
            logger.error("Failed to wait and click element: " + e.getMessage());
            throw new RuntimeException("Wait and click operation failed", e);
        }
    }
    
    @Override
    public String getText(WebElement element) {
        try {
            waitStrategy.waitForVisibility(element);
            String text = element.getText();
            logger.info("Retrieved text: " + text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from element: " + e.getMessage());
            throw new RuntimeException("Get text operation failed", e);
        }
    }
}