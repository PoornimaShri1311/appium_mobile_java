package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentManager {

    private static ExtentReports extent;

    public synchronized static ExtentReports getExtentReports() {
        if (extent == null) {
            // Create timestamp for unique report names
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String reportPath = "reports/ExtentReport_" + timestamp + ".html";
            
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle(FrameworkConfig.getFrameworkName() + " - Test Report");
            sparkReporter.config().setReportName("Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            // Add system and framework information to the report
            extent.setSystemInfo("Framework", FrameworkConfig.getFrameworkName());
            extent.setSystemInfo("Framework Version", FrameworkConfig.getFrameworkVersion());
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("OS Version", System.getProperty("os.version"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("User Name", System.getProperty("user.name"));
            extent.setSystemInfo("Execution Date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        return extent;
    }
}
