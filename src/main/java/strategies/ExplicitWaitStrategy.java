package strategies;

import interfaces.IWaitStrategy;
import interfaces.IDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * ExplicitWaitStrategy - Implementation of IWaitStrategy using explicit waits
 * Follows Strategy Pattern
 */
public class ExplicitWaitStrategy implements IWaitStrategy {
    
    private final WebDriverWait wait;
    
    public ExplicitWaitStrategy(IDriverManager driverManager, long timeoutInSeconds) {
        this.wait = new WebDriverWait(driverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
    }
    
    @Override
    public WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    @Override
    public WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    
    @Override
    public boolean waitForInvisibility(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    
    @Override
    public boolean waitForTextToBePresentInElement(WebElement element, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }
    
    @Override
    public void scrollToElement(WebElement element) {
        // For mobile apps, we need to use WaitUtils mobile scroll functionality
        // Create a WaitUtils instance and use its scrollToElementMobile method
        utils.WaitUtils waitUtils = new utils.WaitUtils(30); // 30 seconds timeout
        waitUtils.scrollToElementMobile(element);
    }
}