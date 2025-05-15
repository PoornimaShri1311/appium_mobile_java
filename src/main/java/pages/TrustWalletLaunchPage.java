package pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtils;

public class TrustWalletLaunchPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(TrustWalletLaunchPage.class);
    private final WaitUtils waitUtils = new WaitUtils(30);
    // Locators for elements on the Trust Wallet launch page
    @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/CreateNewWalletButton")
    private WebElement createNewWallet;

    // Constructor to initialize the page elements
    public TrustWalletLaunchPage() {
        super();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    // Action methods to interact with elements on the launch page

    public void clickCreateNewWalletButton() {
        waitUtils.waitForVisibility(createNewWallet).click();
        logger.info("Clicking on Create New Wallet button");
    }

}
