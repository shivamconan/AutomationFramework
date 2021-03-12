package model;

public class AndroidDevice {

    private static String name;

    private static String udid;

    private static String version;

    public static String getDeviceName() {
        return name;
    }

    public static String getUdid() {
        return udid;
    }

    public static String getOsVersion() {
        return version;
    }
}
