package com.company.framework.utils;

import org.openqa.selenium.*;

import java.io.File;
import java.nio.file.*;

public class ScreenshotUtils {
    public static String captureScreenshot(WebDriver driver, String name) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String dest = "screenshots/" + name + "_" + System.currentTimeMillis() + ".png";
            Files.createDirectories(Paths.get("screenshots"));
            Files.copy(src.toPath(), Paths.get(dest));
            return dest;
        } catch (Exception e) {
            System.out.println("⚠️ Screenshot capture failed: " + e.getMessage());
            return null;
        }
    }
}
