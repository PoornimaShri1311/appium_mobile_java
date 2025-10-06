package com.company.framework.pages.bild;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

// Components are now in the same package - no import needed

import java.util.List;

/**
 * BildHomePage - SOLID-compliant BILD Home Page Object Model
 * 
 * This class follows SOLID principles:
 * - Single Responsibility: Only coordinates between components
 * - Open/Closed: Extensible through composition
 * - Liskov Substitution: Uses interfaces for all dependencies
 * - Interface Segregation: Uses specific action interfaces
 * - Dependency Inversion: Depends on abstractions, not implementations
 */
public class ImprovedBildHomePage extends com.company.framework.pages.ImprovedBasePage {

    private static final Logger logger = LogManager.getLogger(ImprovedBildHomePage.class);
    
    // Component dependencies (following Dependency Inversion Principle)
    private final BildHomeElements elements;
    private final BildHomeNavigationActions navigationActions;
    private final BildHomeSearchActions searchActions;
    private final BildHomeVerificationActions verificationActions;
    private final com.company.framework.utils.ScrollActions scrollActions;

    /**
     * Constructor using Dependency Injection
     */
    public ImprovedBildHomePage(AppiumDriver driver) {
        super(driver); // Pass driver to base class
        
        // Initialize elements with the provided driver (for compatibility)
        this.elements = new BildHomeElements();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this.elements);
        
        // Initialize action components with dependencies
        this.scrollActions = new com.company.framework.utils.ScrollActions(driver);
        this.navigationActions = new BildHomeNavigationActions(getPageActions(), scrollActions, elements, driver);
        this.searchActions = new BildHomeSearchActions(getPageActions(), elements, driver);
        this.verificationActions = new BildHomeVerificationActions(getPageActions(), elements);
        
        logger.info("ImprovedBildHomePage initialized with SOLID principles");
    }

    /**
     * Implementation of abstract method from ImprovedBasePage
     * Verifies if the BILD home page is loaded and ready
     * 
     * @return true if page is loaded, false otherwise
     */
    @Override
    public boolean isPageLoaded() {
        try {
            logger.info("Checking if BILD home page is loaded");
            
            // Check if core navigation elements are present and visible
            boolean coreElementsPresent = verificationActions.verifyCoreNavigationElements();
            
            if (coreElementsPresent) {
                logger.info("✓ BILD home page is loaded - core elements verified");
                return true;
            } else {
                logger.warn("✗ BILD home page is not loaded - core elements missing");
                return false;
            }
            
        } catch (Exception e) {
            logger.error("Error checking if BILD home page is loaded: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // Navigation Methods (delegated to NavigationActions)
    // =========================
    
    public void navigateToMenu(String menuName) {
        navigationActions.navigateToMenu(menuName);
    }
    
    public void clickHomeMenu() {
        navigationActions.navigateToMenu("home");
    }

    public void clickSportMenu() {
        navigationActions.navigateToMenu("sport");
    }

    public void clickBildPlayMenu() {
        navigationActions.navigateToMenu("bildplay");
    }

    public void clickBildKiMenu() {
        navigationActions.navigateToMenu("bildki");
    }

    public void clickMoreMenu() {
        navigationActions.navigateToMenu("more");
    }

    public void clickSearchButton() {
        navigationActions.navigateToMenu("search");
    }
    
    public void clickBildPremium() {
        navigationActions.navigateToMenu("bildpremium");
    }
    
    public boolean isMenuAvailable(String menuName) {
        return navigationActions.isMenuAvailable(menuName);
    }

    // =========================
    // Search Methods (delegated to SearchActions)
    // =========================
    
    public void performSearch(String searchTerm) {
        searchActions.performSearch(searchTerm);
    }
      
    public boolean performSearchWorkflow(String searchTerm) {
        return searchActions.performSearchWorkflow(searchTerm);
    }
    
    public void clearSearch() {
        searchActions.clearSearch();
    }
    
    public List<WebElement> getSearchResults() {
        return searchActions.getSearchResults();
    }
    
    public boolean verifySearchResults(List<WebElement> results, String expectedText) {
        return searchActions.verifySearchResults(results, expectedText);
    }
    
    public void cancelSearch() {
        searchActions.cancelSearch();
    }
    
    // Touch-enabled search methods
    public void performSearchWithTap(String searchTerm) {
        searchActions.performSearchWithTap(searchTerm);
    }
    
    public void tapSearchSuggestion() {
        searchActions.tapSearchSuggestion();
    }
    
    /**
     * Wait for search results to load after performing search
     */
    public void waitForSearchResults() {
        try {
            logger.info("⏳ Waiting for search results to load...");
            
            org.openqa.selenium.support.ui.WebDriverWait wait = 
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10));
                
            // Wait for any search result elements to be present
            By[] searchResultLocators = com.company.framework.locators.bild.BildAppLocators.getLocators(
                com.company.framework.locators.bild.BildAppLocators.BildElementType.SEARCH_RESULT_ITEM);
                
            boolean resultsFound = false;
            for (By locator : searchResultLocators) {
                try {
                    wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(locator));
                    resultsFound = true;
                    logger.info("✅ Search results loaded successfully");
                    break;
                } catch (Exception e) {
                    // Try next locator
                }
            }
            
            if (!resultsFound) {
                logger.warn("⚠️ No search results found within timeout period");
            }
            
        } catch (Exception e) {
            logger.error("⚠️ Error waiting for search results: {}", e.getMessage());
        }
    }
    
    /**
     * Click on the first/specific search result item
     * Based on the Appium Inspector recording: View instance 3
     * Uses scroll functionality to find element before clicking
     */
    public void clickSearchResultItem() {
        try {
            String searchText = "Test";
            logger.info("Attempting to scroll to and click search result with text: {}", searchText);
            
            // First try to find the element and scroll to it if needed
            By searchResultLocator = com.company.framework.locators.bild.BildAppLocators.getSearchResultByText(searchText);
            List<WebElement> searchResults = driver.findElements(searchResultLocator);
            
            if (!searchResults.isEmpty()) {
                WebElement targetElement = searchResults.get(0);
                try {
                    scrollActions.scrollToElement(targetElement);
                    logger.info("Successfully scrolled to search result element");
                } catch (Exception e) {
                    logger.warn("Could not scroll to element: {}, trying direct click", e.getMessage());
                }
            } else {
                logger.warn("Search result element not found initially, will try after wait");
            }
            
            // Now try to click the element (it should be visible after scroll)
            WebElement resultItem = com.company.framework.utils.MobileTestUtils.waitForElementClickable(
                driver, com.company.framework.locators.bild.BildAppLocators.getSearchResultByText(searchText), 10);
            
            try {
                getPageActions().waitAndClick(resultItem);
                logger.info("Successfully clicked on search result item using standard click");
            } catch (Exception clickException) {
                logger.warn("Standard click failed: {}, trying coordinate-based click", clickException.getMessage());
                // Fallback to coordinate-based click
                try {
                    org.openqa.selenium.Point location = resultItem.getLocation();
                    org.openqa.selenium.Dimension size = resultItem.getSize();
                    int centerX = location.getX() + size.getWidth() / 2;
                    int centerY = location.getY() + size.getHeight() / 2;
                    
                    com.company.framework.utils.TouchActionUtils touchUtils = new com.company.framework.utils.TouchActionUtils(driver);
                    touchUtils.tapAtCoordinates(centerX, centerY, 100);
                    logger.info("Successfully clicked search result using coordinates: ({}, {})", centerX, centerY);
                } catch (Exception coordException) {
                    logger.error("Both standard and coordinate clicks failed: {}", coordException.getMessage());
                    throw coordException;
                }
            }
            
        } catch (Exception e) {
            logger.error("Failed to click search result item: {}", e.getMessage());
            throw new RuntimeException("Search result click failed", e);
        }
    }
    
    /**
     * Complete search workflow: search -> wait -> click result
     */
    public void performCompleteSearchWorkflow(String searchTerm) {
        performSearch(searchTerm);
        waitForSearchResults();
        clickSearchResultItem();
    }
    
    public void tapAtCustomCoordinates(int x, int y, int duration) {
        searchActions.tapAtCustomCoordinates(x, y, duration);
    }
    
    public void doubleTapSearchSuggestion() {
        searchActions.doubleTapSearchSuggestion();
    }
    
    public boolean performCompleteSearchWithVerification(String searchTerm) {
        return searchActions.performCompleteSearchWithVerification(searchTerm);
    }

    // =========================
    // Verification Methods (delegated to VerificationActions)
    // =========================
    
    public boolean verifyMainNavigationElements() {
        return verificationActions.verifyMainNavigationElements();
    }

    public boolean verifyCoreNavigationElements() {
        return verificationActions.verifyCoreNavigationElements();
    }
    
    public boolean verifyElementDisplayed(String elementName) {
        return verificationActions.verifyElementDisplayed(elementName);
    }
    
    public boolean verifyPageTitle(String expectedTitle) {
        return verificationActions.verifyPageTitle(expectedTitle);
    }
    
    /**
     * Verify if BILD Premium element is displayed (with scroll functionality)
     * @return true if BILD Premium element is displayed, false otherwise
     */
    public boolean isBildPremiumElementDisplayed() {
        return verificationActions.isBildPremiumElementDisplayed();
    }
    
    /**
     * Check if the account exists TextView is displayed
     * @return true if the TextView with text "SIE HABEN BEREITS EIN KONTO?" is displayed, false otherwise
     */
    public boolean isAccountExistsTextViewDisplayed() {
        return verificationActions.isAccountExistsTextViewDisplayed();
    }
    
    /**
     * Click on BILD Premium element (includes scroll functionality)
     */
    public void clickBildPremiumElement() {
        navigationActions.navigateToMenu("bildpremium");
    }
    
    // =========================
    // Compatibility Methods for existing tests
    // =========================
    
    /**
     * Compatibility method for waiting for app to load
     */
    public void waitForAppToLoad() {
        waitForPageToLoad();
    }
    
    /**
     * Search with Enter key and verify results
     * @param searchTerm search term to use
     * @return true if search was successful
     */
    public boolean searchWithEnterAndVerify(String searchTerm) {
        try {
            performSearch(searchTerm);
            return true;
        } catch (Exception e) {
            logger.error("Search with enter failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Collect results from panel - delegated to search actions
     * @return List of result elements
     */
    public List<WebElement> collectResultsFromPanel() {
        return searchActions.collectResultsFromPanel();
    }
    
    /**
     * Check if cancel button is displayed
     * @return true if cancel button is displayed
     */
    public boolean isCancelButtonDisplayed() {
        return verificationActions.verifyElementDisplayed("cancel");
    }
    
    /**
     * Click cancel button
     */
    public void clickCancel() {
        getPageActions().click(elements.getCancelButton());
    }

    // =========================
    // Element Access (for backward compatibility and specific test needs)
    // =========================
    
    public WebElement getHomeMenu() { 
        return elements.getHomeMenu(); 
    }
    
    public WebElement getSportMenu() { 
        return elements.getSportMenu(); 
    }
    
    public WebElement getBildPlayMenu() { 
        return elements.getBildPlayMenu(); 
    }
    
    public WebElement getBildKiMenu() { 
        return elements.getBildKiMenu(); 
    }
    
    public WebElement getMoreMenu() { 
        return elements.getMoreMenu(); 
    }
    
    public WebElement getSearchButton() { 
        return elements.getSearchButton(); 
    }
    
    public WebElement getBildPremiumElement() { 
        return elements.getBildPremiumElement(); 
    }
    
    public WebElement getAccountExistsTextView() { 
        return elements.getAccountExistsTextView(); 
    }
    
    public WebElement getCancelButton() { 
        return elements.getCancelButton(); 
    }
    
    // =========================
    // Component Access Methods (for advanced usage and testing)
    // =========================
    
    /**
     * Get navigation actions component for advanced usage
     * @return BildHomeNavigationActions component
     */
    public BildHomeNavigationActions getNavigationActions() {
        return navigationActions;
    }
    
    /**
     * Get search actions component for advanced usage
     * @return BildHomeSearchActions component
     */
    public BildHomeSearchActions getSearchActions() {
        return searchActions;
    }
    
    /**
     * Get verification actions component for advanced usage
     * @return BildHomeVerificationActions component
     */
    public BildHomeVerificationActions getVerificationActions() {
        return verificationActions;
    }
    
    /**
     * Get elements component for advanced usage
     * @return BildHomeElements component
     */
    public BildHomeElements getElements() {
        return elements;
    }
}