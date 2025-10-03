package utils;

import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * PlatformManager - Manages platform-specific configurations and capabilities
 * Supports both Android and iOS platforms
 */
public class PlatformManager {
    
    public enum Platform {
        ANDROID("android"),
        IOS("ios");
        
        private final String name;
        
        Platform(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
        
        public static Platform fromString(String platformName) {
            if (platformName == null) return ANDROID; // Default to Android
            
            for (Platform platform : Platform.values()) {
                if (platform.name.equalsIgnoreCase(platformName.trim())) {
                    return platform;
                }
            }
            return ANDROID; // Default fallback
        }
    }
    
    /**
     * Get platform from system property or environment variable
     * Priority: -Dplatform > environment variable > default (Android)
     */
    public static Platform getCurrentPlatform() {
        // Check system property first (mvn test -Dplatform=ios)
        String platformProp = System.getProperty("platform");
        if (platformProp != null) {
            return Platform.fromString(platformProp);
        }
        
        // Check environment variable
        String platformEnv = System.getenv("TEST_PLATFORM");
        if (platformEnv != null) {
            return Platform.fromString(platformEnv);
        }
        
        // Default to Android
        return Platform.ANDROID;
    }
    
    /**
     * Get capabilities for the current platform
     */
    public static DesiredCapabilities getPlatformCapabilities() {
        Platform platform = getCurrentPlatform();
        return CapabilitiesManager.getCapabilitiesForPlatform(platform.getName());
    }
    
    /**
     * Check if current platform is iOS
     */
    public static boolean isiOS() {
        return getCurrentPlatform() == Platform.IOS;
    }
    
    /**
     * Check if current platform is Android
     */
    public static boolean isAndroid() {
        return getCurrentPlatform() == Platform.ANDROID;
    }
    
    /**
     * Get platform-specific app identifier
     */
    public static String getAppIdentifier() {
        if (isiOS()) {
            return "com.bild.news.ios"; // iOS Bundle ID
        } else {
            return "com.netbiscuits.bild.android"; // Android Package Name
        }
    }
    
    /**
     * Get platform-specific automation engine
     */
    public static String getAutomationName() {
        if (isiOS()) {
            return "XCUITest";
        } else {
            return "UiAutomator2";
        }
    }
    
    /**
     * Print current platform configuration
     */
    public static void printPlatformInfo() {
        Platform platform = getCurrentPlatform();
        System.out.println("=== Platform Configuration ===");
        System.out.println("Platform: " + platform.getName().toUpperCase());
        System.out.println("App ID: " + getAppIdentifier());
        System.out.println("Automation: " + getAutomationName());
        System.out.println("==============================");
    }
}