package pages.components;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

/**
 * BildHomeElements - Contains all WebElement definitions for BILD home page
 * Follows Single Responsibility Principle - only element management
 */
public class BildHomeElements {
    
    // Navigation Elements
    @AndroidFindBy(xpath = "//android.widget.ImageView[@content-desc='Bild icon']")
    private WebElement homeBildIcon;

    @AndroidFindBy(xpath = "//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View[1]/android.view.View[2]/android.view.View/android.view.View/android.widget.Button")
    private WebElement searchButton;

    @AndroidFindBy(xpath = "//android.widget.EditText")
    private WebElement searchInput;

    @AndroidFindBy(xpath = "//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View[2]/android.view.View/android.view.View[1]/android.view.View[1]")
    private WebElement homeMenu;

    @AndroidFindBy(xpath = "//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View[2]/android.view.View/android.view.View[2]/android.view.View[1]")
    private WebElement sportMenu;

    @AndroidFindBy(xpath = "//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View[2]/android.view.View/android.view.View[3]/android.view.View[2]")
    private WebElement bildPlayMenu;

    @AndroidFindBy(xpath = "//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View[2]/android.view.View/android.view.View[4]/android.view.View[2]")
    private WebElement bildKiMenu;

    @AndroidFindBy(xpath = "//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View[2]/android.view.View/android.view.View[5]/android.view.View[2]")
    private WebElement moreMenu;

    @AndroidFindBy(xpath = "//android.view.View[@content-desc='Bild Premium Marker Icon'][1]")
    private WebElement bildPremium;
    
    @AndroidFindBy(xpath = "//android.view.View[@content-desc='Bild Premium Marker Icon']")
    private WebElement bildPremiumElement;
    
    @AndroidFindBy(xpath = "//android.widget.TextView[@text='SIE HABEN BEREITS EIN KONTO?']")
    private WebElement accountExistsTextView;
    
    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Abbrechen']")
    private WebElement cancelButton;
    
    // Getters for all elements
    public WebElement getHomeBildIcon() { return homeBildIcon; }
    public WebElement getSearchButton() { return searchButton; }
    public WebElement getSearchInput() { return searchInput; }
    public WebElement getHomeMenu() { return homeMenu; }
    public WebElement getSportMenu() { return sportMenu; }
    public WebElement getBildPlayMenu() { return bildPlayMenu; }
    public WebElement getBildKiMenu() { return bildKiMenu; }
    public WebElement getMoreMenu() { return moreMenu; }
    public WebElement getBildPremium() { return bildPremium; }
    public WebElement getBildPremiumElement() { return bildPremiumElement; }
    public WebElement getAccountExistsTextView() { return accountExistsTextView; }
    public WebElement getCancelButton() { return cancelButton; }
}