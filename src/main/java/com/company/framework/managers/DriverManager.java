package com.company.framework.managers;

import com.company.framework.config.FrameworkConfig;
import com.company.framework.interfaces.config.IConfigurationManager;
import com.company.framework.interfaces.driver.IDriverManager;
import com.company.framework.utils.MobileDeviceUtils;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URI;
import java.util.Properties;

public class DriverManager implements IDriverManager {
    private AppiumDriver driver;
    private final IConfigurationManager configManager;

    public DriverManager(IConfigurationManager configManager) { this.configManager = configManager; }

    @Override
    public AppiumDriver getDriver() {
        if (driver == null) initializeDriver();
        return driver;
    }

    @Override
    public AppiumDriver initializeAndGetDriver() {
        initializeDriver();
        return getDriver();
    }

    @Override
    public void initializeDriver() {
        if (driver != null) return;

        // Get platform from configuration instead of hardcoding
        String platform = configManager.getProperty("platformName", "Android");
        DesiredCapabilities capabilities = MobileDeviceUtils.getPlatformCapabilities(platform);

        String appiumServer = FrameworkConfig.getAppiumServerUrl();
        if (appiumServer == null || appiumServer.isEmpty()) {
            Properties props = configManager.loadProperties("capabilities.properties");
            appiumServer = props.getProperty("appiumServer", FrameworkConfig.getAppiumServerUrl());
        }

        try {
            driver = new AppiumDriver(new URI(appiumServer).toURL(), capabilities);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Appium driver", e);
        }
    }

    @Override
    public void quitDriver() {
        if (driver != null) { driver.quit(); driver = null; }
    }

    @Override
    public boolean isDriverInitialized() { return driver != null; }
}
