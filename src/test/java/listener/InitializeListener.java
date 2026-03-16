package listener;

import com.microsoft.playwright.Page;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InitializeListener implements ITestListener {
    public static Page page;

//    @Override
//    public void onTestStart(ITestResult result) {
//        try {
//            // Get the Playwright Page instance from the test class
//            page = (Page) result.getTestClass().getRealClass()
//                    .getDeclaredField("page")
//                    .get(result.getInstance());
//        } catch (IllegalAccessException | NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (page != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String timestamp = LocalDateTime.now().format(formatter);

            String fileName = result.getName() + "_" + timestamp + ".png";
            String filePath = "src/main/resources/Screenshot" + fileName;

            try {
                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get(filePath))
                        .setFullPage(true));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // Optional: you can also take screenshot on success if needed
    }
}
