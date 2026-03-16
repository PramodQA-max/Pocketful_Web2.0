package util;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class Actionmethod {
    private Page page;
    public Page newpage;
    public Actionmethod(Page page) {
        this.page = page;
    }


    public void clickMethod(String variable_click)
    {
        System.out.println("variable"  + variable_click+"Get Clicked");

        Locator element = page.locator(variable_click);
        try {
           // assertThat(element).isVisible();
            assertThat(element).isEnabled();
            element.click();
        }
        catch (PlaywrightException e)
        {
            System.out.println(e);
        }

    }

    public void EnterText(String variable_text, String Valuetext)
    {
        Locator element = page.locator(variable_text);
        try
        {
           // assertThat(element).isVisible();
            element.fill(Valuetext);
        }
        catch (PlaywrightException e)
        {
            throw new RuntimeException("Failed to fill text field: " + variable_text, e);
        }
    }

    public void EnterDouble(String variable_text, Double Valuetext)
    {
        Locator element = page.locator(variable_text);
        try
        {
            assertThat(element).isVisible();
            assertThat(element).isEnabled();
            element.fill(String.valueOf(Valuetext));
        }
        catch (PlaywrightException e)
        {
            throw new RuntimeException("Failed to fill text field: " + variable_text, e);
        }
    }

    public void ClearText(String variable_text)
    {
        page.locator(variable_text).fill("");

    }

    public String gettext(String variable_text)
    {
        return page.locator(variable_text).textContent();
    }





}
