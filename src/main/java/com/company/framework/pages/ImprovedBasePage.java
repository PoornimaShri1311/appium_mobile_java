package com.company.framework.pages;

import com.company.framework.interfaces.actions.IPageActions;
import com.company.framework.interfaces.actions.IScrollActions;
import com.company.framework.interfaces.wait.IWaitStrategy;

import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * ImprovedBasePage - Base class for all page objects
 * Provides common functionality and SOLID principle implementation
 */
public abstract class ImprovedBasePage {
    
    private static final Logger logger = LogManager.getLogger(ImprovedBasePage.class);
    protected final AppiumDriver driver;
    protected final WebDriverWait wait;
    
    public ImprovedBasePage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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
            // Wait for page readiness using WebDriverWait instead of sleep
            WebDriverWait pageWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            pageWait.until(driver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete"));
        } catch (Exception e) {
            logger.warn("Page stabilization wait failed: {}", e.getMessage());
        }
    }
    
    /**
     * Simple PageActions implementation for basic operations
     */
    private static class SimplePageActions implements IPageActions, IScrollActions {
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
            // Wait for element to be clickable then click
            try {
                WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                localWait.until(ExpectedConditions.elementToBeClickable(element));
                element.click();
            } catch (Exception e) {
                logger.warn("Wait and click failed: {}", e.getMessage());
                // Fallback to direct click
                element.click();
            }
        }
                
        @Override
        public String getText(org.openqa.selenium.WebElement element) {
            return element.getText();
        }

        @Override
        public void scrollToElement(org.openqa.selenium.WebElement element) {
            // Simple scroll implementation
            driver.executeScript("arguments[0].scrollIntoView(true);", element);
        }

        @Override
            public void scrollToText(String visibleText) {
                // Simple example for Android UiScrollable
                try {
                    driver.executeScript("mobile: scroll", 
                        java.util.Map.of("strategy", "accessibility id", "selector", visibleText));
                } catch (Exception e) {
                    // Fallback or log
                }
            }

            @Override
            public void scrollVertically(int pixels) {
                driver.executeScript("window.scrollBy(0," + pixels + ");");
            }

            @Override
            public void scrollHorizontally(int pixels) {
                driver.executeScript("window.scrollBy(" + pixels + ",0);");
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
            try {
                WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                return localWait.until(ExpectedConditions.visibilityOf(element));
            } catch (Exception e) {
                // Fallback to return element as-is
                return element;
            }
        }
        
        @Override
        public org.openqa.selenium.WebElement waitForClickable(org.openqa.selenium.WebElement element) {
            try {
                WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                return localWait.until(ExpectedConditions.elementToBeClickable(element));
            } catch (Exception e) {
                // Fallback to return element as-is
                return element;
            }
        }
        
        @Override
        public boolean waitForInvisibility(org.openqa.selenium.By locator) {
            try {
                WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                return localWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            } catch (Exception e) {
                return true; // Assume invisible if not found
            }
        }
        
        @Override
        public boolean waitForTextToBePresentInElement(org.openqa.selenium.WebElement element, String text) {
            try {
                WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                return localWait.until(ExpectedConditions.textToBePresentInElement(element, text));
            } catch (Exception e) {
                return false;
            }
        }
    }
}