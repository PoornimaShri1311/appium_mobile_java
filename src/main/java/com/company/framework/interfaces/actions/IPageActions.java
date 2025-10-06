package com.company.framework.interfaces.actions;

import org.openqa.selenium.WebElement;

/**
 * IPageActions - Interface for common page actions
 * Follows Interface Segregation Principle - specific interfaces for specific clients
 */
public interface IPageActions {
    
    /**
     * Click on an element
     * @param element WebElement to click
     */
    void click(WebElement element);
    
    /**
     * Enter text in an element
     * @param element WebElement to type in
     * @param text text to enter
     */
    void enterText(WebElement element, String text);
    
    /**
     * Check if element is displayed
     * @param element WebElement to check
     * @return true if displayed, false otherwise
     */
    boolean isDisplayed(WebElement element);
    
    /**
     * Wait for element to be clickable and click
     * @param element WebElement to wait for and click
     */
    void waitAndClick(WebElement element);
    
    /**
     * Get text from an element
     * @param element WebElement to get text from
     * @return text content
     */
    String getText(WebElement element);
    
}