package com.company.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public final class ApplicationConstants {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationConstants.class);
    private static final Properties config = new Properties();
    private static final String CONFIG_FILE_PATH = "/config/app-config.properties";

    static {
        loadConfiguration();
    }

    private ApplicationConstants() {
        throw new AssertionError("Utility class, cannot instantiate");
    }

    // ==========================
    // APPLICATION CONFIG
    // ==========================
    public static class AppConfig {
        public static final String APP_PACKAGE_NAME = getProperty("app.package.name", "com.netbiscuits.bild.android");
        public static final String APP_MAIN_ACTIVITY = getProperty("app.activity.main", ".MainActivity");
        public static final String APP_NAME = getProperty("app.name", "BILD");
        public static final String APP_VERSION = getProperty("app.version", "latest");
    }

    // ==========================
    // APPIUM CONFIG
    // ==========================
    public static class Appium {
        public static final String SERVER_URL = getProperty("appium.server.url", "http://127.0.0.1:4723/");
        public static final String SERVER_HOST = getProperty("appium.server.host", "127.0.0.1");
        public static final int SERVER_PORT = getIntProperty("appium.server.port", 4723);
        public static final String SERVER_PATH = getProperty("appium.server.path", "/wd/hub");
    }

    // ==========================
    // TIMEOUTS
    // ==========================
    public static class Timeouts {
        public static final Duration IMPLICIT_WAIT = Duration.ofSeconds(getIntProperty("timeout.implicit.wait", 10));
        public static final Duration EXPLICIT_WAIT = Duration.ofSeconds(getIntProperty("timeout.explicit.wait", 30));
        public static final Duration PAGE_LOAD = Duration.ofSeconds(getIntProperty("timeout.page.load", 30));
        public static final Duration ELEMENT_WAIT = Duration.ofSeconds(getIntProperty("timeout.element.wait", 20));
        public static final Duration APP_LAUNCH = Duration.ofSeconds(getIntProperty("timeout.app.launch", 60));
    }

    // ==========================
    // RETRY CONFIG
    // ==========================
    public static class Retry {
        public static final int MAX_ATTEMPTS = getIntProperty("retry.max.attempts", 3);
        public static final Duration DELAY = Duration.ofMillis(getIntProperty("retry.delay.ms", 1000));
    }

    // ==========================
    // LOGGING
    // ==========================
    public static class Logging {
        public static final String LEVEL = getProperty("logging.level", "INFO");
        public static final boolean SCREENSHOT_ON_FAILURE = getBooleanProperty("logging.screenshot.on.failure", true);
        public static final boolean DETAILED_STEPS = getBooleanProperty("logging.detailed.steps", true);
        public static final boolean EMOJI_ENABLED = getBooleanProperty("logging.emoji.enabled", true);
    }

    // ==========================
    // TEST DATA
    // ==========================
    public static class TestData {
        public static final String SEARCH_TERM_GERMAN = getProperty("test.search.term.german", "Test");
        public static final String SEARCH_TERM_ENGLISH = getProperty("test.search.term.english", "News");
        public static final int SEARCH_MIN_RESULTS = getIntProperty("test.search.minimum.results", 1);
        public static final int SEARCH_MAX_RESULTS = getIntProperty("test.search.maximum.results", 100);
    }

    // ==========================
    // PRIVATE METHODS
    // ==========================
    private static void loadConfiguration() {
        try (InputStream input = ApplicationConstants.class.getResourceAsStream(CONFIG_FILE_PATH)) {
            if (input != null) {
                config.load(input);
                logger.info("Configuration loaded successfully from {}", CONFIG_FILE_PATH);
            } else {
                logger.warn("Config file not found at {}, using defaults", CONFIG_FILE_PATH);
            }
        } catch (IOException e) {
            logger.error("Failed to load config file: {}", e.getMessage());
        }
    }

    private static String getProperty(String key, String defaultValue) {
        return config.getProperty(key, defaultValue);
    }

    private static int getIntProperty(String key, int defaultValue) {
        try {
            String value = config.getProperty(key);
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer for key {}, using default {}", key, defaultValue);
            return defaultValue;
        }
    }

    private static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = config.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }
}
