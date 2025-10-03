package utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {
    private final AppiumDriver driver;
    private final WebDriverWait wait;

    public WaitUtils(long timeoutInSeconds) {
        this.driver = DriverUtils.getDriver();  // Get driver from your DriverUtils
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    public void waitForElementToBeClickableAndClick(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public boolean waitForInvisibility(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void waitForElementAndType(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element)).sendKeys(text);
    }

    public String waitForTextToBePresent(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        return element.getText();
    }

    /**
     * Waits for elements to be present in the DOM using the specified locator
     * @param locator The By locator to find elements
     * @return true if elements are found, false otherwise
     */
    public boolean waitForElementsToBePresent(By locator) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Waits for search results to load with intelligent fallback strategies
     * @param testReporter ExtentTest instance for logging (optional, can be null)
     * @return true if results are found, false otherwise
     */
    public boolean waitForSearchResults(com.aventstack.extentreports.ExtentTest testReporter) {
        return waitForElementsWithFallback(
            locators.CommonElementLocators.getLocatorsForElementType(locators.CommonElementLocators.ElementType.SEARCH_RESULT),
            "Search results",
            testReporter
        );
    }

    /**
     * Waits for premium elements to be visible with multiple selector strategies
     * @param testReporter ExtentTest instance for logging (optional, can be null)
     * @return true if premium elements are found, false otherwise
     */
    public boolean waitForPremiumElements(com.aventstack.extentreports.ExtentTest testReporter) {
        return waitForElementsWithFallback(
            locators.CommonElementLocators.getLocatorsForElementType(locators.CommonElementLocators.ElementType.PREMIUM),
            "Premium elements",
            testReporter
        );
    }

    /**
     * Generic wait for elements with custom message and fallback selectors
     * @param selectors Array of By selectors to try
     * @param description Description for logging
     * @param testReporter ExtentTest instance for logging (optional, can be null)
     * @return true if any selector finds elements, false otherwise
     */
    public boolean waitForElementsWithFallback(By[] selectors, String description, 
                                             com.aventstack.extentreports.ExtentTest testReporter) {
        for (By selector : selectors) {
            try {
                boolean found = waitForElementsToBePresent(selector);
                if (found) {
                    if (testReporter != null) {
                        testReporter.info(description + " - Elements found using: " + selector);
                    }
                    return true;
                }
            } catch (Exception e) {
                // Continue to next selector
            }
        }
        
        if (testReporter != null) {
            testReporter.info(description + " - No elements found with any selector");
        }
        return false;
    }

    /**
     * Scrolls to make an element visible using TouchActionUtils
     * @param element The element to scroll to
     */
    public void scrollToElementMobile(WebElement element) {
        TouchActionUtils touchUtils = new TouchActionUtils(driver);
        touchUtils.scrollToElement(element);
    }
}
