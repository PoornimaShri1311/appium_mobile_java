package utils;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.ImprovedBildHomePage;
import java.util.List;

/**
 * TestUtils - Utility class for common test operations
 * Provides reusable methods to reduce code duplication in test classes
 */
public class TestUtils {
    private static final Logger logger = LoggerFactory.getLogger(TestUtils.class);
    
    /**
     * Performs search with tap and comprehensive verification
     * @param bildHomePage The page object instance
     * @param searchTerm The term to search for
     * @param test ExtentTest instance for reporting
     * @return boolean indicating if search verification was successful
     */
    public static boolean performSearchWithVerification(ImprovedBildHomePage bildHomePage, 
                                                       String searchTerm, 
                                                       com.aventstack.extentreports.ExtentTest test) {
        try {
            // Perform search with touch tap
            test.info("Performing search with touch tap for: " + searchTerm);
            bildHomePage.performSearchWithTap(searchTerm);
            test.pass("Successfully performed search with touch tap at coordinates (993, 2153)");
            
            // Wait for results to load
            Thread.sleep(2000);
            test.info("Verifying search execution with touch tap method");
            
            // Collect and verify results
            List<WebElement> panelResults = bildHomePage.collectResultsFromPanel();
            test.info("Collected " + panelResults.size() + " results from the main results panel");
            
            boolean verified = bildHomePage.verifySearchResults(panelResults, searchTerm);
            
            if (verified) {
                test.pass("VERIFICATION SUCCESS: Found '" + searchTerm + "' in results panel");
                return true;
            } else {
                test.info("Verification: No '" + searchTerm + "' text found in " + panelResults.size() + " results");
                return false;
            }
            
        } catch (Exception e) {
            test.fail("Error during search verification: " + e.getMessage());
            logger.error("Search verification failed", e);
            return false;
        }
    }
    
    /**
     * Handles cancel button operation safely
     * @param bildHomePage The page object instance
     * @param test ExtentTest instance for reporting
     */
    public static void handleCancelButton(ImprovedBildHomePage bildHomePage, 
                                        com.aventstack.extentreports.ExtentTest test) {
        try {
            test.info("Attempting to click Cancel button after search verification");
            if (bildHomePage.isCancelButtonDisplayed()) {
                bildHomePage.clickCancel();
                test.pass("Successfully clicked Cancel (Abbrechen) button");
            } else {
                test.info("Cancel button not displayed - may not be available in current context");
            }
        } catch (Exception e) {
            test.info("Could not click Cancel button: " + e.getMessage());
            logger.warn("Cancel button operation failed", e);
        }
    }
    
    /**
     * Performs Premium element verification workflow
     * @param bildHomePage The page object instance
     * @param test ExtentTest instance for reporting
     * @return boolean indicating if Premium element verification was successful
     */
    public static boolean verifyPremiumElementWorkflow(ImprovedBildHomePage bildHomePage, 
                                                      com.aventstack.extentreports.ExtentTest test) {
        try {
            // Verify BILD Premium element is displayed
            boolean isPremiumElementDisplayed = bildHomePage.isBildPremiumElementDisplayed();
            if (isPremiumElementDisplayed) {
                test.pass("BILD Premium element is displayed on the home page");
                
                // Click on BILD Premium element
                bildHomePage.clickBildPremiumElement();
                test.pass("Successfully clicked on BILD Premium element");
                
                // Wait for page to load
                Thread.sleep(3000);
                
                // Verify account exists TextView
                boolean isAccountTextDisplayed = bildHomePage.isAccountExistsTextViewDisplayed();
                
                if (isAccountTextDisplayed) {
                    test.pass("SUCCESS: Found TextView with text 'SIE HABEN BEREITS EIN KONTO?' after clicking BILD Premium");
                    return true;
                } else {
                    test.info("TextView 'SIE HABEN BEREITS EIN KONTO?' not found after clicking BILD Premium");
                    return false;
                }
            } else {
                test.info("BILD Premium element is not displayed - this may be expected");
                return false;
            }
        } catch (Exception e) {
            test.info("Could not verify BILD Premium element: " + e.getMessage());
            logger.warn("Premium element verification failed", e);
            return false;
        }
    }
    
    /**
     * Performs navigation menu testing
     * @param bildHomePage The page object instance
     * @param test ExtentTest instance for reporting
     */
    public static void testNavigationMenus(ImprovedBildHomePage bildHomePage, 
                                         com.aventstack.extentreports.ExtentTest test) {
        try {
            // Test Sport menu navigation
            bildHomePage.clickSportMenu();
            test.pass("Successfully clicked on Sport menu");
            
            // Test BILD Play menu navigation
            bildHomePage.clickBildPlayMenu();
            test.pass("Successfully clicked on BILD Play menu");

            // Test Home menu navigation
            bildHomePage.clickHomeMenu();
            test.pass("Successfully clicked on Home menu");
            
        } catch (Exception e) {
            test.fail("Navigation menu testing failed: " + e.getMessage());
            logger.error("Navigation menu test failed", e);
        }
    }
}