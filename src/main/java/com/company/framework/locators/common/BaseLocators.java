package com.company.framework.locators.common;

import org.openqa.selenium.By;
import java.util.HashMap;
import java.util.Map;

public class BaseLocators {

    private static final Map<LocatorType, By[]> locatorMap = new HashMap<>();

    static {
        locatorMap.put(LocatorType.MENU, new By[]{
                By.xpath("//*[contains(@text, 'Sport') or contains(@text, 'Home') or contains(@text, 'BILD Play')]"),
                By.id("com.netbiscuits.bild.android:id/navigation")
        });
        locatorMap.put(LocatorType.INPUT, new By[]{
                By.xpath("//android.widget.EditText"),
                By.id("com.netbiscuits.bild.android:id/search_input")
        });
        locatorMap.put(LocatorType.BUTTON, new By[]{
                By.xpath("//android.widget.Button"),
                By.xpath("//*[@clickable='true' and (@class='android.widget.Button' or @class='android.widget.ImageButton')]")
        });
    }

    public static By[] getLocators(LocatorType type) {
        return locatorMap.getOrDefault(type, new By[]{});
    }
}
