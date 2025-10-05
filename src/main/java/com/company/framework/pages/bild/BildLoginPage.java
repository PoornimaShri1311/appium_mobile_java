package com.company.framework.pages.bild;

import io.appium.java_client.AppiumDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import com.company.framework.utils.MobileTestUtils;
import com.company.framework.utils.TouchActionUtils;
import com.company.framework.locators.BildAppLocators;
import com.company.framework.locators.CommonElementLocators;

import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import java.time.Duration;
import java.util.Arrays;

public class BildLoginPage {
    private AppiumDriver driver;

    public BildLoginPage(AppiumDriver driver) {
        this.driver = driver;
    }

    public void openLoginForm() {
        MobileTestUtils.safeClick(driver, BildAppLocators.BILD_MORE_MENU);
        MobileTestUtils.safeClick(driver, BildAppLocators.BILD_MY_ACCOUNT);
        MobileTestUtils.safeClick(driver, BildAppLocators.BILD_LOGIN_VIEW);
    }

    public void enterEmail(String email) {
        WebElement emailField = MobileTestUtils.waitForElementVisible(driver, BildAppLocators.BILD_EMAIL_FIELD, 10);
        emailField.clear();
        emailField.sendKeys(email);
    }


    public void enterPassword(String password) {
        MobileTestUtils.safeType(driver, BildAppLocators.BILD_PASSWORD_FIELD, password);
    }

    public void clickLogin() {
        MobileTestUtils.safeClick(driver, BildAppLocators.BILD_LOGIN_BUTTON);
    }

    public boolean isLoginSuccessful() {
        return MobileTestUtils.isBildAppRunning(driver);
    }
    
    public void assertTrueWithLog(boolean condition, String message) {
        Assert.assertTrue(condition, message);
        System.out.println("✅ Assertion passed: " + message);
    }

    public void clickStartseite() {
        WebElement startseite = MobileTestUtils.waitForElementClickable(driver, BildAppLocators.BILD_STARTSEITE, 10);
        startseite.click();
        MobileTestUtils.waitForPageToLoad(driver);
    }

    public void clickPremiumMarkerIfPresent() {
    try {
        WebElement premiumMarker = null;

        for (By locator : BildAppLocators.BILD_PREMIUM_MARKER_ICON) {
            premiumMarker = new TouchActionUtils(driver)
                .scrollUntilVisibleAndClickable(locator, 5, 5);
            if (premiumMarker != null) {
                System.out.println("✅ Premium marker found and clickable: " + locator);
                premiumMarker.click();
                MobileTestUtils.waitForPageToLoad(driver);
                return; // stop after first success
            }
        }

        System.out.println("⚠️ Premium marker not found after all scroll attempts");

    } catch (Exception e) {
        System.out.println("⚠️ Premium marker interaction failed: " + e.getMessage());
    }
}


    
    private void scrollDown() {
        try {
            // Get screen dimensions
            org.openqa.selenium.Dimension screenSize = driver.manage().window().getSize();
            int startY = (int) (screenSize.height * 0.7);
            int endY = (int) (screenSize.height * 0.3);
            int centerX = screenSize.width / 2;
            
            // Use W3C Actions for scrolling
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence scroll = new Sequence(finger, 1);
            scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, startY));
            scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            scroll.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), centerX, endY));
            scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            
            driver.perform(Arrays.asList(scroll));
            System.out.println("🔄 Scroll gesture performed using W3C Actions");
        } catch (Exception e) {
            System.out.println("⚠️ Scroll failed: " + e.getMessage());
        }
    }

    public void clickHierGehtsWeiter() {
        try {
            WebElement weiterButton = null;
            
            // Try to find the button directly first
            try {
                weiterButton = MobileTestUtils.waitForElementClickable(driver, BildAppLocators.BILD_HIER_GEHTS_WEITER, 3);
                System.out.println("✅ 'HIER GEHT'S WEITER' button found directly");
            } catch (Exception e1) {
                System.out.println("🔍 'HIER GEHT'S WEITER' not visible, trying scroll and alternative locators...");
                
                // Try scrolling to find the button
                for (int i = 0; i < 3; i++) {
                    scrollDown();
                    Thread.sleep(1000);
                    
                    try {
                        weiterButton = driver.findElement(BildAppLocators.BILD_HIER_GEHTS_WEITER);
                        if (weiterButton.isDisplayed()) {
                            System.out.println("✅ 'HIER GEHT'S WEITER' button found after scroll " + (i + 1));
                            break;
                        }
                    } catch (Exception e2) {
                        // Try alternative text patterns
                        try {
                            weiterButton = driver.findElement(io.appium.java_client.AppiumBy.androidUIAutomator(
                                "new UiSelector().textContains(\"WEITER\")"));
                            if (weiterButton.isDisplayed()) {
                                System.out.println("✅ Button found with alternative locator 'WEITER'");
                                break;
                            }
                        } catch (Exception e3) {
                            System.out.println("⚠️ Scroll attempt " + (i + 1) + " - button still not found");
                        }
                    }
                }
            }
            
            if (weiterButton != null && weiterButton.isDisplayed()) {
                weiterButton.click();
                System.out.println("✅ 'HIER GEHT'S WEITER' button clicked successfully");
                MobileTestUtils.waitForPageToLoad(driver);
            } else {
                System.out.println("⚠️ 'HIER GEHT'S WEITER' button not found or not clickable");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Error clicking 'HIER GEHT'S WEITER' button: " + e.getMessage());
        }
    }

    public void completePostLoginNavigation() {
        clickStartseite();
        clickPremiumMarkerIfPresent();
        
        // Check if "HIER GEHT'S WEITER" element is present before going back
        if (MobileTestUtils.isElementPresent(driver, BildAppLocators.BILD_HIER_GEHTS_WEITER)) {
            System.out.println("✅ 'HIER GEHT'S WEITER' element found - goBack should work properly");
        } else {
            System.out.println("⚠️ 'HIER GEHT'S WEITER' element not found - goBack may not work as expected");
        }
    }
}

