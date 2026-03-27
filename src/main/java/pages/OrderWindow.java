package pages;

import util.Actionmethod;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class OrderWindow {

    private final Page page;
    private final Actionmethod actionmethodpage;
    public Page newpage; // Assuming this is used for context switching if needed

    // --- WATCHLIST LOCATORS (UPPER_SNAKE_CASE) ---
    public static final String WATCHLIST_SEARCH_BAR = "//*[@placeholder=\"Search and Add to watchlist\"]";
    public static final String WATCHLIST_FIRST_STOCK_RESULT_SPAN = "(//*[@id=\"scrollableDiv\"]//span)[1]";
    public static final String WATCHLIST_ADD_BUTTON_ICON = "(//*[@src=\"https://web.pocketful.in/trading/static/media/plus.19cd004cfa635e52605f7b0bf9303b48.svg\"])[1]";
    public static final String WATCHLIST_BUY_BUTTON = "(//button[contains(text(),'B')])[1]";
    public static final String OVERVIEW_BUY_BUTTON = "(//button[contains(text(),'Buy')])[1]";
    public static final String OVERVIEW_SELL_BUTTON = "(//button[contains(text(),'Sell')])[1]";
    public static final String PROCEED_BUTTON = "(//button[contains(text(),'Proceed')])[1]";
    public static final String DELETE_FROM_WATCHLIST_BUTTON = "(//*[@src=\"https://web.pocketful.in/trading/static/media/delete.cc2ac0d69d37a97849b5d5af93a1cd52.svg\"])[1]";
    public static final String WATCHLIST_FIRST_RESULT = "(//div[@id='scrollableDiv']//div[contains(@class,'cursor-pointer')])[1]";

    public static final String PENDING_ORDER_ROWS = "//table[contains(@class,'w-full')]//tbody//tr";
    public static final String INSTRUMENT_NAME_IN_ROW = ".//td[contains(@class,'text-start')]//span[contains(@class,'body_regular') and contains(@class,'text-text-color-primary')]";

    public static final String QTY_MINUS_BUTTON = "//input[@id='quantity']/parent::div/preceding-sibling::div";
    public static final String QTY_PLUS_BUTTON = "//input[@id='quantity']/parent::div/following-sibling::div";

    // --- ORDER WINDOW LOCATORS ---
    public static final String ORDER_WINDOW_BUY_NOW_BUTTON = "(//*[starts-with(normalize-space(),'Buy Now')])[1]";
    public static final String ORDER_WINDOW_SELL_NOW_BUTTON = "(//*[starts-with(normalize-space(),'Sell Now')])[1]";
    public static final String ORDER_WINDOW_CANCEL_ICON = "//*[@src=\"https://web.pocketful.in/trading/static/media/whiteCross.4a286d9665f19fabd6ab520fa63894db.svg\"]";
    public static final String ORDER_PRICE_FIELD = "//*[@id=\"price\"]";
    public static final String ORDER_ID_VALUE = "//*[contains(text(),'OMS Order ID')]//following-sibling::span";
    public static final String PRICE_CHECKBOX = "//*[@id=\"price\"]//following-sibling::input";
    public static final String ORDER_QUANTITY_FIELD = "//*[@id=\"quantity\"]";
    public static final String ORDER_MODIFY_NOW_BUTTON = "//*[contains(text(),\"Modify Now \")]";
    public static final String ORDER_TGRPRICE_FIELD = "//input[@id='triggerPrice']";
    public static final String ORDER_WINDOW_INTRADAY_TAB = "//*[contains(text(),\"Intraday\")]";

    public static final String ORDER_WINDOW_PAYLATER = "(//*[contains(text(),\"Pay Later\")])[1]";
    public static final String ORDER_WINDOW_PAYLATER_BUTTON = "(//*[contains(text(),\"Buy With MTF \")])[1]";
    public static final String ORDER_WINDOW_SELL_PAYLATER = "(//*[contains(text(),\"Sell With MTF \")])[1]";
    public static final String ORDER_WINDOW_COVERORDER = "//*[contains(text(),\"Cover\")]";
    public static final String ORDER_WINDOW_BRACKETORDER = "//*[contains(text(),\"Bracket\")]";

    // --- ORDER TAB LOCATORS ---
    public static final String ORDERS_TAB = "(//*[contains(text(),'Orders')])[1]";
    public static final String PENDING_ORDERS_TAB = "//*[contains(text(),'Pending')]";
    public static final String EXECUTED_ORDERS_TAB = "(//*[contains(text(),'Executed')])[2]";
    public static final String ORDER_ROW_COMPLETED_OR_REJECTED = "(//*[contains(text(),'Completed') or contains(text(),'Rejected') ])[1]";
    public static final String INFO_ICON_ON_ORDER_ROW = "//*[contains(text(),\"Repeat\")]/preceding-sibling::img";
    public static final String INFO_ICON_ON_PENDING_ORDER_ROW = "//*[contains(text(),\"Modify\")]/preceding-sibling::img";
    public static final String GTT_TAB_SELECTOR_STARTS_WITH = "//*[starts-with(text(), 'Executed (') or contains(text(), 'Executed (') ]";
    public static final String PENDING_ORDER_MODIFY_BUTTON = "//*[contains(text(),\"Modify\")]";
    public static final String ORDER_WINDOW_TARGETPRICE_TEXT = "//*[contains(text(),\"Trigger Price\")]";
    public static final String ORDER_WINDOW_TARGETPRICE_FIELD = "(//*[@id=\"triggerPrice\"])[1]";
    public static final String ORDER_WINDOW_CHECHBOX = "//*[@id=\"price\"]/following-sibling::input[@type=\"checkbox\"]";
    public static final String ORDER_WINDOW_TRGCHECHBOX = "//input[@id='triggerPrice']/../div/span[contains(text(), 'Trigger Price')]";
    public static final String ORDER_WINDOW_SL_FIELD = "#stopLoss";
    public static final String ORDER_WINDOW_TL_FIELD = "(//*[@id=\"triggerPrice\"])[2]";
    public static final String ORDER_WINDOW_YOUPAY_TEXT = "//*[contains(text(),\"You Pay\")]";
    public static final String ORDER_WINDOW_YOUSAVING_TEXT = "//*[contains(text(),\"Your Savings\")]";
    public static final String ORDER_WINDOW_TSW_TEXT = "//*[contains(text(),\"Total Stocks worth \")]";
    public static final String ORDER_SIDE_BUY_TEXT = "//div[contains(@class,'no-drag')]//span[text()='BUY']";
    public static final String ORDER_SIDE_SELL_TEXT = "//div[contains(@class,'no-drag')]//span[text()='SELL']";
    public static final String ORDER_SIDE_TOGGLE_ACTIVE = "//div[contains(@class,'no-drag')]//div[span[text()='BUY'] and span[text()='SELL']]//span[contains(@class,'text-green-text') or contains(@class,'text-red-text') or contains(@class,'body_medium')]";
    public static final String ORDER_SIDE_TOGGLE_INACTIVE = "//div[contains(@class,'no-drag')]//div[span[text()='BUY'] and span[text()='SELL']]//span[contains(@class,'text-text-color-primary') or contains(@class,'subtext_regular')]";

    // --- NOTIFICATION LOCATORS ---

    public static final String AMO_ORDER_MODIFY_NOTIFICATION = "//div[contains(@class,'subtext_medium') and contains(text(),'AMO MODIFIED')]";
    public static final String NORMAL_ORDER_MODIFY_NOTIFICATION = "//div[contains(@class,'subtext_medium') and contains(text(),'MODCONFIRMED')]";
    public static final String NORMAL_ORDER_PLACE_NOTIFICATION = "//div[contains(@class,'subtext_medium') and contains(text(),'AMO REQ RECEIVED')]";
    public static final String COMPLETED_ORDER_NOTIFICATION = "//div[contains(@class,'subtext_medium') and contains(text(),'COMPLETED')]";
    public static final String REJECTED_ORDER_NOTIFICATION = "//div[contains(@class,'subtext_medium') and contains(text(),'REJECTED')]";

    /**
     * Constructor for the OrderWindow Page Object Model.
     * 
     * @param page The Playwright Page instance.
     */
    public OrderWindow(Page page) {
        this.page = page;
        this.actionmethodpage = new Actionmethod(page);
    }

    /**
     * Searches for a stock, adds it to the watchlist (if not present), and opens
     * the Order Window.
     */
    public void addInstrumentToWatchlistAndOpenOrderWindow(String stockname, String Order) {
        // 1. Search for stock
        System.out.println("The Order side " + Order);
        try {
            // Wait for search bar to be visible and stable
            page.waitForSelector(WATCHLIST_SEARCH_BAR, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
            
            actionmethodpage.clickMethod(WATCHLIST_SEARCH_BAR);
            actionmethodpage.EnterText(WATCHLIST_SEARCH_BAR, stockname);
            System.out.println("Stock name is:" + stockname);

            // 2. Locate the add/remove button for the stock
            Locator addButtonLocator = page.locator(WATCHLIST_FIRST_RESULT);
            addButtonLocator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));

            addButtonLocator.click();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Order.equalsIgnoreCase("Buy")) {
            try {
                Locator OaddButtonLocator = page.locator(
                        OVERVIEW_BUY_BUTTON);
                OaddButtonLocator.click();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (Order.equalsIgnoreCase("Sell")) {
            try {
                Locator OaddButtonLocator = page.locator(
                        OVERVIEW_SELL_BUTTON);
                OaddButtonLocator.click();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 5. Optional: handle Proceed button if popup appears

    }

    /**
     * Locates the order row, hovers over it to reveal hidden icons, and clicks the
     * Info icon.
     */
    public void hoverOrderRowAndClickInfoIcon(String Stockname) {
        // 1. XPath matching the first row containing the stock name
        String xpath = "(//tr[contains(.,'" + Stockname.toUpperCase() + "')])[1]";

        // 2. Locate the row and wait for it to be visible
        Locator stockRow = page.locator(xpath);
        stockRow.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));

        // 3. Hover the row to reveal hidden icons. Use force to ensure the hover action
        // is executed.
        stockRow.hover(new Locator.HoverOptions().setForce(true));

        // 4. Locate the Info icon inside that same row (using relative locator)
        // Note: Using the generic INFO_ICON_ON_ORDER_ROW as the selector for the inner
        // icon.
        Locator infoIcon = stockRow.locator(INFO_ICON_ON_ORDER_ROW);
        System.out.println("The Info Icon of Executed tab:" + infoIcon);

        // 5. Wait for the icon to be visible after hover and click it.
        try {
            infoIcon.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(3000));
            infoIcon.click();
        } catch (PlaywrightException e) {
            System.err.println("Info icon did not appear or was not clickable after hover.");
            throw e; // Re-throw to fail the test clearly
        }
    }

    public void hoverAndClickInfoIconPending(String stockname) {
        // 1️⃣ XPath to find the specific order row (<tr>) containing the instrument
        // name (case-insensitive)
        String xpath = "(//tr[contains(.,'" + stockname.toUpperCase() + "')])[1]";

        // 2️⃣ Locate and scroll to the row
        Locator stockRow = page.locator(xpath);
        assertThat(stockRow).isVisible();
        stockRow.scrollIntoViewIfNeeded();

        // 3️⃣ Hover the row to reveal hidden icons (simulates the 'group-hover' state)
        stockRow.hover();

        // 4️⃣ Click Info icon inside that same row using the chained locator
        // We use the chained locator approach: stockRow.locator(INFO_ICON_IN_ROW)
        Locator infoIcon = stockRow.locator(INFO_ICON_ON_PENDING_ORDER_ROW);

        // Wait for the icon to become visible after the hover action
        assertThat(infoIcon).isVisible();
        infoIcon.click();

        System.out.println("Hovered over order row for " + stockname + " and clicked Info icon.");
    }

    /**
     * Retrieves all instrument names currently listed in the Pending Orders tab.
     * @return List of stock names.
     */
    public List<String> getAllPendingOrderInstruments() {
        List<String> instruments = new ArrayList<>();
        page.waitForSelector(PENDING_ORDER_ROWS, new Page.WaitForSelectorOptions().setTimeout(5000));
        Locator rows = page.locator(PENDING_ORDER_ROWS);
        int count = rows.count();
        for (int i = 0; i < count; i++) {
            String name = rows.nth(i).locator(INSTRUMENT_NAME_IN_ROW).first().innerText().trim();
            if (!name.isEmpty()) {
                instruments.add(name);
            }
        }
        return instruments;
    }

    /**
     * Increments the quantity field by its initial pre-filled value using the + button.
     */
    public void incrementQuantityByInitialValue() {
        Locator plusBtn = page.locator(QTY_PLUS_BUTTON);
        Locator qtyField = page.locator(ORDER_QUANTITY_FIELD);

        int initialQty = Integer.parseInt(qtyField.getAttribute("value").trim());
        System.out.println("Initial Qty: " + initialQty + ". Incrementing by the same amount.");

        for (int i = 0; i < initialQty; i++) {
            plusBtn.click();
            // Small wait to allow UI updates
            page.waitForTimeout(50);
        }
        System.out.println("Final Qty reached: " + qtyField.getAttribute("value").trim());
    }

    public void hoverAndClickOnModifyButton(String stockname) {
        // 1️⃣ XPath to find the specific order row (<tr>) containing the instrument
        // name (case-insensitive)
        String xpath = "(//tr[contains(.,'" + stockname.toUpperCase() + "')])[1]";

        // 2️⃣ Locate and scroll to the row
        Locator stockRow = page.locator(xpath);
        assertThat(stockRow).isVisible();
        stockRow.scrollIntoViewIfNeeded();

        // 3️⃣ Hover the row to reveal hidden icons (simulates the 'group-hover' state)
        stockRow.hover();

        // 4️⃣ Click Info icon inside that same row using the chained locator
        // We use the chained locator approach: stockRow.locator(INFO_ICON_IN_ROW)
        Locator infoIcon = stockRow.locator(PENDING_ORDER_MODIFY_BUTTON);

        // Wait for the icon to become visible after the hover action
        assertThat(infoIcon).isVisible();
        infoIcon.click();

        System.out.println("Hovered over order row for " + stockname + " and clicked Info icon.");
    }

    /**
     * Retrieves the current price from the order field, applies a discount factor,
     * and returns the new calculated price.
     * 
     * @param DISCOUNT_FACTOR The factor to multiply the current price by (e.g.,
     *                        0.98 for 2% discount).
     * @return The final discounted price as a Double.
     */
    public Double calculateDiscountedPrice(Double DISCOUNT_FACTOR) {
        // Get the current value from the price field
        try {
            String PRICE_FIELD_STATUS = page.locator("#price").getAttribute("value").trim();
            System.out.println("this is value  " + PRICE_FIELD_STATUS);
            // String PRICE_FIELD_STATUS= PRICE_FIELD_STATUS.getAttribute("value").trim();
            if ((PRICE_FIELD_STATUS.equalsIgnoreCase("Market Price"))) {
                page.locator(ORDER_WINDOW_CHECHBOX).click();
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String initialPriceString = page.locator(ORDER_PRICE_FIELD).getAttribute("value").trim();

        // Convert, calculate, and return
        Double finalPriceAfterCalculation = Double.parseDouble(initialPriceString) * DISCOUNT_FACTOR;
        return finalPriceAfterCalculation;
    }

    public Double calculateTrgDiscountedPrice(Double DISCOUNT_FACTOR) {
        // Get the current value from the price field
        page.locator(ORDER_WINDOW_TRGCHECHBOX).click();
        String initialPriceString = page.locator(ORDER_TGRPRICE_FIELD).getAttribute("value").trim();

        // Convert, calculate, and return
        Double finalPriceAfterCalculation = Double.parseDouble(initialPriceString) * DISCOUNT_FACTOR;
        return finalPriceAfterCalculation;
    }

    public Double calculateSL(Double DISCOUNT_FACTOR) {
        // Get the current value from the price field
        try {
            String PRICE_FIELD_STATUS = page.locator("#price").getAttribute("value").trim();
            System.out.println("this is value  " + PRICE_FIELD_STATUS);
            // String PRICE_FIELD_STATUS= PRICE_FIELD_STATUS.getAttribute("value").trim();
            if ((PRICE_FIELD_STATUS.equalsIgnoreCase("Market Price"))) {
                page.locator(ORDER_WINDOW_CHECHBOX).click();
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String initialPriceString = page.locator(ORDER_PRICE_FIELD).getAttribute("value").trim();

        // Convert, calculate, and return
        Double finalPriceAfterCalculation = Double.parseDouble(initialPriceString) * DISCOUNT_FACTOR;
        return finalPriceAfterCalculation;
    }

    public void ClickonExecutecTab() {
        Locator Executedtab = page.getByText(Pattern.compile("^Executed\\("));
        Executedtab.click();
    }

    public void StoreOMSIDintoConstant(String stockname) {
        System.out.println("This is inside the StoreOMSIDintoConstant Block");
        actionmethodpage.clickMethod(ORDERS_TAB);
        hoverAndClickInfoIconPending(stockname);
        System.out.println("The OMS Order ID is " + actionmethodpage.gettext(ORDER_ID_VALUE));
    }

    /**
     * Retrieves current order details from the window.
     */
    public java.util.Map<String, String> getOrderWindowDetails() {
        java.util.Map<String, String> details = new java.util.HashMap<>();
        details.put("qty", page.locator(ORDER_QUANTITY_FIELD).getAttribute("value"));
        details.put("price", page.locator(ORDER_PRICE_FIELD).getAttribute("value"));
        
        // Handle trigger price field (may be and disabled/hidden)
        Locator trgField = page.locator(ORDER_TGRPRICE_FIELD);
        if (trgField.isVisible() && trgField.isEnabled()) {
            details.put("triggerPrice", trgField.getAttribute("value"));
        } else {
            details.put("triggerPrice", "N/A");
        }
        
        // Handle active side
        Locator activeSide = page.locator(ORDER_SIDE_TOGGLE_ACTIVE);
        if (activeSide.isVisible()) {
            details.put("side", activeSide.textContent().trim());
        } else {
            details.put("side", "Unknown");
        }
        
        return details;
    }

    /**
     * Modifies the quantity and price fields.
     */
    public void modifyOrderFields(String qty, String price, String triggerPrice) {
        if (qty != null) {
            actionmethodpage.EnterText(ORDER_QUANTITY_FIELD, qty);
        }
        if (price != null) {
            // If currently market price, need to toggle to limit
            String currentPrice = page.locator(ORDER_PRICE_FIELD).getAttribute("value");
            if (currentPrice.toLowerCase().contains("market")) {
                page.locator(ORDER_WINDOW_CHECHBOX).click();
            }
            actionmethodpage.EnterText(ORDER_PRICE_FIELD, price);
        }
        if (triggerPrice != null) {
             // Ensure trigger price is enabled
             Locator trgCheckbox = page.locator(ORDER_WINDOW_TRGCHECHBOX);
             if (trgCheckbox.isVisible() && !page.locator(ORDER_TGRPRICE_FIELD).isEnabled()) {
                 trgCheckbox.click();
             }
             actionmethodpage.EnterText(ORDER_TGRPRICE_FIELD, triggerPrice);
        }
    }

    /**
     * Verifies that the order side (Buy/Sell) cannot be changed.
     */
    public boolean verifyOrderSideImmutability() {
        String initialSide = getOrderWindowDetails().get("side");
        System.out.println("Initial Order Side: " + initialSide);
        
        Locator inactiveSide = page.locator(ORDER_SIDE_TOGGLE_INACTIVE);
        if (inactiveSide.isVisible()) {
            try {
                // Attempt to click the inactive side
                inactiveSide.click(new Locator.ClickOptions().setTimeout(2000));
            } catch (Exception e) {
                System.out.println("Could not click inactive side (expected): " + e.getMessage());
            }
        }
        
        String postClickSide = getOrderWindowDetails().get("side");
        System.out.println("Post-click Order Side: " + postClickSide);
        
        return initialSide.equals(postClickSide);
    }

}