package com.company.framework.locators;

import org.openqa.selenium.By;

/**
 * CommonElementLocators - Centralized element locator definitions
 * This class contains all the locator strategies for common UI elements
 * Separates element identification logic from wait/action logic
 */
public class CommonElementLocators {
    
    // =================== MENU NAVIGATION ELEMENTS ===================
    
    /**
     * Multiple selector strategies for menu navigation elements
     */
    public static final By[] MENU_SELECTORS = {
        By.xpath("//*[contains(@text, 'Sport') or contains(@text, 'Home') or contains(@text, 'BILD Play')]"),
        By.xpath("//*[@class='android.widget.TextView' and (@text='Sport' or @text='Home')]"),
        By.xpath("//*[contains(@content-desc, 'navigation') or contains(@content-desc, 'menu')]"),
        By.id("com.netbiscuits.bild.android:id/navigation"),
        By.xpath("//*[@resource-id='com.netbiscuits.bild.android:id/bottom_navigation']")
    };
    
    // =================== INPUT ELEMENTS ===================
    
    /**
     * Multiple selector strategies for input elements (search fields, forms)
     */
    public static final By[] INPUT_SELECTORS = {
        By.xpath("//android.widget.EditText"),
        By.xpath("//*[@class='android.widget.EditText' or contains(@content-desc, 'search')]"),
        By.xpath("//*[contains(@resource-id, 'search') or contains(@resource-id, 'input')]"),
        By.xpath("//*[@clickable='true' and @focusable='true']"),
        By.id("com.netbiscuits.bild.android:id/search_input")
    };
    
    // =================== BUTTON ELEMENTS ===================
    
    /**
     * Multiple selector strategies for clickable button elements
     */
    public static final By[] BUTTON_SELECTORS = {
        By.xpath("//android.widget.Button"),
        By.xpath("//*[@clickable='true' and (@class='android.widget.Button' or @class='android.widget.ImageButton')]"),
        By.xpath("//*[contains(@text, 'Cancel') or contains(@text, 'OK') or contains(@text, 'Submit')]"),
        By.xpath("//*[contains(@content-desc, 'button') or contains(@content-desc, 'btn')]"),
        By.xpath("//*[@class='android.widget.ImageView' and @clickable='true']")
    };
    
    // =================== SEARCH RESULT ELEMENTS ===================
    
    /**
     * Multiple selector strategies for search result elements
     */
    public static final By[] SEARCH_RESULT_SELECTORS = {
        By.xpath("//*[contains(@class, 'result') or contains(@class, 'item') or contains(@class, 'article')]"),
        By.xpath("//*[contains(@resource-id, 'text') or contains(@class, 'TextView')]"),
        By.xpath("//*[@class='android.widget.TextView' and string-length(@text) > 10]"),
        By.xpath("//*[contains(@content-desc, 'result') or contains(@content-desc, 'article')]"),
        By.xpath("//*[@resource-id='com.netbiscuits.bild.android:id/article_title']")
    };
    
    // =================== PREMIUM ELEMENTS ===================
    
    /**
     * Multiple selector strategies for premium/subscription elements
     */
    public static final By[] PREMIUM_SELECTORS = {
        By.xpath("//*[contains(@text, 'Premium') or contains(@text, 'Account')]"),
        By.xpath("//*[contains(@content-desc, 'Premium') or contains(@content-desc, 'Account')]"),
        By.xpath("//*[contains(text(), 'HABEN BEREITS EIN KONTO')]"),
        By.xpath("//*[@class='android.widget.TextView' and (contains(@text, 'Premium') or contains(@text, 'KONTO'))]"),
        By.id("com.netbiscuits.bild.android:id/premium_button")
    };
    
    // =================== LOADING INDICATORS ===================
    
    /**
     * Multiple selector strategies for loading/progress indicators
     */
    public static final By[] LOADING_SELECTORS = {
        By.xpath("//*[contains(@text, 'Loading') or contains(@text, 'laden')]"),
        By.xpath("//*[@class='android.widget.ProgressBar']"),
        By.xpath("//*[contains(@content-desc, 'loading') or contains(@content-desc, 'progress')]"),
        By.xpath("//*[contains(@resource-id, 'progress') or contains(@resource-id, 'loading')]"),
        By.id("com.netbiscuits.bild.android:id/loading_indicator")
    };
    
    // =================== CANCEL/CLOSE ELEMENTS ===================
    
    /**
     * Multiple selector strategies for cancel/close buttons
     */
    public static final By[] CANCEL_SELECTORS = {
        By.xpath("//*[contains(@text, 'Cancel') or contains(@text, 'Abbrechen')]"),
        By.xpath("//*[contains(@content-desc, 'close') or contains(@content-desc, 'cancel')]"),
        By.xpath("//*[@class='android.widget.ImageButton' and contains(@content-desc, 'close')]"),
        By.id("com.netbiscuits.bild.android:id/cancel_button"),
        By.xpath("//*[@text='✕' or @text='×']")
    };
    
    // =================== UTILITY METHODS ===================
    
    /**
     * Get locators for a specific element type
     */
    public static By[] getLocatorsForElementType(ElementType elementType) {
        switch (elementType) {
            case MENU:
                return MENU_SELECTORS;
            case INPUT:
                return INPUT_SELECTORS;
            case BUTTON:
                return BUTTON_SELECTORS;
            case SEARCH_RESULT:
                return SEARCH_RESULT_SELECTORS;
            case PREMIUM:
                return PREMIUM_SELECTORS;
            case LOADING:
                return LOADING_SELECTORS;
            case CANCEL:
                return CANCEL_SELECTORS;
            default:
                throw new IllegalArgumentException("Unknown element type: " + elementType);
        }
    }
    
    /**
     * Enum for different element types
     */
    public enum ElementType {
        MENU,
        INPUT,
        BUTTON,
        SEARCH_RESULT,
        PREMIUM,
        LOADING,
        CANCEL
    }
}