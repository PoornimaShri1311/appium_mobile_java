package com.company.framework.pages.bild;

import com.company.framework.utils.MobileTestUtils;
import com.company.framework.utils.TestDataManager;
import com.company.framework.locators.bild.BildAppLocators;
import com.company.framework.locators.bild.BildAppLocators.BildElementType;

import org.openqa.selenium.WebElement;
import io.appium.java_client.AppiumDriver;

public class BildSearchPage {

    private final AppiumDriver driver;

    public BildSearchPage(AppiumDriver driver) {
        this.driver = driver;
    }

    public void performSearch(String searchTerm) {
        // Step 1: Click search button
        WebElement searchButton = MobileTestUtils.waitForElementClickable(driver,
                BildAppLocators.getLocators(BildElementType.SEARCH_BUTTON)[0], 10);
        searchButton.click();

        // Step 2: Enter text in search input field
        WebElement searchInput = MobileTestUtils.waitForElementVisible(driver,
                BildAppLocators.getLocators(BildElementType.SEARCH_INPUT)[0], 10);
        searchInput.sendKeys(searchTerm);

        // Step 3: Tap on search suggestion using coordinates from test data
        int[] coords = TestDataManager.getPatternSearchCoordinates();
        int duration = TestDataManager.getPatternSearchTapDuration();
        MobileTestUtils.tapAtCoordinates(driver, coords[0], coords[1], duration);

        // Step 4: Wait for first result and click
        MobileTestUtils.waitForElementVisible(driver,
                BildAppLocators.getLocators(BildElementType.SEARCH_RESULT_ITEM)[0], 10).click();
    }

    public boolean isResultRelevant(String searchTerm) {
        String pageSource = driver.getPageSource();
        return pageSource.length() > 1000 && pageSource.toLowerCase().contains(searchTerm.toLowerCase());
    }
}
