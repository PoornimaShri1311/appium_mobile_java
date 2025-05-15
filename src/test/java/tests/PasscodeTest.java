package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BiometricConfirmationPage;
import pages.NotificationPage;
import pages.PasscodePage;
import pages.TrustWalletLaunchPage;

public class PasscodeTest extends BaseTest {

    @Test(description = "Validate Create Wallet Launch")
    public void trustWalletTest() {
        try {
            test.info("Starting test: trustWalletTest");
            TrustWalletLaunchPage trustWalletLaunchPage = new TrustWalletLaunchPage();
            trustWalletLaunchPage.clickCreateNewWalletButton();
            test.pass("Clicked on Create New Wallet successfully");
        } catch (Exception e) {
            test.fail("Test failed due to exception: " + e.getMessage());
            // Optionally adding stack trace details
            test.fail(e);
            throw e;  // re-throw to mark the test as failed in TestNG
        }
    }

    @Test(description = "Validate Create Wallet Passcode")
    public void createPasscode() throws InterruptedException {
        try {
            test.info("Starting test: createPasscode");
            PasscodePage passcodePage = new PasscodePage();
            passcodePage.enterPasscode("123456");
            passcodePage.enterPasscode("123456");
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
            test.pass("Skip Later button clicked successfully");
        } catch (Exception e) {
            test.fail("Test failed due to exception: " + e.getMessage());
            // Optionally adding stack trace details
            test.fail(e);
            throw e;  // re-throw to mark the test as failed in TestNG
        }
    }
}
