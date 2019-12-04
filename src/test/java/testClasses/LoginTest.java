package testClasses;

import base.BaseClass;
import constants.Constants;
import org.testng.annotations.Test;
import testFlows.*;
import utility.EnvironmentParameters;
import utility.LogUtility;
import utility.PropertyFileUtility;

import java.util.Properties;

/**
 * @author shivam mishra
 */

public class LoginTest extends BaseClass {

    private LoginFlow loginFlow;
    private LogUtility logUtility = new LogUtility(LoginTest.class);

    @Override
    public void initializeTestObjects() {
        loginFlow = new LoginFlow(sessionManager);
        logUtility.setExtentLogger(sessionManager.getExtentReporter().getExtentlogger());
    }

    @Test
    public void loginTest() {
        logUtility.logTestTitle("Login Test");
        logUtility.logTestInfo("Checking login flow");

        loginFlow.loginSuccessfully();
        //other flows
    }

}

