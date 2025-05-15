package pages;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtils;

public class PasscodePage extends BasePage {
    private static final Logger logger = LogManager.getLogger(PasscodePage.class);
    private final WaitUtils waitUtils = new WaitUtils(30);
    public PasscodePage() {
        super();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    // No @FindBy needed because the keypad digits change dynamically.
    // We'll use XPath in the method directly.

    public void enterPasscode(String passcode) {
        for (char digit : passcode.toCharArray()) {
            String xpath = "//android.widget.TextView[@text='" + digit + "']";
            waitUtils.waitForVisibility(driver.findElement(By.xpath(xpath))).click();
            logger.info("Passcode entered");
        }
    }
}
