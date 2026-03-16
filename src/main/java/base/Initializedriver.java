package base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Browser.NewContextOptions;

/**
 * Manages Playwright resources (Playwright, Browser, Context, Page) using ThreadLocal.
 * This ensures that each running test thread has its own isolated and dedicated browser instance,
 * which is mandatory for stable parallel test execution.
 */
public class Initializedriver {

    // --- ThreadLocal Storage for Parallel Execution ---
    private static ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    private static ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
    private static ThreadLocal<BrowserContext> tlContext = new ThreadLocal<>();
    private static ThreadLocal<Page> tlPage = new ThreadLocal<>();

    // --- Accessor Methods (Getters) ---
    public static Playwright getPlaywright() { return tlPlaywright.get(); }
    public static Browser getBrowser() { return tlBrowser.get(); }
    public static BrowserContext getContext() { return tlContext.get(); }
    public static Page getPage() { return tlPage.get(); }


    /**
     * Initializes the Playwright session (Playwright, Browser, Context) only once per thread.
     * Subsequent calls within the same thread return the existing Page object.
     * @return The active Playwright Page instance for the current thread.
     */
    public static Page initialize() {
        // Only initialize the heavy resources (Playwright/Browser) once per thread.
        if (getPlaywright() == null) {

            // 1. Initialize Playwright
            tlPlaywright.set(Playwright.create());

            // 2. Launch Browser
            Browser browser = getPlaywright().chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(2000)
            );
            tlBrowser.set(browser);

            // 3. Create Context
            BrowserContext context = getBrowser().newContext(
                    new NewContextOptions().setViewportSize(1366, 768)
            );
            tlContext.set(context);

            // 4. Create Page and Navigate
            Page page = getContext().newPage();
            page.navigate("https://dev-trade.pocketful.in/trading/login");
            tlPage.set(page);
        }
        return getPage();
    }

    /**
     * Closes the Playwright session and removes the ThreadLocal variables for the current thread.
     * This is crucial for resource cleanup and memory management after a test execution.
     */
    public static void close() {
        // Safely close the browser and Playwright instances
        if (getBrowser() != null) {
            getBrowser().close();
        }
        if (getPlaywright() != null) {
            getPlaywright().close();
        }

        // CRITICAL: Remove the ThreadLocal references to free resources and prepare for the next run
        tlPage.remove();
        tlContext.remove();
        tlBrowser.remove();
        tlPlaywright.remove();
    }
}
