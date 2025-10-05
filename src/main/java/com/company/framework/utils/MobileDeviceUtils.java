package com.company.framework.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.company.framework.config.FrameworkConfig;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.xml.XmlSuite;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.Properties;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * MobileDeviceUtils - Consolidated utility for mobile device management
 * Combines AppiumServer, Capabilities, Platform, and DeviceMatrix management
 * 
 * Responsibilities:
 * - Appium server lifecycle management
 * - Device capability configuration  
 * - Platform-specific settings (Android/iOS)
 * - Device matrix allocation and TestNG suite generation
 */
public class MobileDeviceUtils {
    
    private static final Logger logger = LogManager.getLogger(MobileDeviceUtils.class);
    
    // ========================================
    // APPIUM SERVER MANAGEMENT
    // ========================================
    
    private static AppiumDriverLocalService service;
    private static boolean serverStartedByFramework = false;
    
    /**
     * Start Appium server if not already running
     */
    public static void startAppiumServer() {
        try {
            if (service != null && service.isRunning()) {
                logger.info("Appium server is already running");
                return;
            }
            
            logger.info("Starting Appium server...");
            
            // Get configured port from framework properties
            int port = FrameworkConfig.getAppiumServerPort();
            String host = FrameworkConfig.getAppiumServerHost();
            
            service = new AppiumServiceBuilder()
                .withIPAddress(host)
                .usingPort(port)
                .build();
                
            service.start();
            serverStartedByFramework = true;
            
            logger.info("Appium server started successfully on: " + service.getUrl());
            
        } catch (Exception e) {
            logger.info("Appium server is already running, skipping startup");
        }
    }
    
    /**
     * Stop Appium server if it was started by this framework
     */
    public static void stopAppiumServer() {
        if (service != null && service.isRunning() && serverStartedByFramework) {
            logger.info("Stopping Appium server...");
            service.stop();
            logger.info("Appium server stopped successfully");
        } else {
            logger.info("Appium server was not started by this manager or is already stopped");
        }
    }
    
    /**
     * Get Appium server URL
     */
    public static URL getAppiumServerUrl() {
        if (service != null && service.isRunning()) {
            return service.getUrl();
        }
        
        try {
            String host = FrameworkConfig.getAppiumServerHost();
            int port = FrameworkConfig.getAppiumServerPort();
            return new URL("http://" + host + ":" + port + "/");
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid default Appium URL", e);
        }
    }
    
    // ========================================
    // PLATFORM MANAGEMENT
    // ========================================
    
    /**
     * Get platform-specific capabilities
     */
    public static DesiredCapabilities getPlatformCapabilities(String platform) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        
        // Load capabilities from properties file instead of hardcoding
        Properties props = FrameworkConfig.loadProperties("config/capabilities.properties");
        
        switch (platform.toLowerCase()) {
            case "android":
                capabilities.setCapability("platformName", props.getProperty("platformName", "Android"));
                capabilities.setCapability("automationName", props.getProperty("automationName", "UiAutomator2"));
                capabilities.setCapability("deviceName", props.getProperty("deviceName", "Android Device"));
                capabilities.setCapability("appPackage", props.getProperty("appPackage", "com.netbiscuits.bild.android"));
                capabilities.setCapability("appActivity", props.getProperty("appActivity", "de.bild.android.app.MainActivity"));
                
                // Optional Android-specific capabilities
                if (props.containsKey("udid")) {
                    capabilities.setCapability("udid", props.getProperty("udid"));
                }
                if (props.containsKey("autoGrantPermissions")) {
                    capabilities.setCapability("autoGrantPermissions", Boolean.parseBoolean(props.getProperty("autoGrantPermissions")));
                }
                if (props.containsKey("noReset")) {
                    capabilities.setCapability("noReset", Boolean.parseBoolean(props.getProperty("noReset")));
                }
                
                // Ensure app launches automatically
                capabilities.setCapability("autoLaunch", true);
                capabilities.setCapability("forceAppLaunch", true);
                break;
                
            case "ios":
                capabilities.setCapability("platformName", "iOS");
                capabilities.setCapability("automationName", "XCUITest");
                capabilities.setCapability("deviceName", "iPhone");
                capabilities.setCapability("bundleId", "com.axelspringer.bildapp");
                break;
                
            default:
                throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
        
        // Common capabilities from properties
        if (props.containsKey("newCommandTimeout")) {
            capabilities.setCapability("newCommandTimeout", Integer.parseInt(props.getProperty("newCommandTimeout", "300")));
        }
        
        return capabilities;
    }
    
    /**
     * Check if platform is supported
     */
    public static boolean isPlatformSupported(String platform) {
        return "android".equalsIgnoreCase(platform) || "ios".equalsIgnoreCase(platform);
    }
    
    // ========================================
    // DEVICE MATRIX MANAGEMENT
    // ========================================
    
    private static final Map<String, Object> deviceMatrix = new ConcurrentHashMap<>();
    private static final Map<String, String> deviceAllocations = new ConcurrentHashMap<>();
    
    /**
     * Load device matrix from JSON configuration
     */
    @SuppressWarnings("unchecked")
    public static void loadDeviceMatrix() {
        try (InputStream inputStream = MobileDeviceUtils.class.getClassLoader()
                .getResourceAsStream("device-matrix.json")) {
                
            if (inputStream == null) {
                logger.warn("device-matrix.json not found, using default configuration");
                createDefaultMatrix();
                return;
            }
            
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            
            deviceMatrix.putAll(gson.fromJson(content, type));
            logger.info("Device matrix loaded successfully with {} devices", 
                getAvailableDevices().size());
                
        } catch (IOException e) {
            logger.error("Failed to load device matrix", e);
            createDefaultMatrix();
        }
    }
    
    /**
     * Create default device matrix if configuration file is not found
     */
    @SuppressWarnings("unchecked")
    private static void createDefaultMatrix() {
        Map<String, Object> defaultMatrix = new HashMap<>();
        
        // Default Android device
        Map<String, Object> androidDevice = new HashMap<>();
        androidDevice.put("platform", "Android");
        androidDevice.put("version", "13.0");
        androidDevice.put("available", true);
        
        // Default iOS device
        Map<String, Object> iosDevice = new HashMap<>();
        iosDevice.put("platform", "iOS");
        iosDevice.put("version", "17.0");
        iosDevice.put("available", true);
        
        Map<String, Object> devices = new HashMap<>();
        devices.put("Pixel_7_API_33", androidDevice);
        devices.put("iPhone_15_Pro", iosDevice);
        
        defaultMatrix.put("devices", devices);
        deviceMatrix.putAll(defaultMatrix);
        
        logger.info("Created default device matrix with 2 devices");
    }
    
    /**
     * Get all available devices
     */
    @SuppressWarnings("unchecked")
    public static Set<String> getAvailableDevices() {
        if (deviceMatrix.isEmpty()) {
            loadDeviceMatrix();
        }
        
        Map<String, Object> devices = (Map<String, Object>) deviceMatrix.get("devices");
        return devices != null ? devices.keySet() : new HashSet<>();
    }
    
    /**
     * Allocate a device for testing
     */
    public static String allocateDevice(String platform) {
        Set<String> availableDevices = getAvailableDevices();
        
        List<String> platformDevices = availableDevices.stream()
            .filter(device -> {
                @SuppressWarnings("unchecked")
                Map<String, Object> devices = (Map<String, Object>) deviceMatrix.get("devices");
                @SuppressWarnings("unchecked")
                Map<String, Object> deviceInfo = (Map<String, Object>) devices.get(device);
                return platform.equalsIgnoreCase((String) deviceInfo.get("platform"));
            })
            .toList();
            
        if (platformDevices.isEmpty()) {
            throw new RuntimeException("No available devices for platform: " + platform);
        }
        
        // Simple random allocation for now
        String selectedDevice = platformDevices.get(
            ThreadLocalRandom.current().nextInt(platformDevices.size()));
            
        String threadId = Thread.currentThread().getName();
        deviceAllocations.put(threadId, selectedDevice);
        
        logger.info("Allocated device '{}' to thread '{}'", selectedDevice, threadId);
        return selectedDevice;
    }
    
    /**
     * Release device allocation
     */
    public static void releaseDevice() {
        String threadId = Thread.currentThread().getName();
        String device = deviceAllocations.remove(threadId);
        
        if (device != null) {
            logger.info("Released device '{}' from thread '{}'", device, threadId);
        }
    }
    
    /**
     * Generate TestNG XML suite for device matrix execution
     */
    public static void generateTestNGSuite(String suiteName, List<String> testClasses, 
                                         String outputPath) {
        XmlSuite suite = new XmlSuite();
        suite.setName(suiteName);
        suite.setParallel(XmlSuite.ParallelMode.TESTS);
        suite.setThreadCount(getOptimalThreadCount());
        
        // Add test for each available platform
        Set<String> platforms = Set.of("Android", "iOS");
        
        for (String platform : platforms) {
            try {
                String deviceName = allocateDevice(platform);
                
                org.testng.xml.XmlTest test = new org.testng.xml.XmlTest(suite);
                test.setName(suiteName + "_" + platform);
                
                // Set parameters
                test.addParameter("platformName", platform);
                test.addParameter("deviceName", deviceName);
                
                // Add test classes
                List<org.testng.xml.XmlClass> classes = new ArrayList<>();
                for (String testClass : testClasses) {
                    classes.add(new org.testng.xml.XmlClass(testClass));
                }
                test.setXmlClasses(classes);
                
            } catch (Exception e) {
                logger.warn("Could not create test for platform {}: {}", platform, e.getMessage());
            }
        }
        
        // Write to file if needed
        if (outputPath != null) {
            try {
                java.io.FileWriter writer = new java.io.FileWriter(outputPath);
                writer.write(suite.toXml());
                writer.close();
                logger.info("Generated TestNG suite at: {}", outputPath);
            } catch (IOException e) {
                logger.error("Failed to write TestNG suite to file", e);
            }
        }
    }
    
    /**
     * Get optimal thread count based on available devices
     */
    public static int getOptimalThreadCount() {
        int deviceCount = getAvailableDevices().size();
        int maxThreads = 4; // Reasonable maximum
        return Math.min(deviceCount, maxThreads);
    }
    
    // ========================================
    // CAPABILITY MANAGEMENT
    // ========================================
    
    /**
     * Get comprehensive capabilities for device and platform
     */
    @SuppressWarnings("unchecked")
    public static DesiredCapabilities getDeviceCapabilities(String deviceName, String platform) {
        DesiredCapabilities capabilities = getPlatformCapabilities(platform);
        
        // Get device-specific settings from matrix
        if (!deviceMatrix.isEmpty()) {
            Map<String, Object> devices = (Map<String, Object>) deviceMatrix.get("devices");
            if (devices != null && devices.containsKey(deviceName)) {
                Map<String, Object> deviceInfo = (Map<String, Object>) devices.get(deviceName);
                
                // Apply device-specific capabilities
                if (deviceInfo.containsKey("version")) {
                    capabilities.setCapability("platformVersion", deviceInfo.get("version"));
                }
                
                if (deviceInfo.containsKey("udid")) {
                    capabilities.setCapability("udid", deviceInfo.get("udid"));
                }
            }
        }
        
        capabilities.setCapability("deviceName", deviceName);
        return capabilities;
    }
    
    /**
     * Merge additional capabilities
     */
    public static DesiredCapabilities mergeCapabilities(DesiredCapabilities base, 
                                                       Map<String, Object> additional) {
        DesiredCapabilities merged = new DesiredCapabilities(base);
        
        for (Map.Entry<String, Object> entry : additional.entrySet()) {
            merged.setCapability(entry.getKey(), entry.getValue());
        }
        
        return merged;
    }
}
