package library;

import constants.Constants;
import model.TestConfiguration;
import objects.Device;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import utility.JavaUtility;
import utility.LogUtility;
import utility.ShellCommandExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shivam mishra
 */

public class IosSessionManager extends SessionManager {

    private AppiumDriver driver = null;
    private Device device = null;
    private LogUtility logUtility = new LogUtility(IosSessionManager.class);
    private LogUtility testLogger;

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
        the IosDriver
     */

    @Override
    public void initiateDriver() {
        DesiredCapabilities clientCapabilities = getClientCapabilities();
        driver = new IOSDriver<IOSElement>(
               appiumLocalService.getUrl(), clientCapabilities);
        logUtility.logDebug("Ios driver intitiated successfully");
    }

    /*
       Gets the list of all the ios devices on which the tests will be running.
       First it will check whether isTestRunOnSpecificDevice flag is set,
       if yes then will return List of devices passed from the command line,
       otherwise will return the list of connected ios devices found via
       idevice_id -l command.
    */

    @Override
    public List<Device> getExecutionDevicesList() {
        List<Device> iosDevices = new ArrayList<Device>();
        if (TestConfiguration.getIsTestRunOnSpecificDevice()) {
           // iosDevices = getListOfDevicesFromDevicesJsonUsing("name", EnvironmentParameters.getExecutionDevicesNamesList());
            Assert.assertTrue(iosDevices.size() > 0, "Specific ios device details was not found. Please add the device details in devices.json file." );
        } else {
            List<String> udidListFetchedFromIdeviceUtility = getConnectedIosUdidsList();
            //Assert.assertTrue(udidListFetchedFromIdeviceUtility.size()>0, "Found no connected ios device from idevice_id -l or instruments-s devices command. Make sure an ios device is connected with enabled trust and is unlocked.");
            logUtility.logDebug("Connected ios udids identified - " + Arrays.toString(udidListFetchedFromIdeviceUtility.toArray()));

            iosDevices = getListOfDevicesFromDevicesJsonUsing("udid", udidListFetchedFromIdeviceUtility);
            //Assert.assertTrue(iosDevices.size() > 0, "Connected ios device details was not found in devices.json file. Please add the device details in it." );
        }
        return iosDevices;
    }

    @Override
    public void quitDriver() {
        logUtility.logDebug("Ios driver quit successfully.");
        driver.quit();
    }

    /*
        Returns a list of connected ios devices returned from
        idevice_id -l command
     */

    public List<String> getConnectedIosUdidsList() {
        String onlyConnectedDevicesUdids = ShellCommandExecutor.executeCommands("idevice_id -l") + "\n";
        List<String> onlyConnectedDevicesUdidList = JavaUtility.getListFromStringSplitVia(onlyConnectedDevicesUdids, "\n");
        String connectedDevicesAndSimulators = ShellCommandExecutor.executeCommands("instruments -s devices");
        List<String> connectedDevicesAndSimulatorsStringList = JavaUtility.getListFromStringSplitVia(connectedDevicesAndSimulators, "\n");
        //skipping first from list as the first line only contains "Known Devices".
        return connectedDevicesAndSimulatorsStringList.stream()
                .skip(2)
                .limit(onlyConnectedDevicesUdidList.size())
                .map(connectedDeviceString -> {
                    String deviceName = JavaUtility.getFirstMatchRegex(connectedDeviceString, ".+?(?= \\()");
                    String deviceOsVersion = JavaUtility.getFirstMatchRegex(connectedDeviceString, "\\((.*?)\\)");
                    String deviceUdidWithSquareBrackets = JavaUtility.getFirstMatchRegex(connectedDeviceString, "\\[(.*?)\\]");
                    String deviceUdid = deviceUdidWithSquareBrackets.replace("[", "").replace("]", "");
                    return deviceUdid;
                }).collect(Collectors.toList());

    }

    /*
       returns client capabilities required for starting ios driver
       with the udid as set for this object
    */

    public DesiredCapabilities getClientCapabilities() {
        DesiredCapabilities clientCapabilities = new DesiredCapabilities();
        clientCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
        clientCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, device.getDeviceName());
        clientCapabilities.setCapability(MobileCapabilityType.UDID, device.getDeviceUdid());
        logUtility.logDebug("Tests running on udid - " + clientCapabilities.getCapability(MobileCapabilityType.UDID));
        clientCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, device.getDeviceOsVersion());
        clientCapabilities.setCapability(IOSMobileCapabilityType.AUTO_DISMISS_ALERTS, false);
        clientCapabilities.setCapability(MobileCapabilityType.APP, Constants.MAIN_RESOURCES_DIRECTORY + "/Some.app");
        clientCapabilities.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT, getRandomFreePort());
        clientCapabilities.setCapability(IOSMobileCapabilityType.USE_NEW_WDA, true);
        clientCapabilities.setCapability(IOSMobileCapabilityType.SHOW_XCODE_LOG, false);
        return clientCapabilities;
    }

    public DesiredCapabilities getBrowserstackCapabilities() {
        DesiredCapabilities clientCapabilities = new DesiredCapabilities();
        clientCapabilities.setCapability("browserstack.user", "");
        clientCapabilities.setCapability("browserstack.key", "");
        clientCapabilities.setCapability(MobileCapabilityType.APP, "bs://");
        clientCapabilities.setCapability("device", "iPhone 11 Pro");
        clientCapabilities.setCapability("os_version", "13");
        clientCapabilities.setCapability("project", "First Java Project");
        clientCapabilities.setCapability("build", "Java iOS 123");
        clientCapabilities.setCapability("name", "first_test 123");
        clientCapabilities.setCapability("autoAcceptAlerts", "true");

        return clientCapabilities;
    }
}
