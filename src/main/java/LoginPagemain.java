import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import javax.xml.xpath.XPath;

public class LoginPagemain extends Initializedriver {
    private WebDriver driver;

    @FindBy(id="emailId")
    private WebElement logintextbox;

    @FindBy(xpath="//*[@id='root']//div//div//div/div/img[@src='https://dev-trade.pocketful.in/tradingV2/static/media/loginScreen.04baca23833ca3b1e6bb0d1dca8b7461.svg']")
    private WebElement image;

    @FindBy(xpath="//*[@id='password']")
    private WebElement passwordfield;

    @FindBy(xpath="//*[@id='pin0']")
    private WebElement Field01;

    @FindBy(xpath="//*[@id='pin1']")
    private WebElement Field2;

    @FindBy(xpath="//*[@id='pin2']")
    private WebElement Field3;

    @FindBy(xpath="//*[@id='pin3']")
    private WebElement Field4;

    @FindBy(xpath="//*[@id='pin4']")
    private WebElement Field5;

    @FindBy(xpath="//*[@id='pin5']")
    private WebElement Field6;

    @FindBy(xpath="//*[contains(text(),'Access your account securely. Scan, sign in, and start trading!')]")
    private WebElement TextOnloginpage;

    @FindBy(xpath="//*[contains(text(),'Continue with Google')]")
    private WebElement Loginwithgooglebutton;

    @FindBy(xpath="//button[@type='submit']")
    private WebElement submit;

    @FindBy(xpath="(//*[contains(text(),'Login')])[1]")
    private WebElement login;

    @FindBy(xpath="//*[contains(text(),'Create Your Account')]")
    private WebElement CreateAccount;


    @FindBy(xpath="//*[contains(text(),'Invalid email address')]")
    private WebElement Invalidtextemail;

    public LoginPagemain(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void EnteremailAndClickEnterKey(String email)
    {
        logintextbox.sendKeys(email, Keys.ENTER);
    }
    public boolean LoginPageUIVerification()
    {
        image.isDisplayed();
        return true;
    }
    public void EnterPassword(String pass)
    {
        passwordfield.sendKeys(pass, Keys.ENTER);
    }

    public boolean TextVerification()
    {
        TextOnloginpage.isDisplayed();
        return true;
    }

    public boolean LoginWithGoogle()
    {
        Loginwithgooglebutton.isDisplayed();
        return true;
    }

    public boolean LoginWithGoogleisClickable()
    {
        Loginwithgooglebutton.isEnabled();
        return true;
    }

    public boolean Submit()
    {
        submit.isDisplayed();
        return true;
    }

    public boolean SubmitisClickable()
    {
        submit.isEnabled();
        return true;
    }

    public boolean logintext()
    {
        login.isDisplayed();
        return true;
    }

    public boolean createAccount()
    {
        CreateAccount.isDisplayed();
        return true;
    }

    public boolean InvalidAdd()
    {
        Invalidtextemail.isDisplayed();
        return true;
    }
}
