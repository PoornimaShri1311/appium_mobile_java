package com.company.framework.interfaces;

import io.appium.java_client.AppiumDriver;

/**
 * IDriverManager - Interface for WebDriver management
 * Follows Dependency Inversion Principle - depend on abstractions, not concretions
 */
public interface IDriverManager {
    
    /**
     * Initialize the driver with capabilities
     */
    void initializeDriver();
    
    /**
     * Get the current driver instance
     * @return AppiumDriver instance
     */
    AppiumDriver getDriver();
    
    /**
     * Quit the driver and clean up resources
     */
    void quitDriver();

    /**
     * Check if driver is initialized
     * @return true if driver is active, false otherwise
     */
    boolean isDriverInitialized();
    
    /**
     * Initialize driver and return the instance
     * @return AppiumDriver instance
     */
    AppiumDriver initializeAndGetDriver();
}