package com.company.framework.utils;

import com.company.framework.interfaces.driver.IDriverManager;
import com.company.framework.managers.DependencyManager;
import io.appium.java_client.AppiumDriver;

/**
 * DriverUtils - Legacy wrapper for backward compatibility
 * Delegates to the new DriverManager through DependencyFactory
 * @deprecated Use DependencyFactory.getInstance().getDriverManager() instead
 */
@Deprecated
public class DriverUtils {

    private static IDriverManager getDriverManager() {
        return DependencyManager.getInstance().getDriverManager();
    }

    public static void initializeDriver() {
        getDriverManager().initializeDriver();
    }

    public static AppiumDriver getDriver() {
        return getDriverManager().getDriver();
    }

    public static void quitDriver() {
        getDriverManager().quitDriver();
        // Reset factory to clean up all dependencies
        DependencyManager.resetInstance();
    }
    
    public static boolean isDriverInitialized() {
        return getDriverManager().isDriverInitialized();
    }
}
