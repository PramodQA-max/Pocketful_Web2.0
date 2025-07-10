import Util.Exceldatareader;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;

import static Util.Constant.*;

@Listeners(InitializeListener.class)
public class LoginPageTest  extends  Initializedriver{
    WebDriver driver;
    LoginPagemain login;
    @BeforeTest
    public void init()
    {
       driver=Initialize();
      login =  new LoginPagemain(driver);

    }

    @DataProvider(name="emaildata")
    public Object[][] Emaildata() throws IOException {
        return Exceldatareader.getExcelData(ExcelFilePathName, SheetName);

    }


    @Test(priority = 0,retryAnalyzer = RetryAnalyzer.class,dataProvider = "emaildata")
    public  void Login(String email) throws InterruptedException {
        login.EnteremailAndClickEnterKey(email);

        Assert.assertTrue(
                login.TextVerification() || login.InvalidAdd(),
                "Neither the expected text nor the invalid email message is visible."
        );
        Thread.sleep(2000);


//        Assert.assertTrue(login.TextVerification(),"Access your account securely. Scan, sign in, and start trading!  is not Visible");
//        Assert.assertTrue(login.InvalidAdd(),"Invalid email address!  is not Visible");
        driver.navigate().refresh();
    }


    @Test(priority = 1,retryAnalyzer = RetryAnalyzer.class)
    public void LoginPageverification()
    {
        Assert.assertTrue(login.LoginPageUIVerification(),"Image is not Visible On Login Page");
        Assert.assertTrue(login.TextVerification(),"Access your account securely. Scan, sign in, and start trading!  is nit Visible");
        Assert.assertTrue(login.LoginWithGoogle(),"Login with Google Button is not Visible");
        Assert.assertTrue(login.Submit(),"Submit button is not visible");
        Assert.assertTrue(login.LoginWithGoogleisClickable(),"Login With Google Button is not Enabled");
        Assert.assertTrue(login.SubmitisClickable(),"Submit button is not Enabled");
    }

    @Test(priority = 2)
    public void Enterpasswordintopassfiled()
    {
        login.EnterPassword(Pass);
        Assert.assertTrue(login.LoginPageUIVerification(),"Image is not Visible On Login Page");
    }

}
