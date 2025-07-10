import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestNGListener;
import org.testng.ITestResult;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class InitializeListener implements ITestListener
{
    public static WebDriver driver;

@Override
public  void onTestStart(ITestResult result)
{
    try {
        driver = (WebDriver)result.getTestClass().getRealClass().getDeclaredField("driver").get(result.getInstance());
    } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
    } catch (NoSuchFieldException e) {
        throw new RuntimeException(e);
    }
}
    @Override
    public void onTestSuccess(ITestResult result)
    {

    }

    @Override
    public void onTestFailure(ITestResult result)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        File destinationfile= new File("C:\\Users\\Neelr\\OneDrive\\Desktop\\Web Automation 2.0\\Automation2.O\\src\\main\\resources\\Screenshot"+result.getTestName()+timestamp+ ".png");

        try
        {
            FileUtils.copyFile(src,destinationfile);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
