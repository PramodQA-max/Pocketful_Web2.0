package pages;

import com.microsoft.playwright.Page;

public class LoginPage {
    private Page page;
    public  Page parentpage;
    public Page newpage;

    // Locators
    public static String userEmailInput = "#emailId";
    public static String passwordInput = "#password";
    private String Field01 = "#pin0";
    private String Field2 = "#pin1";
    private String Field3 = "#pin2";
    private String Field4 = "#pin3";
    private String Field5 = "#pin4";
    private String Field6 = "#pin5";
    private String submit = "button[type='submit']";
    private String IvalidEmailmessage = "//*[contains(text(),'Invalid email address')]";
    private String Logintext = "//*[contains(text(),'Login')]";
    private String Logintext1 = "//*[contains(text(),'Access your account securely. Scan, sign in, and start trading!')]";
    private String Googlebutton = "//*[contains(text(),'Continue with Google')]";
    private String EmailPlaceholder = "//*[@placeholder='Enter your email Id ']";
    private String Haveaccount = "//*[contains(text(),\"Don't have an account with us?\")]";
    private String Createaccount = "//*[contains(text(),\"Create Account\")]";
    private String Qrcode = "//*[@id=\"qrCodeImageId\"]";
    private String chooseaccount = "(//*[contains(text(),'Choose an account')])[1]";
    public static String EmailRequiredmsg = "//*[contains(text(),'Email is required')]";
    private String EnterPassPlaceholder = "//*[@placeholder='Enter your password']";
    private String Forgotpasstext = "//*[contains(text(),'Forgot Password?')]";
    private String Loginwithotptext = "//*[contains(text(),'Login with OTP')]";
    private String HideIcon = "//*[@src='https://web.pocketful.in/trading/static/media/visiblityOn.f560e17e0ea9d797ff79a4364761740a.svg']";
    private String Continuebutton = "//*[contains(text(),'Continue')]";
    private String UnblockAccount = "//*[contains(text(),'Unblock Account')]";
    private String CreateAccountPage = "//*[contains(text(),'Create Your Account')]";
    private String Resetpass = "//*[contains(text(),'Reset')]";
    private String EnterPanPlaceholder = "//*[@placeholder='Enter your PAN Number']";
    private String EnterEmailPlaceholder = "//*[@placeholder='Enter your Email ID']";
    private String Invalidpanmsg = "//*[contains(text(),'INVALID PAN')]";
    private String forgotpassword = "//*[contains(text(),'Forgot Password')]";
    private String EnterOTPText = "//*[contains(text(),'Access your account securely. Enter the OTP to start trading!')]";
    private String OTPSendText = "//*[contains(text(),' OTP has been sent to ')]";
    private String VerifyOTPButton = "//*[contains(text(),'Verify')]";
    private String resendOTPButton = "//*[contains(text(),'Resend')]";
    private String pleaseenter6digitotp = "//*[contains(text(),'Please enter the 6-digit OTP sent to you.')]";
    private String pleaseentervalidOTP = "//*[contains(text(),'Please enter a valid OTP')]";
    public static String entermpintext = "//*[contains(text(),'Enter MPIN')]";
    private String forgotmpinlink = "//*[contains(text(),'Forgot MPIN')]";
    private String loginbutton = "//*[contains(text(),'Login')]";
    private String ResetMPINtext = "//*[contains(text(),'Reset MPIN')]";
    private String panplaceholderinsideforgot = "#pan";
    private String gobackbutton = "//*[contains(text(),'Go Back')]";
    private String invalidpanmsg1 = "//*[contains(text(),'Invalid PAN')]";
    private String mpinmsg = "//*[contains(text(),'Please enter all 6 digits of the MPIN.')]";
    private String invalidpinmsg = "//*[contains(text(),'Invalid Pin')]";
    public static String Requiredmsg = "//*[contains(text(),'Required')]";
    private String FNOText = "//*[contains(text(),'Trade Responsibly in Futures & Options')]";
    public static String Invalidlogin = "//*[contains(text(),'Invalid login attempt.')]";
    // Constructor
    public LoginPage(Page page) {
        this.page = page;
    }

    // Actions
    public void login(String email) {
        page.fill(userEmailInput, email);
        page.locator(userEmailInput).press("Enter");
    }
    public void password(String Pass,String loct) {
        page.fill(loct, Pass);
        System.out.print("Locater is:"+loct);
        page.locator(loct).press("Enter");
    }
    public void Mpin(String a1, String a2, String a3, String a4, String a5, String a6) throws InterruptedException {
        page.fill(Field01, a1);
        page.fill(Field2, a2);
        page.fill(Field3, a3);
        page.fill(Field4, a4);
        page.fill(Field5, a5);
        page.fill(Field6, a6);
    }
    public void SubmitToProceed()
    {
        page.click(submit);

    }
    public String InvalidEmailmsg()
    {

      String message=  page.locator(IvalidEmailmessage).textContent();
        return message;
    }
    public boolean isLoginTextVisible() {
        return page.isVisible(Logintext);
    }

    public boolean isLoginText1Visible() {
        return page.isVisible(Logintext1);
    }

    public boolean isGoogleButtonVisible() {
        return page.isVisible(Googlebutton) && page.locator(submit).isEnabled();
    }

    public boolean isEmailPlaceholderVisible() {
        return page.isVisible(EmailPlaceholder);
    }

    public boolean isHaveAccountVisible() {
        return page.isVisible(Haveaccount);
    }

    public boolean isCreateAccountVisible() {
        return page.isVisible(Createaccount);
    }

    public boolean isSubmitbuttonVisible() {
        return page.isVisible(submit) && page.locator(submit).isEnabled();
    }

    public boolean isQRCodeVisible() {
        return page.isVisible(Qrcode) ;
    }

    public  void ClickOnContinueWithGoogleButton()
   {
    page.locator(Googlebutton).click();
   }
   public String getnewpageurl()
   {
       return newpage.url();
   }
  public boolean gettext()
   {
     parentpage=this.page;
     newpage= parentpage.waitForPopup(()->{ClickOnContinueWithGoogleButton();});
    newpage.waitForLoadState();
    newpage.locator(chooseaccount).isVisible();
    return true;
   }
   public void closenewwindow()
   {
       newpage.close();
       parentpage.bringToFront();
   }
    public String getparentpageurl()
    {
        return parentpage.url();
    }
    public String getInvalidmsg(String locat)
    {
        return page.locator(locat).textContent();
    }
    public boolean gettextofCreateAccount()
    {
        parentpage=this.page;
        newpage= parentpage.waitForPopup(()->{parentpage.click(Createaccount);});
        newpage.waitForLoadState();
        newpage.locator(CreateAccountPage).isVisible();
        return true;
    }

    public boolean EnterPassPlaceholder()
    {
        page.locator(EnterPassPlaceholder).isVisible();
        return true;
    }

    public boolean Forgotpasstext()
    {
        page.locator(Forgotpasstext).isVisible();
        return true;
    }

    public boolean Loginwithotptext()
    {
        page.locator(Loginwithotptext).isVisible();
        return true;
    }

    public boolean HideIcon()
    {
        page.locator(HideIcon).isVisible();
        return true;
    }

    public boolean Continuebutton()
    {

        return  page.isVisible(Continuebutton) && page.locator(Continuebutton).isEnabled();
    }

    public boolean UnblockAccount()
    {
        page.locator(UnblockAccount).isVisible();
        return true;
    }

    public void clickOnForgotpass()
    {
        page.locator(Forgotpasstext).click();
    }
    public boolean isPANCardPlaceholderVisible() {
        return page.isVisible(EnterPanPlaceholder);
    }

    public boolean isEmailidPlaceholderVisible() {
        return page.isVisible(EnterEmailPlaceholder);
    }

    public boolean isResetpassVisible() {
        return page.isVisible(Resetpass);
    }

    public void clickOnContinuebutton()
    {
        page.locator(Continuebutton).click();
    }

    public String getInvalidpammsg()
    {
        return page.locator(Invalidpanmsg).textContent();
    }

    public String getforgotpasswordmmsg()
    {
        return page.locator(forgotpassword).textContent();
    }

    public void Enterpanandemail(String pan, String email)
    {
        page.fill(EnterPanPlaceholder,pan);
        page.fill(EnterEmailPlaceholder,email);
        page.locator(Continuebutton).click();
    }

    public void clickOnLoginwithOTP()
    {
        page.locator(Loginwithotptext).click();
    }

    public boolean isLoginwithOTPtextVisible() {
        return page.isVisible(EnterOTPText);
    }

    public boolean isOTPSendTextVisible() {
        return page.isVisible(OTPSendText);
    }

    public boolean isVerifyOTPButtonVisible() {
        return page.isVisible(VerifyOTPButton);
    }

    public boolean isResendOTPButtonVisible() {
        return page.isVisible(resendOTPButton);
    }

    public boolean isAllOTPBoxVisible() {
        return page.isVisible(Field01) && page.isVisible(Field2) && page.isVisible(Field3) && page.isVisible(Field4) && page.isVisible(Field5) && page.isVisible(Field6);
    }
    public boolean is6digitOTPmessageVisible() {
        page.locator(VerifyOTPButton).click();
        return page.isVisible(pleaseenter6digitotp);
    }
    public boolean isEntervalidMSGVisible() throws InterruptedException {
        page.locator(VerifyOTPButton).click();
        EnterMpin("548925");
        return page.isVisible(pleaseentervalidOTP);
    }

    public boolean ismpinmsgtextVisible() {
        page.locator(gobackbutton).click();
        page.click(loginbutton);
        return page.isVisible(mpinmsg);
    }

    public boolean isinvalidpinmsgVisible() {
        return page.isVisible(invalidpinmsg);
    }

    public boolean isResetMPINtextVisible() {
        return page.isVisible(ResetMPINtext);
    }

    public boolean isRFNOTextVisible() {
        return page.isVisible(FNOText);
    }

    public void ClickOnforgotMPIN() {
        page.locator(forgotmpinlink).click();

    }

    public boolean isRequiredmsgVisible() {
        page.locator(VerifyOTPButton).click();
        return page.isVisible(Requiredmsg);
    }

    public boolean isinvalidpanmsg1Visible() {
        page.fill(panplaceholderinsideforgot,"ABCD");
        page.locator(VerifyOTPButton).click();
        return page.isVisible(invalidpanmsg1);
    }

    public void EnterMpin(String Mpin) throws InterruptedException {
        String mpin=Mpin;
        char[] digits = new char[mpin.length()];

        for (int i = 0; i < mpin.length(); i++) {
            digits[i] = mpin.charAt(i);

        }
        Mpin(
                String.valueOf(digits[0]),
                String.valueOf(digits[1]),
                String.valueOf(digits[2]),
                String.valueOf(digits[3]),
                String.valueOf(digits[4]),
                String.valueOf(digits[5])
        );
      //  page.click(loginbutton);
    }

    public void mpinback()
    {
        for (int i = 0; i < 5; i++) {
             page.locator(Field01).press("Backspace");
        }

    }

    public void ClickOnContinueButton(String locat)
    {
        page.locator(locat).click();
    }
}
