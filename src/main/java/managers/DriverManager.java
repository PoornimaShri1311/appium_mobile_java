package managers;

import config.ApplicationConstants;
import interfaces.IDriverManager;
import interfaces.IConfigurationManager;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import utils.CapabilitiesManager;
import utils.FrameworkConfig;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Properties;

/**
 * DriverManager - Implementation of IDriverManager
 * Follows Dependency Inversion Principle
 */
public class DriverManager implements IDriverManager {
    
    private AppiumDriver driver;
    private final IConfigurationManager configManager;
    private static final String CONFIG_FILE = "capabilities.properties";
    
    public DriverManager(IConfigurationManager configManager) {
        this.configManager = configManager;
    }
    
    @Override
    public void initializeDriver() {
        if (driver != null) {
            return; // Driver already initialized
        }
        
        DesiredCapabilities capabilities = CapabilitiesManager.getCapabilities();
        
        // Get Appium server URL from FrameworkConfig (which takes precedence)
        // Fall back to capabilities.properties if not available in framework config
        String appiumServerUrl;
        try {
            appiumServerUrl = FrameworkConfig.getAppiumServerUrl();
        } catch (Exception e) {
            // Fallback to capabilities.properties
            Properties props = configManager.loadProperties(CONFIG_FILE);
            appiumServerUrl = props.getProperty("appiumServer", ApplicationConstants.APPIUM_SERVER_URL);
        }
        
        try {
            driver = new AppiumDriver(URI.create(appiumServerUrl).toURL(), capabilities);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Appium server URL is invalid: " + appiumServerUrl, e);
        }
    }
    
    @Override
    public AppiumDriver getDriver() {
        if (driver == null) {
            throw new IllegalStateException("Driver not initialized. Please call initializeDriver first.");
        }
        return driver;
    }
    
    @Override
    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
    
    @Override
    public boolean isDriverInitialized() {
        return driver != null;
    }
}