package testFlows;

import constants.Constants;
import devices.Dealer;
import library.SessionManager;
import org.testng.Assert;
import testSteps.LoginSteps;
import utility.DBUtility;
import utility.JavaUtility;
import utility.LogUtility;
import utility.PropertyFileUtility;

import java.util.Properties;

public class LoginFlow {

    private LoginSteps loginSteps;
    private LogUtility logUtility = new LogUtility(LoginFlow.class);
    private Properties environmentProperties = PropertyFileUtility.propertyFile(Constants.ENVIRONMENT_PROPERTIES_PATH);
    private DBUtility dbUtility;

    public LoginFlow(SessionManager sessionManager) {
        loginSteps = new LoginSteps(sessionManager);
        dbUtility = new DBUtility(sessionManager);
        logUtility.setExtentLogger(sessionManager.getExtentReporter().getExtentlogger());
    }

    public void loginSuccessfully() {
        loginSteps.enterUserName("username");
        loginSteps.enterPassword("password");
        loginSteps.clickOnLoginButton();

        Assert.assertTrue(loginSteps.checkWelcomeScreen());
    }

    public void logout() {
        //steps for logout
    }

}