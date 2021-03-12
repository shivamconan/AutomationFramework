package objects;

public class Device {

    private String deviceOs;

    private String deviceName;

    private String deviceOsVersion;

    private String deviceUdid;

    private String model = null;

    private String brand = null;

    public Device(String deviceOs, String deviceName, String deviceOsVersion, String deviceUdid) {
        this.deviceOs = deviceOs;
        this.deviceName = deviceName;
        this.deviceOsVersion = deviceOsVersion;
        this.deviceUdid = deviceUdid;
    }

    public String getDeviceOs() {
        return deviceOs;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceUdid() {
        return deviceUdid;
    }

    public String getDeviceOsVersion() {
        return deviceOsVersion;
    }

    @Override
    public String toString() {
        return "Device - name: " + deviceName + ", udid: " + deviceUdid + ", deviceOs: " + deviceOs + ", deviceOsVersion: " + deviceOsVersion;
    }
}
