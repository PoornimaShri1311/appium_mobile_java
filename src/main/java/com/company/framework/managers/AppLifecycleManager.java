// package com.company.framework.managers;

// import io.appium.java_client.AppiumDriver;
// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;
// import org.openqa.selenium.support.ui.WebDriverWait;

// import java.time.Duration;

// public class AppLifecycleManager {
    
//     private static final Logger logger = LogManager.getLogger(AppLifecycleManager.class);
//     private final AppiumDriver driver;
//     private final String appPackage;
//     private final String mainActivity;

//     public AppLifecycleManager(AppiumDriver driver, String appPackage, String mainActivity) {
//         this.driver = driver;
//         this.appPackage = appPackage;
//         this.mainActivity = mainActivity;
//     }

//     public void ensureAppIsRunning() {
//         try {
//             String pageSource = driver.getPageSource();
//             if (!pageSource.contains(appPackage)) {
//                 launchApp();
//             }
//         } catch (Exception e) {
//             logger.warn("⚠️ App launch check failed: {}", e.getMessage());
//         }
//     }

//     private void launchApp() throws Exception {
//         logger.info("Launching app: {}/{}", appPackage, mainActivity);
//         new ProcessBuilder("adb", "shell", "am", "start", "-n", appPackage + "/" + mainActivity).start();
//         // Wait for app launch to complete using proper conditions
//         try {
//             WebDriverWait appLaunchWait = new WebDriverWait(driver, Duration.ofSeconds(10));
//             // Wait for app to be responsive by checking page source availability
//             appLaunchWait.until(driver -> {
//                 try {
//                     return !driver.getPageSource().isEmpty();
//                 } catch (Exception e) {
//                     return false;
//                 }
//             });
//             logger.info("App launch validation completed");
//         } catch (Exception e) {
//             logger.warn("App launch wait failed, continuing: {}", e.getMessage());
//         }
//         logger.info("App launch completed");
//     }
// }

package com.company.framework.managers;

import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

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

    /**
     * Ensures that the target app is launched and responsive.
     */
    public void ensureAppIsRunning() {
        try {
            if (!isAppForeground()) {
                logger.info("📱 App '{}' not detected in foreground. Launching...", appPackage);
                launchApp();
            } else {
                logger.info("✅ App '{}' is already running.", appPackage);
            }
        } catch (Exception e) {
            logger.warn("⚠️ App lifecycle check failed: {}", e.getMessage());
        }
    }

    /**
     * Launch the app using ADB shell if not already active.
     */
    private void launchApp() {
        try {
            logger.info("🚀 Launching app: {}/{}", appPackage, mainActivity);

            new ProcessBuilder("adb", "shell", "am", "start", "-n",
                    appPackage + "/" + mainActivity).start();

            waitForAppToBeReady(10);
            logger.info("✅ App launch completed successfully.");
        } catch (Exception e) {
            logger.error("❌ App launch failed: {}", e.getMessage());
        }
    }

    /**
     * Wait until the app becomes responsive.
     */
    private void waitForAppToBeReady(int timeoutSec) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
            wait.until(d -> {
                try {
                    return d.getPageSource() != null && !d.getPageSource().isEmpty();
                } catch (Exception e) {
                    return false;
                }
            });
        } catch (Exception e) {
            logger.warn("⏳ App readiness wait timed out after {}s: {}", timeoutSec, e.getMessage());
        }
    }

    /**
     * Checks if the expected app package is currently in the foreground.
     */
    private boolean isAppForeground() {
        try {
            // Works for Android (using mobile: shell)
            String currentApp = (String) driver.executeScript("mobile: getAppiumSettings");
            if (currentApp != null && currentApp.contains(appPackage)) {
                return true;
            }
        } catch (Exception ignored) {
            // Fallback: use driver context or page source
        }

        try {
            String src = driver.getPageSource();
            return src != null && !src.isEmpty() && src.contains("android");
        } catch (Exception e) {
            return false;
        }
    }
}

