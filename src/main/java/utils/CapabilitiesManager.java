package utils;

import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Properties;

public class CapabilitiesManager {

    private static final String CAPABILITIES_FILE = "capabilities.properties";

    public static DesiredCapabilities getCapabilities() {
        Properties props = PropertyLoader.loadProperties(CAPABILITIES_FILE);
        DesiredCapabilities capabilities = new DesiredCapabilities();

        // Read and set app path, adjust for absolute path if needed
        String appPath = System.getProperty("user.dir") + props.getProperty("app");
        capabilities.setCapability(MobileCapabilityType.APP, appPath);

        // Loop through each property and set it as a capability except "app" which is handled
        for (String key : props.stringPropertyNames()) {
            if (!key.equalsIgnoreCase("app")) {
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
