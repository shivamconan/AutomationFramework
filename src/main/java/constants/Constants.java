/**
 *
 */
package constants;

import model.TestConfiguration;

/**
 * @author shivam mishra
 */

public class Constants {

    private Constants() {
    }


    public static final String slash = System.getProperty("file.separator");

    public static final String PROJECT_DIRECTORY = System.getProperty("user.dir");

    public static final String MAIN_DIRECTORY = PROJECT_DIRECTORY + slash + "src" + slash + "main";

    public static final String TEST_DIRECTORY = PROJECT_DIRECTORY + slash + "src" + slash + "test";

    public static final String MAIN_RESOURCES_DIRECTORY = MAIN_DIRECTORY + slash + "resources";

    public static final String START_ACTIVITY_NAME = "com.xxxx.ui.SplashActivity";

    public static final int DEFAULT_OBJECT_WAIT_TIME = 10;

    public static final String DEVICES_JSON_PATH = MAIN_DIRECTORY + slash + "resources" + slash + "devices.json";

    public static final String LOCATORS_PATH = MAIN_DIRECTORY + slash + "locators" + slash + TestConfiguration.getPlatformOs();

    public static final String WEB_LOCATORS_PATH = MAIN_DIRECTORY + slash + "locators" + slash + "web";

    public static final String TEST_OUTPUT = PROJECT_DIRECTORY + slash + "test-output";

    public static final String SCREENSHOT_FOLDER_NAME = "Screenshot";

    public static final String EXTENT_REPORTS_PATH = TEST_OUTPUT + slash + "extentReport.html";

    public static final String EXTENT_CONFIG_PATH = TEST_DIRECTORY+ slash + "resources" + slash + "extent-config.xml";

    public static final String EXTENT_JS_PATH = TEST_DIRECTORY + slash + "resources" + slash + "extentReportJs.js";

    public static final String CHROME_DRIVER_PATH = MAIN_DIRECTORY + slash + "resources" + slash + "chromedriver";

    public static final String API_REQUESTS_PATH = MAIN_RESOURCES_DIRECTORY + slash + "apiRequests";


}