package com.company.framework.managers;

import com.company.framework.interfaces.*;
import com.company.framework.config.ApplicationConstants;
import com.company.framework.utils.ExplicitWaitStrategy;
import com.company.framework.utils.PageActions;

/**
 * DependencyFactory - Factory for creating and managing dependencies
 * Follows Factory Pattern and helps with Dependency Injection
 * This acts as a simple IoC container
 */
public class DependencyFactory {
    
    private static DependencyFactory instance;
    private IDriverManager driverManager;
    private IConfigurationManager configurationManager;
    private IWaitStrategy waitStrategy;
    private IPageActions pageActions;
    
    private DependencyFactory() {
        initializeDependencies();
    }
    
    public static synchronized DependencyFactory getInstance() {
        if (instance == null) {
            instance = new DependencyFactory();
        }
        return instance;
    }
    
    private void initializeDependencies() {
        try {
            // Create configuration manager first (no dependencies)
            configurationManager = new ConfigurationManager();
            
            // Create driver manager (depends on configuration)
            driverManager = new DriverManager(configurationManager);
            
            // Don't initialize driver-dependent components here
            // They will be lazily initialized when first accessed
            
        } catch (Exception e) {
            // Log the initialization issue but don't fail completely
            System.err.println("Warning: DependencyFactory initialization encountered an issue: " + e.getMessage());
            // Provide fallback initialization if needed
            configurationManager = new ConfigurationManager();
        }
    }
    
    public IDriverManager getDriverManager() {
        return driverManager;
    }
    
    public IConfigurationManager getConfigurationManager() {
        return configurationManager;
    }
    
    public IWaitStrategy getWaitStrategy() {
        if (waitStrategy == null) {
            try {
                waitStrategy = new ExplicitWaitStrategy(driverManager, (int) ApplicationConstants.Timeouts.EXPLICIT_WAIT.getSeconds());
            } catch (Exception e) {
                System.err.println("Warning: Could not initialize WaitStrategy: " + e.getMessage());
                return null;
            }
        }
        return waitStrategy;
    }
    
    public IPageActions getPageActions() {
        if (pageActions == null) {
            try {
                IWaitStrategy strategy = getWaitStrategy();
                if (strategy != null) {
                    pageActions = new PageActions(strategy);
                }
            } catch (Exception e) {
                System.err.println("Warning: Could not initialize PageActions: " + e.getMessage());
                return null;
            }
        }
        return pageActions;
    }
    
    /**
     * Static factory methods for easy access
     */
    public static IDriverManager createDriverManager() {
        return getInstance().getDriverManager();
    }
    
    public static IConfigurationManager createConfigurationManager() {
        return getInstance().getConfigurationManager();
    }
    
    public static IWaitStrategy createWaitStrategy() {
        return getInstance().getWaitStrategy();
    }
    
    public static IPageActions createPageActions() {
        return getInstance().getPageActions();
    }
    
    /**
     * Reset factory instance - useful for testing
     */
    public static void resetInstance() {
        if (instance != null && instance.driverManager != null) {
            instance.driverManager.quitDriver();
        }
        instance = null;
    }
}