package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * @author shivam mishra
 */

public class RetryListener implements IRetryAnalyzer {

    private int retryCount = 0;
    private int maxRetryCount = 0;

    // Below method returns 'true' if the test method has to be retried else 'false'
    //and it takes the 'Result' as parameter of the test method that just ran
    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) {
            if (retryCount < maxRetryCount) {
                System.out.println("Retrying test " + iTestResult.getName() + " with status "
                        + getResultStatusName(iTestResult.getStatus()) + " for the " + (retryCount + 1) + " time(s).");
                retryCount++;
                iTestResult.setStatus(ITestResult.FAILURE);
                iTestResult.setAttribute("isRetry", true);
                return true;
            } else {
                iTestResult.setStatus(ITestResult.FAILURE);
            }
        } else {
            iTestResult.setStatus(ITestResult.SUCCESS);
        }
        return false;
    }

    public String getResultStatusName(int status) {
        String resultName = null;
        if (status == 1)
            resultName = "SUCCESS";
        if (status == 2)
            resultName = "FAILURE";
        if (status == 3)
            resultName = "SKIP";
        return resultName;
    }

}
