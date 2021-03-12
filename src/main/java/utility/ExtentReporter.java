package utility;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import constants.Constants;

import java.io.File;

public class ExtentReporter {

    private ExtentReports extent;
    private ExtentTest extentlogger;

    public ExtentReporter() {
        extent = new ExtentReports(Constants.EXTENT_REPORTS_PATH, false);
        extent.addSystemInfo("Host Name", "Master automation framework")
                .addSystemInfo("Environment", "Automation Testing")
                .addSystemInfo("User Name", "Automation");
        extent.loadConfig(new File(Constants.EXTENT_CONFIG_PATH));
    }

    public ExtentReporter(ExtentReports extent) {
        this.extent = extent;
    }

    public void startLogger(String testName) {
        extentlogger = extent.startTest(testName);
    }

    public ExtentReports getExtent() {
        return extent;
    }

    public void setExtent(ExtentReports extent) {
        this.extent = extent;
    }

    public ExtentTest getExtentlogger() {
        return extentlogger;
    }

    public void endExtentReport() {
        extent.endTest(extentlogger);
        extent.flush();
    }

    public void closeExtentReport() {
        extent.close();
    }

}
