package testScreens;

import io.appium.java_client.MobileBy;
import model.ProjectMobileBy;

public class LoginScreen {

    public final static ProjectMobileBy SIGN_IN_WITH_EMAIL_BTN = new ProjectMobileBy(MobileBy.id("some_id"), MobileBy.xpath("//*[contains(@name, 'some_name')]"));
    public final static ProjectMobileBy USER_NAME_FIELD = new ProjectMobileBy(MobileBy.xpath("//android.widget.EditText[contains(@resource-id,'some_resource_id')]"), MobileBy.xpath("//*[contains(@name,'some_name')]"));
    public final static ProjectMobileBy PASSWORD_FIELD = new ProjectMobileBy(MobileBy.xpath("//android.widget.EditText[contains(@resource-id,'some_resource_id')]"), MobileBy.xpath("//*[contains(@name,'some_name')]"));
    public final static ProjectMobileBy LOGIN_BTN = new ProjectMobileBy(MobileBy.id("some_name"), MobileBy.xpath("//*[contains(@name,'some_name')]"));

	public final static ProjectMobileBy LOGIN_WITH_FB = new ProjectMobileBy(MobileBy.id("some_id"), MobileBy.xpath("//*[contains(@name, 'some_name')]"));
	public final static ProjectMobileBy SIGN_UP = new ProjectMobileBy(MobileBy.xpath("//*[@resource-id='some_resource_id']"), MobileBy.xpath("//*[contains(@name,'some_name')]"));

}
