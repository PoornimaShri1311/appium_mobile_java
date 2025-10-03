package utils;

import config.ApplicationConstants;
import java.util.Properties;

/**
 * FrameworkConfig - Centralized configuration management for the framework
 * This class provides easy access to framework-wide configurations
 */
public class FrameworkConfig {
    
    private static final String FRAMEWORK_CONFIG_FILE = "framework.properties";
    private static Properties frameworkProps;
    
    static {
        frameworkProps = PropertyLoader.loadProperties(FRAMEWORK_CONFIG_FILE);
    }
    
    public static String getFrameworkName() {
        return frameworkProps.getProperty("framework.name", "Mobile Automation Framework");
    }
    
    public static String getFrameworkVersion() {
        return frameworkProps.getProperty("framework.version", "1.0.0");
    }
    
    public static int getImplicitWait() {
        return Integer.parseInt(frameworkProps.getProperty("implicit.wait", String.valueOf(ApplicationConstants.TIMEOUT_IMPLICIT_WAIT)));
    }
    
    public static int getExplicitWait() {
        return Integer.parseInt(frameworkProps.getProperty("explicit.wait", String.valueOf(ApplicationConstants.TIMEOUT_EXPLICIT_WAIT)));
    }
    
    public static int getPageLoadTimeout() {
        return Integer.parseInt(frameworkProps.getProperty("page.load.timeout", String.valueOf(ApplicationConstants.TIMEOUT_PAGE_LOAD)));
    }
    
    public static boolean isRetryEnabled() {
        return Boolean.parseBoolean(frameworkProps.getProperty("retry.failed.tests", "false"));
    }
    
    public static int getRetryCount() {
        return Integer.parseInt(frameworkProps.getProperty("retry.count", "1"));
    }
    
    public static boolean isScreenshotOnFailure() {
        return Boolean.parseBoolean(frameworkProps.getProperty("screenshot.on.failure", "true"));
    }
    
    public static boolean isScreenshotOnSuccess() {
        return Boolean.parseBoolean(frameworkProps.getProperty("screenshot.on.success", "false"));
    }
    
    public static String getLogLevel() {
        return frameworkProps.getProperty("log.level", "INFO");
    }
    
    // Appium Server Configuration Methods
    public static boolean isAppiumServerAutoStart() {
        return Boolean.parseBoolean(frameworkProps.getProperty("appium.server.auto.start", "false"));
    }
    
    public static String getAppiumServerHost() {
        return frameworkProps.getProperty("appium.server.host", "127.0.0.1");
    }
    
    public static int getAppiumServerPort() {
        return Integer.parseInt(frameworkProps.getProperty("appium.server.port", "4723"));
    }
    
    public static String getAppiumServerPath() {
        return frameworkProps.getProperty("appium.server.path", "/wd/hub");
    }
    
    public static int getAppiumServerStartupTimeout() {
        return Integer.parseInt(frameworkProps.getProperty("appium.server.startup.timeout", "30"));
    }
    
    public static int getAppiumServerShutdownTimeout() {
        return Integer.parseInt(frameworkProps.getProperty("appium.server.shutdown.timeout", "10"));
    }
    
    public static String getAppiumServerLogLevel() {
        return frameworkProps.getProperty("appium.server.log.level", "info");
    }
    
    public static String getAppiumServerExecutablePath() {
        return frameworkProps.getProperty("appium.server.executable.path", "");
    }
    
    public static String getAppiumServerUrl() {
        return String.format("http://%s:%d%s", getAppiumServerHost(), getAppiumServerPort(), getAppiumServerPath());
    }
}