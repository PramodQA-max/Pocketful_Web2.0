package listener;

import com.microsoft.playwright.Page;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import util.Base;
import util.Constant;
import util.ExtentManager;

import java.io.File;
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
            String directoryPath = "src/main/resources/Screenshot/";
            String filePath = directoryPath + fileName;

            // Use absolute path for saving but relative/absolute for Extent Report
            java.io.File directory = new java.io.File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            try {
                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get(filePath))
                        .setFullPage(true));
                
                System.out.println("✅ Screenshot saved: " + filePath);
                
                // Add screenshot to Extent Report
                ExtentTest test = ExtentManager.getTest();
                if (test != null) {
                    java.io.File screenshotFile = new java.io.File(filePath);
                    String absPath = screenshotFile.getAbsolutePath();
                    test.fail("Test failed. Screenshot: ", 
                        MediaEntityBuilder.createScreenCaptureFromPath(absPath).build());
                } else {
                    System.out.println("⚠️ ExtentTest instance not found in ExtentManager.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onFinish(ITestContext context) {
        System.out.println("🏁 Test Suite Execution Finished. Flushing Extent Report...");
        ExtentManager.getInstance().flush();
        
        String reportPath = "src/main/java/reports/Pocketful_Auto_report.html";
        System.out.println("📤 Uploading report to S3: " + reportPath);
        
        try {
            String s3FileName = Base.uploadReportToS3(reportPath);
            if (s3FileName != null) {
                String s3Url = "https://s3.console.aws.amazon.com/s3/object/" + "extent-report-container" + "?region=" + "ap-south-1" + "&prefix=" + s3FileName;
                String publicUrl = "https://" + "extent-report-container" + ".s3." + "ap-south-1" + ".amazonaws.com/" + s3FileName;
                
                System.out.println("✅ Report uploaded successfully to S3 as: " + s3FileName);
                System.out.println("🔗 Console Link: " + s3Url);
                System.out.println("🔗 Public Link: " + publicUrl);
                
                // If you have a discord webhook, you can call this:
                 String discordWebhookUrl = Constant.discordWebhookUrl;
                 Base.sendTestReportToDiscord(discordWebhookUrl, s3FileName);
            } else {
                System.err.println("❌ S3 Upload failed (returned null filename).");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Failed to upload report to S3: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
