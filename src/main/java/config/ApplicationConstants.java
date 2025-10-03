package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ApplicationConstants - Centralized configuration constants for the BILD mobile testing framework
 * 
 * <p>This class follows SOLID principles by providing a single source of truth for all
 * configuration values used throughout the application. It eliminates hardcoded values
 * and provides type-safe access to configuration properties.</p>
 * 
 * <p><strong>SOLID Principles Applied:</strong></p>
 * <ul>
 *   <li><strong>Single Responsibility</strong>: Manages only application configuration</li>
 *   <li><strong>Open/Closed</strong>: Easy to extend with new configuration sections</li>
 *   <li><strong>Dependency Inversion</strong>: Other classes depend on this abstraction</li>
 * </ul>
 * 
 * @author Framework Team
 * @version 1.0
 * @since 2025-10-02
 */
public final class ApplicationConstants {
    
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConstants.class);
    private static final Properties config = new Properties();
    
    // Configuration file path
    private static final String CONFIG_FILE_PATH = "/config/app-config.properties";
    
    static {
        loadConfiguration();
    }
    
    // =====================================
    // APPLICATION CONSTANTS
    // =====================================
    
    /** BILD application package name */
    public static final String APP_PACKAGE_NAME = getProperty("app.package.name", "com.netbiscuits.bild.android");
    
    /** Main activity class name */
    public static final String APP_MAIN_ACTIVITY = getProperty("app.activity.main", ".MainActivity");
    
    /** Application display name */
    public static final String APP_NAME = getProperty("app.name", "BILD");
    
    /** Application version for testing */
    public static final String APP_VERSION = getProperty("app.version", "latest");
    
    // =====================================
    // APPIUM SERVER CONSTANTS
    // =====================================
    
    /** Complete Appium server URL */
    public static final String APPIUM_SERVER_URL = getProperty("appium.server.url", "http://127.0.0.1:4723/");
    
    /** Appium server host address */
    public static final String APPIUM_SERVER_HOST = getProperty("appium.server.host", "127.0.0.1");
    
    /** Appium server port number */
    public static final int APPIUM_SERVER_PORT = getIntProperty("appium.server.port", 4723);
    
    /** Appium server endpoint path */
    public static final String APPIUM_SERVER_PATH = getProperty("appium.server.path", "/wd/hub");
    
    // =====================================
    // TIMEOUT CONSTANTS (in seconds)
    // =====================================
    
    /** Default implicit wait timeout */
    public static final int TIMEOUT_IMPLICIT_WAIT = getIntProperty("timeout.implicit.wait", 10);
    
    /** Default explicit wait timeout */
    public static final int TIMEOUT_EXPLICIT_WAIT = getIntProperty("timeout.explicit.wait", 30);
    
    /** Page load timeout */
    public static final int TIMEOUT_PAGE_LOAD = getIntProperty("timeout.page.load", 30);
    
    /** Element wait timeout */
    public static final int TIMEOUT_ELEMENT_WAIT = getIntProperty("timeout.element.wait", 20);
    
    /** Application launch timeout */
    public static final int TIMEOUT_APP_LAUNCH = getIntProperty("timeout.app.launch", 60);
    
    /** Search results timeout */
    public static final int TIMEOUT_SEARCH_RESULTS = getIntProperty("timeout.search.results", 15);
    
    // =====================================
    // RETRY AND ATTEMPT CONSTANTS
    // =====================================
    
    /** Maximum retry attempts for operations */
    public static final int RETRY_MAX_ATTEMPTS = getIntProperty("retry.max.attempts", 3);
    
    /** Delay between retry attempts in milliseconds */
    public static final int RETRY_DELAY_MS = getIntProperty("retry.delay.ms", 1000);
    
    /** Maximum page load attempts */
    public static final int PAGE_LOAD_MAX_ATTEMPTS = getIntProperty("page.load.max.attempts", 10);
    
    /** Maximum element find attempts */
    public static final int ELEMENT_FIND_MAX_ATTEMPTS = getIntProperty("element.find.max.attempts", 5);
    
    // =====================================
    // DELAY CONSTANTS (in milliseconds)
    // =====================================
    
    /** Delay after click operations */
    public static final int DELAY_AFTER_CLICK = getIntProperty("delay.after.click", 1000);
    
    /** Delay after typing operations */
    public static final int DELAY_AFTER_TYPE = getIntProperty("delay.after.type", 500);
    
    /** App launch stabilization delay */
    public static final int DELAY_APP_LAUNCH = getIntProperty("delay.app.launch", 5000);
    
    /** Search execution delay */
    public static final int DELAY_SEARCH_EXECUTION = getIntProperty("delay.search.execution", 3000);
    
    /** Navigation delay */
    public static final int DELAY_NAVIGATION = getIntProperty("delay.navigation", 2000);
    
    // =====================================
    // TEST DATA CONSTANTS
    // =====================================
    
    /** German search term for testing */
    public static final String TEST_SEARCH_TERM_GERMAN = getProperty("test.search.term.german", "Umfrage");
    
    /** English search term for testing */
    public static final String TEST_SEARCH_TERM_ENGLISH = getProperty("test.search.term.english", "News");
    
    /** Minimum expected search results */
    public static final int TEST_SEARCH_MIN_RESULTS = getIntProperty("test.search.minimum.results", 1);
    
    /** Maximum expected search results */
    public static final int TEST_SEARCH_MAX_RESULTS = getIntProperty("test.search.maximum.results", 100);
    
    // =====================================
    // XPATH SELECTOR CONSTANTS
    // =====================================
    
    /** Android Button XPath selector */
    public static final String XPATH_ANDROID_BUTTON = getProperty("xpath.android.button", "//android.widget.Button");
    
    /** Android EditText XPath selector */
    public static final String XPATH_ANDROID_EDITTEXT = getProperty("xpath.android.edittext", "//android.widget.EditText");
    
    /** Android ImageView XPath selector */
    public static final String XPATH_ANDROID_IMAGEVIEW = getProperty("xpath.android.imageview", "//android.widget.ImageView");
    
    /** Android ImageButton XPath selector */
    public static final String XPATH_ANDROID_IMAGEBUTTON = getProperty("xpath.android.imagebutton", "//android.widget.ImageButton");
    
    /** AndroidX ComposeView XPath selector */
    public static final String XPATH_ANDROIDX_COMPOSEVIEW = getProperty("xpath.androidx.composeview", "//androidx.compose.ui.platform.ComposeView");
    
    /** Android View XPath selector */
    public static final String XPATH_ANDROID_VIEW = getProperty("xpath.android.view", "//android.view.View");
    
    // =====================================
    // CONTENT DESCRIPTION CONSTANTS
    // =====================================
    
    /** Search content description */
    public static final String CONTENT_DESC_SEARCH = getProperty("content.desc.search", "Search");
    
    /** Home content description */
    public static final String CONTENT_DESC_HOME = getProperty("content.desc.home", "Home");
    
    /** Sport content description */
    public static final String CONTENT_DESC_SPORT = getProperty("content.desc.sport", "Sport");
    
    /** BildPlay content description */
    public static final String CONTENT_DESC_BILDPLAY = getProperty("content.desc.bildplay", "BildPlay");
    
    /** BildKI content description */
    public static final String CONTENT_DESC_BILDKI = getProperty("content.desc.bildki", "BildKI");
    
    /** More content description */
    public static final String CONTENT_DESC_MORE = getProperty("content.desc.more", "More");
    
    /** BILD Premium content description */
    public static final String CONTENT_DESC_PREMIUM = getProperty("content.desc.premium", "BILD Premium");
    
    // =====================================
    // TOUCH ACTION COORDINATES
    // =====================================
    
    /** Search suggestion tap X coordinate */
    public static final int SEARCH_TAP_X_COORDINATE = getIntProperty("search.tap.x.coordinate", 993);
    
    /** Search suggestion tap Y coordinate */
    public static final int SEARCH_TAP_Y_COORDINATE = getIntProperty("search.tap.y.coordinate", 2153);
    
    /** Search suggestion tap duration in milliseconds */
    public static final int SEARCH_TAP_DURATION = getIntProperty("search.tap.duration", 50);
    
    // =====================================
    // LOGGING CONSTANTS
    // =====================================
    
    /** Logging level */
    public static final String LOGGING_LEVEL = getProperty("logging.level", "INFO");
    
    /** Screenshot on failure flag */
    public static final boolean LOGGING_SCREENSHOT_ON_FAILURE = getBooleanProperty("logging.screenshot.on.failure", true);
    
    /** Detailed steps logging flag */
    public static final boolean LOGGING_DETAILED_STEPS = getBooleanProperty("logging.detailed.steps", true);
    
    /** Emoji logging enabled flag */
    public static final boolean LOGGING_EMOJI_ENABLED = getBooleanProperty("logging.emoji.enabled", true);
    
    // =====================================
    // ENVIRONMENT CONSTANTS
    // =====================================
    
    /** Current test environment */
    public static final String ENVIRONMENT = getProperty("environment", "test");
    
    /** Parallel execution flag */
    public static final boolean TEST_PARALLEL_EXECUTION = getBooleanProperty("test.parallel.execution", false);
    
    /** Retry on failure flag */
    public static final boolean TEST_RETRY_ON_FAILURE = getBooleanProperty("test.retry.on.failure", true);
    
    // =====================================
    // PRIVATE HELPER METHODS
    // =====================================
    
    /**
     * Load configuration from properties file
     */
    private static void loadConfiguration() {
        try (InputStream input = ApplicationConstants.class.getResourceAsStream(CONFIG_FILE_PATH)) {
            if (input == null) {
                logger.warn("Configuration file not found at {}, using default values", CONFIG_FILE_PATH);
                return;
            }
            config.load(input);
            logger.info("✅ Configuration loaded successfully from {}", CONFIG_FILE_PATH);
            logConfigurationSummary();
        } catch (IOException e) {
            logger.error("❌ Failed to load configuration file: {}", e.getMessage());
            logger.info("ℹ️ Using default configuration values");
        }
    }
    
    /**
     * Get string property with default value
     */
    private static String getProperty(String key, String defaultValue) {
        return config.getProperty(key, defaultValue);
    }
    
    /**
     * Get integer property with default value
     */
    private static int getIntProperty(String key, int defaultValue) {
        try {
            String value = config.getProperty(key);
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer value for property {}, using default: {}", key, defaultValue);
            return defaultValue;
        }
    }
    
    /**
     * Get boolean property with default value
     */
    private static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = config.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }
    
    /**
     * Log configuration summary for debugging
     */
    private static void logConfigurationSummary() {
        if (logger.isInfoEnabled()) {
            logger.info("==========================================");
            logger.info("🔧 FRAMEWORK CONFIGURATION SUMMARY");
            logger.info("==========================================");
            logger.info("📱 App Package: {}", APP_PACKAGE_NAME);
            logger.info("🌐 Appium Server: {}", APPIUM_SERVER_URL);
            logger.info("⏱️ Default Timeout: {}s", TIMEOUT_EXPLICIT_WAIT);
            logger.info("🔍 Search Term: {}", TEST_SEARCH_TERM_GERMAN);
            logger.info("📊 Logging Level: {}", LOGGING_LEVEL);
            logger.info("🔄 Retry Enabled: {}", TEST_RETRY_ON_FAILURE);
            logger.info("==========================================");
        }
    }
    

    
    /**
     * Private constructor to prevent instantiation
     */
    private ApplicationConstants() {
        throw new AssertionError("ApplicationConstants is a utility class and should not be instantiated");
    }
}