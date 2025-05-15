package tests;

import org.testng.annotations.Test;
import pages.TrustWalletLaunchPage;

public class TrustWalletLaunchTest extends BaseTest{
    @Test(description = "Validate Create Wallet")
    public void trustWalletTest() {
        TrustWalletLaunchPage trustWalletLaunchPage = new TrustWalletLaunchPage();
        trustWalletLaunchPage.clickCreateNewWalletButton();
    }
}
