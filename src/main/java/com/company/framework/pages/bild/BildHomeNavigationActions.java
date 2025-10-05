package com.company.framework.pages.bild;

import com.company.framework.interfaces.INavigationActions;
import com.company.framework.interfaces.IPageActions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import java.util.List;

/**
 * BildHomeNavigationActions - Handles navigation actions for BILD home page
 * Follows Single Responsibility Principle - only navigation logic
 */
public class BildHomeNavigationActions implements INavigationActions {
    
    private static final Logger logger = LogManager.getLogger(BildHomeNavigationActions.class);
    private final IPageActions pageActions;
    private final BildHomeElements elements;
    private final AppiumDriver driver;
    
    public BildHomeNavigationActions(IPageActions pageActions, BildHomeElements elements, AppiumDriver driver) {
        this.pageActions = pageActions;
        this.elements = elements;
        this.driver = driver;
    }
    
    // Backward compatibility constructor
    public BildHomeNavigationActions(IPageActions pageActions, BildHomeElements elements) {
        this.pageActions = pageActions;
        this.elements = elements;
        this.driver = null; // Will need to be handled in methods
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
        logger.info("Going back to previous page");
        
        try {
            // Approach 1: Try Android back button using key event (if driver is available)
            if (driver != null && driver instanceof AndroidDriver) {
                try {
                    ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
                    logger.info("Successfully performed back navigation using Android back key");
                    return;
                } catch (Exception e1) {
                    logger.warn("Android back key failed: " + e1.getMessage());
                }
            }
            
            // Approach 2: Try WebDriver navigate back (if driver is available)
            if (driver != null) {
                try {
                    driver.navigate().back();
                    logger.info("Successfully performed back navigation using WebDriver navigate().back()");
                    return;
                } catch (Exception e2) {
                    logger.warn("WebDriver navigate().back() failed: " + e2.getMessage());
                }
            }
            
            // Approach 3: Look for back button elements on screen
            try {
                By[] backButtonLocators = {
                    By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                    By.xpath("//*[@content-desc='Back' or @content-desc='back']"),
                    By.xpath("//android.widget.Button[contains(@text, 'Back') or contains(@text, 'Zurück')]"),
                    By.xpath("//*[contains(@class, 'BackButton') or contains(@resource-id, 'back')]"),
                    By.xpath("//android.widget.ImageView[@content-desc='Navigate up']")
                };
                
                for (By backLocator : backButtonLocators) {
                    try {
                        if (driver != null) {
                            List<WebElement> backElements = driver.findElements(backLocator);
                            if (!backElements.isEmpty() && pageActions.isDisplayed(backElements.get(0))) {
                                pageActions.waitAndClick(backElements.get(0));
                                logger.info("Successfully clicked back button using locator: " + backLocator);
                                return;
                            }
                        }
                    } catch (Exception be) {
                        logger.debug("Back button locator failed: {} - {}", backLocator, be.getMessage());
                    }
                }
                
                logger.warn("No back button found on screen");
            } catch (Exception e3) {
                logger.warn("Back button search failed: " + e3.getMessage());
            }
            
            logger.error("All back navigation approaches failed - no driver available and no back button found");
            
        } catch (Exception e) {
            logger.error("Critical error in goBack method: " + e.getMessage());
            throw new RuntimeException("Go back navigation failed: " + e.getMessage(), e);
        }
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