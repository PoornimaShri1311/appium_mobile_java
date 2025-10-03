package pages;

import factories.DependencyFactory;
import interfaces.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.PageFactory;

/**
 * ImprovedBasePage - Enhanced base page following SOLID principles
 * Uses Dependency Injection instead of static dependencies
 */
public abstract class ImprovedBasePage {
    
    protected static final Logger logger = LogManager.getLogger(ImprovedBasePage.class);
    protected final AppiumDriver driver;
    protected final IPageActions pageActions;
    protected final IWaitStrategy waitStrategy;
    protected final IConfigurationManager configManager;
    
    /**
     * Constructor with dependency injection
     */
    protected ImprovedBasePage() {
        DependencyFactory factory = DependencyFactory.getInstance();
        
        this.driver = factory.getDriverManager().getDriver();
        this.pageActions = factory.getPageActions();
        this.waitStrategy = factory.getWaitStrategy();
        this.configManager = factory.getConfigurationManager();
        
        // Initialize page elements
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        
        logger.info("Initialized " + this.getClass().getSimpleName());
    }
    
    /**
     * Constructor for testing with custom dependencies
     */
    protected ImprovedBasePage(IDriverManager driverManager, 
                             IPageActions pageActions, 
                             IWaitStrategy waitStrategy,
                             IConfigurationManager configManager) {
        this.driver = driverManager.getDriver();
        this.pageActions = pageActions;
        this.waitStrategy = waitStrategy;
        this.configManager = configManager;
        
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        logger.info("Initialized " + this.getClass().getSimpleName() + " with injected dependencies");
    }
    
    /**
     * Get page title - common functionality
     */
    public String getPageTitle() {
        try {
            return driver.getTitle();
        } catch (Exception e) {
            logger.warn("Could not retrieve page title: " + e.getMessage());
            return "";
        }
    }
    
    /**
     * Check if page is loaded - to be implemented by subclasses
     */
    public abstract boolean isPageLoaded();
    
    /**
     * Wait for page to load - common functionality with customizable implementation
     */
    public void waitForPageToLoad() {
        int maxAttempts = configManager.getIntProperty("page.load.max.attempts", 10);
        int attemptDelay = configManager.getIntProperty("page.load.attempt.delay", 1000);
        
        for (int i = 0; i < maxAttempts; i++) {
            if (isPageLoaded()) {
                logger.info("Page loaded successfully on attempt " + (i + 1));
                return;
            }
            
            try {
                Thread.sleep(attemptDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Page load wait interrupted", e);
            }
        }
        
        throw new RuntimeException("Page did not load within expected time");
    }
    
    /**
     * Getter methods for dependencies - protected access for subclasses
     */
    protected IPageActions getPageActions() {
        return pageActions;
    }
    
    protected IWaitStrategy getWaitStrategy() {
        return waitStrategy;
    }
    
    protected IConfigurationManager getConfigurationManager() {
        return configManager;
    }
    
    protected AppiumDriver getDriver() {
        return driver;
    }
}