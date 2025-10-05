package com.company.framework.pages;

import com.company.framework.interfaces.IPageActions;
import com.company.framework.interfaces.IWaitStrategy;
import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ImprovedBasePage - Base class for all page objects
 * Provides common functionality and SOLID principle implementation
 */
public abstract class ImprovedBasePage {
    
    private static final Logger logger = LogManager.getLogger(ImprovedBasePage.class);
    protected final AppiumDriver driver;
    
    public ImprovedBasePage(AppiumDriver driver) {
        this.driver = driver;
    }
    
    /**
     * Get page actions for common operations
     * @return IPageActions instance
     */
    protected IPageActions getPageActions() {
        // Return a simple implementation or inject via DI
        return new SimplePageActions(driver);
    }
    
    /**
     * Get wait strategy for timing operations
     * @return IWaitStrategy instance
     */
    protected IWaitStrategy getWaitStrategy() {
        // Return a simple implementation or inject via DI
        return new SimpleWaitStrategy(driver);
    }
    
    /**
     * Check if page is loaded - to be implemented by subclasses
     * @return true if page is loaded
     */
    public abstract boolean isPageLoaded();
    
    /**
     * Wait for page to load
     */
    protected void waitForPageToLoad() {
        // Default implementation - can be overridden
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Wait interrupted: {}", e.getMessage());
        }
    }
    
    /**
     * Simple PageActions implementation for basic operations
     */
    private static class SimplePageActions implements IPageActions {
        private final AppiumDriver driver;
        
        public SimplePageActions(AppiumDriver driver) {
            this.driver = driver;
        }
        
        @Override
        public void click(org.openqa.selenium.WebElement element) {
            element.click();
        }
        
        @Override
        public void enterText(org.openqa.selenium.WebElement element, String text) {
            element.clear();
            element.sendKeys(text);
        }
        
        @Override
        public boolean isDisplayed(org.openqa.selenium.WebElement element) {
            try {
                return element.isDisplayed();
            } catch (Exception e) {
                return false;
            }
        }
        
        @Override
        public void waitAndClick(org.openqa.selenium.WebElement element) {
            // Simple wait and click
            try {
                Thread.sleep(1000);
                element.click();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        @Override
        public void scrollToElement(org.openqa.selenium.WebElement element) {
            // Simple scroll implementation
            driver.executeScript("arguments[0].scrollIntoView(true);", element);
        }
        
        @Override
        public String getText(org.openqa.selenium.WebElement element) {
            return element.getText();
        }
    }
    
    /**
     * Simple WaitStrategy implementation
     */
    private static class SimpleWaitStrategy implements IWaitStrategy {
        private final AppiumDriver driver;
        
        public SimpleWaitStrategy(AppiumDriver driver) {
            this.driver = driver;
        }
        
        @Override
        public org.openqa.selenium.WebElement waitForVisibility(org.openqa.selenium.WebElement element) {
            // Simple implementation
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return element;
        }
        
        @Override
        public org.openqa.selenium.WebElement waitForClickable(org.openqa.selenium.WebElement element) {
            // Simple implementation
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return element;
        }
        
        @Override
        public boolean waitForInvisibility(org.openqa.selenium.By locator) {
            // Simple implementation
            try {
                Thread.sleep(1000);
                return !driver.findElement(locator).isDisplayed();
            } catch (Exception e) {
                return true; // Assume invisible if not found
            }
        }
        
        @Override
        public boolean waitForTextToBePresentInElement(org.openqa.selenium.WebElement element, String text) {
            // Simple implementation
            try {
                Thread.sleep(1000);
                return element.getText().contains(text);
            } catch (Exception e) {
                return false;
            }
        }
        
        @Override
        public void scrollToElement(org.openqa.selenium.WebElement element) {
            // Simple scroll implementation
            driver.executeScript("arguments[0].scrollIntoView(true);", element);
        }
    }
}