package utility;

import objects.Device;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shivam mishra
 */

/*
    Manages a global deviceStatusMap which maintains the statuses of devices.
    All the tests will fetch a free device from this map.
 */

public class DeviceUtility {

    public static HashMap<Device, String> deviceStatusMap = new HashMap<>();
    private static LogUtility logUtility = new LogUtility(DeviceUtility.class);

    /*
        Initialize the global deviceStatusMap with devices passed as argument
        with each device having status as free.
        All the tests will use this global deviceStatusMap for getting the current
        status of devices and find a free device if available.
     */

    public static synchronized void initiazeDeviceMap(List<Device> devices) {
        devices.forEach((device) -> {
            deviceStatusMap.put(device, "free");
        });
        logUtility.logDebug("Devices map-");
        deviceStatusMap.forEach((device, status) -> logUtility.logDebug(device.getDeviceName() + " - " + status));
    }

    /*
        Iterates through the deviceStatusMap and returns the first device whose
        status is found to be free
     */

    public static synchronized Device getAFreeDevice() {
        Device freeDevice = null;
        for (Map.Entry<Device, String> entry : deviceStatusMap.entrySet()) {
            Device key = entry.getKey();
            String value = entry.getValue();
            if (value.equalsIgnoreCase("free")) {
                freeDevice = key;
                break;
            }
        }
        Assert.assertNotNull(freeDevice, "No free device found");
        return freeDevice;
    }

    /*
        Marks the device passed in argument as busy in the deviceStatusMap
     */

    public static synchronized void bookTheDevice(Device device) {
        deviceStatusMap.put(device, "busy");
        logUtility.logDebug("Marked " + device.getDeviceName() + " as busy.");
    }

    public static synchronized void freeTheDevice(Device device) {
        deviceStatusMap.put(device, "free");
        logUtility.logDebug("Marked " + device.getDeviceName() + " as free.");
    }

    public static synchronized Device getAFreeDeviceAndBookIt() {
        Device freeDevice = getAFreeDevice();
        bookTheDevice(freeDevice);
        return freeDevice;
    }


}
