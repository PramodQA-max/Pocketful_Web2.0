  package testPage;

import pages.LoginPage;
import util.ExcelDataReader;
import util.ExtentManager;
import base.Initializedriver;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.microsoft.playwright.Page;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import util.LoggerUtil;

import java.util.logging.Logger;

import static pages.LoginPage.*;
import static util.Base.authlogTestSummary;
import static util.Constant.*;

public class Login extends Initializedriver {
    private final static ExtentReports extent = ExtentManager.getInstance();
    private static final Logger logger = LoggerUtil.getLogger(Login.class);
       public Page page;
    LoginPage loginpage;
    boolean result=true;
    public Page newpage;
    public  Page parentpage;



    String emailid="";
    @BeforeTest
    public void init()
    {
        page=initialize();
        loginpage =  new LoginPage(page);



    }

    @DataProvider(name="getemaildata")
    public Object[][] getemaildata()
    {
        return ExcelDataReader.getData(ExcelFilePathName,SheetName);
    }
    @AfterClass
    public static void pushReport() {
        extent.flush();
        authlogTestSummary(extent, "Login Page Test");
    }

    @Test(priority = 0)
    public void UIVerifyOnLoginWithEmailId() {
        ExtentTest test = extent.createTest("Verify Login Page UI").assignAuthor("UI Tests");

        verifyElementVisibility(test, "Login Text", loginpage.isLoginTextVisible());
        verifyElementVisibility(test, "Access your account securely. Scan, sign in, and start trading!", loginpage.isLoginText1Visible());
        verifyElementVisibility(test, "Continue with Google button", loginpage.isGoogleButtonVisible());
        verifyElementVisibility(test, "Email placeholder", loginpage.isEmailPlaceholderVisible());
        verifyElementVisibility(test, "Don't have an account text", loginpage.isHaveAccountVisible());
        verifyElementVisibility(test, "Create Account link", loginpage.isCreateAccountVisible());
        verifyElementVisibility(test, "Submit button", loginpage.isSubmitbuttonVisible());
        verifyElementVisibility(test, "QR Code", loginpage.isQRCodeVisible());
    }

    private void verifyElementVisibility(ExtentTest parentTest, String elementName, boolean visible) {
        ExtentTest node = parentTest.createNode("Verify " + elementName);
        node.assignCategory("UI Verification");

        Assert.assertTrue(visible, elementName + " is not visible");

        if (visible) {
            node.pass(elementName + " is visible");
        } else {
            node.fail(elementName + " is not visible");
        }
    }

@Test(priority = 1)
    public void VerifyContinueWithGoogleAndVerifyPageNavigation()
{
    ExtentTest test = extent.createTest("VerifyContinueWithGoogleAndVerifyPageNavigation").assignAuthor("Continue With Google Button Functionality");
    boolean isTextVisible = loginpage.gettext();
    Assert.assertTrue(isTextVisible, "Google Sign in text not visible");
    test.info("Navigated to Google Login Page. Current URL: " + loginpage.getnewpageurl());
    loginpage.closenewwindow();
    test.info("Navigated to Parent  Page. Current URL: " + loginpage.getparentpageurl());

}
    @Test(priority = 2)
    public void ClickOnCreateAccountlink()
    {
       ExtentTest test = extent.createTest( "CreateAccountlinkNavigation").assignAuthor("Create Account Link Functionality");
        boolean isTextVisible = loginpage.gettextofCreateAccount();
        Assert.assertTrue(isTextVisible, "Create Your Account text not visible");
        test.info("Navigated to Create Account Page. Current URL: " + loginpage.getnewpageurl());
        loginpage.closenewwindow();
        test.info("Navigated to Parent  Page. Current URL: " + loginpage.getparentpageurl());

    }


    @Test(priority = 3,dataProvider = "getemaildata")
    public void EnterEmailidTest(String email) {
        logger.info("Entering Login ID");
        ExtentTest test = extent.createTest("Login With Email ID").assignAuthor("EnterEmailidTest");
        System.out.println(email);
        page.navigate("https://web.pocketful.in/trading/login");
       // page.navigate("https://web.pocketful.in/trading/login");
        verify(test,null,"Login With Valid Email ID", email, null);
    }

    private void verify(ExtentTest parentTest,String msg,String testname, String email, String expectedError) {
        ExtentTest node = parentTest.createNode(testname);
        node.assignCategory("Verify Email: " + email);
        if(email != null || !email.isEmpty())
        {
            loginpage.login(email);
        }


        if (expectedError != null) {
            Assert.assertEquals(msg, expectedError,
                    "Validation message mismatch for email: " + email);
            node.pass("Verified invalid email message: " + expectedError);
        } else {
            node.pass("Entered valid email: " + email);
        }
    }
@Test(priority = 4)
public void EnterPassword()
{
    ExtentTest test = extent.createTest("Verify Enter Password Page UI").assignAuthor("UI Tests");
    UIVerifyOnEnterPassword(test);
    VerifyForgotPassword(test);
    loginpage.password("", passwordInput);
    enterPass(test,"","Enter  Blank Password","Required","Required");
    loginpage.password("chhf", passwordInput);
    enterPass(test,"xjhgfj@34","Enter Wrong Password","Invalid login attempt.","Invalid login attempt.");
    loginpage.password(Pass, passwordInput);
    enterPass(test,Pass,"Enter Valid Password","Enter MPIN","Enter MPIN");
}

    public void UIVerifyOnEnterPassword(ExtentTest Subparenttest) {
        ExtentTest test = Subparenttest.createNode( "Verify UI On Enter Password").assignAuthor("Enter Password funtionality Functionality");

        verifyElementVisibility(test, "Enter Password Placeholder", loginpage.EnterPassPlaceholder());
        verifyElementVisibility(test, "Forgot password Link", loginpage.Forgotpasstext());
        verifyElementVisibility(test, "Continue with OTP Link", loginpage.Loginwithotptext());
        verifyElementVisibility(test, "Password Hide Icon", loginpage.HideIcon());
        verifyElementVisibility(test, "Continue Button", loginpage.Continuebutton());
        verifyElementVisibility(test, "Unblock Account link", loginpage.UnblockAccount());
    }

     public void VerifyForgotPassword(ExtentTest subParenttest)
    {
        ExtentTest test = subParenttest.createNode( "VerifyForgotPassword").assignAuthor("Forgot Password Functionality");
        loginpage.clickOnForgotpass();
        verifyElementVisibility(test, "Enter Your PAN Number  Placeholder", loginpage.isPANCardPlaceholderVisible());
        verifyElementVisibility(test, "Enter Your Email ID", loginpage.isEmailidPlaceholderVisible());
        verifyElementVisibility(test, "Reset Password Text Visible", loginpage.isResetpassVisible());
        verifyElementVisibility(test, "Continue Button", loginpage.Continuebutton());
        loginpage.clickOnContinuebutton();
        verify(test,"Required", "Verify With Blank Field", " ", "Required");
        Resetpass(test,"Enter Valid PAN and Invalid Email ID","DOOPP7669F","Sarvamgoel@pacefin.in","INVALID PAN");
        page.navigate("https://web.pocketful.in/trading/login");
        verify(test,null,"Login With Valid Email ID", emailaddress, null);

    }

    public void Resetpass(ExtentTest parentTest,String testname, String PAN, String Email,String Expectederror)
    {
        ExtentTest node = parentTest.createNode(testname);

        loginpage.Enterpanandemail(PAN,Email);

        if(Expectederror != null)
        {
            Assert.assertEquals(loginpage.getInvalidpammsg(),Expectederror);
            node.pass("The Expected message is:"+loginpage.getInvalidpammsg());
        }
        else
        {
            Assert.assertEquals(loginpage.getforgotpasswordmmsg(),"Forgot Password");
            node.pass("The Expected message is:"+loginpage.getforgotpasswordmmsg());
        }

    }

    public void enterPass(ExtentTest parentTest, String pass, String testName, String actualmsg,String expectedMsg) {
        ExtentTest test = parentTest.createNode(testName);


        try {
            if (actualmsg != null && !actualmsg.isEmpty()) {
                Assert.assertEquals(actualmsg, expectedMsg, "Required message mismatch");
                test.log(Status.PASS, "Validation message displayed: " + actualmsg);
            }
             if (actualmsg != null && !actualmsg.isEmpty()) {
                Assert.assertEquals(actualmsg, expectedMsg, "Invalid login message mismatch");
                test.log(Status.PASS, "Validation message displayed: " + actualmsg);
            }
            else if (pass.equals(Pass)) {
                Assert.assertEquals(actualmsg, expectedMsg, "User not navigated to Enter MPIN page");
                test.log(Status.PASS, "User navigated to Enter MPIN page: " + actualmsg);
            }
            else {
                test.log(Status.FAIL, "No validation message is visible for password: " + pass);
                Assert.fail("Validation message missing for password input");
            }
        } catch (AssertionError e) {
            test.log(Status.FAIL, "Assertion failed: " + e.getMessage());
            throw e; // Re-throw to fail the test in TestNG
        } catch (Exception e) {
            test.log(Status.FAIL, "Exception occurred while entering password: " + e.getMessage());
            throw e;
        }
    }


    @Test(priority = 5)
    public void LoginWithOTP() throws InterruptedException {
         ExtentTest test = extent.createTest( "LoginWithOTP").assignAuthor("Login With OTP Functionality");
       // EnterEmailidTest();
         loginpage.clickOnLoginwithOTP();
         verifyElementVisibility(test, "'Access your account securely. Enter the OTP to start trading!' Text Visible", loginpage.isLoginwithOTPtextVisible());
         verifyElementVisibility(test, "'OTP has been sent to text Visible'", loginpage.isOTPSendTextVisible());
         verifyElementVisibility(test, "Verify Button Visible", loginpage.isVerifyOTPButtonVisible());
         verifyElementVisibility(test, "Resend OTP Button", loginpage.isResendOTPButtonVisible());
         verifyElementVisibility(test, "Verify All The 6 OTP Boxes are Visible", loginpage.isAllOTPBoxVisible());
         verifyElementVisibility(test, "Enter Blank OTP and Click On Verify Button", loginpage.is6digitOTPmessageVisible());
         verifyElementVisibility(test, "Enter Invalid OTP and Click On Verify Button", loginpage.isEntervalidMSGVisible());
     }

     @Test(priority = 6)
    public void EnterMPIN() throws InterruptedException {
         ExtentTest test = extent.createTest( "EnterMPIN").assignAuthor("Enter MPIN Functionality");
        // EnterEmailidTest();
         loginpage.password(Pass, passwordInput);
         enterPass(test,Pass,"Enter Valid Password","Enter MPIN","Enter MPIN");
         loginpage.ClickOnforgotMPIN();
         verifyElementVisibility(test, "Forgot MPIN Functionality", loginpage.isResetMPINtextVisible());
         verifyElementVisibility(test, "Enter Blank PAN Card ", loginpage.isRequiredmsgVisible());
         verifyElementVisibility(test, "Enter Invalid PAN Card ", loginpage.isinvalidpanmsg1Visible());
         verifyElementVisibility(test, "Enter Blank MPIN Card ", loginpage.ismpinmsgtextVisible());
         loginpage.EnterMpin("548925");
         verifyElementVisibility(test, "Enter Invalid MPIN ", loginpage.isinvalidpinmsgVisible());
         loginpage.mpinback();
         loginpage.EnterMpin(Mpin);
         verifyElementVisibility(test, "Enter a Valid  MPIN ", loginpage.isRFNOTextVisible());
         loginpage.SubmitToProceed();
     }
    @Test
    public void RunLogin() throws InterruptedException {
        ExtentTest test = extent.createTest("Login Test").assignAuthor("Login With Valid Process");
        verify(test,null,"Login With Valid Email ID", emailaddress, null);
        loginpage.password(Pass, passwordInput);
        enterPass(test,Pass,"Enter Valid Password","Enter MPIN","Enter MPIN");
        loginpage.EnterMpin(Mpin);
        loginpage.SubmitToProceed();
    }

    }


