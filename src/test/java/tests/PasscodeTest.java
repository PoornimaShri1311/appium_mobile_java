package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;

public class PasscodeTest extends BaseTest {

    @Test(description = "Validate Create Wallet Launch")
    public void trustWalletTest() {
        try {
            test.info("Starting test: trustWalletTest");
            TrustWalletLaunchPage trustWalletLaunchPage = new TrustWalletLaunchPage();
            trustWalletLaunchPage.clickCreateNewWalletButton();
            test.pass("Clicked on Create New Wallet successfully");
            PasscodePage passcodePage = new PasscodePage();
            passcodePage.isCreatePasscodeTitleDisplayed();
            test.pass("Create Passcode title displayed!");

        } catch (Exception e) {
            test.fail("Test failed due to exception: " + e.getMessage());
            test.fail(e);
            throw e;
        }
    }

    @Test(dataProvider = "passcodeData", description = "Validate Create Wallet Passcode")
    public void createPasscode(String passcode) throws InterruptedException {
        try {
            TrustWalletLaunchPage trustWalletLaunchPage = new TrustWalletLaunchPage();
            trustWalletLaunchPage.clickCreateNewWalletButton();
            test.pass("Clicked on Create New Wallet successfully");
            PasscodePage passcodePage = new PasscodePage();
            passcodePage.enterPasscode(passcode);
            passcodePage.enterPasscode(passcode);
            test.pass("Passcode entered successfully");

            test.info("Starting test: biometricConfirmationTest");
            BiometricConfirmationPage biometricConfirmationPage = new BiometricConfirmationPage();
            Assert.assertTrue(biometricConfirmationPage.isBiometricLoginDisplayed(), "Biometric Login text not displayed!");
            biometricConfirmationPage.tapOnDenyButton();
            test.pass("Deny button clicked successfully");

            test.info("Starting test: notificationTest");
            NotificationPage notificationPage = new NotificationPage();
            Assert.assertEquals(notificationPage.getKeepUpWithMarketText(), "Keep up with the market!", "Market text does not match!");
            notificationPage.isEnableNotificationsDisplayed();
            notificationPage.clickSkipLater();
            notificationPage.clickSkipLater();
            test.pass("Skip Later button clicked successfully");

            HomePage homePage = new HomePage();
            // Assert label is visible
            Assert.assertTrue(homePage.isHomeLabelDisplayed(), "Home label is not displayed");

            // Print or assert text
            Assert.assertEquals(homePage.getHomeLabelText(), "Home");
            test.pass("Home Page verified");
        } catch (Exception e) {
            test.fail("Test failed due to exception: " + e.getMessage());
            test.fail(e);
            throw e;
        }
    }

    @DataProvider(name = "passcodeData")
    public Object[][] passcodeData() {
        return new Object[][]{
                {"123456"}
        };
    }
}
