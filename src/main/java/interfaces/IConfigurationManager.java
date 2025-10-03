package interfaces;

import java.util.Properties;

/**
 * IConfigurationManager - Interface for configuration management
 * Follows Single Responsibility and Interface Segregation principles
 */
public interface IConfigurationManager {
    
    /**
     * Load properties from a file
     * @param fileName name of the properties file
     * @return Properties object
     */
    Properties loadProperties(String fileName);
    
    /**
     * Get a string property value
     * @param key property key
     * @param defaultValue default value if key not found
     * @return property value or default
     */
    String getProperty(String key, String defaultValue);
    
    /**
     * Get an integer property value
     * @param key property key
     * @param defaultValue default value if key not found
     * @return property value or default
     */
    int getIntProperty(String key, int defaultValue);
    
    /**
     * Get a boolean property value
     * @param key property key
     * @param defaultValue default value if key not found
     * @return property value or default
     */
    boolean getBooleanProperty(String key, boolean defaultValue);
}