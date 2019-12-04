package constants;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.io.File;

public class Defaults {

    public static final String slash = System.getProperty("file.separator");

    public static final String DEFAULT_ENVIRONMENT_PATH = System.getProperty("user.dir") + slash + "src" + slash + "main" + slash + "resources" + File.separator + "defaultEnvironment.properties";

    public static final String USER_NAME_PATH = File.separator + "Users" + slash + System.getProperty("user.name");

    public static final String DEALER_DEFAULT_ANDROID_APP_PATH = USER_NAME_PATH  + slash + "Documents" + slash + "builds" + slash + "Staging.apk";

    public static final String DEALER_DEFAULT_IOS_APP_PATH = USER_NAME_PATH + slash + "Documents" + slash + "builds" + slash + "Staging.ipa";

    public static final String TEXT = "Test Automation";

    public static final String MOBILE_NUMBER = "9999999";

    public static final String PASSWORD = "123456";

    public static final String SELF_PICKUP_ADDRESS = "Green Ave St, Vasant Kunj, New Delhi, Delhi 110070  New Delhi,  - 110070";

    public static final String HOME_DELIVERY_ADDRESS = "Green Ave St, Vasant Kunj, New Delhi Delhi Delhi, Delhi - 110070";

    public static final String HOME_DELIVERY_HOUSE = "Green Ave St, Vasant Kunj,";

    public static final String HOME_DELIVERY_AREA = "New Delhi Delhi Delhi,";
}
