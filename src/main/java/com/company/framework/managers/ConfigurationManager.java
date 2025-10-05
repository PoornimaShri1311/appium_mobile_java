package com.company.framework.managers;

import com.company.framework.interfaces.IConfigurationManager;
import com.company.framework.config.FrameworkConfig;

import java.util.Properties;

/**
 * ConfigurationManager - Implementation of IConfigurationManager
 * Follows Single Responsibility Principle
 */
public class ConfigurationManager implements IConfigurationManager {
    
    @Override
    public Properties loadProperties(String fileName) {
        return FrameworkConfig.loadProperties(fileName);
    }
    
    @Override
    public String getProperty(String key, String defaultValue) {
        // This could be enhanced to cache properties from multiple files
        Properties props = loadProperties("framework.properties");
        return props.getProperty(key, defaultValue);
    }
    
    @Override
    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    @Override
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key, String.valueOf(defaultValue));
        return Boolean.parseBoolean(value);
    }
}