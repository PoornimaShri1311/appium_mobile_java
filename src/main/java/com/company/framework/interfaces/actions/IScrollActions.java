package com.company.framework.interfaces.actions;

import org.openqa.selenium.WebElement;

/**
 * IScrollActions - Interface dedicated to scrolling-related actions
 * 
 * Purpose:
 * - Separate scrolling logic from waiting and element actions
 * - Supports mobile and web scrolling behavior
 * - Keeps framework modular and adheres to Interface Segregation Principle
 */
public interface IScrollActions {
    
    /**
     * Scroll to make a specific element visible
     * @param element WebElement to scroll to
     */
    void scrollToElement(WebElement element);

    /**
     * Scroll to a specific text (useful for mobile automation)
     * @param visibleText text visible on screen to scroll to
     */
    void scrollToText(String visibleText);

    /**
     * Scroll vertically by a specific amount (e.g., pixels)
     * @param pixels number of pixels to scroll vertically
     */
    void scrollVertically(int pixels);

    /**
     * Scroll horizontally by a specific amount (e.g., pixels)
     * @param pixels number of pixels to scroll horizontally
     */
    void scrollHorizontally(int pixels);
}
