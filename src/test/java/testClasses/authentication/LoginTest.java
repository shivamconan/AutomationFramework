package testClasses.authentication;

import base.BaseClass;
import constants.Defaults;

import org.testng.annotations.Test;
import testFlows.*;
import utility.LogUtility;

/**
 * @author shivam mishra
 */

public class LoginTest extends BaseClass {

    private LoginFlow loginFlow;
    //private HomeFlow homeFlow;
    //private CommonFlow commonFlow;
    private LogUtility logUtility = new LogUtility(LoginTest.class);
    

    @Override
    public void initializeTestObjects() {
        loginFlow = new LoginFlow(sessionManager);
        //homeFlow = new HomeFlow(sessionManager);
        //commonFlow = new CommonFlow(sessionManager);
        logUtility.setExtentLogger(sessionManager.getExtentReporter().getExtentlogger());
    }

    @Test(priority=0)
    public void verifySigninWithEmail() {
        logUtility.logTestTitle("Checking sign in with email flow");

        loginFlow.signInWithEmailSuccessfully("","");
        //commonFlow.openHome();
        //homeFlow.someVerfication();
    }
}

