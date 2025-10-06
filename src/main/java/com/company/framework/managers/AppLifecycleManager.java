package com.company.framework.managers;

import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AppLifecycleManager {
    
    private static final Logger logger = LogManager.getLogger(AppLifecycleManager.class);
    private final AppiumDriver driver;
    private final String appPackage;
    private final String mainActivity;

    public AppLifecycleManager(AppiumDriver driver, String appPackage, String mainActivity) {
        this.driver = driver;
        this.appPackage = appPackage;
        this.mainActivity = mainActivity;
    }

    public void ensureAppIsRunning() {
        try {
            String pageSource = driver.getPageSource();
            if (!pageSource.contains(appPackage)) {
                launchApp();
            }
        } catch (Exception e) {
            logger.warn("⚠️ App launch check failed: {}", e.getMessage());
        }
    }

    private void launchApp() throws Exception {
        logger.info("Launching app: {}/{}", appPackage, mainActivity);
        new ProcessBuilder("adb", "shell", "am", "start", "-n", appPackage + "/" + mainActivity).start();
        // Wait for app launch to complete using proper conditions
        try {
            WebDriverWait appLaunchWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            // Wait for app to be responsive by checking page source availability
            appLaunchWait.until(driver -> {
                try {
                    return !driver.getPageSource().isEmpty();
                } catch (Exception e) {
                    return false;
                }
            });
            logger.info("App launch validation completed");
        } catch (Exception e) {
            logger.warn("App launch wait failed, continuing: {}", e.getMessage());
        }
        logger.info("App launch completed");
    }
}
