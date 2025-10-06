package com.company.framework.managers;

import io.appium.java_client.AppiumDriver;

public class AppLifecycleManager {
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
            System.err.println("⚠️ App launch check failed: " + e.getMessage());
        }
    }

    private void launchApp() throws Exception {
        new ProcessBuilder("adb", "shell", "am", "start", "-n", appPackage + "/" + mainActivity).start();
        Thread.sleep(4000);
    }
}
