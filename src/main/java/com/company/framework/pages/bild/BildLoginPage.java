package com.company.framework.pages.bild;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.company.framework.locators.bild.BildAppLocators;
import com.company.framework.locators.bild.BildAppLocators.BildElementType;
import com.company.framework.utils.MobileTestUtils;
import com.company.framework.utils.TouchActionUtils;
import com.company.framework.utils.WaitUtils;

import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BildLoginPage {

    private static final Logger logger = LogManager.getLogger(BildLoginPage.class);
    private final AppiumDriver driver;

    public BildLoginPage(AppiumDriver driver) {
        this.driver = driver;
    }

    public void openLoginForm() {
        // Click \"Mehr\" menu
        MobileTestUtils.safeClick(driver, BildAppLocators.getButtonByText("Mehr"));
        // Click \"Mein Konto\" 
        MobileTestUtils.safeClick(driver, BildAppLocators.getButtonByText("Mein Konto"));
        // Click login view
        By[] loginLocators = BildAppLocators.getLocators(BildElementType.LOGIN);
        MobileTestUtils.safeClick(driver, loginLocators[2]); // login view locator
    }

    public void enterEmail(String email) {
        WebElement emailField = MobileTestUtils.waitForElementVisible(driver, 
                BildAppLocators.getLoginFieldById("identifier"), 10);
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void enterPassword(String password) {
        MobileTestUtils.safeType(driver,
                BildAppLocators.getLoginFieldById("password"), 
                password);
    }

    public void clickLogin() {
        MobileTestUtils.safeClick(driver,
                BildAppLocators.getButtonByText("JETZT ANMELDEN"));
    }

    public boolean isLoginSuccessful() {
        return MobileTestUtils.isBildAppRunning(driver);
    }

    public void assertTrueWithLog(boolean condition, String message) {
        Assert.assertTrue(condition, message);
        logger.info("✅ Assertion passed: " + message);
    }

    public void clickStartseite() {
        WebElement startseite = MobileTestUtils.waitForElementClickable(driver,
                BildAppLocators.getLocators(BildElementType.STARTSEITE)[0], 10); 
        startseite.click();
        MobileTestUtils.waitForPageToLoad(driver);
    }

    public void clickPremiumMarkerIfPresent() {
        try {
            WebElement premiumMarker = null;
            TouchActionUtils touch = new TouchActionUtils(driver);

            for (By locator : BildAppLocators.getLocators(BildElementType.PREMIUM_MARKER_ICON)) { 
                premiumMarker = touch.scrollUntilVisibleAndClickable(locator, 5, 5);
                if (premiumMarker != null) {
                    logger.info("✅ Premium marker found and clickable: " + locator);
                    premiumMarker.click();
                    MobileTestUtils.waitForPageToLoad(driver);
                    return;
                }
            }

            logger.warn("⚠️ Premium marker not found after all scroll attempts");

        } catch (Exception e) {
            logger.error("⚠️ Premium marker interaction failed: " + e.getMessage());
        }
    }

    private void scrollDown() {
        try {
            org.openqa.selenium.Dimension screenSize = driver.manage().window().getSize();
            int startY = (int) (screenSize.height * 0.7);
            int endY = (int) (screenSize.height * 0.3);
            int centerX = screenSize.width / 2;

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence scroll = new Sequence(finger, 1);
            scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, startY));
            scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            scroll.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), centerX, endY));
            scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Arrays.asList(scroll));
            logger.info("🔄 Scroll gesture performed using W3C Actions");
        } catch (Exception e) {
            logger.error("⚠️ Scroll failed: " + e.getMessage());
        }
    }

    public void clickHierGehtsWeiter() {
        try {
            WebElement weiterButton = null;

            WaitUtils waitUtils = new WaitUtils(5);
            
            for (int i = 0; i < 3; i++) {
                scrollDown();
                
                // Wait for elements to be available after scroll
                List<By> possibleLocators = Arrays.asList(
                        BildAppLocators.getLocators(BildElementType.HIER_GEHTS_WEITER)[0]
                );

                for (By locator : possibleLocators) {
                    try {
                        if (waitUtils.waitForElementsToBePresent(locator)) {
                            weiterButton = driver.findElement(locator);
                            if (weiterButton.isDisplayed()) {
                                weiterButton.click();
                                logger.info("✅ 'HIER GEHT'S WEITER' button clicked successfully");
                                MobileTestUtils.waitForPageToLoad(driver);
                                return;
                            }
                        }
                    } catch (Exception ignored) {}
                }
            }

            logger.warn("⚠️ 'HIER GEHT'S WEITER' button not found");

        } catch (Exception e) {
            logger.error("⚠️ Error clicking 'HIER GEHT'S WEITER' button: " + e.getMessage());
        }
    }

    private void checkHierGehtsWeiterElement() {
        if (MobileTestUtils.isElementPresent(driver, BildAppLocators.getLocators(BildElementType.HIER_GEHTS_WEITER)[0])) {
            logger.info("✅ 'HIER GEHT'S WEITER' element found - goBack should work properly");
        } else {
            logger.warn("⚠️ 'HIER GEHT'S WEITER' element not found - goBack may not work as expected");
        }
    }

    public void completePostLoginNavigation() {
        clickStartseite();
        clickPremiumMarkerIfPresent();
        checkHierGehtsWeiterElement();
    }
}
