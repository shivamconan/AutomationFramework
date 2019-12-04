package library;

import devices.Device;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import utility.EnvironmentParameters;
import utility.JavaUtility;
import utility.LogUtility;
import utility.ShellCommandExecutor;

import java.util.ArrayList;
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
        startAppiumService();
        DesiredCapabilities clientCapabilities = getClientCapabilities();
        driver = new IOSDriver<>(appiumLocalService.getUrl(), clientCapabilities);
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
        if (EnvironmentParameters.getIsTestRunOnSpecificDevice()) {
            iosDevices = getListOfDevicesFromDevicesJsonUsing("name", EnvironmentParameters.getExecutionDevicesNamesList());
        } else {
            iosDevices = getListOfDevicesFromDevicesJsonUsing("udid", getConnectedIosUdidsList());
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
        String onlyConnectedDevicesUdids = ShellCommandExecutor.executeCommands("idevice_id -l");
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
        clientCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone");
        clientCapabilities.setCapability(MobileCapabilityType.UDID, device.getDeviceUdid());
        logUtility.logDebug("Tests running on udid - " + clientCapabilities.getCapability(MobileCapabilityType.UDID));
        clientCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, device.getDeviceOsVersion());
        clientCapabilities.setCapability(IOSMobileCapabilityType.AUTO_DISMISS_ALERTS, false);
        clientCapabilities.setCapability(IOSMobileCapabilityType.BUNDLE_ID, environmentProperties.getProperty("iosPackageName"));
        clientCapabilities.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT, getRandomFreePort());
        clientCapabilities.setCapability(IOSMobileCapabilityType.USE_NEW_WDA, true);
        clientCapabilities.setCapability(IOSMobileCapabilityType.SHOW_XCODE_LOG, false);
        return clientCapabilities;
    }
}
