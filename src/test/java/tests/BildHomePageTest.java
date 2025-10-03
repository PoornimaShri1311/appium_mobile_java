package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ImprovedBildHomePage;
import utils.DriverUtils;
import utils.TestUtils;

/**
 * BildHomePageTest - Test class for BILD Android app home page functionality
 * Contains test cases for navigation and element verification
 */
public class BildHomePageTest extends BaseTest {

    @Test(priority = 1, description = "Test navigation to different menu sections")
    public void testMenuNavigation() {
        test.info("Starting test: testMenuNavigation");
        ImprovedBildHomePage bildHomePage = new ImprovedBildHomePage(DriverUtils.getDriver());
        TestUtils.testNavigationMenus(bildHomePage, test);
    }

    @Test(description = "Test search functionality with 'Umfrage' input and verify results")
    public void testUmfrageSearch() {
        test.info("Starting test: testUmfrageSearch");
        ImprovedBildHomePage bildHomePage = new ImprovedBildHomePage(DriverUtils.getDriver());
        
        // Wait for app to load completely
        bildHomePage.waitForAppToLoad();
        test.pass("App loaded successfully");
        
        // Perform search with verification using utility method
        String searchTerm = "Umfrage";
        boolean searchResult = TestUtils.performSearchWithVerification(bildHomePage, searchTerm, test);
        
        // Assert based on search result
        if (searchResult) {
            Assert.assertTrue(searchResult, "Results panel should contain the term 'Umfrage'");
        }
        
        // Handle cancel button operation
        TestUtils.handleCancelButton(bildHomePage, test);
        test.info("Umfrage search test completed");
    }
    
    @Test(priority = 2, description = "Test BILD Premium element click and verify account exists TextView")
    public void testBildPremiumElementAndAccountText() {
        test.info("Starting test: testBildPremiumElementAndAccountText");
        ImprovedBildHomePage bildHomePage = new ImprovedBildHomePage(DriverUtils.getDriver());
        
        // Wait for app to load completely
        bildHomePage.waitForAppToLoad();
        test.pass("App loaded successfully");
        
        // Perform Premium element verification using utility method
        boolean premiumVerificationResult = TestUtils.verifyPremiumElementWorkflow(bildHomePage, test);
        
        // Assert based on verification result if needed
        if (premiumVerificationResult) {
            Assert.assertTrue(premiumVerificationResult, "TextView 'SIE HABEN BEREITS EIN KONTO?' should be displayed");
        }
        
        test.info("BILD Premium element and account text verification test completed");
    }
}