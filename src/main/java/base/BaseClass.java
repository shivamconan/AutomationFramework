package base;

import com.relevantcodes.extentreports.ExtentReports;
import constants.Constants;
import objects.Device;
import io.appium.java_client.AppiumDriver;
import library.AndroidSessionManager;
import library.IosSessionManager;
import library.SessionManager;
import model.TestConfiguration;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utility.*;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author shivam mishra
 */

/*
    This class contains all the setup and tear down methods
    It will interact with SessionManager objects to provide
    driver to the individual tests through inheritance
 */

public class BaseClass {

    protected SessionManager sessionManager;
    public AppiumDriver driver;
    private LogUtility logUtility = new LogUtility(BaseClass.class);
    private static ExtentReports extentReports;
    private static ConcurrentHashMap<String, Integer> suiteExecutionMetadata = new ConcurrentHashMap<String, Integer>();
    /*
         1) initializes the device map -
        Gets devices list from session manager object and prepares
         a map of the same marking each device as free initially.
         2) deletes old extent report if present.
     */

    @BeforeSuite
    public void setUpForSuite(ITestContext iTestContext) {
    	
    	YAMLFileUtility.convertYAMLToJava(System.getProperty("user.dir")+"/TestConfiguration.yaml", TestConfiguration.class);

    	sessionManager = initializeSessionManager();
        logUtility.logInfo("Preparing devices map");
        
        iTestContext.setAttribute("sessionManager", sessionManager);
        
        //DeviceUtility.initiazeDeviceMap(sessionManager.getExecutionDevicesList());
        //TODO check whether uninstall is required or not
        //sessionManager.getExecutionDevicesList().forEach(AndroidUtility::uninstallAppiumApps);
        sessionManager.deleteOldExtentReportIfPresent();

        sessionManager.initializeExtentReporter();
        extentReports = sessionManager.getExtentReporter().getExtent();
        
        suiteExecutionMetadata.put("total", 0);
        suiteExecutionMetadata.put("pass", 0);
        suiteExecutionMetadata.put("fail", 0);
        suiteExecutionMetadata.put("skip", 0);
    }

    @BeforeTest
    @Parameters("deviceName")
    public void setupDevice( ITestContext iTestContext, @Optional String deviceName) {
        //JavaUtility.randomSleep();

        //get a free device and book it
        //Device currentTestDevice = DeviceUtility.getAFreeDeviceAndBookIt();

        Map<String,String> device = TestConfiguration.getDevice();
        //Device currentTestDevice = sessionManager.getListOfDevicesFromDevicesJsonUsing("name", Arrays.asList(deviceName)).get(0);
        Device currentTestDevice = new Device(TestConfiguration.getPlatformOs(), device.get("name"), device.get("version"), device.get("udid"));

        //sessionManager = (SessionManager) iTestContext.getAttribute("sessionManager");
        sessionManager = initializeSessionManager();
        //initialize extent report
        sessionManager.initializeExtentReporter(extentReports, iTestContext.getCurrentXmlTest().getName());

        logUtility.setExtentLogger(sessionManager.getExtentReporter().getExtentlogger());

        //sets the udid of found free device on the respective session manager
        //logUtility.logTestInfo("Current Test Device- " + currentTestDevice.getDeviceName());
        sessionManager.setCurrentTestDevice(currentTestDevice);
        //initiates the driver on the respective session manager

    }

    /*
        1) Initializes extent report for particular test method.
        2) Gets a free device from devices map and books it for the current test.
        3) intitiate driver as per the environment specified (android/ios).
        4) calls initializeObjects method which initializes steps objects and extent logger in individual test

        if this is not synchornized, it wont work on ios
     */

    @BeforeClass
    public synchronized void setUpForTest(ITestContext iTestContext) {

        sessionManager.startAppiumService();
        logUtility.logInfo("Current Test Appium url- " + sessionManager.getAppiumService().getUrl());
        sessionManager.initiateDriver();
        driver = sessionManager.getDriver();

        //initialize steps class objects to be used in the current test class
        this.initializeTestObjects();

        logUtility.logTestInfo("Current test device - " + sessionManager.getCurrentTestDevice().getDeviceName());
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
    public void takesScreenshotIfFailed(ITestResult iTestResult) {
        try {
            // if(!(iTestResult.getAttributeNames().contains("isRetry"))) {
            if (!iTestResult.isSuccess()) {
                TakesScreenshot ts = (TakesScreenshot) sessionManager.getDriver();
                File source = ts.getScreenshotAs(OutputType.FILE);
                logUtility.logException(ExceptionUtils.getStackTrace(iTestResult.getThrowable()));
                logUtility.logScreenshot(source, iTestResult.getTestContext().getName());
            }
        }
            catch (Exception e) {
                logUtility.logDebug("There was a problem in executing tear down.");
                logUtility.logDebug(ExceptionUtils.getStackTrace(e));
            }
        }

        @AfterClass(alwaysRun = true)
        public void closeAppiumDriver() {
        try {
            sessionManager.endExtentReport();
            sessionManager.quitDriver();
            sessionManager.stopAppiumService();
        }
        catch(Exception e) {
            //continue
            }
         finally {
            DeviceUtility.freeTheDevice(sessionManager.getCurrentTestDevice());
        }

        }

        @AfterTest(alwaysRun=true)
        public void setTestsCount(ITestContext iTestContext)  {
        	suiteExecutionMetadata.replace("total",suiteExecutionMetadata.get("total")+iTestContext.getAllTestMethods().length);
        	suiteExecutionMetadata.replace("pass",suiteExecutionMetadata.get("pass")+iTestContext.getPassedTests().size());
        	suiteExecutionMetadata.replace("fail",suiteExecutionMetadata.get("fail")+iTestContext.getFailedTests().size());
        	suiteExecutionMetadata.replace("skip",suiteExecutionMetadata.get("skip")+iTestContext.getSkippedTests().size());
        }

    @AfterSuite(alwaysRun=true)
    public void setSuiteTestsCount()  {
        replaceInFile(Constants.EXTENT_JS_PATH, "totalTests = \\d+", "totalTests = " + suiteExecutionMetadata.get("total"));
        replaceInFile(Constants.EXTENT_JS_PATH, "passedTests = \\d+", "passedTests = " + suiteExecutionMetadata.get("pass"));
        replaceInFile(Constants.EXTENT_JS_PATH, "failedTests = \\d+", "failedTests = " + suiteExecutionMetadata.get("fail"));
        replaceInFile(Constants.EXTENT_JS_PATH, "skippedTests = \\d+", "skippedTests = " + suiteExecutionMetadata.get("skip"));
        replaceInFile(Constants.EXTENT_REPORTS_PATH, "<script src='https://cdn.rawgit.com/anshooarora/extentreports/6032d73243ba4fe4fb8769eb9c315d4fdf16fe68/cdn/extent.js'",
                "<script src='../../src/test/resources/extentReportJs.js'");
        logUtility.logDebug("TOTAL TESTS IN SUITE: "+suiteExecutionMetadata.get("total"));
        logUtility.logDebug("TOTAL PASSED IN SUITE: "+suiteExecutionMetadata.get("pass"));
        logUtility.logDebug("TOTAL FAILED IN SUITE: "+suiteExecutionMetadata.get("fail"));
        logUtility.logDebug("TOTAL SKIPPED IN SUITE: "+suiteExecutionMetadata.get("skip"));

    }

    public void replaceInFile(String fileName, String regex, String replaceWith) {

            try {
                StringBuilder bldr = new StringBuilder();
                String str;

                try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
                    while ((str = in.readLine()) != null) {
                        bldr.append(str);
                        bldr.append(System.lineSeparator());
                    }
                }
                String content = bldr.toString();
                Pattern pattern = Pattern.compile(regex);
                content = pattern.matcher(content).replaceAll(replaceWith);

                try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)))) {
                    bw.write(content);
                    bw.newLine();
                }
            }
            catch (IOException e) {
                logUtility.logException("Exception- " + e.getStackTrace());
            }

        }

    /*
        Returns respective Android or iOS Session Manager Objects
        based on environment specified
     */

    public SessionManager initializeSessionManager() {
        String platformOs = TestConfiguration.getPlatformOs();
        logUtility.logInfo("Platform os: "+platformOs);
        if (platformOs.equalsIgnoreCase("android")) {
            return new AndroidSessionManager();
        } else {
            return new IosSessionManager();
        }
    }

}
