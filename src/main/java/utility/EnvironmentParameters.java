package utility;

import constants.Constants;
import constants.Defaults;
import org.testng.Reporter;
import sun.dc.pr.PRError;

import javax.swing.*;
import java.util.List;
import java.util.Properties;

/**
 * @author shivam mishra
 */

/*
    Contains methods which returns various configuration parameters passed in command line
 */

public class EnvironmentParameters {

    private static String testEnvironment = System.getProperty("testEnvironment");
    private static String platformOs = System.getProperty("platformOs");
    private static String isTestRunOnSpecificDevice = System.getProperty("isTestRunOnSpecificDevice");
    private static String executionDevicesListParameter = System.getProperty("executionDevicesList");
    private static String testSuites = System.getProperty("testSuites");

    private static String carType = System.getProperty("carType");
    private static String noOfCars = System.getProperty("noOfCars");
    private static String make = System.getProperty("make");
    private static String model = System.getProperty("model");
    private static String variant = System.getProperty("variant");
    private static String year = System.getProperty("year");
    private static String kms = System.getProperty("kms");
    private static String ownerNumber = System.getProperty("ownerNumber");
    private static String fuelType = System.getProperty("fuelType");
    private static String scrap = System.getProperty("scrap");
    private static String tp = System.getProperty("tp");
    private static String ocbCarType = System.getProperty("ocbCarType");
    private static String requestedC24Quote = System.getProperty("requestedC24Quote");
    private static String requestedHb = System.getProperty("requestedHb");
    private static String city = System.getProperty("city");
    private static String bidAmount = System.getProperty("bidAmount");
    private static String dealerEmail = System.getProperty("dealerEmail");

    private static Properties defaultEnvironmentProperties = PropertyFileUtility.propertyFile(Defaults.DEFAULT_ENVIRONMENT_PATH);
    private static LogUtility logUtility = new LogUtility(EnvironmentParameters.class);

    public static String getTestEnvironment() {
        return testEnvironment == null ? getDefaultTestEnvironment() : testEnvironment;
    }

    public static String getDefaultTestEnvironment() {
        return defaultEnvironmentProperties.getProperty("testEnvironment");
    }

    public static String getPlatformOs() {
        return platformOs == null ? getDefaultPlatformOs() : platformOs;
    }

    private static String getDefaultPlatformOs() {
        return defaultEnvironmentProperties.getProperty("platformOs");
    }

    public static boolean getIsTestRunOnSpecificDevice() {
        return isTestRunOnSpecificDevice == null ? getDefaultIsTestRunOnSpecificDevice() : Boolean.parseBoolean(isTestRunOnSpecificDevice);
    }

    private static boolean getDefaultIsTestRunOnSpecificDevice() {
        String check = defaultEnvironmentProperties.getProperty("isTestRunOnSpecificDevice");
        return Boolean.parseBoolean(check);
    }

    public static List<String> getTestSuites() {
        String testSuitesString = testSuites == null ? getDefaultTestSuites() : testSuites;
        Reporter.log("Test suites---" + testSuitesString);
        return JavaUtility.getListFromStringSplitVia(testSuitesString, ",");
    }

    public static String getDefaultTestSuites() {
        return defaultEnvironmentProperties.getProperty("testSuites");
    }

    public static List<String> getExecutionDevicesNamesList() {
        String executionDevicesCheck = executionDevicesListParameter == null ? getDefaultExecutionDevicesNames() : executionDevicesListParameter;
        return JavaUtility.getListFromStringSplitVia(executionDevicesCheck, ",");
    }

    private static String getDefaultExecutionDevicesNames() {
        return defaultEnvironmentProperties.getProperty("executionDevicesList");
    }

    /*
        Environment parameters for car creation flow
     */

    public static int getNoOfCars() {
        if(noOfCars == null) {
            noOfCars = getDefaultNoOfCars();
        }
        return JavaUtility.getIntegerFromString(noOfCars);
    }

    public static String getDefaultNoOfCars() {
        return defaultEnvironmentProperties.getProperty("noOfCars");
    }

    public static String getCarType() {
        return carType == null? getDefaultCarType(): carType;
    }

    public static String getDefaultCarType() {
        return defaultEnvironmentProperties.getProperty("carType");
    }

    public static String getMake() {
        return make == null? getDefaultMake() : make;
    }

    public static String getDefaultMake() {
        return defaultEnvironmentProperties.getProperty("make");
    }

    public static String getModel() {
        return model == null? getDefaultModel() : model;
    }

    public static String getDefaultModel() {
        return defaultEnvironmentProperties.getProperty("model");
    }

    public static String getVariant() {
        return variant == null? getDefaultVariant() : variant;
    }

    public static String getDefaultVariant() {
        return defaultEnvironmentProperties.getProperty("variant");
    }

    public static String getYear() {
       return year == null? getDefaultYear() : year;
    }

    public static String getDefaultYear() {
        return defaultEnvironmentProperties.getProperty("year");
    }

    public static int getKms() {
        if(kms == null) {
            kms = getDefaultKms();
        }
        return JavaUtility.getIntegerFromString(kms);
    }

    public static String getDefaultKms() {
        return defaultEnvironmentProperties.getProperty("kms");
    }

    public static int getTp() {
        if(tp == null) {
            tp = getDefaulTp();
        }
        return JavaUtility.getIntegerFromString(tp);
    }

    public static String getDefaulTp() {
        return defaultEnvironmentProperties.getProperty("tp");
    }

    public static String getFuelType() {
        return fuelType == null? getDefaulFuelType() : fuelType;
    }

    public static String getDefaulFuelType() {
        return defaultEnvironmentProperties.getProperty("fuelType");
    }

    public static String getScrap() {
        return scrap == null? getDefaultScrap() : scrap;
    }

    public static String getDefaultScrap() {
        return defaultEnvironmentProperties.getProperty("scrap");
    }

    public static String getOwnerNumber() {
        return ownerNumber == null? getDefaulOwnerNumber() : ownerNumber;
    }

    public static String getDefaulOwnerNumber() {
        return defaultEnvironmentProperties.getProperty("ownerNumber");
    }

    public static String getOcbCarType() {
        return ocbCarType == null? getDefaulOcbCarType() : ocbCarType;
    }

    public static String getDefaulOcbCarType() {
        return defaultEnvironmentProperties.getProperty("ocbCarType");
    }

    public static String getRequestedC24Quote() {
        return requestedC24Quote == null? getDefaulRequestedC24Quote() : requestedC24Quote;
    }

    public static String getDefaulRequestedC24Quote() {
        return defaultEnvironmentProperties.getProperty("requestedC24Quote");
    }

    public static String getRequestedHb() {
        return requestedHb == null? getDefaultRequestedHb() : requestedHb;
    }

    public static String getDefaultRequestedHb() {
        return defaultEnvironmentProperties.getProperty("requestedHb");
    }

    public static String getCity() {
        return city == null? getDefaultCity() :city;
    }

    public static String getDefaultCity() {
        return defaultEnvironmentProperties.getProperty("city");
    }

    public static String getBidAmount() {
        return bidAmount == null? getDefaultBidAmount() : bidAmount;
    }

    public static String getDefaultBidAmount() {
        return defaultEnvironmentProperties.getProperty("bidAmount");
    }

    public static String getDealerEmail() {
        return dealerEmail;
    }
}
