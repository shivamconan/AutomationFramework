package utility;

import constants.Constants;
import objects.Device;

import java.util.Properties;

/**
 * @author shivam mishra
 */

/*
    Will contain adb helper methods like clear data, etc.
 */

public class AndroidUtility {

    public Properties environmentProperties = PropertyFileUtility.propertyFile("");

    public void grantPermissions(Device device) {
        String permissionsPrefix = "adb -s " + device.getDeviceUdid() + " shell pm grant " + environmentProperties.getProperty("androidPackageName");
        ShellCommandExecutor.executeCommands(permissionsPrefix + " android.permission.CAMERA");
        ShellCommandExecutor.executeCommands(permissionsPrefix + " android.permission.RECORD_AUDIO");
        ShellCommandExecutor.executeCommands(permissionsPrefix + " android.permission.RECEIVE_SMS");
        ShellCommandExecutor.executeCommands(permissionsPrefix + " android.permission.ACCESS_FINE_LOCATION");
        ShellCommandExecutor.executeCommands(permissionsPrefix + " android.permission.WRITE_EXTERNAL_STORAGE");
        ShellCommandExecutor.executeCommands(permissionsPrefix + " android.permission.READ_EXTERNAL_STORAGE");
        ShellCommandExecutor.executeCommands(permissionsPrefix + " android.permission.ACCESS_COARSE_LOCATION");
    }

    public static void uninstallAppiumApps(Device device){
        String permissionsPrefix = "adb -s " + device.getDeviceUdid() + " shell pm uninstall";
        ShellCommandExecutor.executeCommands(permissionsPrefix + " io.appium.uiautomator2.server");
        ShellCommandExecutor.executeCommands(permissionsPrefix + " io.appium.uiautomator2.server.test");
        //ShellCommandExecutor.executeCommands(permissionsPrefix + " io.appium.unlock");
        ShellCommandExecutor.executeCommands(permissionsPrefix + " io.appium.settings");
    }

}
