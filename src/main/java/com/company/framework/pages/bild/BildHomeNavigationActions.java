package com.company.framework.pages.bild;

import com.company.framework.interfaces.INavigationActions;
import com.company.framework.interfaces.IPageActions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * BildHomeNavigationActions - Handles navigation actions for BILD home page
 * Follows Single Responsibility Principle - only navigation logic
 */
public class BildHomeNavigationActions implements INavigationActions {
    
    private static final Logger logger = LogManager.getLogger(BildHomeNavigationActions.class);
    private final IPageActions pageActions;
    private final BildHomeElements elements;
    
    public BildHomeNavigationActions(IPageActions pageActions, BildHomeElements elements) {
        this.pageActions = pageActions;
        this.elements = elements;
    }
    
    @Override
    public void navigateToMenu(String menuName) {
        logger.info("Navigating to menu: " + menuName);
        
        switch (menuName.toLowerCase()) {
            case "home":
                clickHomeMenu();
                break;
            case "sport":
                clickSportMenu();
                break;
            case "bild play":
                clickBildPlayMenu();
                break;
            case "bild ki":
                clickBildKiMenu();
                break;
            case "more":
                clickMoreMenu();
                break;
            case "search":
                clickSearchButton();
                break;
            case "bild premium":
            case "premium":
                clickBildPremium();
                break;
            default:
                logger.error("Unknown menu name: " + menuName);
                throw new IllegalArgumentException("Menu '" + menuName + "' is not recognized. " +
                    "Available menus: home, sport, bildplay, bildki, more, search, bildpremium");
        }
    }
    
    @Override
    public void goBack() {
        // Implementation for going back - could use driver.navigate().back() or specific back button
        logger.info("Going back to previous page");
    }
    
    @Override
    public void refresh() {
        // Implementation for refresh
        logger.info("Refreshing current page");
    }
    
    @Override
    public boolean isMenuAvailable(String menuName) {
        switch (menuName.toLowerCase()) {
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
                return pageActions.isDisplayed(elements.getBildPremiumElement());
            default:
                return false;
        }
    }
    
    // Private helper methods for specific menu clicks
    private void clickHomeMenu() {
        pageActions.waitAndClick(elements.getHomeMenu());
        logger.info("Clicked on Home menu");
    }
    
    private void clickSportMenu() {
        pageActions.waitAndClick(elements.getSportMenu());
        logger.info("Clicked on Sport menu");
    }
    
    private void clickBildPlayMenu() {
        pageActions.waitAndClick(elements.getBildPlayMenu());
        logger.info("Clicked on BILD Play menu");
    }
    
    private void clickBildKiMenu() {
        pageActions.waitAndClick(elements.getBildKiMenu());
        logger.info("Clicked on BILD KI menu");
    }
    
    private void clickMoreMenu() {
        pageActions.waitAndClick(elements.getMoreMenu());
        logger.info("Clicked on More menu");
    }
    
    private void clickSearchButton() {
        pageActions.waitAndClick(elements.getSearchButton());
        logger.info("Clicked on search button");
    }
    
    private void clickBildPremium() {
        try {
            // First scroll to make element visible
            pageActions.scrollToElement(elements.getBildPremiumElement());
            pageActions.waitAndClick(elements.getBildPremiumElement());
            logger.info("Clicked on BILD Premium element");
        } catch (Exception e) {
            logger.error("Failed to click BILD Premium element: " + e.getMessage());
            throw new RuntimeException("BILD Premium element not clickable: " + e.getMessage(), e);
        }
    }
}