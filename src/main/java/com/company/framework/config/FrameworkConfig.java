package com.company.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

/**
 * FrameworkConfig - Centralized configuration management for the framework
 * <p>
 * Provides easy access to framework-wide configurations, leveraging defaults
 * from ApplicationConstants.
 * </p>
 */
public final class FrameworkConfig {

    private static final String CONFIG_FILE = "config/framework.properties";
    private static final Properties props = loadProperties(CONFIG_FILE);

    private FrameworkConfig() {
        throw new AssertionError("Utility class - cannot instantiate");
    }

    // ==========================
    // PROPERTY LOADING
    // ==========================
    public static Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        try (InputStream input = FrameworkConfig.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load framework properties from " + fileName, e);
        }
        return properties;
    }

    // ==========================
    // HELPER METHODS
    // ==========================
    private static String getString(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    private static int getInt(String key, int defaultValue) {
        String value = props.getProperty(key);
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static boolean getBoolean(String key, boolean defaultValue) {
        String value = props.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    private static Duration getDurationInSeconds(String key, int defaultSeconds) {
        return Duration.ofSeconds(getInt(key, defaultSeconds));
    }

    // ==========================
    // GENERAL FRAMEWORK CONFIG
    // ==========================
    public static String getFrameworkName() {
        return getString("framework.name", "Mobile Automation Framework");
    }

    public static String getFrameworkVersion() {
        return getString("framework.version", "1.0.0");
    }

    public static Duration getImplicitWait() {
        return getDurationInSeconds("implicit.wait", (int) ApplicationConstants.Timeouts.IMPLICIT_WAIT.getSeconds());
    }

    public static Duration getExplicitWait() {
        return getDurationInSeconds("explicit.wait", (int) ApplicationConstants.Timeouts.EXPLICIT_WAIT.getSeconds());
    }

    public static Duration getPageLoadTimeout() {
        return getDurationInSeconds("page.load.timeout", (int) ApplicationConstants.Timeouts.PAGE_LOAD.getSeconds());
    }

    public static boolean isRetryEnabled() {
        return getBoolean("retry.failed.tests", false);
    }

    public static int getRetryCount() {
        return getInt("retry.count", 1);
    }

    public static boolean isScreenshotOnFailure() {
        return getBoolean("screenshot.on.failure", true);
    }

    public static boolean isScreenshotOnSuccess() {
        return getBoolean("screenshot.on.success", false);
    }

    public static String getLogLevel() {
        return getString("log.level", "INFO");
    }

    // ==========================
    // APPIUM SERVER CONFIG
    // ==========================
    public static boolean isAppiumServerAutoStart() {
        return getBoolean("appium.server.auto.start", false);
    }

    public static String getAppiumServerHost() {
        return getString("appium.server.host", ApplicationConstants.Appium.SERVER_HOST);
    }

    public static int getAppiumServerPort() {
        return getInt("appium.server.port", ApplicationConstants.Appium.SERVER_PORT);
    }

    public static String getAppiumServerPath() {
        return getString("appium.server.path", ApplicationConstants.Appium.SERVER_PATH);
    }

    public static Duration getAppiumServerStartupTimeout() {
        return getDurationInSeconds("appium.server.startup.timeout", 30);
    }

    public static Duration getAppiumServerShutdownTimeout() {
        return getDurationInSeconds("appium.server.shutdown.timeout", 10);
    }

    public static String getAppiumServerLogLevel() {
        return getString("appium.server.log.level", "info");
    }

    public static String getAppiumServerExecutablePath() {
        return getString("appium.server.executable.path", "");
    }

    public static String getAppiumServerUrl() {
        return String.format("http://%s:%d%s",
                getAppiumServerHost(),
                getAppiumServerPort(),
                getAppiumServerPath());
    }
}
