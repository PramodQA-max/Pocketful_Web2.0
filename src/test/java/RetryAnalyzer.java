import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    final  int maxcount=2;
    public int count=0;
    @Override
    public boolean retry(ITestResult iTestResult) {
        if(count<maxcount)
        {
            count++;
            return true;
        }
        return false;
    }
}
