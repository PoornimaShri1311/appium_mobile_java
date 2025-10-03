package pages.components;

import config.ApplicationConstants;
import interfaces.ISearchActions;
import interfaces.IPageActions;
import interfaces.IWaitStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import io.appium.java_client.AppiumDriver;
import utils.TouchActionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * BildHomeSearchActions - Handles search actions for BILD home page
 * Follows Single Responsibility Principle - only search logic
 */
public class BildHomeSearchActions implements ISearchActions {
    
    private static final Logger logger = LogManager.getLogger(BildHomeSearchActions.class);
    private final IPageActions pageActions;
    private final BildHomeElements elements;
    private final AppiumDriver driver;
    private final TouchActionUtils touchActions;
    
    public BildHomeSearchActions(IPageActions pageActions, IWaitStrategy waitStrategy, 
                                BildHomeElements elements, AppiumDriver driver) {
        this.pageActions = pageActions;
        this.elements = elements;
        this.driver = driver;
        this.touchActions = new TouchActionUtils(driver);
    }
    
    @Override
    public void performSearch(String searchTerm) {
        logger.info("Performing search for: " + searchTerm);
        
        try {
            // Open search first
            if (!openSearch()) {
                throw new RuntimeException("Could not open search functionality");
            }
            
            // Find and use search input
            WebElement searchField = findSearchInput();
            if (searchField == null) {
                throw new RuntimeException("Search input field not found");
            }
            
            pageActions.enterText(searchField, searchTerm);
            
            // Wait for search suggestions to appear
            Thread.sleep(1000);
            
            logger.info("Search performed successfully for: " + searchTerm);
            
        } catch (Exception e) {
            logger.error("Search operation failed: " + e.getMessage());
            throw new RuntimeException("Search failed: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void clearSearch() {
        logger.info("Clearing search field");
        WebElement searchField = findSearchInput();
        if (searchField != null) {
            searchField.clear();
        }
    }
    
    @Override
    public List<WebElement> getSearchResults() {
        logger.info("Collecting search results from results panel");
        
        List<WebElement> results = new ArrayList<>();
        
        try {
            Thread.sleep(ApplicationConstants.DELAY_SEARCH_EXECUTION); // Wait for results to load
            
            // Target the specific results panel
            String panelXPath = ApplicationConstants.XPATH_ANDROIDX_COMPOSEVIEW + "/android.view.View/android.view.View/android.view.View/android.view.View[1]";
            List<WebElement> panelChildren = driver.findElements(By.xpath(panelXPath + "//android.view.View"));
            
            if (!panelChildren.isEmpty()) {
                results.addAll(panelChildren);
                logger.info("Found " + panelChildren.size() + " result elements in the results panel");
            } else {
                // Alternative search
                List<WebElement> alternativeChildren = driver.findElements(By.xpath(panelXPath + "//*"));
                results.addAll(alternativeChildren);
                logger.info("Alternative: Found " + alternativeChildren.size() + " elements in the panel");
            }
            
        } catch (Exception e) {
            logger.error("Error collecting results from panel: " + e.getMessage());
        }
        
        return results;
    }
    
    @Override
    public boolean verifySearchResults(List<WebElement> results, String expectedText) {
        logger.info("Verifying search results contain: " + expectedText);
        
        if (results.isEmpty()) {
            logger.warn("No search results found to verify");
            return false;
        }
        
        boolean found = false;
        int resultCount = 0;
        
        for (WebElement result : results) {
            resultCount++;
            try {
                String resultText = getElementText(result);
                
                if (resultText != null && !resultText.isEmpty()) {
                    String displayText = resultText.length() > 100 ? resultText.substring(0, 100) + "..." : resultText;
                    logger.info("Result " + resultCount + " text: " + displayText);
                    
                    if (resultText.toLowerCase().contains(expectedText.toLowerCase())) {
                        logger.info("Found search term '" + expectedText + "' in result " + resultCount);
                        found = true;
                    }
                } else {
                    logger.info("Result " + resultCount + " has no readable text");
                }
                
            } catch (Exception e) {
                logger.warn("Could not get text from result " + resultCount + ": " + e.getMessage());
            }
        }
        
        logger.info("Search verification result: " + (found ? "PASS" : "FAIL") + " (checked " + resultCount + " results)");
        return found;
    }
    
    @Override
    public void cancelSearch() {
        logger.info("Cancelling search operation");
        
        try {
            if (pageActions.isDisplayed(elements.getCancelButton())) {
                pageActions.waitAndClick(elements.getCancelButton());
                logger.info("Clicked Cancel button");
            } else {
                logger.info("Cancel button not available");
            }
        } catch (Exception e) {
            logger.info("Could not cancel search: " + e.getMessage());
        }
    }
    
    // Private helper methods
    private boolean openSearch() {
        try {
            pageActions.waitAndClick(elements.getSearchButton());
            Thread.sleep(1000);
            return isSearchInputAvailable();
        } catch (Exception e) {
            logger.info("Could not open search: " + e.getMessage());
            return false;
        }
    }
    
    private boolean isSearchInputAvailable() {
        try {
            List<WebElement> editTexts = driver.findElements(By.xpath(ApplicationConstants.XPATH_ANDROID_EDITTEXT));
            return !editTexts.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    
    private WebElement findSearchInput() {
        try {
            // Try the original searchInput element first
            if (pageActions.isDisplayed(elements.getSearchInput())) {
                return elements.getSearchInput();
            }
        } catch (Exception e) {
            logger.info("Original search input not available: " + e.getMessage());
        }
        
        // Fallback to any EditText element
        try {
            List<WebElement> editTexts = driver.findElements(By.xpath("//android.widget.EditText"));
            if (!editTexts.isEmpty()) {
                return editTexts.get(0);
            }
        } catch (Exception e) {
            logger.info("No EditText elements found: " + e.getMessage());
        }
        
        return null;
    }
    
    private String getElementText(WebElement element) {
        // Try multiple methods to get text
        String text = null;
        
        try { text = element.getAttribute("content-desc"); } catch (Exception e) { /* ignore */ }
        if (text == null || text.isEmpty()) {
            try { text = element.getAttribute("text"); } catch (Exception e) { /* ignore */ }
        }
        if (text == null || text.isEmpty()) {
            try { text = element.getText(); } catch (Exception e) { /* ignore */ }
        }
        if (text == null || text.isEmpty()) {
            try { text = element.getAttribute("resource-id"); } catch (Exception e) { /* ignore */ }
        }
        
        return text;
    }
    
    /**
     * Perform complete search workflow for Umfrage with specific panel targeting
     * @param searchTerm The search term to use
     * @return true if results contain the search term, false otherwise
     */
    public boolean performUmfrageSearchWorkflow(String searchTerm) {
        logger.info("Starting Umfrage search workflow for: " + searchTerm);
        
        try {
            // Step 1: Open search and enter text (without Enter key)
            boolean searchOpened = openSearch();
            if (!searchOpened) {
                logger.error("Could not open search functionality");
                return false;
            }
            
            WebElement searchField = findSearchInput();
            if (searchField == null) {
                logger.error("Could not find search input field");
                return false;
            }
            
            pageActions.enterText(searchField, searchTerm);
            logger.info("Entered '" + searchTerm + "' in search field");
            
            // Step 2: Wait for results to appear (no Enter key needed)
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Step 3: Collect results from the specific panel
            List<WebElement> results = collectResultsFromPanel();
            logger.info("Collected " + results.size() + " results from panel");
            
            // Step 4: Verify if any result contains the search term
            boolean found = verifySearchResults(results, searchTerm);
            
            logger.info("Umfrage search workflow completed. Found match: " + found);
            return found;
            
        } catch (Exception e) {
            logger.error("Umfrage search workflow failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Collect search results from the specific results panel
     * @return List of result elements
     */
    public List<WebElement> collectResultsFromPanel() {
        logger.info("Collecting search results from specific results panel");
        
        List<WebElement> results = new ArrayList<>();
        
        try {
            // Wait for results to appear
            Thread.sleep(3000);
            
            // Target the specific results panel: //androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View/android.view.View[1]
            String panelXPath = "//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View/android.view.View[1]";
            
            // Get all child elements within this panel
            List<WebElement> panelChildren = driver.findElements(By.xpath(panelXPath + "//android.view.View"));
            
            if (!panelChildren.isEmpty()) {
                results.addAll(panelChildren);
                logger.info("Found " + panelChildren.size() + " result elements in the results panel");
            } else {
                logger.warn("No results found in the specified panel");
                
                // Alternative: try to find any child elements in the panel
                List<WebElement> alternativeChildren = driver.findElements(By.xpath(panelXPath + "//*"));
                results.addAll(alternativeChildren);
                logger.info("Alternative: Found " + alternativeChildren.size() + " elements in the panel");
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted while waiting for results: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error collecting results from panel: " + e.getMessage());
        }
        
        return results;
    }
    
    /**
     * Performs enhanced search with tap action for selecting search suggestions
     * This method includes the touch tap functionality for better search interaction
     * @param searchTerm The term to search for
     * @return true if search was performed successfully, false otherwise
     */
    public boolean performSearchWithTap(String searchTerm) {
        logger.info("Performing enhanced search with tap for: " + searchTerm);
        
        try {
            // Open search first
            if (!openSearch()) {
                logger.error("Could not open search functionality");
                return false;
            }
            
            // Find and use search input
            WebElement searchField = findSearchInput();
            if (searchField == null) {
                logger.error("Search input field not found");
                return false;
            }
            
            // Enter the search term
            pageActions.enterText(searchField, searchTerm);
            logger.info("Search term '{}' entered successfully", searchTerm);
            
            // Wait for search suggestions to appear
            Thread.sleep(ApplicationConstants.DELAY_AFTER_TYPE);
            
            // Perform tap action at configured coordinates to select search suggestion
            tapSearchSuggestion();
            
            // Wait for search results to load
            Thread.sleep(ApplicationConstants.DELAY_SEARCH_EXECUTION);
            
            logger.info("Enhanced search with tap completed successfully for: " + searchTerm);
            return true;
            
        } catch (Exception e) {
            logger.error("Enhanced search with tap failed: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Performs tap action on search suggestion at configured coordinates
     */
    public void tapSearchSuggestion() {
        logger.info("Tapping search suggestion at coordinates ({}, {})", 
            ApplicationConstants.SEARCH_TAP_X_COORDINATE, 
            ApplicationConstants.SEARCH_TAP_Y_COORDINATE);
        
        try {
            touchActions.tapAtCoordinates(
                ApplicationConstants.SEARCH_TAP_X_COORDINATE,
                ApplicationConstants.SEARCH_TAP_Y_COORDINATE,
                ApplicationConstants.SEARCH_TAP_DURATION
            );
            
            logger.info("Search suggestion tap completed successfully");
            
        } catch (Exception e) {
            logger.error("Failed to tap search suggestion: " + e.getMessage(), e);
            throw new RuntimeException("Search suggestion tap failed", e);
        }
    }
    
    /**
     * Performs tap action with custom coordinates
     * @param x X coordinate
     * @param y Y coordinate
     * @param duration Tap duration in milliseconds
     */
    public void tapAtCustomCoordinates(int x, int y, int duration) {
        logger.info("Performing custom tap at coordinates ({}, {}) with duration {}ms", x, y, duration);
        touchActions.tapAtCoordinates(x, y, duration);
    }
    
    /**
     * Performs double tap on search suggestion for alternative interaction
     */
    public void doubleTapSearchSuggestion() {
        logger.info("Performing double tap on search suggestion");
        touchActions.doubleTap(
            ApplicationConstants.SEARCH_TAP_X_COORDINATE,
            ApplicationConstants.SEARCH_TAP_Y_COORDINATE
        );
    }
    
    /**
     * Enhanced search method that combines text entry with tap action and result verification
     * @param searchTerm The term to search for
     * @return true if search was successful and results contain the term, false otherwise
     */
    public boolean performCompleteSearchWithVerification(String searchTerm) {
        logger.info("Performing complete search with verification for: " + searchTerm);
        
        try {
            // Perform search with tap
            if (!performSearchWithTap(searchTerm)) {
                return false;
            }
            
            // Collect and verify results
            List<WebElement> results = getSearchResults();
            boolean verified = verifySearchResults(results, searchTerm);
            
            if (verified) {
                logger.info("Complete search verification SUCCESS for: " + searchTerm);
            } else {
                logger.warn("Complete search verification FAILED for: " + searchTerm);
            }
            
            return verified;
            
        } catch (Exception e) {
            logger.error("Complete search with verification failed: " + e.getMessage(), e);
            return false;
        }
    }
}