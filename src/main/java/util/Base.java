package util;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.ReportStats;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Base {
    protected static String bucketName;
    protected static String awsAccessKey;
    protected static String awsSecretKey;
    protected static String REGION;
    protected static LocalDateTime currentDateTime = LocalDateTime.now();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
    static String formattedDateTime = currentDateTime.format(formatter);
    static List<String[]> combinedArray = new ArrayList<>();
    static String[] prevArray = new String[6];

    public static void authlogTestSummary(ExtentReports extent, String apiName) {
        ReportStats stats = extent.getStats();

        Map<Status, Long> childStats = stats.getChild();

        long passCount = childStats.getOrDefault(Status.PASS, 0L);
        long failCount = childStats.getOrDefault(Status.FAIL, 0L);
        long skipCount = childStats.getOrDefault(Status.SKIP, 0L);
        long warningCount = childStats.getOrDefault(Status.WARNING, 0L);
        long totalCount = passCount + failCount + skipCount + warningCount;

        String[] testSummaryArray = new String[6];
        testSummaryArray[0] = apiName;
        testSummaryArray[1] = String.valueOf(totalCount);
        testSummaryArray[2] = String.valueOf(passCount);
        testSummaryArray[3] = String.valueOf(failCount);
        testSummaryArray[4] = String.valueOf(skipCount);
        testSummaryArray[5] = String.valueOf(warningCount);

        testSummaryFunc(testSummaryArray);
    }
    public static void testSummaryFunc(String[] testSummaryvector) {
        appendToVector(combinedArray, testSummaryvector);
    }

    public static void appendToVector(List<String[]> vector, String[] newElement) {
        if (vector.isEmpty()) {
            prevArray = newElement;
            vector.add(newElement);
        } else {
            String[] currArray = new String[6];
            for (int i = 0; i < newElement.length; i++) {
                if (i == 0) {
                    currArray[i] = newElement[i];
                } else {
                    currArray[i] = String.valueOf(Integer.parseInt(newElement[i]) - Integer.parseInt(prevArray[i]));
                }
            }
            prevArray = newElement;
            vector.add(currArray);
        }
    }

    public static String uploadReportToS3(String sourcePath) {
        readSecretFile("src/test/java/DaoAPI/jsonConfig/config.json");

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);

        // Create S3 client
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(REGION)
                .build();

        // File to upload
        File reportFile = new File(sourcePath);
        String fileName = reportFile.getName();
        fileName = fileName.replace(".html", "") + formattedDateTime + ".html";


        // Upload file to S3
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, reportFile));
        return fileName;
    }

    protected static void readSecretFile(String filePath) {
        try {
            FileReader reader = new FileReader(filePath);
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonObject secretObject = jsonObject.getAsJsonObject("secret");

            bucketName = secretObject.get("bucketName").getAsString();
            awsAccessKey = secretObject.get("awsAccessKey").getAsString();
            awsSecretKey = secretObject.get("awsSecretKey").getAsString();
            REGION = secretObject.get("region").getAsString();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static void deleteReportFromS3() {
        try {
            // Specify your AWS credentials
            String configFilePath = "src/test/java/DaoAPI/jsonConfig/config.json";
            readSecretFile(configFilePath);

            // Initialize S3 client with your credentials
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);

            // Create S3 client
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(REGION)
                    .build();

            LocalDate sevenDayBefore = LocalDate.now().minusDays(6);
            Date sevenDayBeforeDate = Date.from(sevenDayBefore.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // List objects in the bucket
            ObjectListing objectListing = s3Client.listObjects(bucketName);
            List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();

            for (S3ObjectSummary objectSummary : objectSummaries) {
                String key = objectSummary.getKey();
                if (key.endsWith(".html")) {
                    Date lastModifiedReportDate = objectSummary.getLastModified();
                    if (sevenDayBeforeDate.compareTo(lastModifiedReportDate) >= 0) {
                        // Delete the object
                        s3Client.deleteObject(bucketName, key);
                    }
                }
            }
        } catch (Exception e) {
//            logger.log(Level.SEVERE, "An error occurred while deleting reports from S3", e);
        }
    }


    public static void sendTestReportToDiscord(String discordWebhookUrl, String sourceFileName) throws IOException {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
    
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("**Test Report for DAO API Automation** - ").append(formattedDateTime).append("\\n\\n");
    
        for (String[] array : combinedArray) {
            String apiTest = array[0];
            String totalTest = array[1];
            String passTest = array[2];
            String failTest = array[3];
            String skipTest = array[4];
            String warningTest = array[5];
    
            contentBuilder.append("**API Test Name:** ").append(apiTest).append("\\n")
                          .append("**Total Test:** ").append(totalTest).append("\\n")
                          .append("**Pass Test:** ").append(passTest).append("\\n")
                          .append("**Fail Test:** ").append(failTest).append("\\n")
                          .append("**Skip Test:** ").append(skipTest).append("\\n")
                          .append("**Warnings:** ").append(warningTest).append("\\n\\n");
    
            // If the content length exceeds 1800 characters, send the message
            if (contentBuilder.length() > 1800) {
                sendToDiscordInChunks(discordWebhookUrl, contentBuilder.toString());
                contentBuilder.setLength(0); // Reset the content builder for the next chunk
            }
        }
    
        // Add the report link at the end
        String s3HtmlFileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + sourceFileName;
        contentBuilder.append("[**DAO API Automation Report Link**](").append(s3HtmlFileUrl).append(")");
    
        // Send any remaining content in the contentBuilder
        if (contentBuilder.length() > 0) {
            sendToDiscordInChunks(discordWebhookUrl, contentBuilder.toString());
        }
    }
    
    public static void sendToDiscordInChunks(String discordWebhookUrl, String content) throws IOException {
        String payload = "{\"content\": \"" + content + "\"}";
    
        // Send the request to Discord
        URL url = new URL(discordWebhookUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
    
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = payload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
    
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
            System.out.println("Message sent successfully to Discord.");
        } else {
            System.out.println("Failed to send message. Response code: " + responseCode);
        }
    }
    

}
