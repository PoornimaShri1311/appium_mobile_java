package pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends BasePage {

    public HomePage() {
        super();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @FindBy(xpath = "//android.widget.TextView[@text='Home']")
    private WebElement homeLabel;

    public boolean isHomeLabelDisplayed() {
        return homeLabel.isDisplayed();
    }

    public String getHomeLabelText() {
        return homeLabel.getText();
    }
}
