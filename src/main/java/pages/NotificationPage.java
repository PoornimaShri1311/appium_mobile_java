package pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtils;

public class NotificationPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(NotificationPage.class);
    private final WaitUtils waitUtils = new WaitUtils(30);
    public NotificationPage() {
        super();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    // Keep up with the market! text
    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Keep up with the market!']")
    private WebElement keepUpWithMarketText;

    // Enable Notifications text with resource-id 'buttonTitle'
    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id='buttonTitle' and @text='Enable Notifications']")
    private WebElement enableNotificationsText;

    // Skip, I'll do it later button (clickable TextView)
    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Skip, I'll do it later\"]")
    private WebElement skipLaterButton;

    public String getKeepUpWithMarketText() {
        return keepUpWithMarketText.getText();
    }

    public boolean isEnableNotificationsDisplayed() {
        return enableNotificationsText.isDisplayed();
    }

    public void clickSkipLater() {
        waitUtils.waitForElementToBeClickableAndClick(skipLaterButton);
        logger.info("Clicking on Skip Later button");
    }
}
