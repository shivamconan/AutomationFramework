package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestConfiguration {

	private static String platformOs;
	private static String allowAPIUtilsOnProdEnvironment;
	private static String baseUrl;
	private static String qaBaseUrl;
	private static String isTestRunOnSpecificDevice;
	private static String androidPackageName;
	private static String iosPackageName;
	private static String executionEnvironment;
	private static String testEnvironment;
	private static Map<String, String> android;
	private static Map<String, String> ios;
	private static String nodePath;
	private static String appiumPath;

	public static Map<String, String> getAndroid() {
		return android;
	}

	public void setAndroid(Map<String, String> android) {
		this.android = android;
	}

	public static Map<String, String> getIos() {
		return ios;
	}

	public void setIos(Map<String, String> ios) {
		this.ios = ios;
	}
	
	public static String getAllowAPIUtilsOnProdEnvironment() {
		return allowAPIUtilsOnProdEnvironment;
	}

	public void setAllowAPIUtilsOnProdEnvironment(String allowAPIUtilsOnProdEnvironment) {
		this.allowAPIUtilsOnProdEnvironment = allowAPIUtilsOnProdEnvironment;
	}

	public static String getTestEnvironment() {
		return testEnvironment;
	}
	public void setTestEnvironment(String testEnvironment) {
		this.testEnvironment = testEnvironment;
	}
	public static String getPlatformOs() {
		if(System.getProperty("platformOs")!=null) {
			platformOs = System.getProperty("platformOs");
		}
		return platformOs;
	}
	public void setPlatformOs(String platformOs) {
		this.platformOs = platformOs;
	}
	public static boolean getIsTestRunOnSpecificDevice() {
		return Boolean.valueOf(isTestRunOnSpecificDevice);
	}
	public void setIsTestRunOnSpecificDevice(String isTestRunOnSpecificDevice) {
		this.isTestRunOnSpecificDevice = isTestRunOnSpecificDevice;
	}
	public static String getAndroidPackageName() {
		return androidPackageName;
	}
	public void setAndroidPackageName(String androidPackageName) {
		this.androidPackageName = androidPackageName;
	}
	public static String getIosPackageName() {
		return iosPackageName;
	}
	public void setIosPackageName(String iosPackageName) {
		this.iosPackageName = iosPackageName;
	}
	public static String getExecutionEnvironment() {
		return executionEnvironment;
	}
	public void setExecutionEnvironment(String executionEnvironment) {
		this.executionEnvironment = executionEnvironment;
	}
	public static String getBaseUrl() {
		return (Boolean.valueOf(allowAPIUtilsOnProdEnvironment)||(baseUrl.equalsIgnoreCase(qaBaseUrl))?baseUrl:null);
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	public static String getQaBaseUrl() {
		return qaBaseUrl;
	}
	public void setQaBaseUrl(String qaBaseUrl) {
		this.qaBaseUrl = qaBaseUrl;
	}

	public static String getPackageName() {
		if(getPlatformOs().equalsIgnoreCase("android")) {
			return getAndroidPackageName();
		}
		else {
			return getIosPackageName();
		}
	}

	public static Map<String,String> getDevice() {
		if(getPlatformOs().equalsIgnoreCase("android")) {
			return getAndroid();
		}
		else {
			return getIos();
		}
	}
	
	public static String getNodePath() {
		return nodePath;
	}

	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}
	
	public static String getAppiumPath() {
		return appiumPath;
	}

	public void setAppiumPath(String appiumPath) {
		this.appiumPath = appiumPath;
	}
}
