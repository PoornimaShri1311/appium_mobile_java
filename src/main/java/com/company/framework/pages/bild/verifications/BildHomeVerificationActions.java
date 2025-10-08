package com.company.framework.pages.bild.verifications;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.company.framework.interfaces.actions.IPageActions;
import com.company.framework.interfaces.actions.IScrollActions;
import com.company.framework.interfaces.actions.IVerificationActions;
import com.company.framework.pages.bild.elements.BildHomeElements;

/**
 * BildHomeVerificationActions - Handles verification actions for BILD home page
 * 
 * This class is responsible for all verification-related actions on the BILD home page.
 * Follows Single Responsibility Principle - only verification logic.
 * 
 * Moved to verifications package for better organization and separation of concerns.
 */
public class BildHomeVerificationActions implements IVerificationActions {
    
    private static final Logger logger = LogManager.getLogger(BildHomeVerificationActions.class);
    private final IPageActions pageActions;
    private final BildHomeElements elements;
    
    public BildHomeVerificationActions(IPageActions pageActions, BildHomeElements elements) {
        this.pageActions = pageActions;
        this.elements = elements;
    }
    
    @Override
    public boolean verifyMainNavigationElements() {
        logger.info("Verifying main navigation elements");
        
        try {
            boolean homeMenu = pageActions.isDisplayed(elements.getHomeMenu());
            boolean sportMenu = pageActions.isDisplayed(elements.getSportMenu());
            boolean bildPlayMenu = pageActions.isDisplayed(elements.getBildPlayMenu());
            boolean bildKiMenu = pageActions.isDisplayed(elements.getBildKiMenu());
            boolean moreMenu = pageActions.isDisplayed(elements.getMoreMenu());
            boolean searchButton = pageActions.isDisplayed(elements.getSearchButton());
            
            // BILD Premium is optional
            boolean premiumElement = false;
            try {
                premiumElement = pageActions.isDisplayed(elements.getBildPremiumElement());
            } catch (Exception e) {
                logger.info("BILD Premium element is optional and not found: " + e.getMessage());
            }
            
            boolean allMainElementsPresent = homeMenu && sportMenu && bildPlayMenu && 
                                           bildKiMenu && moreMenu && searchButton;
            
            logger.info(String.format("Navigation elements verification: Home=%s, Sport=%s, BildPlay=%s, " +
                    "BildKI=%s, More=%s, Search=%s, Premium=%s (optional)",
                    homeMenu, sportMenu, bildPlayMenu, bildKiMenu, moreMenu, searchButton, premiumElement));
            
            return allMainElementsPresent;
            
        } catch (Exception e) {
            logger.error("Error during main navigation verification: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean verifyCoreNavigationElements() {
        logger.info("Verifying core navigation elements");
        
        try {
            // Core elements are the most essential ones
            boolean homeMenu = pageActions.isDisplayed(elements.getHomeMenu());
            boolean searchButton = pageActions.isDisplayed(elements.getSearchButton());
            
            logger.info(String.format("Core navigation verification: Home=%s, Search=%s", 
                    homeMenu, searchButton));
            
            return homeMenu && searchButton;
            
        } catch (Exception e) {
            logger.error("Error during core navigation verification: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean verifyElementDisplayed(String elementName) {
        logger.info("Verifying element displayed: " + elementName);
        
        try {
            switch (elementName.toLowerCase()) {
                case "home":
                    return pageActions.isDisplayed(elements.getHomeMenu());
                case "sport":
                    return pageActions.isDisplayed(elements.getSportMenu());
                case "bildplay":
                    return pageActions.isDisplayed(elements.getBildPlayMenu());
                case "bildki":
                    return pageActions.isDisplayed(elements.getBildKiMenu());
                case "more":
                    return pageActions.isDisplayed(elements.getMoreMenu());
                case "search":
                    return pageActions.isDisplayed(elements.getSearchButton());
                case "bildpremium":
                    return isBildPremiumElementDisplayed();
                case "accounttext":
                    return pageActions.isDisplayed(elements.getAccountExistsTextView());
                case "cancel":
                    return pageActions.isDisplayed(elements.getCancelButton());
                default:
                    logger.warn("Unknown element name: " + elementName);
                    return false;
            }
        } catch (Exception e) {
            logger.info("Element '" + elementName + "' not displayed: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean verifyPageTitle(String expectedTitle) {
        logger.info("Verifying page title contains: " + expectedTitle);
        
        try {
            // Since this is a mobile app, we might check app name or specific text elements
            // This could be customized based on how the app displays title information
            return true; // Placeholder - implement based on app structure
        } catch (Exception e) {
            logger.error("Error verifying page title: " + e.getMessage());
            return false;
        }
    }
    
    // =================== BILD-SPECIFIC VERIFICATION METHODS ===================
    
    /**
     * Verify if BILD Premium element is displayed with scroll functionality
     * @return true if BILD Premium element is displayed, false otherwise
     */
    public boolean isBildPremiumElementDisplayed() {
        try {
            logger.info("Checking if BILD Premium element is displayed");
            // First scroll to make element visible
            if (pageActions instanceof IScrollActions) {
                ((IScrollActions) pageActions).scrollToElement(elements.getBildPremiumElement());
            }
            
            // Try to wait for visibility
            boolean isDisplayed = pageActions.isDisplayed(elements.getBildPremiumElement());
            logger.info("BILD Premium element displayed: " + isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            logger.info("BILD Premium element not found or not displayed: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if the account exists TextView is displayed
     * @return true if the TextView with text "SIE HABEN BEREITS EIN KONTO?" is displayed, false otherwise
     */
    public boolean isAccountExistsTextViewDisplayed() {
        try {
            boolean isDisplayed = pageActions.isDisplayed(elements.getAccountExistsTextView());
            logger.info("Account exists TextView displayed: " + isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            logger.info("Account exists TextView not found or not displayed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Verify if search functionality is available and working
     * @return true if search button is displayed and clickable
     */
    public boolean verifySearchFunctionality() {
        try {
            logger.info("Verifying search functionality availability");
            boolean searchDisplayed = pageActions.isDisplayed(elements.getSearchButton());
            boolean searchEnabled = elements.getSearchButton().isEnabled();
            
            logger.info("Search functionality verification: displayed={}, enabled={}", searchDisplayed, searchEnabled);
            return searchDisplayed && searchEnabled;
        } catch (Exception e) {
            logger.error("Error verifying search functionality: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Verify if all menu items are accessible and functional
     * @return true if all menu items are displayed and enabled
     */
    public boolean verifyAllMenusAccessible() {
        try {
            logger.info("Verifying all menus are accessible");
            
            boolean homeAccessible = pageActions.isDisplayed(elements.getHomeMenu()) && elements.getHomeMenu().isEnabled();
            boolean sportAccessible = pageActions.isDisplayed(elements.getSportMenu()) && elements.getSportMenu().isEnabled();
            boolean bildPlayAccessible = pageActions.isDisplayed(elements.getBildPlayMenu()) && elements.getBildPlayMenu().isEnabled();
            boolean bildKiAccessible = pageActions.isDisplayed(elements.getBildKiMenu()) && elements.getBildKiMenu().isEnabled();
            boolean moreAccessible = pageActions.isDisplayed(elements.getMoreMenu()) && elements.getMoreMenu().isEnabled();
            
            boolean allAccessible = homeAccessible && sportAccessible && bildPlayAccessible && bildKiAccessible && moreAccessible;
            
            logger.info("Menu accessibility: Home={}, Sport={}, BildPlay={}, BildKI={}, More={}", 
                homeAccessible, sportAccessible, bildPlayAccessible, bildKiAccessible, moreAccessible);
                
            return allAccessible;
        } catch (Exception e) {
            logger.error("Error verifying menu accessibility: " + e.getMessage());
            return false;
        }
    }
}