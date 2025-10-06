package com.company.framework.locators.bild;

import org.openqa.selenium.By;
import io.appium.java_client.AppiumBy;
import java.util.HashMap;
import java.util.Map;

public class BildAppLocators {

        public enum BildElementType {
                MENU,
                SEARCH,
                SEARCH_INPUT_FIELD,
                SEARCH_INPUT,
                SEARCH_BUTTON,
                SEARCH_RESULT_ITEM,
                SEARCH_INPUT_ALTERNATIVES,
                SEARCH_BUTTON_ALTERNATIVES,
                PREMIUM,
                PREMIUM_MARKER_ICON,
                ARTICLE,
                CANCEL,
                LOGIN,
                EMAIL_FIELD,
                PASSWORD_FIELD,
                LOGIN_BUTTON,
                STARTSEITE,
                HIER_GEHTS_WEITER
        }

    private static final Map<BildElementType, By[]> locatorMap = new HashMap<>();

    static {
        // =================== MENU LOCATORS ===================
        locatorMap.put(BildElementType.MENU, new By[]{
                By.xpath("//*[@text='Sport' or @text='Home' or @text='BILD Play']"),
                By.id("com.netbiscuits.bild.android:id/menu_sport"),
                By.id("com.netbiscuits.bild.android:id/menu_home"),
                By.id("com.netbiscuits.bild.android:id/menu_bild_play"),
                By.xpath("//*[@content-desc='Sport navigation' or @content-desc='Home navigation']")
        });

        // =================== SEARCH LOCATORS ===================
        locatorMap.put(BildElementType.SEARCH, new By[]{
                By.id("com.netbiscuits.bild.android:id/search"),
                By.id("com.netbiscuits.bild.android:id/search_input"),
                By.xpath("//android.widget.EditText[@content-desc='Search']"),
                By.xpath("//*[contains(@resource-id, 'search') and @class='android.widget.EditText']"),
                By.xpath("//android.widget.EditText[contains(@hint, 'Search') or contains(@hint, 'Suchen')]")
        });

        locatorMap.put(BildElementType.SEARCH_INPUT_ALTERNATIVES, new By[]{
                By.xpath("//android.widget.AutoCompleteTextView"),
                By.xpath("//*[contains(@hint,'search') or contains(@hint,'Search')]"),
                By.xpath("//*[contains(@content-desc,'search') or contains(@content-desc,'Search')]"),
                By.xpath("//android.view.View[contains(@content-desc,'search')]")
        });

        locatorMap.put(BildElementType.SEARCH_BUTTON_ALTERNATIVES, new By[]{
                By.xpath("//android.widget.Button[contains(@content-desc,'search') or contains(@content-desc,'Search')]"),
                By.xpath("//*[contains(@resource-id, 'search') and @class='android.widget.Button']"),
                By.xpath("//*[@content-desc='Search' or @content-desc='search']")
        });

        // =================== PREMIUM LOCATORS ===================
        locatorMap.put(BildElementType.PREMIUM, new By[]{
                By.id("com.netbiscuits.bild.android:id/premium_button"),
                By.xpath("//*[@text='BILD Premium' or contains(@text, 'Premium')]"),
                By.xpath("//*[contains(@text, 'SIE HABEN BEREITS EIN KONTO')]"),
                By.xpath("//*[@content-desc='Premium subscription']"),
                By.xpath("//*[@class='android.widget.TextView' and contains(@text, 'KONTO')]")
        });

        locatorMap.put(BildElementType.PREMIUM_MARKER_ICON, new By[]{
                AppiumBy.accessibilityId("Bild Premium Marker Icon"),
                AppiumBy.androidUIAutomator("new UiSelector().description(\"Bild Premium Marker Icon\")"),
                By.id("com.netbiscuits.bild.android:id/premium_button"),
                By.xpath("//*[@text='BILD Premium' or contains(@text, 'Premium')]")
        });

        // =================== ARTICLE LOCATORS ===================
        locatorMap.put(BildElementType.ARTICLE, new By[]{
                By.id("com.netbiscuits.bild.android:id/article_title"),
                By.id("com.netbiscuits.bild.android:id/article_text"),
                By.xpath("//*[@class='android.widget.TextView' and string-length(@text) > 20]"),
                By.xpath("//*[contains(@resource-id, 'article') or contains(@resource-id, 'news')]"),
                By.xpath("//*[@content-desc='Article content']")
        });

        // =================== CANCEL / CLOSE LOCATORS ===================
        locatorMap.put(BildElementType.CANCEL, new By[]{
                By.id("com.netbiscuits.bild.android:id/cancel_button"),
                By.xpath("//*[@text='Abbrechen' or @text='Cancel']"),
                By.xpath("//*[@content-desc='Close' or @content-desc='Schließen']"),
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                By.xpath("//*[@class='android.widget.ImageView' and (@content-desc='close' or @content-desc='cancel')]")
        });

        // =================== LOGIN LOCATORS ===================
        locatorMap.put(BildElementType.LOGIN, new By[]{
                AppiumBy.androidUIAutomator("new UiSelector().text(\"Mehr\")"),
                AppiumBy.androidUIAutomator("new UiSelector().text(\"Mein Konto\")"),
                AppiumBy.androidUIAutomator("new UiSelector().className(\"android.view.View\").instance(4)"),
                AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"identifier\")"),
                AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"password\")"),
                AppiumBy.androidUIAutomator("new UiSelector().text(\"JETZT ANMELDEN\")")
        });

        // =================== POST-LOGIN LOCATORS ===================
        locatorMap.put(BildElementType.STARTSEITE, new By[]{
                AppiumBy.androidUIAutomator("new UiSelector().text(\"Startseite\")")
        });

        locatorMap.put(BildElementType.HIER_GEHTS_WEITER, new By[]{
                AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.Button\").text(\"HIER GEHT'S WEITER\")")
        });

        // =================== PATTERN SEARCH LOCATORS ===================
        locatorMap.put(BildElementType.SEARCH_BUTTON, new By[]{
                AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.Button\").instance(1)")
        });

        locatorMap.put(BildElementType.SEARCH_INPUT_FIELD, new By[]{
                AppiumBy.className("android.widget.EditText")
        });

        locatorMap.put(BildElementType.SEARCH_RESULT_ITEM, new By[]{
                By.xpath("//android.widget.TextView[contains(@text, 'Test')]")
        });
    }

    public static By[] getLocators(BildElementType type) {
        return locatorMap.getOrDefault(type, new By[]{});
    }

     /**
     * Dynamic locator for search result by text
     */
    public static By getSearchResultByText(String text) {
        return By.xpath("//android.widget.TextView[contains(@text, '" + text + "')]");
    }

    /**
     * Dynamic locator for login field by resource id
     */
    public static By getLoginFieldById(String id) {
        return AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"" + id + "\")");
    }

    /**
     * Dynamic locator for button by visible text
     */
    public static By getButtonByText(String text) {
        return AppiumBy.androidUIAutomator("new UiSelector().text(\"" + text + "\")");
    }
}
