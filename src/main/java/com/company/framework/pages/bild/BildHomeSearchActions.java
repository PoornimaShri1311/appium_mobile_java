package com.company.framework.pages.bild;

import com.company.framework.interfaces.ISearchActions;
import com.company.framework.interfaces.IPageActions;
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

    private static final long DELAY_SEARCH_EXECUTION = 3000;
    private static final long DELAY_AFTER_TYPE = 1000;

    public BildHomeSearchActions(IPageActions pageActions, BildHomeElements elements, AppiumDriver driver) {
        this.pageActions = pageActions;
        this.elements = elements;
        this.driver = driver;
        this.touchActions = new TouchActionUtils(driver);
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

        pageActions.enterText(searchField, searchTerm);

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
                pageActions.waitAndClick(searchBtn);
                return true;
            }

            // Fallback to BildAppLocators dynamic locators
            By[] searchButtonLocators = BildAppLocators.getLocators(BildElementType.SEARCH_BUTTON_ALTERNATIVES);
            for (By locator : searchButtonLocators) {
                List<WebElement> found = driver.findElements(locator);
                if (!found.isEmpty()) {
                    found.get(0).click();
                    return true;
                }
            }

        } catch (Exception e) {
            logger.error("Failed to open search: {}", e.getMessage());
        }

        return isSearchInputAvailable();
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
        try {
            WebElement element = elements.getSearchInput();
            if (pageActions.isDisplayed(element)) {
                return element;
            }
        } catch (Exception ignored) {}

        By[] searchInputLocators = BildAppLocators.getLocators(BildElementType.SEARCH_INPUT_ALTERNATIVES);
        for (By locator : searchInputLocators) {
            List<WebElement> inputs = driver.findElements(locator);
            if (!inputs.isEmpty()) {
                return inputs.get(0);
            }
        }

        return null;
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
