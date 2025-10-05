package com.company.framework.interfaces;

/**
 * IVerificationActions - Interface for verification-specific actions
 * Follows Interface Segregation Principle
 */
public interface IVerificationActions {
    
    /**
     * Verify main navigation elements are present
     * @return true if all main elements are present
     */
    boolean verifyMainNavigationElements();
    
    /**
     * Verify core navigation elements are present
     * @return true if core elements are present
     */
    boolean verifyCoreNavigationElements();
    
    /**
     * Verify specific element is displayed
     * @param elementName name of the element to verify
     * @return true if element is displayed
     */
    boolean verifyElementDisplayed(String elementName);
    
    /**
     * Verify page title contains expected text
     * @param expectedTitle expected title text
     * @return true if title matches
     */
    boolean verifyPageTitle(String expectedTitle);
}