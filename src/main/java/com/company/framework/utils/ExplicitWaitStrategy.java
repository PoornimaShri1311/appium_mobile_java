package com.company.framework.utils;

import com.company.framework.interfaces.driver.IDriverManager;
import com.company.framework.interfaces.wait.IWaitStrategy;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.AppiumDriver;

import java.time.Duration;

/**
 * ExplicitWaitStrategy - Implementation of IWaitStrategy using explicit waits
 * Follows Strategy Pattern
 */
public class ExplicitWaitStrategy implements IWaitStrategy {
    
    private final WebDriverWait wait;
    
    public ExplicitWaitStrategy(IDriverManager driverManager, long timeoutInSeconds) {
        this.wait = new WebDriverWait(driverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
    }
    
    @Override
    public WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    @Override
    public WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    
    @Override
    public boolean waitForInvisibility(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    
    @Override
    public boolean waitForTextToBePresentInElement(WebElement element, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }
    
    public void scrollToElement(WebElement element) {
        // For mobile apps, use TouchActionUtils directly for mobile-optimized scrolling
        AppiumDriver driver = com.company.framework.managers.DependencyManager.getInstance().getDriverManager().getDriver();
        com.company.framework.utils.TouchActionUtils touchUtils = new com.company.framework.utils.TouchActionUtils(driver);
        touchUtils.scrollToElement(element);
    }
}