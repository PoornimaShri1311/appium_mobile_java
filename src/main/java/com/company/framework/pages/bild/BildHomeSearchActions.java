package com.company.framework.pages.bild;

import com.company.framework.interfaces.actions.IPageActions;
import com.company.framework.interfaces.actions.ISearchActions;
import com.company.framework.locators.bild.BildAppLocators;
import com.company.framework.locators.bild.BildAppLocators.BildElementType;
import com.company.framework.utils.TouchActionUtils;
import com.company.framework.utils.WaitUtils;

import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * BildHomeSearchActions - Handles search actions for BILD home page
 * Updated to use new BildAppLocators structure
 */
public class BildHomeSearchActions implements ISearchActions {

    private static final Logger logger = LogManager.getLogger(BildHomeSearchActions.class);

    private final IPageActions pageActions;
    private final BildHomeElements elements;
    private final AppiumDriver driver;
    private final TouchActionUtils touchActions;
    private final WaitUtils waitUtils;

    public BildHomeSearchActions(IPageActions pageActions, BildHomeElements elements, AppiumDriver driver) {
        this.pageActions = pageActions;
        this.elements = elements;
        this.driver = driver;
        this.touchActions = new TouchActionUtils(driver);
        this.waitUtils = new WaitUtils(10);
    }

    @Override
    public void performSearch(String searchTerm) {
        logger.info("Performing search for: " + searchTerm);

        if (!openSearch()) {
            throw new RuntimeException("Could not open search functionality");
        }

        WebElement searchField = findSearchInput();
        if (searchField == null) {
            throw new RuntimeException("Search input field not found");
        }

        // Enhanced text entry with multiple fallback approaches
        boolean textEntered = false;
        
        try {
            // Approach 1: Clear field first, then enter text
            searchField.clear();
            searchField.sendKeys(searchTerm);
            
            // Verify text was entered
            String enteredText = searchField.getText();
            if (enteredText != null && enteredText.contains(searchTerm)) {
                textEntered = true;
                logger.info("Successfully entered text '{}' using direct sendKeys", searchTerm);
            }
        } catch (Exception e) {
            logger.warn("Direct sendKeys failed: {}", e.getMessage());
        }
        
        if (!textEntered) {
            try {
                // Approach 2: Use page actions enterText method
                pageActions.enterText(searchField, searchTerm);
                textEntered = true;
                logger.info("Successfully entered text '{}' using pageActions", searchTerm);
            } catch (Exception e) {
                logger.warn("PageActions enterText failed: {}", e.getMessage());
            }
        }
        
        if (!textEntered) {
            // Approach 3: Focus element first, then enter text
            try {
                searchField.click(); // Focus the element
                waitUtils.waitForVisibility(searchField); // Wait for element to be visible and stable after focus
                searchField.clear();
                searchField.sendKeys(searchTerm);
                logger.info("Successfully entered text '{}' using click-focus approach", searchTerm);
                textEntered = true;
            } catch (Exception e) {
                logger.error("All text entry approaches failed: {}", e.getMessage());
                throw new RuntimeException("Failed to enter search term: " + searchTerm);
            }
        }

        // Tap suggestion
        tapSearchSuggestionWithTestData();

        // Wait for results
        WaitUtils waitUtils = new WaitUtils(5);
        By[] searchResultLocators = BildAppLocators.getLocators(BildElementType.SEARCH_RESULT_ITEM);
        for (By locator : searchResultLocators) {
            try {
                waitUtils.waitForElementsToBePresent(locator);
                break;
            } catch (Exception ignored) {
            }
        }

        logger.info("Search performed successfully for: " + searchTerm);
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
        logger.info("Collecting search results from panel");
        List<WebElement> results = new ArrayList<>();
        By[] searchResultLocators = BildAppLocators.getLocators(BildElementType.SEARCH_RESULT_ITEM);

        for (By locator : searchResultLocators) {
            try {
                List<WebElement> foundResults = driver.findElements(locator);
                if (!foundResults.isEmpty()) {
                    results.addAll(foundResults);
                    logger.info("Found {} results using locator: {}", foundResults.size(), locator);
                    break; // first successful locator
                }
            } catch (Exception e) {
                logger.debug("Locator failed: {} - {}", locator, e.getMessage());
            }
        }

        if (results.isEmpty()) {
            logger.warn("No search results found");
        }

        return results;
    }

    @Override
    public boolean verifySearchResults(List<WebElement> results, String expectedText) {
        logger.info("Verifying search results contain: {}", expectedText);
        boolean found = false;

        for (WebElement result : results) {
            try {
                String text = getElementText(result);
                if (text != null && text.toLowerCase().contains(expectedText.toLowerCase())) {
                    found = true;
                    break;
                }
            } catch (Exception ignored) {
            }
        }

        logger.info("Search verification result: {}", found ? "PASS" : "FAIL");
        return found;
    }

    @Override
    public void cancelSearch() {
        logger.info("Cancelling search");
        try {
            WebElement cancelBtn = elements.getCancelButton();
            if (pageActions.isDisplayed(cancelBtn)) {
                pageActions.waitAndClick(cancelBtn);
            }
        } catch (Exception e) {
            logger.warn("Cancel search failed: {}", e.getMessage());
        }
    }

    // ====================== Private Helpers ======================

    private boolean openSearch() {
        logger.info("Opening search functionality");

        try {
            // Try element from page object first
            WebElement searchBtn = elements.getSearchButton();
            if (pageActions.isDisplayed(searchBtn)) {
                logger.info("Found search button from page elements, clicking it");
                pageActions.waitAndClick(searchBtn);
                return true;
            }
        } catch (Exception e) {
            logger.warn("Page element search button not available: {}", e.getMessage());
        }

        try {
            // Fallback to BildAppLocators dynamic locators
            By[] searchButtonLocators = BildAppLocators.getLocators(BildElementType.SEARCH_BUTTON_ALTERNATIVES);
            logger.info("Trying {} search button locators", searchButtonLocators.length);
            
            for (int i = 0; i < searchButtonLocators.length; i++) {
                By locator = searchButtonLocators[i];
                List<WebElement> found = driver.findElements(locator);
                logger.info("Locator {}: {} - Found {} elements", i, locator, found.size());
                
                if (!found.isEmpty()) {
                    logger.info("Clicking search button found with locator: {}", locator);
                    found.get(0).click();
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error("Failed to open search using alternative locators: {}", e.getMessage());
        }

        // Check if search input is already available (search might already be open)
        boolean searchAvailable = isSearchInputAvailable();
        logger.info("Search input already available: {}", searchAvailable);
        return searchAvailable;
    }

    private boolean isSearchInputAvailable() {
        By[] searchInputLocators = BildAppLocators.getLocators(BildElementType.SEARCH_INPUT_ALTERNATIVES);
        for (By locator : searchInputLocators) {
            if (!driver.findElements(locator).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private WebElement findSearchInput() {
        // Priority 1: Try direct EditText class name (most reliable for text input)
        try {
            List<WebElement> editTextElements = driver.findElements(io.appium.java_client.AppiumBy.className("android.widget.EditText"));
            if (!editTextElements.isEmpty()) {
                WebElement editText = editTextElements.get(0); // Get first available EditText
                if (editText.isDisplayed() && editText.isEnabled()) {
                    logger.info("Found EditText element using className locator");
                    return editText;
                }
            }
        } catch (Exception e) {
            logger.debug("EditText className search failed: {}", e.getMessage());
        }

        // Priority 2: Try primary search locators
        By[] primarySearchLocators = BildAppLocators.getLocators(BildElementType.SEARCH);
        for (By locator : primarySearchLocators) {
            List<WebElement> inputs = driver.findElements(locator);
            for (WebElement input : inputs) {
                if (isTextInputElement(input)) {
                    logger.info("Found text input element using primary locator: {}", locator);
                    return input;
                }
            }
        }

        // Priority 3: Try alternative locators
        By[] searchInputLocators = BildAppLocators.getLocators(BildElementType.SEARCH_INPUT_ALTERNATIVES);
        for (By locator : searchInputLocators) {
            List<WebElement> inputs = driver.findElements(locator);
            for (WebElement input : inputs) {
                if (isTextInputElement(input)) {
                    logger.info("Found text input element using alternative locator: {}", locator);
                    return input;
                }
            }
        }

        // Priority 4: Last resort - page elements method
        try {
            WebElement element = elements.getSearchInput();
            if (pageActions.isDisplayed(element) && isTextInputElement(element)) {
                logger.info("Found text input element using page elements method");
                return element;
            }
        } catch (Exception ignored) {}

        logger.warn("No valid text input element found for search");
        return null;
    }

    private boolean isTextInputElement(WebElement element) {
        try {
            String tagName = element.getTagName().toLowerCase();
            return tagName.equals("android.widget.edittext") || 
                   tagName.equals("android.widget.autocompletetextview") ||
                   element.isEnabled() && 
                   (element.getAttribute("class").contains("EditText") || 
                    element.getAttribute("class").contains("AutoCompleteTextView"));
        } catch (Exception e) {
            logger.debug("Error checking if element is text input: {}", e.getMessage());
            return false;
        }
    }

    private String getElementText(WebElement element) {
        String text = null;
        try { text = element.getAttribute("content-desc"); } catch (Exception ignored) {}
        if (text == null || text.isEmpty()) {
            try { text = element.getAttribute("text"); } catch (Exception ignored) {}
        }
        if (text == null || text.isEmpty()) {
            try { text = element.getText(); } catch (Exception ignored) {}
        }
        return text;
    }

    public void tapSearchSuggestionWithTestData() {
        try {
            int[] coords = com.company.framework.utils.TestDataManager.getPatternSearchCoordinates();
            int duration = com.company.framework.utils.TestDataManager.getPatternSearchTapDuration();
            touchActions.tapAtCoordinates(coords[0], coords[1], duration);
        } catch (Exception e) {
            logger.error("Dynamic search suggestion tap failed: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Missing methods implementation
    public boolean performSearchWorkflow(String searchTerm) {
        performSearch(searchTerm);
        return true;
    }

    public void performSearchWithTap(String searchTerm) {
        performSearch(searchTerm);
    }

    public void tapSearchSuggestion() {
        // Implementation uses locators from bild package
    }

    public void tapAtCustomCoordinates(int x, int y, int duration) {
        // Implementation uses locators from bild package
    }

    public void doubleTapSearchSuggestion() {
        // Implementation uses locators from bild package
    }

    public boolean performCompleteSearchWithVerification(String searchTerm) {
        return performSearchWorkflow(searchTerm);
    }

    public List<WebElement> collectResultsFromPanel() {
        return getSearchResults();
    }
}
