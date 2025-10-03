package interfaces;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * IWaitStrategy - Interface for different wait strategies
 * Follows Strategy Pattern and Interface Segregation
 */
public interface IWaitStrategy {
    
    /**
     * Wait for element to be visible
     * @param element WebElement to wait for
     * @return visible WebElement
     */
    WebElement waitForVisibility(WebElement element);
    
    /**
     * Wait for element to be clickable
     * @param element WebElement to wait for
     * @return clickable WebElement
     */
    WebElement waitForClickable(WebElement element);
    
    /**
     * Wait for element to be invisible
     * @param locator By locator to wait for invisibility
     * @return true if element becomes invisible
     */
    boolean waitForInvisibility(By locator);
    
    /**
     * Wait for text to be present in element
     * @param element WebElement to check
     * @param text expected text
     * @return true if text is present
     */
    boolean waitForTextToBePresentInElement(WebElement element, String text);
    
    /**
     * Scroll to make an element visible using mobile-specific scrolling
     * @param element WebElement to scroll to
     */
    void scrollToElement(WebElement element);
}