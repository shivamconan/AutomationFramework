package testSteps;

import library.AppiumLibrary;
import library.SessionManager;
import model.TestConfiguration;
import testScreens.LoginScreen;
import utility.JavaUtility;

/**
 * @author shivam mishra
 */

public class LoginSteps extends LoginScreen
{
    AppiumLibrary appiumLibrary;
    //UserOnboardingDetailsSteps userOnboardingDetailsSteps;

    public LoginSteps(SessionManager sessionManager)
    {
        appiumLibrary = new AppiumLibrary(sessionManager);
        //userOnboardingDetailsSteps = new UserOnboardingDetailsSteps(sessionManager);
    }

    public void clickOnLoginWithEmailButton() {
        appiumLibrary.clickOnMobileElement(SIGN_IN_WITH_EMAIL_BTN);
    }

    public void enterUserName(String userName)
    {
        appiumLibrary.enterText(USER_NAME_FIELD, userName);
    }

    public void enterPassword(String password)
    {
        appiumLibrary.enterText(PASSWORD_FIELD, password);
    }

    public void clickOnLoginButton()
    {
        appiumLibrary.clickOnMobileElement(LOGIN_BTN);
    }

    public void clickOnSignUp() {
    	appiumLibrary.clickOnMobileElement(SIGN_UP);
    }
}