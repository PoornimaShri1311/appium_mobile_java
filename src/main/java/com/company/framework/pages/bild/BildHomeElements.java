package com.company.framework.pages.bild;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

/**
 * BildHomeElements - Optimized BILD home page element definitions
 */
public class BildHomeElements {
    
    // Navigation & Search Elements
    @AndroidFindBy(xpath = "//android.widget.ImageView[@content-desc='Bild icon']")
    public WebElement homeBildIcon;

    @AndroidFindBy(xpath = "//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View[1]/android.view.View[2]/android.view.View/android.view.View/android.widget.Button")
    public WebElement searchButton;

    @AndroidFindBy(xpath = "//android.widget.EditText")
    public WebElement searchInput;

    // Menu Navigation Elements (Optimized with shorter XPaths where possible)
    @AndroidFindBy(xpath = "//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View[2]/android.view.View/android.view.View[1]/android.view.View[1]")
    public WebElement homeMenu;

    @AndroidFindBy(xpath = "//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View[2]/android.view.View/android.view.View[2]/android.view.View[1]")
    public WebElement sportMenu;

    @AndroidFindBy(xpath = "//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View[2]/android.view.View/android.view.View[3]/android.view.View[2]")
    public WebElement bildPlayMenu;

    @AndroidFindBy(xpath = "//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View[2]/android.view.View/android.view.View[4]/android.view.View[2]")
    public WebElement bildKiMenu;

    @AndroidFindBy(xpath = "//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View[2]/android.view.View/android.view.View[5]/android.view.View[2]")
    public WebElement moreMenu;

    // Premium & Account Elements
    @AndroidFindBy(xpath = "//android.view.View[@content-desc='Bild Premium Marker Icon']")
    public WebElement bildPremiumElement;
    
    @AndroidFindBy(xpath = "//android.widget.TextView[@text='SIE HABEN BEREITS EIN KONTO?']")
    public WebElement accountExistsTextView;
    
    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Abbrechen']")
    public WebElement cancelButton;
    
    // Getters (required by dependent classes)
    public WebElement getHomeBildIcon() { return homeBildIcon; }
    public WebElement getSearchButton() { return searchButton; }
    public WebElement getSearchInput() { return searchInput; }
    public WebElement getHomeMenu() { return homeMenu; }
    public WebElement getSportMenu() { return sportMenu; }
    public WebElement getBildPlayMenu() { return bildPlayMenu; }
    public WebElement getBildKiMenu() { return bildKiMenu; }
    public WebElement getMoreMenu() { return moreMenu; }
    public WebElement getBildPremiumElement() { return bildPremiumElement; }
    public WebElement getAccountExistsTextView() { return accountExistsTextView; }
    public WebElement getCancelButton() { return cancelButton; }
}