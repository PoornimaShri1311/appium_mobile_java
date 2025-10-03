package utils;

import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.Properties;

public class CapabilitiesManager {

    private static final String CAPABILITIES_FILE = "capabilities.properties";

    public static DesiredCapabilities getCapabilities() {
        Properties props = PropertyLoader.loadProperties(CAPABILITIES_FILE);
        DesiredCapabilities capabilities = new DesiredCapabilities();

        // Handle app installation vs app package/activity
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
            throw new RuntimeException("Either 'app' path or both 'appPackage' and 'appActivity' must be specified in capabilities.properties");
        }

        // Loop through each property and set it as a capability except app-related and server properties
        for (String key : props.stringPropertyNames()) {
            if (!key.equalsIgnoreCase("app") && !key.equalsIgnoreCase("appPackage") && 
                !key.equalsIgnoreCase("appActivity") && !key.equalsIgnoreCase("appiumServer")) {
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
