package library;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.AndroidServerFlag;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.ServerSocket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DemoClass {

    AppiumDriverLocalService appiumLocalService;

    public DesiredCapabilities getClientCapabilities(String udid, String version, String wdaPort) {
        DesiredCapabilities clientCapabilities = new DesiredCapabilities();
        clientCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
        clientCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone");
        clientCapabilities.setCapability(MobileCapabilityType.UDID, udid);
        clientCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, version);
        clientCapabilities.setCapability(IOSMobileCapabilityType.AUTO_DISMISS_ALERTS, false);
        clientCapabilities.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.cars24.dealerapp");
        clientCapabilities.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT, wdaPort);
        clientCapabilities.setCapability(IOSMobileCapabilityType.USE_NEW_WDA, true);
        clientCapabilities.setCapability(IOSMobileCapabilityType.SHOW_XCODE_LOG, false);
        return clientCapabilities;
    }

    private DesiredCapabilities getServerCapabilities() {
        DesiredCapabilities serverCapabilities = new DesiredCapabilities();
        serverCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 60 * 15);
        serverCapabilities.setCapability(MobileCapabilityType.NO_RESET, true);
        return serverCapabilities;
    }

    public AppiumDriverLocalService startAppiumService() {
        DesiredCapabilities serverCapabilities = getServerCapabilities();

        int chromeDriverPort = getRandomFreePort();
        int port = getRandomFreePort();
        int bootstrapPort = getRandomFreePort();
        AppiumServiceBuilder appiumBuilder = new AppiumServiceBuilder().withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js")).withCapabilities(serverCapabilities)
                .withArgument(GeneralServerFlag.LOG_LEVEL, "warn").withLogFile(new File("appiumlogs.txt")).usingAnyFreePort();
        AppiumDriverLocalService appiumLocalService = appiumBuilder.build();
        appiumLocalService.start();
        return appiumLocalService;
    }

    public int getRandomFreePort() {
        int port = 0;
        try (ServerSocket socket = new ServerSocket(0)) {
            socket.setReuseAddress(true);
            port = socket.getLocalPort();
        } catch (Exception e) {
        }
        return port;
    }

    public static void main(String[] args) {

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        now = now.minusDays(1);
        String currentDateTime = now.format(format);
        System.out.println(currentDateTime);

    }
}
