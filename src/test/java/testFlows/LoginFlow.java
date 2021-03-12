package testFlows;

import constants.Constants;
import objects.User;
import library.SessionManager;

import model.TestConfiguration;
import org.json.simple.JSONObject;
import testSteps.LoginSteps;
import utility.ApiUtility;
import utility.JSONFileUtility;
import utility.LogUtility;

import java.util.HashMap;
import java.util.Map;

public class LoginFlow extends LoginSteps{

    //private CommonFlow commonFlow;
    private ApiUtility apiUtility;
    //private HomeSteps homeSteps;
    private SessionManager sessionManager;
    private LogUtility logUtility = new LogUtility(LoginFlow.class);

    public LoginFlow(SessionManager sessionManager) {
    	super(sessionManager);
    	this.sessionManager = sessionManager;
    	//homeSteps = new HomeSteps(sessionManager);
        apiUtility = new ApiUtility(sessionManager);
        //commonFlow= new CommonFlow(sessionManager);
        logUtility.setExtentLogger(sessionManager.getExtentReporter().getExtentlogger());
    }
    
    public void loginWithEmail(String email, String passowrd) {
    	clickOnLoginWithEmailButton();
        enterUserName(email);
        enterPassword(passowrd);
        clickOnLoginButton();
    }

    public String loginWithEmailViaApi(User user) {
        String baseUrl = TestConfiguration.getBaseUrl() + "/api/login/";
        Map<String, String> headers = apiUtility.getCommonHeaders();
        JSONObject requestBody = JSONFileUtility.getJsonFileAsJsonObject(Constants.API_REQUESTS_PATH + Constants.slash + "loginRequest.json");
        Map<String, Object> variablesMap = new HashMap<>();
        variablesMap.put("email", user.getEmail());
        variablesMap.put("password", user.getPassword());
        JSONObject finalBody = JSONFileUtility.replaceVariablesInJsonWithTheirValuesFromMap(requestBody, variablesMap);
        JSONObject my_api_response_json = apiUtility.convertResponseToJsonObject(apiUtility.sendRequest("post", baseUrl, headers, finalBody));
        JSONObject dataJson = (JSONObject) my_api_response_json.get("data");
        String token = dataJson.get("token").toString();
        String userId = dataJson.get("id").toString();
        user.setToken(token);
        user.setUserId(userId);
        return token;
    }

    public void signInWithEmailSuccessfully(String email,String password) {
    	loginWithEmail(email,password);
        logUtility.logSuccess("Sign in user - " + email + " successfully");
    }
}