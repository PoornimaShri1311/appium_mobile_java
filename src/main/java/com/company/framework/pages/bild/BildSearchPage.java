package com.company.framework.pages.bild;

import com.company.framework.utils.MobileTestUtils;
import com.company.framework.locators.BildAppLocators;
import com.company.framework.utils.TestDataManager;
import org.openqa.selenium.WebElement;
import io.appium.java_client.AppiumDriver;


public class BildSearchPage {

    private final AppiumDriver driver;

    public BildSearchPage(AppiumDriver driver) {
        this.driver = driver;
    }

    public void performSearch(String searchTerm) {
    WebElement searchButton = MobileTestUtils.waitForElementClickable(driver,
            BildAppLocators.BILD_SEARCH_BUTTON, 10);
    searchButton.click();

    WebElement searchInput = MobileTestUtils.waitForElementVisible(driver,
            BildAppLocators.BILD_SEARCH_INPUT_FIELD, 10);
    searchInput.sendKeys(searchTerm);

    int[] coords = TestDataManager.getPatternSearchCoordinates();
    int duration = TestDataManager.getPatternSearchTapDuration();
    MobileTestUtils.tapAtCoordinates(driver, coords[0], coords[1], duration);

    MobileTestUtils.waitForElementVisible(driver,
            BildAppLocators.BILD_SEARCH_RESULT_ITEM, 10).click();
}

    public boolean isResultRelevant(String searchTerm) {
        String pageSource = driver.getPageSource();
        return pageSource.length() > 1000 && pageSource.toLowerCase().contains(searchTerm.toLowerCase());
    }
}
