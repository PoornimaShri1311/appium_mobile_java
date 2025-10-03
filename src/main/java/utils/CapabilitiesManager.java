package utils;

import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.Properties;

public class CapabilitiesManager {

    private static final String DEFAULT_CAPABILITIES_FILE = "capabilities.properties";
    private static final String IOS_CAPABILITIES_FILE = "capabilities-ios.properties";
    
    /**
     * Get capabilities using default Android configuration
     */
    public static DesiredCapabilities getCapabilities() {
        return getCapabilitiesFromFile(DEFAULT_CAPABILITIES_FILE);
    }
    
    /**
     * Get capabilities for specific platform
     * @param platform "android" or "ios"
     */
    public static DesiredCapabilities getCapabilitiesForPlatform(String platform) {
        String capFile = DEFAULT_CAPABILITIES_FILE;
        if ("ios".equalsIgnoreCase(platform)) {
            capFile = IOS_CAPABILITIES_FILE;
        }
        return getCapabilitiesFromFile(capFile);
    }
    
    /**
     * Get capabilities from specific file
     */
    public static DesiredCapabilities getCapabilitiesFromFile(String capabilitiesFile) {
        Properties props = PropertyLoader.loadProperties(capabilitiesFile);
        DesiredCapabilities capabilities = new DesiredCapabilities();

        // Handle platform-specific app configuration
        String platformName = props.getProperty("platformName", "Android");
        
        if ("iOS".equalsIgnoreCase(platformName)) {
            handleiOSAppConfiguration(props, capabilities);
        } else {
            handleAndroidAppConfiguration(props, capabilities);
        }

        // Loop through each property and set it as a capability except app-related and server properties
        for (String key : props.stringPropertyNames()) {
            if (!key.equalsIgnoreCase("app") && !key.equalsIgnoreCase("appPackage") && 
                !key.equalsIgnoreCase("appActivity") && !key.equalsIgnoreCase("bundleId") && 
                !key.equalsIgnoreCase("appiumServer")) {
                String value = props.getProperty(key);

                // Handle boolean capabilities like noReset
                if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                    capabilities.setCapability(key, Boolean.parseBoolean(value));
                } else {
                    capabilities.setCapability(key, value);
                }
            }
        }
        return capabilities;
    }
    
    /**
     * Configure Android app capabilities
     */
    private static void handleAndroidAppConfiguration(Properties props, DesiredCapabilities capabilities) {
        String appPath = props.getProperty("app");
        String appPackage = props.getProperty("appPackage");
        String appActivity = props.getProperty("appActivity");
        
        if (appPath != null && !appPath.isEmpty()) {
            // APK file installation approach
            String fullAppPath = System.getProperty("user.dir") + appPath;
            File apkFile = new File(fullAppPath);
            if (!apkFile.exists()) {
                throw new RuntimeException("APK file not found at path: " + fullAppPath + 
                    "\nPlease ensure the APK file is placed at the correct location as mentioned in the README.md");
            }
            capabilities.setCapability("app", fullAppPath);
        } else if (appPackage != null && !appPackage.isEmpty() && appActivity != null && !appActivity.isEmpty()) {
            // Package/Activity approach for already installed apps
            capabilities.setCapability("appPackage", appPackage);
            capabilities.setCapability("appActivity", appActivity);
        } else {
            throw new RuntimeException("For Android: Either 'app' path or both 'appPackage' and 'appActivity' must be specified");
        }
    }
    
    /**
     * Configure iOS app capabilities
     */
    private static void handleiOSAppConfiguration(Properties props, DesiredCapabilities capabilities) {
        String appPath = props.getProperty("app");
        String bundleId = props.getProperty("bundleId");
        
        if (appPath != null && !appPath.isEmpty()) {
            // IPA file installation approach
            String fullAppPath = System.getProperty("user.dir") + appPath;
            File ipaFile = new File(fullAppPath);
            if (!ipaFile.exists()) {
                throw new RuntimeException("IPA file not found at path: " + fullAppPath + 
                    "\nPlease ensure the IPA file is placed at the correct location");
            }
            capabilities.setCapability("app", fullAppPath);
        } else if (bundleId != null && !bundleId.isEmpty()) {
            // Bundle ID approach for already installed apps
            capabilities.setCapability("bundleId", bundleId);
        } else {
            throw new RuntimeException("For iOS: Either 'app' path or 'bundleId' must be specified");
        }
    }
}

//Multiple device handling
/*String devicePrefix = "device1"; // or pass dynamically
for (String key : props.stringPropertyNames()) {
        if (key.startsWith(devicePrefix + ".")) {
String capabilityKey = key.substring((devicePrefix + ".").length());
        if (!capabilityKey.equalsIgnoreCase("app")) {
String value = props.getProperty(key);
            if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
        capabilities.setCapability(capabilityKey, Boolean.parseBoolean(value));
        } else {
        capabilities.setCapability(capabilityKey, value);
            }
                    }
                    }
                    }*/

//Dynamically we can pass device details from TestNg parameter file or cmd "mvn test -Ddevice=device1mvn test -Ddevice=device1"
