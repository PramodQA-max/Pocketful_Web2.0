package testPage;

import pages.OrderWindow;
import util.Actionmethod;
import util.ExtentManager;
import base.Initializedriver;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Arrays;
import java.util.List;

import static pages.OrderWindow.*;
import static util.Base.authlogTestSummary;
import static util.Constant.Stock_idea;
import static util.Constant.Stock_Nifty;
//import static sun.security.provider.certpath.BuildStep.FAIL;

public class OrderWindowTest extends Initializedriver {

        private final static ExtentReports extent = ExtentManager.getInstance();
        public Page page;
        OrderWindow orderWindowpage;
        Actionmethod actionmethodpage;
        SoftAssert softAssert = new SoftAssert();

        @BeforeTest
        public void init() {
                page = initialize();
                orderWindowpage = new OrderWindow(page);
                actionmethodpage = new Actionmethod(page);

        }

        @AfterClass
        public static void pushReport() {
                extent.flush();
                authlogTestSummary(extent, "Order Page Test");
        }

        /**
         * Test Case 1: Verifies the successful placement of a regular limit order
         * after applying a discount.
         */
        @Test(priority = 0)
        public void testPlaceRegularOrder() throws InterruptedException {
                ExtentTest test = extent.createTest("Order Place").assignAuthor("Place Regular Order");

                // --- 1. Regular Orders (Product: Invest / Intraday) ---
                // C01: Buy - Invest - Regular Limit Order (Equity)
                executeOrder(test, "TC01: Buy Invest Regular Limit (Equity)", "Invest",
                                "null", "Buy", Stock_idea, "1",
                                0.98,
                                null, null, null);
                // TC02: Sell - Invest - Regular Limit Order (Equity)
                executeOrder(test, "TC02: Sell Invest Regular Limit (Equity)", "Invest", "null", "Sell", Stock_idea,
                                "1", 1.02,
                                null, null, null);

                // TC03: Buy - Invest - Regular Market Order (Equity)
                executeOrder(test, "TC03: Buy Invest Regular Market (Equity)", "Invest", "null", "Buy", Stock_idea, "1",
                                null,
                                null, null, null);
                // TC04: Sell - Invest - Regular Market Order (Equity)
                executeOrder(test, "TC04: Sell Invest Regular Market (Equity)", "Invest", "null", "Sell", Stock_idea,
                                "1", null,
                                null, null, null);

                // TC05: Buy - Invest - Regular Limit Order (FNO)
                executeOrder(test, "TC05: Buy Invest Regular Limit (FNO)", "Invest", "null", "Buy", Stock_Nifty, "1",
                                0.98,
                                null, null, null);
                // TC06: Sell - Invest - Regular Limit Order (FNO)
                executeOrder(test, "TC06: Sell Invest Regular Limit (FNO)", "Invest", "null", "Sell", Stock_Nifty, "1",
                                1.02,
                                null, null, null);

                // TC07: Buy - Intraday - Regular Limit Order
                executeOrder(test, "TC07: Buy Intraday Regular Limit", "Intraday", "null", "Buy", Stock_idea, "1", 0.98,
                                null,
                                null, null);
                // TC08: Sell - Intraday - Regular Limit Order
                executeOrder(test, "TC08: Sell Intraday Regular Limit", "Intraday", "null", "Sell", Stock_idea, "1",
                                1.02, null,
                                null, null);

                // TC09: Buy - Intraday - Regular Market Order
                executeOrder(test, "TC09: Buy Intraday Regular Market", "Intraday", "null", "Buy", Stock_idea, "1",
                                null, null,
                                null, null);
                // TC10: Sell - Intraday - Regular Market Order
                executeOrder(test, "TC10: Sell Intraday Regular Market", "Intraday", "null", "Sell", Stock_idea, "1",
                                null,
                                null, null, null);

                // --- 2. Pay Later / MTF Orders ---
                // TC11: Buy - Pay Later - Regular Limit Order
                executeOrder(test, "TC11: Buy Pay Later Regular Limit", "Pay Later", "null", "Buy", Stock_idea, "1",
                                0.98, null,
                                null, null);
                // TC12: Buy - Pay Later - Regular Market Order
                executeOrder(test, "TC12: Buy Pay Later Regular Market", "Pay Later", "null", "Buy", Stock_idea, "1",
                                null,
                                null, null, null);

                // --- 3. Cover Orders (Sub-product: Cover) ---
                // TC13: Buy - Intraday - Cover Order (Limit)
                // Note: For cover order, we simulate stop loss using the trigger logic in
                // executeOrder (if OrderSide Buy, we calculate SL)
                executeOrder(test, "TC13: Buy Intraday Cover Order Limit", "Intraday", "Cover", "Buy", Stock_idea, "1",
                                0.98,
                                null, null, null);
                // TC14: Sell - Intraday - Cover Order (Limit)
                executeOrder(test, "TC14: Sell Intraday Cover Order Limit", "Intraday", "Cover", "Sell", Stock_idea,
                                "1", 1.02,
                                null, null, null);

                // --- 4. Bracket Orders (Sub-product: Bracket) ---
                // TC17: Buy - Intraday - Bracket Order
                executeOrder(test, "TC17: Buy Intraday Bracket Order", "Intraday", "Bracket", "Buy", Stock_idea, "1",
                                0.98,
                                null, null, null);
                // TC18: Sell - Intraday - Bracket Order
                executeOrder(test, "TC18: Sell Intraday Bracket Order", "Intraday", "Bracket", "Sell", Stock_idea, "1",
                                1.02,
                                null, null, null);

                // --- 5. Stop Loss / Trigger Orders ---
                // TC21: Buy - Invest - Stop Loss Limit Order
                executeOrder(test, "TC21: Buy Invest SL Limit Order", "Invest", "null", "Buy", Stock_idea, "1", 0.98,
                                "1.01",
                                null, null);
                // TC22: Sell - Invest - Stop Loss Limit Order
                executeOrder(test, "TC22: Sell Invest SL Limit Order", "Invest", "null", "Sell", Stock_idea, "1", 1.02,
                                "0.99",
                                null, null);

                // TC23: Buy - Intraday - Stop Loss Limit Order
                executeOrder(test, "TC23: Buy Intraday SL Limit Order", "Intraday", "null", "Buy", Stock_idea, "1",
                                0.98,
                                "1.01", null, null);
                // TC24: Sell - Intraday - Stop Loss Limit Order
                executeOrder(test, "TC24: Sell Intraday SL Limit Order", "Intraday", "null", "Sell", Stock_idea, "1",
                                1.02,
                                "0.99", null, null);
        }

        /**
         * Helper method to execute the steps for Placing order.
         * Any parameter set to null will be skipped.
         */
        private void executeOrder(
                        ExtentTest parentTest, String Testname, String product, String subproduct, String OrderSide,
                        String Stockname, String newQuantity,
                        Double newPrice, String newTriggerPrice, String newTargetPrice,
                        String newStopLossPrice) {

                ExtentTest node = parentTest.createNode(Testname);
                node.assignCategory("Place Order");

                orderWindowpage.addInstrumentToWatchlistAndOpenOrderWindow(Stockname, OrderSide);

                // --- Step 1: Enter Quantity (and assert) ---

                if (product != null) {
                        if (product.equalsIgnoreCase("Intraday")) {
                                actionmethodpage.clickMethod(ORDER_WINDOW_INTRADAY_TAB);
                                String actualProduct = page.locator(ORDER_WINDOW_INTRADAY_TAB).textContent().trim();
                                softAssert.assertEquals(product, actualProduct,
                                                "Product check failed. Expected: " + product + ", Actual: "
                                                                + actualProduct);
                                node.info("Product  is: " + actualProduct);
                        } else if (product.equalsIgnoreCase("Pay Later")) {

                                actionmethodpage.clickMethod(ORDER_WINDOW_PAYLATER);
                                String actualProduct = page.locator(ORDER_WINDOW_PAYLATER).textContent().trim();
                                softAssert.assertEquals(product, actualProduct,
                                                "Product check failed. Expected: " + product + ", Actual: "
                                                                + actualProduct);
                                node.info("Product  is: " + actualProduct);

                                Locator text1 = page.locator(ORDER_WINDOW_YOUPAY_TEXT);
                                softAssert.assertTrue(text1.isVisible(), "You Pay is not Showing");

                                Locator text2 = page.locator(ORDER_WINDOW_YOUSAVING_TEXT);
                                softAssert.assertTrue(text2.isVisible(), "You Pay is not Showing");

                                Locator text3 = page.locator(ORDER_WINDOW_TSW_TEXT);
                                softAssert.assertTrue(text3.isVisible(), "You Pay is not Showing");

                        }

                        if (subproduct != "null") {
                                if (subproduct.equalsIgnoreCase("Cover")) {
                                        actionmethodpage.clickMethod(ORDER_WINDOW_COVERORDER);
                                        String actualProduct = page.locator(ORDER_WINDOW_COVERORDER).textContent()
                                                        .trim();
                                        softAssert.assertEquals(product, actualProduct,
                                                        "Product check failed. Expected: " + product + ", Actual: "
                                                                        + actualProduct);
                                        node.info("Product  is: " + actualProduct);

                                        if (OrderSide.equalsIgnoreCase("Buy")) {
                                                Double price_value = orderWindowpage.calculateDiscountedPrice(0.95);
                                                String finalPriceString = String.format("%.1f", price_value);
                                                actionmethodpage.EnterText(ORDER_WINDOW_SL_FIELD, finalPriceString);
                                                String actualPrice = page.locator(ORDER_WINDOW_SL_FIELD)
                                                                .getAttribute("value").trim();
                                                softAssert.assertEquals(actualPrice, finalPriceString,
                                                                "Price check failed. Expected: " + finalPriceString
                                                                                + ", Actual: " + actualPrice);
                                                node.info("Entered  Price is:" + finalPriceString);
                                        } else if (OrderSide.equalsIgnoreCase("Sell")) {

                                                Double price_value = orderWindowpage.calculateDiscountedPrice(1.05);
                                                String finalPriceString = String.format("%.1f", price_value);
                                                actionmethodpage.EnterText(ORDER_WINDOW_SL_FIELD, finalPriceString);
                                                String actualPrice = page.locator(ORDER_WINDOW_SL_FIELD)
                                                                .getAttribute("value").trim();
                                                softAssert.assertEquals(actualPrice, finalPriceString,
                                                                "Price check failed. Expected: " + finalPriceString
                                                                                + ", Actual: " + actualPrice);
                                                node.info("Entered  Price is:" + finalPriceString);
                                        }

                                }

                                if (subproduct.equalsIgnoreCase("Bracket")) {
                                        actionmethodpage.clickMethod(ORDER_WINDOW_BRACKETORDER);
                                        String actualProduct = page.locator(ORDER_WINDOW_BRACKETORDER).textContent()
                                                        .trim();
                                        softAssert.assertEquals(product, actualProduct,
                                                        "Product check failed. Expected: " + product + ", Actual: "
                                                                        + actualProduct);
                                        node.info("Product  is: " + actualProduct);

                                        if (OrderSide.equalsIgnoreCase("Buy")) {

                                                // Set STOP LOSS
                                                Double price_value = orderWindowpage.calculateDiscountedPrice(0.97);
                                                String finalPriceString = String.format("%.1f", price_value);
                                                actionmethodpage.EnterText(ORDER_WINDOW_SL_FIELD, finalPriceString);
                                                String actualPrice = page.locator(ORDER_WINDOW_SL_FIELD)
                                                                .getAttribute("value").trim();
                                                softAssert.assertEquals(actualPrice, finalPriceString,
                                                                "Price check failed. Expected: " + finalPriceString
                                                                                + ", Actual: " + actualPrice);
                                                node.info("Entered  Price is:" + finalPriceString);
                                                // SET TARGET
                                                Double Tprice_value = orderWindowpage.calculateDiscountedPrice(1.04);
                                                String TfinalPriceString = String.format("%.1f", Tprice_value);
                                                actionmethodpage.clickMethod(ORDER_WINDOW_TL_FIELD);
                                                actionmethodpage.EnterText(ORDER_WINDOW_TL_FIELD, TfinalPriceString);
                                                String TactualPrice = page.locator(ORDER_WINDOW_TL_FIELD)
                                                                .getAttribute("value").trim();
                                                softAssert.assertEquals(TactualPrice, TfinalPriceString,
                                                                "Price check failed. Expected: " + TfinalPriceString
                                                                                + ", Actual: " + TactualPrice);
                                                node.info("Entered  Price is:" + TfinalPriceString);

                                        } else if (OrderSide.equalsIgnoreCase("Sell")) {
                                                // Set STOP LOSS
                                                Double price_value = orderWindowpage.calculateDiscountedPrice(1.04);
                                                String finalPriceString = String.format("%.1f", price_value);
                                                actionmethodpage.EnterText(ORDER_WINDOW_SL_FIELD, finalPriceString);
                                                String actualPrice = page.locator(ORDER_WINDOW_SL_FIELD)
                                                                .getAttribute("value").trim();
                                                softAssert.assertEquals(actualPrice, finalPriceString,
                                                                "Price check failed. Expected: " + finalPriceString
                                                                                + ", Actual: " + actualPrice);
                                                node.info("Entered  Price is:" + finalPriceString);

                                                // SET TARGET

                                                Double Tprice_value = orderWindowpage.calculateDiscountedPrice(0.97);
                                                String TfinalPriceString = String.format("%.1f", Tprice_value);
                                                actionmethodpage.EnterText(ORDER_WINDOW_TL_FIELD, TfinalPriceString);
                                                String TactualPrice = page.locator(ORDER_WINDOW_TL_FIELD)
                                                                .getAttribute("value").trim();
                                                softAssert.assertEquals(TactualPrice, TfinalPriceString,
                                                                "Price check failed. Expected: " + TfinalPriceString
                                                                                + ", Actual: " + TactualPrice);
                                                node.info("Entered  Price is:" + TfinalPriceString);

                                        }

                                }
                        }

                }

                if (newQuantity != null) {
                        actionmethodpage.EnterText(ORDER_QUANTITY_FIELD, newQuantity);
                        String actualQty = page.locator(ORDER_QUANTITY_FIELD).getAttribute("value").trim();
                        softAssert.assertEquals(actualQty, newQuantity,
                                        "Quantity check failed. Expected: " + newQuantity + ", Actual: " + actualQty);
                        node.info("Entered Quantity is: " + newQuantity);
                }

                // --- Step 2: Enter Price (Limit Price) (and assert) ---
                if (newPrice != null) {
                        Double price_value = orderWindowpage.calculateDiscountedPrice(newPrice);
                        String finalPriceString = String.format("%.1f", price_value);
                        actionmethodpage.EnterText(ORDER_PRICE_FIELD, finalPriceString);
                        String actualPrice = page.locator(ORDER_PRICE_FIELD).getAttribute("value").trim();
                        softAssert.assertEquals(actualPrice, finalPriceString,
                                        "Price check failed. Expected: " + finalPriceString + ", Actual: "
                                                        + actualPrice);
                        node.info("Entered  Price is:" + finalPriceString);
                }

                // --- Step 3: Enter Trigger Price (and assert) ---
                if (newTriggerPrice != null) {
                        System.out.println("Under newtriggerprice block");
                        Double targetprice_value = orderWindowpage
                                        .calculateTrgDiscountedPrice(Double.valueOf(newTriggerPrice));
                        String finaltargetprice = String.format("%.1f", targetprice_value);
                        // actionmethodpage.clickMethod(ORDER_WINDOW_TARGETPRICE_TEXT);
                        actionmethodpage.EnterText(ORDER_WINDOW_TARGETPRICE_FIELD, finaltargetprice);
                        String actualTriggerPrice = page.locator(ORDER_WINDOW_TARGETPRICE_FIELD).getAttribute("value")
                                        .trim();
                        softAssert.assertEquals(actualTriggerPrice, finaltargetprice,
                                        "Trigger Price check failed. Expected: " + finaltargetprice + ", Actual: "
                                                        + actualTriggerPrice);
                        node.info("Trigger Price is: " + finaltargetprice);
                }

                /**
                 * // --- Step 4: Modify Target Price (and assert) ---
                 * if (newTargetPrice != null && !newTargetPrice.trim().isEmpty()) {
                 * actionmethodpage.EnterText(MODIFY_TARGET_PRICE_FIELD, newTargetPrice);
                 * String actualTargetPrice =
                 * page.locator(MODIFY_TARGET_PRICE_FIELD).getAttribute("value").trim();
                 * softAssert.assertEquals(actualTargetPrice, newTargetPrice, "Target Price
                 * check failed. Expected: " + newTargetPrice + ", Actual: " +
                 * actualTargetPrice);
                 * node.info("Modified Target Price to: **" + newTargetPrice + "** (Asserted)");
                 * }
                 * 
                 * // --- Step 5: Modify Stop Loss Price (and assert) ---
                 * if (newStopLossPrice != null && !newStopLossPrice.trim().isEmpty()) {
                 * actionmethodpage.EnterText(MODIFY_STOP_LOSS_PRICE_FIELD, newStopLossPrice);
                 * String actualStopLossPrice =
                 * page.locator(MODIFY_STOP_LOSS_PRICE_FIELD).getAttribute("value").trim();
                 * softAssert.assertEquals(actualStopLossPrice, newStopLossPrice, "Stop Loss
                 * Price check failed. Expected: " + newStopLossPrice + ", Actual: " +
                 * actualStopLossPrice);
                 * node.info("Modified Stop Loss Price to: **" + newStopLossPrice + "**
                 * (Asserted)");
                 * }
                 */

                // --- Step 6: Confirm Modification ---
                // List<Locator> possibleLocators = Arrays.asList(
                // page.locator(NORMAL_ORDER_PLACE_NOTIFICATION), // Replace with your actual
                // locator
                // // variables/strings
                // page.locator(NORMAL_ORDER_MODIFY_NOTIFICATION));

                // boolean isElementVisible = false;

                // 2. Perform the click action to trigger the modification

                if (OrderSide.equalsIgnoreCase("Buy")) {
                        if (product != null && product.equalsIgnoreCase("Pay Later")) {
                                // If it's a Pay Later Buy order, click the specific "Buy With MTF" button
                                actionmethodpage.clickMethod(ORDER_WINDOW_PAYLATER_BUTTON);
                        } else {
                                // Otherwise, click the standard Buy button
                                actionmethodpage.clickMethod(ORDER_WINDOW_BUY_NOW_BUTTON);
                        }

                } else if (OrderSide.equalsIgnoreCase("Sell")) {
                        // Click the standard Sell button for Sell orders (Standard and Pay Later
                        if (product != null && product.equalsIgnoreCase("Pay Later")) {
                                // If it's a Pay Later Buy order, click the specific "Buy With MTF" button
                                actionmethodpage.clickMethod(ORDER_WINDOW_SELL_PAYLATER);
                        } else {
                                // Otherwise, click the standard Buy button
                                actionmethodpage.clickMethod(ORDER_WINDOW_SELL_NOW_BUTTON);
                        }
                }

                // if (OrderSide.equalsIgnoreCase("Buy")) {

                // if (product != null && product.equalsIgnoreCase("Pay Later")) {
                // actionmethodpage.clickMethod(ORDER_WINDOW_PAYLATER);
                // } else {
                // actionmethodpage.clickMethod(ORDER_WINDOW_BUY_NOW_BUTTON);
                // }

                // }
                // if (OrderSide.equalsIgnoreCase("Buy")) {
                // actionmethodpage.clickMethod(ORDER_WINDOW_BUY_NOW_BUTTON);
                // }
                // if (OrderSide.equalsIgnoreCase("Sell")) {
                // actionmethodpage.clickMethod(ORDER_WINDOW_SELL_NOW_BUTTON);
                // }

                // if (product != null && product.equalsIgnoreCase("Pay Later")) {
                // actionmethodpage.clickMethod(ORDER_WINDOW_SELL_PAYLATER);
                // } else {
                // actionmethodpage.clickMethod(ORDER_WINDOW_SELL_NOW_BUTTON);
                // }
                // }

                // Click on Proceed Button and handle Notifications dynamically
                Locator proceedBtn = page.locator(PROCEED_BUTTON);
                long startTime = System.currentTimeMillis();
                long timeoutMs = 8000; // 8 seconds total waiting time
                boolean proceedClicked = false;

                // while (System.currentTimeMillis() - startTime < timeoutMs) {
                // // 1. Check if the Proceed button is visible, and click it if not already
                // // clicked
                // if (!proceedClicked && proceedBtn.isVisible(new
                // Locator.IsVisibleOptions().setTimeout(100))) {
                // proceedBtn.click();
                // proceedClicked = true;
                // System.out.println("Clicked on Proceed button");
                // node.info("Clicked on Proceed button");
                // }

                // // 2. Quickly check if any confirmation toast/notification is visible
                // for (Locator locator : possibleLocators) {
                // if (locator.isVisible(new Locator.IsVisibleOptions().setTimeout(100))) {
                // isElementVisible = true;
                // System.out.println("PASS: Confirmation element found with locator: "
                // + locator.toString());
                // break;
                // }
                // }

                // // 3. If notification found, break out of loop early!
                // if (isElementVisible) {
                // break;
                // }
                // }
                // 4. Final Assertion and Extent Report Logging
                // softAssert.assertTrue(isElementVisible, "None of the expected confirmation
                // elements became visible.");

                // if (isElementVisible) {
                // node.pass("Order Placed confirmed successfully.");
                // } else {
                // node.fail("Order place confirmation failed: None of the expected elements
                // were visible.");
                // }
        }

        // private void executeRegularLimitOrderPlacement(ExtentTest parentTest,String
        // Testname,Double Discount,String Stockname,Double Targetprice, Double
        // Stoplossprice) throws InterruptedException {
        // ExtentTest node = parentTest.createNode( Testname);
        // node.assignCategory("Place Order");
        // orderWindowpage.addInstrumentToWatchlistAndOpenOrderWindow(Stockname);
        // Double price_value=orderWindowpage.calculateDiscountedPrice(Discount);
        // // Note: Formatting to one decimal place may still cause issues if the UI
        // expects two.
        // String finalPriceString = String.format("%.1f", price_value);
        // actionmethodpage.clickMethod(ORDER_PRICE_FIELD);
        // actionmethodpage.EnterText(ORDER_PRICE_FIELD, finalPriceString);
        // actionmethodpage.clickMethod(PRICE_CHECKBOX);
        // actionmethodpage.clickMethod(PRICE_CHECKBOX);
        // String initialPrice=
        // page.locator(ORDER_PRICE_FIELD).getAttribute("value").trim();
        // softAssert.assertEquals(finalPriceString,initialPrice,"The Value Entered by
        // user is not same");
        // actionmethodpage.clickMethod(ORDER_WINDOW_BUY_NOW_BUTTON);
        // }

        // @Test(priority = 1)
        // public void testModifyExistingOrder() {
        // ExtentTest test = extent.createTest("Order Place").assignAuthor("Modify
        // Order");
        // actionmethodpage.clickMethod(ORDERS_TAB);

        // // 1. Locate the pending order and click the Info icon to open the
        // modification
        // // window.
        // orderWindowpage.hoverAndClickOnModifyButton(Stock_idea);

        // // 2. Call the new helper method to modify the order (e.g., changing only
        // // Quantity and Price)
        // executeOrderModification(test, "Modify Qty ", "2", null, null, null, null);
        // orderWindowpage.hoverAndClickOnModifyButton(Stock_idea);
        // String newprice =
        // String.valueOf(orderWindowpage.calculateDiscountedPrice(1.02));
        // executeOrderModification(test, "Modify Price ", null, newprice, null, null,
        // null);
        // }

        // private void executeOrderModification(
        // ExtentTest parentTest, String Testname, String newQuantity,
        // String newPrice, String newTriggerPrice, String newTargetPrice,
        // String newStopLossPrice) {

        // ExtentTest node1 = parentTest.createNode(Testname);
        // node1.assignCategory("Modify Order");

        // // --- Step 1: Modify Quantity (and assert) ---
        // if (newQuantity != null && !newQuantity.trim().isEmpty()) {
        // actionmethodpage.EnterText(ORDER_QUANTITY_FIELD, newQuantity);
        // String actualQty =
        // page.locator(ORDER_QUANTITY_FIELD).getAttribute("value").trim();
        // softAssert.assertEquals(actualQty, newQuantity,
        // "Quantity check failed. Expected: " + newQuantity + ", Actual: " +
        // actualQty);
        // node1.info("Modified Quantity to: " + newQuantity);
        // }

        // // --- Step 2: Modify Price (Limit Price) (and assert) ---
        // if (newPrice != null) {
        // actionmethodpage.EnterText(ORDER_PRICE_FIELD, String.valueOf(newPrice));
        // String actualPrice =
        // page.locator(ORDER_PRICE_FIELD).getAttribute("value").trim();
        // softAssert.assertEquals(actualPrice, newPrice,
        // "Price check failed. Expected: " + newPrice + ", Actual: " + actualPrice);
        // node1.info("Modified Price to:" + newPrice);
        // }
        /**
         * // --- Step 3: Modify Trigger Price (and assert) ---
         * if (newTriggerPrice != null && !newTriggerPrice.trim().isEmpty()) {
         * actionmethodpage.EnterText(MODIFY_TRIGGER_PRICE_FIELD, newTriggerPrice);
         * String actualTriggerPrice =
         * page.locator(MODIFY_TRIGGER_PRICE_FIELD).getAttribute("value").trim();
         * softAssert.assertEquals(actualTriggerPrice, newTriggerPrice, "Trigger Price
         * check failed. Expected: " + newTriggerPrice + ", Actual: " +
         * actualTriggerPrice);
         * node.info("Modified Trigger Price to: **" + newTriggerPrice + "**
         * (Asserted)");
         * }
         * 
         * // --- Step 4: Modify Target Price (and assert) ---
         * if (newTargetPrice != null && !newTargetPrice.trim().isEmpty()) {
         * actionmethodpage.EnterText(MODIFY_TARGET_PRICE_FIELD, newTargetPrice);
         * String actualTargetPrice =
         * page.locator(MODIFY_TARGET_PRICE_FIELD).getAttribute("value").trim();
         * softAssert.assertEquals(actualTargetPrice, newTargetPrice, "Target Price
         * check failed. Expected: " + newTargetPrice + ", Actual: " +
         * actualTargetPrice);
         * node.info("Modified Target Price to: **" + newTargetPrice + "** (Asserted)");
         * }
         * 
         * // --- Step 5: Modify Stop Loss Price (and assert) ---
         * if (newStopLossPrice != null && !newStopLossPrice.trim().isEmpty()) {
         * actionmethodpage.EnterText(MODIFY_STOP_LOSS_PRICE_FIELD, newStopLossPrice);
         * String actualStopLossPrice =
         * page.locator(MODIFY_STOP_LOSS_PRICE_FIELD).getAttribute("value").trim();
         * softAssert.assertEquals(actualStopLossPrice, newStopLossPrice, "Stop Loss
         * Price check failed. Expected: " + newStopLossPrice + ", Actual: " +
         * actualStopLossPrice);
         * node.info("Modified Stop Loss Price to: **" + newStopLossPrice + "**
         * (Asserted)");
         * }
         */
        // --- Step 6: Confirm Modification ---
        // List<Locator> possibleLocators1 = Arrays.asList(
        // page.locator(AMO_ORDER_MODIFY_NOTIFICATION), // Replace with your actual
        // locator
        // // variables/strings
        // page.locator(NORMAL_ORDER_MODIFY_NOTIFICATION));

        // boolean isElementVisible1 = false;

        // // 2. Perform the click action to trigger the modification
        // actionmethodpage.clickMethod(ORDER_MODIFY_NOW_BUTTON);

        // // 3. Loop and check for visibility dynamically to avoid sequential timeouts
        // long startTime1 = System.currentTimeMillis();
        // long timeoutMs1 = 8000; // 8 seconds total waiting time

        // while (System.currentTimeMillis() - startTime1 < timeoutMs1) {
        // for (Locator locator : possibleLocators1) {
        // if (locator.isVisible(new Locator.IsVisibleOptions().setTimeout(100))) {
        // isElementVisible1 = true;
        // System.out.println("PASS: Confirmation element found with locator: "
        // + locator.toString());
        // break;
        // }
        // }
        // if (isElementVisible1) {
        // break;
        // }
        // }
        // // 4. Final Assertion and Extent Report Logging
        // softAssert.assertTrue(isElementVisible1, "None of the expected confirmation
        // elements became visible.");

        // if (isElementVisible1) {
        // node1.pass("Modification confirmed successfully.");
        // } else {
        // node1.fail("Modification confirmation failed: None of the expected elements
        // were visible.");
        // }

        // }

}
