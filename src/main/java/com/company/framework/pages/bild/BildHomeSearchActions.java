package com.company.framework.pages.bild;

import com.company.framework.interfaces.ISearchActions;
import com.company.framework.interfaces.IPageActions;
import com.company.framework.interfaces.IWaitStrategy;
import com.company.framework.locators.BildAppLocators;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import io.appium.java_client.AppiumDriver;
import com.company.framework.utils.TouchActionUtils;
import com.company.framework.utils.WaitUtils;

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
    
    // Search-related constants
    private static final long DELAY_SEARCH_EXECUTION = 3000;
    private static final long DELAY_AFTER_TYPE = 1000;
    private static final int SEARCH_TAP_X_COORDINATE = 540;
    private static final int SEARCH_TAP_Y_COORDINATE = 2153;
    private static final int SEARCH_TAP_DURATION = 50;

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

            // Tab search using dynamic coordinates from test data
            tapSearchSuggestionWithTestData();


            // Wait for search suggestions to appear using smart wait
            WaitUtils waitUtils = new WaitUtils(5);
            try {
                // Try search result locators which include suggestion patterns
                By[] suggestionLocators = BildAppLocators.BILD_SEARCH_RESULTS;
                waitUtils.waitForElementsToBePresent(suggestionLocators[1]); // Use suggestion locator
            } catch (Exception e) {
                // Suggestions may not appear immediately, continue
            }

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
            // Wait for search results to load using smart wait
            WaitUtils waitUtils = new WaitUtils((int)(DELAY_SEARCH_EXECUTION / 1000));
            By[] searchResultLocators = BildAppLocators.BILD_SEARCH_RESULTS;
            try {
                waitUtils.waitForElementsToBePresent(searchResultLocators[0]);
            } catch (Exception e) {
                // Results may take time to appear, continue with execution
            }

            // Try all search result locators from BildAppLocators
            for (By resultLocator : searchResultLocators) {
                try {
                    List<WebElement> foundResults = driver.findElements(resultLocator);
                    if (!foundResults.isEmpty()) {
                        results.addAll(foundResults);
                        logger.info("Found " + foundResults.size() + " result elements using locator: " + resultLocator);
                        break; // Use first successful locator
                    }
                } catch (Exception e) {
                    logger.debug("Search result locator failed: {} - {}", resultLocator, e.getMessage());
                }
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
                    String displayText = resultText.length() > 100 ? 
                        resultText.substring(0, 100) + "..." : resultText;
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

        logger.info("Search verification result: " + (found ? "PASS" : "FAIL") + 
                   " (checked " + resultCount + " results)");
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
        logger.info("Attempting to open search functionality");
        
        // Try multiple approaches to open search
        try {
            // Approach 1: Use the defined search button element
            try {
                logger.info("Trying approach 1: Using defined search button element");
                pageActions.waitAndClick(elements.getSearchButton());
                Thread.sleep(1000);
                if (isSearchInputAvailable()) {
                    logger.info("Search opened successfully using approach 1");
                    return true;
                }
            } catch (Exception e1) {
                logger.warn("Approach 1 failed: " + e1.getMessage());
            }
            
            // Approach 2: Try to find search button by alternative locators from BildAppLocators
            try {
                logger.info("Trying approach 2: Alternative search button locators from BildAppLocators");
                By[] searchButtonAlternatives = BildAppLocators.BILD_SEARCH_BUTTON_ALTERNATIVES;
                for (By locator : searchButtonAlternatives) {
                    try {
                        List<WebElement> searchButtons = driver.findElements(locator);
                        if (!searchButtons.isEmpty()) {
                            searchButtons.get(0).click();
                            Thread.sleep(1000);
                            if (isSearchInputAvailable()) {
                                logger.info("Search opened successfully using approach 2 with locator: {}", locator);
                                return true;
                            }
                        }
                    } catch (Exception le) {
                        logger.debug("Search button locator failed: {} - {}", locator, le.getMessage());
                    }
                }
            } catch (Exception e2) {
                logger.warn("Approach 2 failed: " + e2.getMessage());
            }
            
            // Approach 3: Try coordinate-based tap at expected search button location
            try {
                logger.info("Trying approach 3: Coordinate-based tap for search button");
                // Use coordinates that are typically where search buttons are located
                touchActions.quickTap(200, 200); // Adjust coordinates as needed
                Thread.sleep(1000);
                if (isSearchInputAvailable()) {
                    logger.info("Search opened successfully using approach 3");
                    return true;
                }
            } catch (Exception e3) {
                logger.warn("Approach 3 failed: " + e3.getMessage());
            }
            
            // Approach 4: Check if search is already available (might be pre-opened)
            try {
                logger.info("Trying approach 4: Checking if search is already available");
                if (isSearchInputAvailable()) {
                    logger.info("Search input is already available - no need to open");
                    return true;
                }
            } catch (Exception e4) {
                logger.warn("Approach 4 failed: " + e4.getMessage());
            }
            
            logger.error("All approaches to open search failed");
            return false;
            
        } catch (Exception e) {
            logger.error("Critical error in openSearch: " + e.getMessage());
            return false;
        }
    }

    private boolean isSearchInputAvailable() {
        try {
            List<WebElement> editTexts = driver.findElements(By.xpath(BildAppLocators.XPATH_ANDROID_EDITTEXT));
            return !editTexts.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private WebElement findSearchInput() {
        logger.info("Attempting to find search input field");
        
        // Approach 1: Try the original searchInput element first
        try {
            logger.info("Trying approach 1: Using defined search input element");
            if (pageActions.isDisplayed(elements.getSearchInput())) {
                logger.info("Found search input using defined element");
                return elements.getSearchInput();
            }
        } catch (Exception e) {
            logger.warn("Approach 1 failed - Original search input not available: " + e.getMessage());
        }

        // Approach 2: Fallback to any EditText element
        try {
            logger.info("Trying approach 2: Looking for any EditText elements");
            List<WebElement> editTexts = new ArrayList<>();
            for (By locator : BildAppLocators.BILD_SEARCH_SELECTORS) {
                editTexts.addAll(driver.findElements(locator));
            }

            if (!editTexts.isEmpty()) {
                logger.info("Found {} EditText elements, using the first one", editTexts.size());
                return editTexts.get(0);
            }
        } catch (Exception e) {
            logger.warn("Approach 2 failed - No EditText elements found: " + e.getMessage());
        }

        // Approach 3: Try alternative search input locators from BildAppLocators
        try {
            logger.info("Trying approach 3: Alternative search input locators from BildAppLocators");
            By[] alternativeLocators = BildAppLocators.BILD_SEARCH_INPUT_ALTERNATIVES;
            
            for (By locator : alternativeLocators) {
                try {
                    List<WebElement> elements = driver.findElements(locator);
                    if (!elements.isEmpty()) {
                        logger.info("Found search input using alternative locator: {}", locator);
                        return elements.get(0);
                    }
                } catch (Exception xe) {
                    logger.debug("Locator failed: {} - {}", locator, xe.getMessage());
                }
            }
        } catch (Exception e) {
            logger.warn("Approach 3 failed - Alternative locators not found: " + e.getMessage());
        }

        logger.error("All approaches to find search input failed");
        return null;
    }

    private String getElementText(WebElement element) {
        // Try multiple methods to get text
        String text = null;
        
        try {
            text = element.getAttribute("content-desc");
        } catch (Exception e) { /* ignore */ }
        
        if (text == null || text.isEmpty()) {
            try {
                text = element.getAttribute("text");
            } catch (Exception e) { /* ignore */ }
        }
        
        if (text == null || text.isEmpty()) {
            try {
                text = element.getText();
            } catch (Exception e) { /* ignore */ }
        }
        
        if (text == null || text.isEmpty()) {
            try {
                text = element.getAttribute("resource-id");
            } catch (Exception e) { /* ignore */ }
        }
        
        return text;
    }

    /**
     * Performs enhanced search with tap action for selecting search suggestions
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
            logger.info("Search term entered successfully: {}", searchTerm);

            // Wait for search suggestions to appear
            Thread.sleep(DELAY_AFTER_TYPE);

            // Perform tap action using dynamic coordinates to select search suggestion
            tapSearchSuggestionWithTestData();

            // Wait for search results to load
            Thread.sleep(DELAY_SEARCH_EXECUTION);

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
                   SEARCH_TAP_X_COORDINATE, SEARCH_TAP_Y_COORDINATE);
        
        try {
            touchActions.tapAtCoordinates(
                SEARCH_TAP_X_COORDINATE,
                SEARCH_TAP_Y_COORDINATE,
                SEARCH_TAP_DURATION
            );
            logger.info("Search suggestion tap completed successfully");
        } catch (Exception e) {
            logger.error("Failed to tap search suggestion: " + e.getMessage(), e);
            throw new RuntimeException("Search suggestion tap failed", e);
        }
    }
    
    /**
     * Performs tap action using dynamic coordinates from TestDataManager
     * This method reads coordinates from test data instead of hardcoded constants
     */
    public void tapSearchSuggestionWithTestData() {
        try {
            // Get coordinates from TestDataManager
            int[] coords = com.company.framework.utils.TestDataManager.getPatternSearchCoordinates();
            int duration = com.company.framework.utils.TestDataManager.getPatternSearchTapDuration();
            
            logger.info("Tapping search suggestion at dynamic coordinates ({}, {}) with duration {}ms", 
                       coords[0], coords[1], duration);
            
            touchActions.tapAtCoordinates(coords[0], coords[1], duration);
            logger.info("Dynamic search suggestion tap completed successfully");
        } catch (Exception e) {
            logger.error("Failed to tap search suggestion with test data: " + e.getMessage(), e);
            throw new RuntimeException("Dynamic search suggestion tap failed", e);
        }
    }

    /**
     * Perform complete search workflow for Umfrage with specific panel targeting
     * @param searchTerm The search term to use
     * @return true if results contain the search term, false otherwise
     */
    public boolean performSearchWorkflow(String searchTerm) {
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
            WaitUtils waitUtils = new WaitUtils(10);
            try {
                By[] searchResultLocators = BildAppLocators.BILD_SEARCH_RESULTS;
                waitUtils.waitForElementsToBePresent(searchResultLocators[0]); // Use first result locator
            } catch (Exception e) {
                // Continue even if results take time to load
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

            // Use search result locators from BildAppLocators
            By[] searchResultLocators = BildAppLocators.BILD_SEARCH_RESULTS;
            
            // Try each locator until one succeeds
            for (By resultLocator : searchResultLocators) {
                try {
                    List<WebElement> foundResults = driver.findElements(resultLocator);
                    if (!foundResults.isEmpty()) {
                        results.addAll(foundResults);
                        logger.info("Found " + foundResults.size() + " result elements using locator: " + resultLocator);
                        break; // Use first successful locator
                    }
                } catch (Exception e) {
                    logger.debug("Search result locator failed: {} - {}", resultLocator, e.getMessage());
                }
            }
            
            if (results.isEmpty()) {
                logger.warn("No results found using any of the configured locators");
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
            SEARCH_TAP_X_COORDINATE,
            SEARCH_TAP_Y_COORDINATE
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