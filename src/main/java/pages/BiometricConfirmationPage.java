package pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtils;

public class BiometricConfirmationPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(BiometricConfirmationPage.class);
    private final WaitUtils waitUtils = new WaitUtils(30);
    // Since there's no resource-id, we use XPath
    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Biometric Login']")
    private WebElement biometricLoginTitle;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Deny']")
    private WebElement denyButton;

    // Constructor to initialize the page elements
    public BiometricConfirmationPage() {
        super();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    // Action methods
    public boolean isBiometricLoginDisplayed() {
        try {
            return biometricLoginTitle.isDisplayed(); // biometricLoginText is your WebElement
        } catch (NoSuchElementException e) {
            return false;
        }
    }


    public void tapOnDenyButton() {
        waitUtils.waitForElementToBeClickableAndClick(denyButton);
        logger.info("Clicking on Deny button");
    }
}
