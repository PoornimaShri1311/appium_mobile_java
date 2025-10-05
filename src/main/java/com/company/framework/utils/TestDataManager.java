package com.company.framework.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;

/**
 * TestDataManager - Utility class for managing JSON test data
 * Provides methods to load and access test data from JSON files
 */
public class TestDataManager {
    
    private static final String TEST_DATA_PATH = "/testdata/mobile/";
    private static JsonNode bildHomePageData;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    static {
        loadBildHomePageTestData();
    }
    
    /**
     * Load BILD Home Page test data from JSON file
     */
    private static void loadBildHomePageTestData() {
        try {
            InputStream inputStream = TestDataManager.class.getResourceAsStream(TEST_DATA_PATH + "BildHomePageTestData.json");
            if (inputStream != null) {
                bildHomePageData = objectMapper.readTree(inputStream);
            } else {
                throw new RuntimeException("BildHomePageTestData.json file not found in testdata/mobile/");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load BildHomePageTestData.json: " + e.getMessage(), e);
        }
    }
    
    // ========================================
    // SEARCH DATA METHODS
    // ========================================
    
    /**
     * Get a valid search term by index (default: first term)
     */
    public static String getValidSearchTerm() {
        return getValidSearchTerm(0);
    }
    
    /**
     * Get a valid search term by index
     */
    public static String getValidSearchTerm(int index) {
        try {
            JsonNode searchTerms = bildHomePageData.get("searchData").get("validTerms");
            if (index < searchTerms.size()) {
                return searchTerms.get(index).asText();
            }
            return searchTerms.get(0).asText(); // Fallback to first term
        } catch (Exception e) {
            return "Umfrage"; // Default fallback
        }
    }
    
    // ========================================
    // LOGIN DATA METHODS
    // ========================================
    
    /**
     * Get valid test email for login
     */
    public static String getValidLoginEmail() {
        try {
            return bildHomePageData.get("loginCredentials").get("validEmail").asText();
        } catch (Exception e) {
            return "shriga90@gmail.com"; // Fallback default
        }
    }
    
    /**
     * Get valid test password for login
     */
    public static String getValidLoginPassword() {
        try {
            return bildHomePageData.get("loginCredentials").get("validPassword").asText();
        } catch (Exception e) {
            return "password"; // Fallback default
        }
    }
    
    /**
     * Get invalid test email for negative testing
     */
    public static String getInvalidLoginEmail() {
        try {
            return bildHomePageData.get("loginCredentials").get("invalidEmail").asText();
        } catch (Exception e) {
            return "invalid@test.com"; // Fallback default
        }
    }
    

    /**
 * Get pattern search term from JSON test data
 * @return search term as String
 * @throws RuntimeException if search term is missing
 */
public static String getPatternSearchTerm() {
    if (bildHomePageData == null) {
        throw new RuntimeException("BILD home page test data not initialized");
    }

    try {
        String searchTerm = bildHomePageData
                .get("TestSearchData")
                .get("searchTerm")
                .asText();

        if (searchTerm == null || searchTerm.isEmpty()) {
            throw new RuntimeException("Search term is empty in test data");
        }

        return searchTerm;
    } catch (Exception e) {
        throw new RuntimeException("Failed to retrieve 'searchTerm' from TestSearchData: " + e.getMessage(), e);
    }
}
    
    /**
     * Get pattern search submit button coordinates
     */
    public static int[] getPatternSearchCoordinates() {
        try {
            int x = bildHomePageData.get("TestSearchData").get("submitButtonCoordinates").get("x").asInt();
            int y = bildHomePageData.get("TestSearchData").get("submitButtonCoordinates").get("y").asInt();
            return new int[]{x, y};
        } catch (Exception e) {
            return new int[]{1000, 2164}; // Fallback default
        }
    }
    
    /**
     * Get pattern search tap duration
     */
    public static int getPatternSearchTapDuration() {
        try {
            return bildHomePageData.get("TestSearchData").get("tapDuration").asInt();
        } catch (Exception e) {
            return 50; // Fallback default
        }
    }
    
    /**
     * Get invalid test password for negative testing
     */
    public static String getInvalidLoginPassword() {
        try {
            return bildHomePageData.get("loginCredentials").get("invalidPassword").asText();
        } catch (Exception e) {
            return "wrongpassword"; // Fallback default
        }
    }
    
    // ========================================
    // ACCOUNT VERIFICATION METHODS
    // ========================================
    
    /**
     * Get primary account verification text
     */
    public static String getPrimaryAccountVerificationText() {
        try {
            return bildHomePageData.get("accountVerificationData").get("primaryText").asText();
        } catch (Exception e) {
            return "SIE HABEN BEREITS EIN KONTO?"; // Fallback default
        }
    }
    
    // ========================================
    // MENU DATA METHODS
    // ========================================
    
    /**
     * Get menu name by index
     */
    public static String getMenuName(int index) {
        try {
            JsonNode menuNames = bildHomePageData.get("menuData").get("menuNames");
            if (index < menuNames.size()) {
                return menuNames.get(index).asText();
            }
            return menuNames.get(0).asText(); // Fallback to first menu
        } catch (Exception e) {
            // Default fallback menu names
            String[] defaultMenus = {"Home", "Sport", "BILD Play", "BILD KI", "Mehr"};
            if (index < defaultMenus.length) {
                return defaultMenus[index];
            }
            return defaultMenus[0];
        }
    }
}