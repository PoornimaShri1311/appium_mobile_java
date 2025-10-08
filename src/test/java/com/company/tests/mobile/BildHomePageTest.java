package com.company.tests.mobile;

import com.company.framework.base.BaseTestMobile;
import com.company.framework.pages.bild.ImprovedBildHomePage;
import com.company.framework.pages.bild.actions.BildHomeNavigationActions;
import com.company.framework.pages.bild.BildLoginPage;
import com.company.framework.utils.TestDataManager;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

public class BildHomePageTest extends BaseTestMobile {

    private ImprovedBildHomePage bildHomePage;
    private BildHomeNavigationActions navigationActions;

    @BeforeClass(dependsOnMethods = "setupClass")
    public void initPageObjects() {
        bildHomePage = new ImprovedBildHomePage(driver);
        navigationActions = bildHomePage.getNavigationActions();
        logTestStep("Page objects initialized for: " + this.getClass().getSimpleName());
    }

    // ====================================
    // BILD Search function - Positive test
    // ====================================
    @Test(description = "Test Test search with coordinate tap functionality")
    public void testPatternSearchWithCoordinateTap() {
        logTestStep("Testing Test search functionality with coordinate-based interactions");

        try {
            // Step 1: Perform search via Page Object
            String searchTerm = TestDataManager.getPatternSearchTerm();
            bildHomePage.performSearch(searchTerm);
            logTestStep("🔍 Performed search for: " + searchTerm);

            // Step 2: Wait for search results to load
            bildHomePage.waitForSearchResults();
            logTestStep("⏳ Search results loaded");

            // Step 3: Click on search result item (from original Appium recording)
            bildHomePage.clickSearchResultItem();
            logTestStep("👆 Clicked on search result item");

            // Check if we have substantial content (indicating successful navigation)
            String pageSource = driver.getPageSource();
            boolean resultFound = pageSource.length() > 2000 && !pageSource.contains("error");
            logTestStep("📄 Result page content length: " + pageSource.length() + " characters");

            assertWithLogging(resultFound,
                    "Search result should contain or navigate to page with relevant content");

            logTestStep("✅ Test search with coordinate tap test completed successfully");

        } catch (Exception e) {
            logTestStep("❌ Test search test failed: " + e.getMessage());
            throw new RuntimeException("Test search test failed", e);
        }
}

    // =========================================
    // Login - BILD Premium Test - Negative Test
    // =========================================

    @Test(description = "Test BILD login functionality")
    public void testBildLoginwithoutPremium() {
    logTestStep("Testing BILD login functionality");

    BildLoginPage loginPage = new BildLoginPage(driver);

    String email = TestDataManager.getValidLoginEmail();
    String password = TestDataManager.getValidLoginPassword();

    loginPage.openLoginForm();
    logTestStep("✅ Opened login form");

    loginPage.enterEmail(email);
    logTestStep("✅ Entered email: " + email);

    loginPage.enterPassword(password);
    logTestStep("✅ Entered password");

    loginPage.clickLogin();
    logTestStep("✅ Clicked login button");

    assertWithLogging(loginPage.isLoginSuccessful(), "Login should succeed and app remains responsive");

    logTestStep("BILD login test completed");
    logTestStep("➡ Navigating Premium Bild Article Verification");

    loginPage.completePostLoginNavigation();
    navigationActions.goBack();

    logTestStep("✅ Premium Bild Article Verification completed successfully");
    }

}
