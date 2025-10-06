package com.company.framework.managers;

import com.company.framework.interfaces.actions.IPageActions;
import com.company.framework.interfaces.config.IConfigurationManager;
import com.company.framework.interfaces.driver.IDriverManager;
import com.company.framework.interfaces.wait.IWaitStrategy;
import com.company.framework.utils.ExplicitWaitStrategy;
import com.company.framework.utils.PageActions;

public class DependencyManager {
    private static DependencyManager instance;
    private IDriverManager driverManager;
    private IConfigurationManager configurationManager;
    private IWaitStrategy waitStrategy;
    private IPageActions pageActions;

    private DependencyManager() {
        configurationManager = new com.company.framework.managers.ConfigurationManager();
        driverManager = new com.company.framework.managers.DriverManager(configurationManager);
    }

    public static synchronized DependencyManager getInstance() {
        if (instance == null) instance = new DependencyManager();
        return instance;
    }

    public IDriverManager getDriverManager() { return driverManager; }
    public IConfigurationManager getConfigurationManager() { return configurationManager; }

    public IWaitStrategy getWaitStrategy() {
        if (waitStrategy == null) waitStrategy = new ExplicitWaitStrategy(driverManager, 30);
        return waitStrategy;
    }

    public IPageActions getPageActions() {
        if (pageActions == null) pageActions = new PageActions(getWaitStrategy());
        return pageActions;
    }

    public static void resetInstance() {
        if (instance != null && instance.driverManager != null) instance.driverManager.quitDriver();
        instance = null;
    }
}
