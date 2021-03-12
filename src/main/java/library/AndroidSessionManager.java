package library;

import constants.Constants;
import model.TestConfiguration;
import objects.Device;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.Setting;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import utility.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shivam mishra
 */

public class AndroidSessionManager extends SessionManager {

    private AppiumDriver driver = null;
    private Device device = null;
    private LogUtility logUtility = new LogUtility(AndroidSessionManager.class);


    @Override
    public Device getCurrentTestDevice() {
        return device;
    }

    @Override
    public AppiumDriver getDriver() {
        return driver;
    }

    @Override
    public void setCurrentTestDevice(Device device) {
        this.device = device;
    }

    /*
        Starts appium service on a free port and then specifies client capabilities
        including the udid assigned to this object. Using those capabilities, initializes
        the AndroidDriver
     */

    @Override
    public synchronized void initiateDriver() {
        DesiredCapabilities clientCapabilities = getClientCapabilities();
        driver = new AndroidDriver<AndroidElement>(
                appiumLocalService.getUrl(), clientCapabilities);
        ((AndroidDriver) driver).setSetting(Setting.NORMALIZE_TAG_NAMES, true);
        logUtility.logDebug("Android driver intitiated successfully");
    }

    /*
        Gets the list of all the android devices on which the tests will be running.
        First it will check whether isTestRunOnSpecificDevice flag is set,
        if yes then will return List of devices passed from the command line,
        otherwise will return the list of connected android devices found via
        adb devices command.
     */

    @Override
    public List<Device> getExecutionDevicesList() {
        List<Device> androidDevices = new ArrayList<>();
        if (TestConfiguration.getIsTestRunOnSpecificDevice()) {
           // androidDevices = getListOfDevicesFromDevicesJsonUsing("name", TestConfiguration.getExecutionDevicesNamesList());
            Assert.assertTrue(androidDevices.size() > 0, "Specific android device details was not found. Please add the device details in devices.json file." );
        } else {
            List<String> udidListFetchedFromAdb = getConnectedAndroidUdidsList();
            //Assert.assertTrue(udidListFetchedFromAdb.size()>0, "Found no connected android device from adb devices command. Make sure an android device is connected with usb debugging enabled and is unlocked.");
            logUtility.logDebug("Connected android udids identified - " + Arrays.toString(udidListFetchedFromAdb.toArray()));

            androidDevices = getListOfDevicesFromDevicesJsonUsing("udid", udidListFetchedFromAdb);
            //Assert.assertTrue(androidDevices.size() > 0, "Connected android device details was not found in devices.json file. Please add the device details in it." );
            logUtility.logDebug("Connected android device identified - " + Arrays.toString(udidListFetchedFromAdb.toArray()));
        }
        return androidDevices;
    }

    @Override
    public void quitDriver() {
        driver.quit();
        logUtility.logDebug("Android driver quit successfully.");
    }

    /*
        Returns a list of connected android devices returned from
        adb devices command
     */

    public List<String> getConnectedAndroidUdidsList() {
        List<Device> devicesCapturedFromCommandLine = new ArrayList<>();
        String adbDevicesLogs = ShellCommandExecutor.executeCommands("adb devices");
        List<String> adbDevicesLogList = JavaUtility.getListFromStringSplitVia(adbDevicesLogs, "\n");
        logUtility.logDebug("Connected android devices- " + adbDevicesLogList);
        return adbDevicesLogList.stream()
                .skip(1)
                .filter(connectedDeviceString -> connectedDeviceString.contains("device"))
                .map(onlyConnectedDeviceString -> onlyConnectedDeviceString.split("device")[0].trim()).collect(Collectors.toList());
    }

    /*
        returns client capabilities required for starting android driver
        with the udid as set for this object
     */

    public DesiredCapabilities getClientCapabilities() {
        DesiredCapabilities clientCapabilities = new DesiredCapabilities();
        clientCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, device.getDeviceName());
        clientCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        clientCapabilities.setCapability(MobileCapabilityType.UDID, device.getDeviceUdid());
        logUtility.logDebug("Tests running on udid - " + clientCapabilities.getCapability(MobileCapabilityType.UDID));
        // os specific details :
        //clientCapabilities.setCapability(MobileCapabilityType.APP, Defaults.DEALER_DEFAULT_ANDROID_APP_PATH);
        clientCapabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, TestConfiguration.getAndroidPackageName());
        clientCapabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, Constants.START_ACTIVITY_NAME);
        clientCapabilities.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
        clientCapabilities.setCapability(AndroidMobileCapabilityType.APP_WAIT_ACTIVITY, Constants.START_ACTIVITY_NAME);
        clientCapabilities.setCapability(AndroidMobileCapabilityType.ANDROID_INSTALL_TIMEOUT, 90000 * 10);
        clientCapabilities.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT, getRandomFreePort());
        clientCapabilities.setCapability("mjpegServerPort", getRandomFreePort());
        return clientCapabilities;
    }

    public DesiredCapabilities getBrowserstackCapabilities() {
        DesiredCapabilities clientCapabilities = new DesiredCapabilities();
        clientCapabilities.setCapability("browserstack.user", "");
        clientCapabilities.setCapability("browserstack.key", "");
        clientCapabilities.setCapability(MobileCapabilityType.APP, "");
        clientCapabilities.setCapability("device", "Google Pixel 3");
        clientCapabilities.setCapability("os_version", "9.0");
        clientCapabilities.setCapability("project", "First Java Project");
        clientCapabilities.setCapability("build", "Java Android");
        clientCapabilities.setCapability("name", "first_test");
        return clientCapabilities;
    }

}
