package com.company.framework.utils;

import com.company.framework.locators.common.BaseLocators;
import com.company.framework.locators.common.LocatorType;
import com.company.framework.locators.bild.BildAppLocators;
import org.openqa.selenium.By;
import java.util.ArrayList;
import java.util.List;

public class LocatorFactory {

    public static By[] getCombinedLocators(LocatorType commonType, BildAppLocators.BildElementType bildType) {
        List<By> combined = new ArrayList<>();

        // BILD-specific first
        By[] bildLocators = BildAppLocators.getLocators(bildType);
        if (bildLocators != null) {
            for (By b : bildLocators) combined.add(b);
        }

        // Common locators as fallback
        By[] commonLocators = BaseLocators.getLocators(commonType);
        if (commonLocators != null) {
            for (By c : commonLocators) combined.add(c);
        }

        return combined.toArray(new By[0]);
    }
}
