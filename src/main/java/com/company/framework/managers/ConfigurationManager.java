package com.company.framework.managers;

import com.company.framework.config.FrameworkConfig;
import com.company.framework.interfaces.IConfigurationManager;

import java.util.Properties;

public class ConfigurationManager implements IConfigurationManager {
    private final Properties cachedProps;

    public ConfigurationManager() {
        cachedProps = FrameworkConfig.loadProperties("framework.properties");
    }

    @Override
    public Properties loadProperties(String fileName) {
        return cachedProps; // return cached instead of reading every time
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return cachedProps.getProperty(key, defaultValue);
    }

    @Override
    public int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        return Boolean.parseBoolean(getProperty(key, String.valueOf(defaultValue)));
    }
}
