package com.company.framework.interfaces;

/**
 * INavigationActions - Interface for navigation-specific actions
 * Follows Interface Segregation Principle
 */
public interface INavigationActions {
    
    /**
     * Navigate to a specific menu by name
     * @param menuName name of the menu to navigate to
     */
    void navigateToMenu(String menuName);
    
    /**
     * Go back to previous page
     */
    void goBack();
    
    /**
     * Refresh the current page
     */
    void refresh();
    
    /**
     * Check if a menu is available
     * @param menuName name of the menu to check
     * @return true if menu is available, false otherwise
     */
    boolean isMenuAvailable(String menuName);
}