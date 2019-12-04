package base;

import com.relevantcodes.extentreports.ExtentReports;
import devices.Device;
import io.appium.java_client.AppiumDriver;
import library.AndroidSessionManager;
import library.IosSessionManager;
import library.SessionManager;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utility.*;

import java.io.File;

/**
 * @author shivam mishra
 */

/*
    This class contains all the setup and tear down methods
    It will interact with SessionManager objects to provide
    driver to the individual tests through inheritance
 */

public class BaseClass {

    protected SessionManager sessionManager = initializeSessionManager();
    public AppiumDriver driver;
    private LogUtility logUtility = new LogUtility(BaseClass.class);
    private DBUtility dbUtility;
    private static ExtentReports extentReports;

    /*
         1) initializes the device map -
        Gets devices list from session manager object and prepares
         a map of the same marking each device as free initially.
         2) deletes old extent report if present.
     */

    @BeforeSuite
    public void setUpForSuite() {
        logUtility.logInfo("Preparing devices map");
        DeviceUtility.initiazeDeviceMap(sessionManager.getExecutionDevicesList());
        //TODO check whether uninstall is required or not
        //sessionManager.getExecutionDevicesList().forEach(AndroidUtility::uninstallAppiumApps);
        sessionManager.deleteOldExtentReportIfPresent();

        sessionManager.initializeExtentReporter();
        extentReports = sessionManager.getExtentReporter().getExtent();
    }


    /*
        1) Initializes extent report for particular test method.
        2) Gets a free device from devices map and books it for the current test.
        3) intitiate driver as per the environment specified (android/ios).
        4) calls initializeObjects method which initializes steps objects and extent logger in individual test

        if this is not synchornized, it wont work on ios
     */


    @BeforeMethod
    public synchronized void setUpForTest(ITestContext iTestContext) {

        sessionManager = initializeSessionManager();
        JavaUtility.randomSleep();
        //get a free device and book it
        Device currentTestDevice = DeviceUtility.getAFreeDeviceAndBookIt();
        //initialize extent report
        sessionManager.initializeExtentReporter(extentReports, iTestContext.getCurrentXmlTest().getName());

        logUtility.setExtentLogger(sessionManager.getExtentReporter().getExtentlogger());
        iTestContext.setAttribute("sessionManager", sessionManager);

        //sets the udid of found free device on the respective session manager
        logUtility.logTestInfo("Current Test Device- " + currentTestDevice.getDeviceName());
        sessionManager.setCurrentTestDevice(currentTestDevice);
        //initiates the driver on the respective session manager
        sessionManager.startAppiumService();
        logUtility.logInfo("Current Test Appium url- " + sessionManager.getAppiumService().getUrl());
        sessionManager.initiateDriver();
        driver = sessionManager.getDriver();

        //initialize steps class objects to be used in the current test class
        this.initializeTestObjects();

    }

    /*
    Will initialize Steps Class objects for each functionality with driver.
    Code to initialize respective Steps objects implemented in respective Test Class implementations
    For every test case only those step classes will be initialized which are actually used in the test case.
     */

    public void initializeTestObjects() {
    }

    /*
        tearDown method
     */

    @AfterMethod(alwaysRun = true)
    public void closeAppiumDriver(ITestResult iTestResult) {
        try {
            if(!(iTestResult.getAttributeNames().contains("isRetry"))) {
                if(!iTestResult.isSuccess()) {
                    TakesScreenshot ts = (TakesScreenshot) sessionManager.getDriver();
                    File source = ts.getScreenshotAs(OutputType.FILE);
                    logUtility.logException(ExceptionUtils.getStackTrace(iTestResult.getThrowable()));
                    logUtility.logScreenshot(source, iTestResult.getTestContext().getName());
                }
                else {
                    logUtility.logSuccess("Test Case has been passed.");
                }
                sessionManager.endExtentReport();
            }

            sessionManager.quitDriver();
            sessionManager.stopAppiumService();
        }
        catch (Exception e) {
            logUtility.logDebug("There was a problem in executing tear down.");
            logUtility.logDebug(ExceptionUtils.getStackTrace(e));
        }
        finally {
            DeviceUtility.freeTheDevice(sessionManager.getCurrentTestDevice());
        }

    }

    /*
        Returns respective Android or iOS Session Manager Objects
        based on environment specified
     */

    public SessionManager initializeSessionManager() {
        String platformOs = EnvironmentParameters.getPlatformOs();
        if (platformOs.equalsIgnoreCase("android")) {
            return new AndroidSessionManager();
        } else {
            return new IosSessionManager();
        }
    }

}
