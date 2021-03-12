package library;

import com.relevantcodes.extentreports.ExtentReports;
import constants.Constants;
import objects.Device;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.AndroidServerFlag;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import model.TestConfiguration;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;
import utility.*;

import java.io.File;
import java.net.ServerSocket;
import java.util.*;

/**
 * @author shivam mishra
 */

/*
    Contains methods required to initiate and maintain a test session.
    Respective implementations specific to android or ios are written in their
    respective child classes. All the methods common like startAppiumService()
    is written in this parent class itself
 */

public abstract class SessionManager {

    public AppiumDriverLocalService appiumLocalService = null;
    public Device device = null;

    public abstract AppiumDriver getDriver();

    private LogUtility logUtility = new LogUtility(SessionManager.class);

    private ExtentReporter extentReporter;

    /*
        Initiate driver based on environment specified subject to free device availablity.
        If specified android, will create a Android Driver otherwise IosDriver
        Code for each is written in respective child classes
     */


    public abstract void initiateDriver();

    public abstract List<Device> getExecutionDevicesList();

    public abstract void quitDriver();

    public abstract void setCurrentTestDevice(Device device);

    public abstract Device getCurrentTestDevice();

    public void deleteOldExtentReportIfPresent()
    {
        FileUtility.deleteOldExtentReportIfPresent();
    }

    /* will intialize only ExtentReports Object
      ExtentTest needs to be initialized for each test method separately
     */
    public void initializeExtentReporter() {
        extentReporter = new ExtentReporter();
    }

    public void initializeExtentReporter(ExtentReports extent, String testName) {
        extentReporter = new ExtentReporter(extent);
        extentReporter.startLogger(testName);
    }

    public ExtentReporter getExtentReporter() {
        return extentReporter;
    }

    public void setExtentReporter(ExtentReporter extentReporter) {
        this.extentReporter = extentReporter;
    }

    public void endExtentReport() {
        extentReporter.endExtentReport();
    }

    public void closeExtentReport() {
        FileUtility fileUtility = new FileUtility(this);
        //fileUtility.copyReportsFromTargetToTestOutput();
        extentReporter.closeExtentReport();
    }

    //TODO Get android version & device name automatically from adb
    public List<Device> getListOfDevicesFromDevicesJsonUsing(String parameter, List<String> devicesParametersList) {
        String platformOs = TestConfiguration.getPlatformOs();
        JSONObject devicesJsonFile = JSONFileUtility.getJsonFileAsJsonObject(Constants.DEVICES_JSON_PATH);
        JSONArray platformDevicesArray = (JSONArray) devicesJsonFile.get(platformOs);

        List<Device> devices = new ArrayList<Device>();
        for (int i = 0; i < platformDevicesArray.size(); i++) {
            JSONObject deviceJSON = (JSONObject) platformDevicesArray.get(i);
            if (devicesParametersList.contains(deviceJSON.get(parameter).toString())) {
                String deviceName = (String) deviceJSON.get("name");
                String deviceOsVersion = (String) deviceJSON.get("version");
                String deviceUdid = (String) deviceJSON.get("udid");
                devices.add(new Device(platformOs, deviceName, deviceOsVersion, deviceUdid));
            }
        }
        return devices;
    }

//        return devicesParametersList.stream().map(deviceParameterValue -> {
//            JSONObject devicesJsonFile = JSONFileUtility.getJsonFileAsJsonObject(Constants.DEVICES_JSON_PATH);
//            JSONArray platformDevicesArray = (JSONArray) devicesJsonFile.get(platformOs);
//            JSONObject deviceFoundFromJsonFile = (JSONObject) platformDevicesArray.stream().filter(deviceFromJson ->
//                    ((JSONObject) deviceFromJson).get(parameter).toString().equalsIgnoreCase(deviceParameterValue)).findFirst().get();
//            String deviceName = (String) deviceFoundFromJsonFile.get("name");
//            String deviceOsVersion = (String) deviceFoundFromJsonFile.get("version");
//            String deviceUdid = (String) deviceFoundFromJsonFile.get("udid");
//            return new Device(platformOs, deviceName, deviceOsVersion, deviceUdid);
//        }).collect(Collectors.toList());

    private DesiredCapabilities getServerCapabilities() {
        DesiredCapabilities serverCapabilities = new DesiredCapabilities();
        serverCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 60 * 15);
        serverCapabilities.setCapability(MobileCapabilityType.NO_RESET, false);
        return serverCapabilities;
    }

    public synchronized void startAppiumService() {
        DesiredCapabilities serverCapabilities = getServerCapabilities();
        logUtility.logDebug("The server capibilties passed to appium is" + serverCapabilities.toString());

        int chromeDriverPort = getRandomFreePort();
        int systemPort = getRandomFreePort();
        int bootstrapPort = getRandomFreePort();
        logUtility.logDebug("ports-" + chromeDriverPort + " " + systemPort + " " + bootstrapPort);
        AppiumServiceBuilder appiumBuilder = new AppiumServiceBuilder()
                .usingDriverExecutable(new File(TestConfiguration.getNodePath()))
                .withAppiumJS(new File(TestConfiguration.getAppiumPath())).withCapabilities(serverCapabilities)
                .withArgument(GeneralServerFlag.LOG_LEVEL, "warn").withLogFile(new File("appiumlogs.txt"))
                .withArgument(AndroidServerFlag.SUPPRESS_ADB_KILL_SERVER).withArgument(AndroidServerFlag.BOOTSTRAP_PORT_NUMBER, Integer.toString(bootstrapPort))
                .withArgument(AndroidServerFlag.CHROME_DRIVER_PORT, Integer.toString(chromeDriverPort)).withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(AndroidServerFlag.CHROME_DRIVER_EXECUTABLE, System.getProperty("user.dir") + "/src/resources/chromedriver").usingAnyFreePort();

        appiumLocalService = appiumBuilder.build();
        logUtility.logDebug("appium service builder build is completed");
        logUtility.logDebug("Appium local service url" + appiumLocalService.getUrl());
        appiumLocalService.start();
        logUtility.logDebug("appium service builder start is completed");
    }

    public AppiumDriverLocalService getAppiumService() {
        return appiumLocalService;
    }

    public int getRandomFreePort() {
        int port = 0;
        try (ServerSocket socket = new ServerSocket(0)) {
            socket.setReuseAddress(true);
            port = socket.getLocalPort();
        } catch (Exception e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
        return port;
    }

    public void stopAppiumService() {
        appiumLocalService.stop();
    }
}
