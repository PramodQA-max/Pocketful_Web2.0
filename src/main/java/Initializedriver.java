import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Initializedriver {

    public static WebDriver driver;
    public static WebDriverWait wait;

       public static WebDriver Initialize()
       {

//           ChromeOptions options = new ChromeOptions();
//           options.addArguments("--headless=new");
           driver= new ChromeDriver();
           driver.get("https://dev-trade.pocketful.in/tradingV2/login");
           driver.manage().window().maximize();
           driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
           wait= new WebDriverWait(driver,Duration.ofSeconds(20));
           return driver;
       }
     public  static void QuitWindow()
     {
        driver.quit();
     }
}
