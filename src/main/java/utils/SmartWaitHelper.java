package utils;

import org.openqa.selenium.By;
import com.aventstack.extentreports.ExtentTest;
import locators.CommonElementLocators;
import locators.CommonElementLocators.ElementType;

/**
 * SmartWaitHelper - Provides pre-configured smart wait strategies for common scenarios
 * This utility class encapsulates complex wait logic into simple, reusable methods
 */
public class SmartWaitHelper {
    
    private final WaitUtils waitUtils;
    
    public SmartWaitHelper(int timeoutSeconds) {
        this.waitUtils = new WaitUtils(timeoutSeconds);
    }
    
    public SmartWaitHelper() {
        this(10); // Default 10 second timeout
    }
    
    /**
     * Wait for search results with comprehensive logging
     * Replaces: complex try-catch blocks with multiple selectors
     * Usage: SmartWaitHelper.waitForSearchResults(test);
     */
    public boolean waitForSearchResults(ExtentTest test) {
        return waitUtils.waitForSearchResults(test);
    }
    
    /**
     * Wait for premium elements with multiple strategies
     * Replaces: nested try-catch blocks with fallback selectors
     * Usage: SmartWaitHelper.waitForPremiumElements(test);
     */
    public boolean waitForPremiumElements(ExtentTest test) {
        return waitUtils.waitForPremiumElements(test);
    }
    
    /**
     * Wait for menu navigation elements
     * Usage: SmartWaitHelper.waitForMenuElements(test);
     */
    public boolean waitForMenuElements(ExtentTest test) {
        return waitUtils.waitForElementsWithFallback(
            CommonElementLocators.getLocatorsForElementType(ElementType.MENU), 
            "Menu navigation elements", 
            test
        );
    }
    
    /**
     * Wait for text input elements (search fields, forms)
     * Usage: SmartWaitHelper.waitForInputElements(test);
     */
    public boolean waitForInputElements(ExtentTest test) {
        return waitUtils.waitForElementsWithFallback(
            CommonElementLocators.getLocatorsForElementType(ElementType.INPUT), 
            "Input elements", 
            test
        );
    }
    
    /**
     * Wait for any clickable button elements
     * Usage: SmartWaitHelper.waitForButtonElements(test);
     */
    public boolean waitForButtonElements(ExtentTest test) {
        return waitUtils.waitForElementsWithFallback(
            CommonElementLocators.getLocatorsForElementType(ElementType.BUTTON), 
            "Button elements", 
            test
        );
    }
    
    /**
     * Wait for page loading indicators to disappear
     * Usage: SmartWaitHelper.waitForPageLoad(test);
     */
    public boolean waitForPageLoad(ExtentTest test) {
        By[] loadingSelectors = CommonElementLocators.getLocatorsForElementType(ElementType.LOADING);
        
        // Wait for loading elements to disappear
        for (By selector : loadingSelectors) {
            try {
                waitUtils.waitForInvisibility(selector);
                if (test != null) {
                    test.info("Page loading completed - loading indicator disappeared");
                }
                return true;
            } catch (Exception e) {
                // Continue to next selector or assume no loading indicator
            }
        }
        
        if (test != null) {
            test.info("No loading indicators found - page assumed to be loaded");
        }
        return true;
    }
    
    /**
     * Static convenience method for quick access
     * Usage: SmartWaitHelper.quickWaitForSearchResults(test);
     */
    public static boolean quickWaitForSearchResults(ExtentTest test) {
        return new SmartWaitHelper().waitForSearchResults(test);
    }
    
    /**
     * Static convenience method for premium elements
     * Usage: SmartWaitHelper.quickWaitForPremiumElements(test);
     */
    public static boolean quickWaitForPremiumElements(ExtentTest test) {
        return new SmartWaitHelper().waitForPremiumElements(test);
    }
    
    // =================== ADDITIONAL CONVENIENCE METHODS ===================
    
    /**
     * Wait for cancel/close buttons
     * Usage: SmartWaitHelper.waitForCancelElements(test);
     */
    public boolean waitForCancelElements(ExtentTest test) {
        return waitUtils.waitForElementsWithFallback(
            CommonElementLocators.getLocatorsForElementType(ElementType.CANCEL), 
            "Cancel/Close elements", 
            test
        );
    }
    
    /**
     * Generic wait method for any element type
     * Usage: SmartWaitHelper.waitForElementType(ElementType.MENU, test);
     */
    public boolean waitForElementType(ElementType elementType, ExtentTest test) {
        return waitUtils.waitForElementsWithFallback(
            CommonElementLocators.getLocatorsForElementType(elementType), 
            elementType.name() + " elements", 
            test
        );
    }
    
    // =================== STATIC CONVENIENCE METHODS ===================
    
    /**
     * Static method for menu elements
     */
    public static boolean quickWaitForMenuElements(ExtentTest test) {
        return new SmartWaitHelper().waitForMenuElements(test);
    }
    
    /**
     * Static method for input elements  
     */
    public static boolean quickWaitForInputElements(ExtentTest test) {
        return new SmartWaitHelper().waitForInputElements(test);
    }
    
    /**
     * Static method for button elements
     */
    public static boolean quickWaitForButtonElements(ExtentTest test) {
        return new SmartWaitHelper().waitForButtonElements(test);
    }
    
    /**
     * Static method for cancel elements
     */
    public static boolean quickWaitForCancelElements(ExtentTest test) {
        return new SmartWaitHelper().waitForCancelElements(test);
    }
    
    /**
     * Static method for any element type
     */
    public static boolean quickWaitForElementType(ElementType elementType, ExtentTest test) {
        return new SmartWaitHelper().waitForElementType(elementType, test);
    }
}