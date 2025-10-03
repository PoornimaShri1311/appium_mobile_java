package utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Map;

public class WaitUtils {
    private final AppiumDriver driver;
    private final WebDriverWait wait;

    public WaitUtils(long timeoutInSeconds) {
        this.driver = DriverUtils.getDriver();  // Get driver from your DriverUtils
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    public void waitForElementToBeClickableAndClick(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public boolean waitForInvisibility(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void waitForElementAndType(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element)).sendKeys(text);
    }

    public String waitForTextToBePresent(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        return element.getText();
    }

    /**
     * Scrolls down on mobile device using UiAutomator scroll gestures
     */
    public void scrollDown() {
        try {
            // First try using touch actions with Dimension
            org.openqa.selenium.Dimension size = driver.manage().window().getSize();
            int startX = size.width / 2;
            int startY = (int) (size.height * 0.8);
            int endX = startX;
            int endY = (int) (size.height * 0.2);
            
            // Use mobile: swipeGesture for Android UiAutomator2
            Map<String, Object> params = Map.of(
                "left", 0,
                "top", startY,
                "width", size.width,
                "height", endY - startY,
                "direction", "up",
                "percent", 0.75
            );
            ((JavascriptExecutor) driver).executeScript("mobile: swipeGesture", params);
            
        } catch (Exception e) {
            // Fallback: Try using direct coordinates
            try {
                Map<String, Object> swipeParams = Map.of(
                    "startX", 500,
                    "startY", 1500, 
                    "endX", 500,
                    "endY", 800,
                    "duration", 1000
                );
                ((JavascriptExecutor) driver).executeScript("mobile: swipe", swipeParams);
            } catch (Exception ex) {
                // Final fallback - use UiScrollable if available
                try {
                    ((JavascriptExecutor) driver).executeScript("mobile: scrollGesture", Map.of(
                        "left", 0, "top", 0, "width", 500, "height", 1000,
                        "direction", "down",
                        "percent", 3.0
                    ));
                } catch (Exception finalEx) {
                    // Most basic fallback
                    System.out.println("All scroll methods failed, using basic web scroll");
                    ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500);");
                }
            }
        }
    }

    /**
     * Scrolls to make an element visible using mobile-specific scrolling
     * @param element The element to scroll to
     */
    public void scrollToElementMobile(WebElement element) {
        try {
            // First try to find if element is already visible
            if (isElementVisible(element)) {
                System.out.println("Element is already visible, no scroll needed");
                return;
            }
            
            System.out.println("Element not visible, attempting to scroll to it");
            
            // Try multiple scroll approaches
            boolean elementFound = false;
            
            // Approach 1: Scroll down multiple times to find the element
            int maxScrolls = 5;
            for (int i = 0; i < maxScrolls && !elementFound; i++) {
                try {
                    scrollDown();
                    Thread.sleep(1000); // Wait for scroll to complete
                    
                    if (isElementVisible(element)) {
                        System.out.println("Element found after " + (i + 1) + " scroll(s)");
                        elementFound = true;
                        break;
                    }
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                } catch (Exception scrollEx) {
                    System.out.println("Scroll attempt " + (i + 1) + " failed: " + scrollEx.getMessage());
                }
            }
            
            // If still not found, try JavaScript scroll as fallback
            if (!elementFound) {
                try {
                    System.out.println("Trying JavaScript scroll as fallback");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                    Thread.sleep(1000);
                    
                    if (isElementVisible(element)) {
                        System.out.println("Element found using JavaScript scroll");
                    } else {
                        System.out.println("Element still not visible after all scroll attempts");
                    }
                } catch (Exception jsEx) {
                    System.out.println("JavaScript scroll also failed: " + jsEx.getMessage());
                }
            }
            
        } catch (Exception e) {
            System.out.println("ScrollToElementMobile encountered an error: " + e.getMessage());
            // Final fallback to basic scroll
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            } catch (Exception finalEx) {
                System.out.println("All scroll methods failed: " + finalEx.getMessage());
            }
        }
    }

    /**
     * Check if element is visible without waiting
     * @param element The element to check
     * @return true if visible, false otherwise
     */
    private boolean isElementVisible(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
