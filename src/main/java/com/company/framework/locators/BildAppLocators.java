package com.company.framework.locators;

import org.openqa.selenium.By;
import io.appium.java_client.AppiumBy;

/**
 * BildAppLocators - BILD News App specific element locators
 * This class contains locator strategies specific to the BILD application
 * Extends the common element pattern with app-specific elements
 */
public class BildAppLocators {
    
    // =================== BILD SPECIFIC MENU ELEMENTS ===================
    
    /**
     * BILD app specific menu items
     */
    public static final By[] BILD_MENU_SELECTORS = {
        By.xpath("//*[@text='Sport' or @text='Home' or @text='BILD Play']"),
        By.id("com.netbiscuits.bild.android:id/menu_sport"),
        By.id("com.netbiscuits.bild.android:id/menu_home"),  
        By.id("com.netbiscuits.bild.android:id/menu_bild_play"),
        By.xpath("//*[@content-desc='Sport navigation' or @content-desc='Home navigation']")
    };
    
    // =================== BILD SEARCH ELEMENTS ===================
    
    /**
     * BILD app search input field locators
     */
    public static final By[] BILD_SEARCH_SELECTORS = {
        By.id("com.netbiscuits.bild.android:id/search"),
        By.id("com.netbiscuits.bild.android:id/search_input"),
        By.xpath("//android.widget.EditText[@content-desc='Search']"),
        By.xpath("//*[contains(@resource-id, 'search') and @class='android.widget.EditText']"),
        By.xpath("//android.widget.EditText[contains(@hint, 'Search') or contains(@hint, 'Suchen')]")
    };
    
    // =================== BILD PREMIUM ELEMENTS ===================
    
    /**
     * BILD Premium subscription related elements
     */
    public static final By[] BILD_PREMIUM_SELECTORS = {
        By.id("com.netbiscuits.bild.android:id/premium_button"),
        By.xpath("//*[@text='BILD Premium' or contains(@text, 'Premium')]"),
        By.xpath("//*[contains(@text, 'SIE HABEN BEREITS EIN KONTO')]"),
        By.xpath("//*[@content-desc='Premium subscription']"),
        By.xpath("//*[@class='android.widget.TextView' and contains(@text, 'KONTO')]")
    };
    
    // =================== BILD ARTICLE ELEMENTS ===================
    
    /**
     * BILD article and news content selectors
     */
    public static final By[] BILD_ARTICLE_SELECTORS = {
        By.id("com.netbiscuits.bild.android:id/article_title"),
        By.id("com.netbiscuits.bild.android:id/article_text"),
        By.xpath("//*[@class='android.widget.TextView' and string-length(@text) > 20]"),
        By.xpath("//*[contains(@resource-id, 'article') or contains(@resource-id, 'news')]"),
        By.xpath("//*[@content-desc='Article content']")
    };
    
    // =================== BILD CANCEL/CLOSE ELEMENTS ===================

    /**
     * BILD app specific cancel and close button selectors
     */
    public static final By[] BILD_CANCEL_SELECTORS = {
        By.id("com.netbiscuits.bild.android:id/cancel_button"),
        By.xpath("//*[@text='Abbrechen' or @text='Cancel']"),
        By.xpath("//*[@content-desc='Close' or @content-desc='Schließen']"),
        By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
        By.xpath("//*[@class='android.widget.ImageView' and (@content-desc='close' or @content-desc='cancel')]")
    };

    // =================== BILD LOGIN ELEMENTS ===================

    /**
     * BILD login flow specific selectors
     */
    public static final By BILD_MORE_MENU = AppiumBy.androidUIAutomator("new UiSelector().text(\"Mehr\")");
    public static final By BILD_MY_ACCOUNT = AppiumBy.androidUIAutomator("new UiSelector().text(\"Mein Konto\")");
    public static final By BILD_LOGIN_VIEW = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.view.View\").instance(4)");
    public static final By BILD_EMAIL_FIELD = AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"identifier\")");
    public static final By BILD_PASSWORD_FIELD = AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"password\")");
    public static final By BILD_LOGIN_BUTTON = AppiumBy.androidUIAutomator("new UiSelector().text(\"JETZT ANMELDEN\")");

    /**
     * BILD login selectors array for fallback scenarios
     */
    public static final By[] BILD_LOGIN_SELECTORS = {
        BILD_MORE_MENU,
        BILD_MY_ACCOUNT,
        BILD_LOGIN_VIEW,
        BILD_EMAIL_FIELD,
        BILD_PASSWORD_FIELD,
        BILD_LOGIN_BUTTON
    };

    // =================== BILD POST-LOGIN ELEMENTS ===================

    /**
     * BILD post-login navigation and premium elements
     */
    public static final By BILD_STARTSEITE = AppiumBy.androidUIAutomator("new UiSelector().text(\"Startseite\")");
    public static final By BILD_PREMIUM_MARKER_ICON = AppiumBy.androidUIAutomator("new UiSelector().description(\"Bild Premium Marker Icon\").instance(0)");
    public static final By BILD_HIER_GEHTS_WEITER = AppiumBy.androidUIAutomator("new UiSelector().text(\"HIER GEHT'S WEITER\")");
    
    // =================== BILD PATTERN SEARCH ELEMENTS ===================
    
    /**
     * BILD pattern search specific locators
     */
    public static final By BILD_SEARCH_BUTTON = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.Button\").instance(1)");
    public static final By BILD_SEARCH_INPUT_FIELD = AppiumBy.className("android.widget.EditText");
    public static final By BILD_SEARCH_RESULT_ITEM = AppiumBy.xpath("//android.widget.TextView[contains(@text, 'Test')]");

    // =================== UTILITY METHODS ===================
    
    /**
     * Get BILD-specific locators for element types
     */
    public static By[] getBildLocatorsForElementType(BildElementType elementType) {
        switch (elementType) {
            case BILD_MENU:
                return BILD_MENU_SELECTORS;
            case BILD_SEARCH:
                return BILD_SEARCH_SELECTORS;
            case BILD_PREMIUM:
                return BILD_PREMIUM_SELECTORS;
            case BILD_ARTICLE:
                return BILD_ARTICLE_SELECTORS;
            case BILD_CANCEL:
                return BILD_CANCEL_SELECTORS;
            case BILD_LOGIN:
                return BILD_LOGIN_SELECTORS;
            default:
                throw new IllegalArgumentException("Unknown BILD element type: " + elementType);
        }
    }
    
    /**
     * Combine common and BILD-specific locators for enhanced fallback
     */
    public static By[] getCombinedLocators(CommonElementLocators.ElementType commonType, BildElementType bildType) {
        By[] commonLocators = CommonElementLocators.getLocatorsForElementType(commonType);
        By[] bildLocators = getBildLocatorsForElementType(bildType);
        
        // Merge arrays - BILD-specific first (higher priority)
        By[] combined = new By[bildLocators.length + commonLocators.length];
        System.arraycopy(bildLocators, 0, combined, 0, bildLocators.length);
        System.arraycopy(commonLocators, 0, combined, bildLocators.length, commonLocators.length);
        
        return combined;
    }
    
    /**
     * Enum for BILD-specific element types
     */
    public enum BildElementType {
        BILD_MENU,
        BILD_SEARCH,
        BILD_PREMIUM,
        BILD_ARTICLE,
        BILD_CANCEL,
        BILD_LOGIN
    }
}