package testSteps;

import constants.Constants;
import io.appium.java_client.AppiumDriver;
import library.AppiumLibrary;
import library.SessionManager;
import utility.JavaUtility;
import utility.PropertyFileUtility;

import java.util.Properties;

/**
 * @author shivam mishra
 */

public class LoginSteps
{
    Properties loginPageLocator = PropertyFileUtility.propertyFile(Constants.LOCATORS_PATH + "/loginPage.properties");
    AppiumLibrary appiumLibrary;

    public LoginSteps(SessionManager sessionManager)
    {
        appiumLibrary = new AppiumLibrary(sessionManager);
    }

    public void enterUserName(String userName)
    {
        appiumLibrary.enterText(loginPageLocator.getProperty("userNameField"), userName);
    }

    public void enterPassword(String password)
    {
        appiumLibrary.enterText(loginPageLocator.getProperty("passwordField"), password);
    }

    public void clickOnLoginButton()
    {
        appiumLibrary.clickOnMobileElement(loginPageLocator.getProperty("loginBtn"));
    }

    public boolean checkWelcomeScreen() {
        return appiumLibrary.isElementPresent(loginPageLocator.getProperty("welcomeScreen"), 10);
    }
}