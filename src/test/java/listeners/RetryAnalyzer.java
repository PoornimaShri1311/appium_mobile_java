package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import utils.FrameworkConfig;

/**
 * RetryAnalyzer - Handles retry logic for failed tests
 * This class implements IRetryAnalyzer to automatically retry failed tests
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    
    private int retryCount = 0;
    private final int maxRetryCount = FrameworkConfig.getRetryCount();

    @Override
    public boolean retry(ITestResult result) {
        if (!FrameworkConfig.isRetryEnabled()) {
            return false;
        }
        
        if (retryCount < maxRetryCount) {
            retryCount++;
            System.out.println("Retrying test: " + result.getName() + " (Attempt " + retryCount + " of " + maxRetryCount + ")");
            return true;
        }
        return false;
    }
}