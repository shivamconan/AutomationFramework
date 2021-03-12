package utility;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import constants.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
    Log levels and their messages -
    Steps - INFO
 */

public class LogUtility {

    private Logger logger;
    private ExtentReports extent;
    private ExtentTest extentlogger;

    public LogUtility(Class className) {
        logger = Logger.getLogger(className);
        logger.getLevel();
    }

    public void setExtentLogger(ExtentTest extentlogger) {
        this.extentlogger = extentlogger;
    }

    public void logStep(String stepMessage) {
        logger.info(stepMessage);
        Reporter.log("<br><h3>" + stepMessage + "</h3></br>");
        extentlogger.log(LogStatus.INFO, stepMessage);
    }

    public void logWarning(String warningMessage) {
        logger.warn(warningMessage);
        Reporter.log("<br><h4>" + warningMessage + "</br></h4>");
    }

    public void logTestTitle(String titleMessage) {
        logger.info(titleMessage);
        Reporter.log("<br><h2>" + titleMessage + "</h2></br>");
        extentlogger.log(LogStatus.INFO, titleMessage);
    }

    public void logInfo(String infoMessage) {
        logger.info(infoMessage);
        Reporter.log("<br>" + infoMessage + "</br>");
    }

    public void logTestInfo(String testInfoMessage) {
        logger.info(testInfoMessage);
        Reporter.log("<br><h4>" + testInfoMessage + "</h4></br>");
        extentlogger.log(LogStatus.INFO, testInfoMessage);
    }

    public void logDebug(String debugMessage) {
        logger.debug(debugMessage);
        Reporter.log("<br>" + debugMessage + "</br>");
    }

    public void logException(String logException) {
        logger.error(logException);
        Reporter.log("<br>" + logException + "</br>");
        extentlogger.log(LogStatus.FAIL, logException);
    }

    public void logMessage(String message, boolean extentCheck) {
        logger.info(message);
        Reporter.log("<br>" + message + "</br>");
        if (extentCheck) {
            extentlogger.log(LogStatus.INFO, message);
        }
    }

    public void logSuccess(String message) {
        logger.info(message);
        Reporter.log("<br>" + message + "</br>");
        extentlogger.log(LogStatus.PASS, message);
    }

    public void logScreenshot(File screenshot, String testName) {
        try {
            String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
            String screenshotName = testName + "-" + dateName + ".png";
            String screenshotPath = Constants.TEST_OUTPUT + Constants.slash  + Constants.SCREENSHOT_FOLDER_NAME + Constants.slash + screenshotName;
            File finalDestination = new File(screenshotPath);
            FileUtils.copyFile(screenshot, finalDestination);
            logException("" + extentlogger.addScreenCapture(Constants.SCREENSHOT_FOLDER_NAME + Constants.slash + screenshotName));
        } catch (IOException e) {
           logException(ExceptionUtils.getStackTrace(e));
        }
    }

}
