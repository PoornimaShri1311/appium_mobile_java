package com.company.framework.interfaces;

import org.openqa.selenium.WebElement;
import java.util.List;

/**
 * ISearchActions - Interface for search-specific actions
 * Follows Interface Segregation Principle
 */
public interface ISearchActions {
    
    /**
     * Perform search with given term
     * @param searchTerm term to search for
     */
    void performSearch(String searchTerm);
    
    /**
     * Clear search field
     */
    void clearSearch();
    
    /**
     * Get search results
     * @return list of search result elements
     */
    List<WebElement> getSearchResults();
    
    /**
     * Verify if search results contain specific text
     * @param results list of result elements
     * @param expectedText text to look for
     * @return true if text is found in results
     */
    boolean verifySearchResults(List<WebElement> results, String expectedText);
    
    /**
     * Cancel search operation
     */
    void cancelSearch();
}