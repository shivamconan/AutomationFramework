
## Cubii automation framework - Web + App + Api

## Based on

1. Appium

2. Selenium

3. HttpClient

4. TestNG

5. Extent Reports

## Supports

1. Parallel Execution of Android or iOS devices

2. Api + Web automation

3. Page object Model using a single screen class to store both android and ios locators at same place.

4. Common gestures and controls like Full Screen Swipe, Half Screen swipe, Pickerwheel interactions, Keyboard handling, Bring element to focus, scroll, click, enter text, etc. 

5. Execution configuration for local and cloud devices as well as application environment via single yaml file.

6. Log4j for logging.

7. Extent Reports.

8. Retry for failed cases (In Progress)

## Usage -

Every Module/Screen Test Cases you will create using this framework will have following 4 things (Example provided below) - 

* ### Screen Class

	Each Screen Class will have static fields to store both android & ios locators of CubiiMobileBy type.

* ### Test Step Class

	- Contains utility methods for micro-interaction steps needed to perform on the app. Like "clickOnXXX", "getTitle", etc.
	
    - Each Test Steps Class must have a constructor which intializes `AppiumLibrary` object with the driver passed in argument

* ### Test Flow Class- 
     
     - This will contain abstracted navigation flows to be used in your test automation and assertions. 
     
     - Each method in the class will contain multiple interactions utility methods created in Steps class to be reused while writing tests across different modules.

* ### Test Class-

    - This is the main Test Class for each functionality/suite. This will contain your `@Test` methods and is responsible for calling the test code (defined in Flows class).
        
	- Each and every Test Class must extend BaseClass which will provide driver and other session related fields.
    And override `initializeTestObjects()` method which will intialize helper objects to be used in the test case.
    

We are going to see an example of a common login test case. I will attach code snippets from the framework style and then explain what it does below them.

### Example of Login Functionality-


#### Step 1 : Create a `LoginScreen.java` class file to store all the locators for Login functionality - 

 ```java
 public final static CubiiMobileBy SIGN_IN_WITH_EMAIL_BTN = new CubiiMobileBy(MobileBy.id("ib_login_email"), MobileBy.xpath("//*[contains(@name, 'emailButton')]"));
 public final static CubiiMobileBy USER_NAME_FIELD = new CubiiMobileBy(MobileBy.xpath("//android.widget.EditText[contains(@resource-id,'editText')]"), MobileBy.xpath("//*[contains(@name,'emailTextField')]"));
 ```

Here you will define fields of type CubiiMobileBy which accepts 2 MobileBy as arguments, one for android and other for ios.


`CubiiMobileBy` is a special class in the framework, object of which holds together both os locators for the common element. It will supply the appropriate `MobileBy` locator to `AppiumDriver` based on execution os automatically on the go.

#### Step 2 : Create a Test Steps Class - `LoginTestSteps.java`, extending `LoginScreen` class created in Step 1

````java
    public class LoginSteps extends LoginScreen
    {
        AppiumLibrary appiumLibrary;
        UserOnboardingDetailsSteps userOnboardingDetailsSteps;

        public LoginSteps(SessionManager sessionManager) {
            appiumLibrary = new AppiumLibrary(sessionManager);
        }

        public void clickOnLoginWithEmailButton() {
            appiumLibrary.clickOnMobileElement(SIGN_IN_WITH_EMAIL_BTN);
        }

        public void enterUserName(String userName) {
            appiumLibrary.enterText(USER_NAME_FIELD, userName);
        }

        public void enterPassword(String password) {
            appiumLibrary.enterText(PASSWORD_FIELD, password);
        }

        public void clickOnLoginButton() {
            appiumLibrary.clickOnMobileElement(LOGIN_BTN);
        }
    }
````

As you can see, it contains micro-interaction utility methods using the locator objects you created in Step 1. In the example, its trying to perform 4 different steps - 

1. Click on Login with email option (to land on login screen)

2. Enter username

3. Enter password

4. Click on login button
    
You might have noticed the constructor contains 2 classes - 

`SessionManager` - This class manages the appium session across the test suite. For every steps, flow class we will create we must supply this object in the constructor while creating their object. It will be supplied via `BaseClass`. (More on this later)

`AppiumLibrary` - Contains appium interaction methods to actually interact with app on mentioned element locators.

Every Steps class MUST have the constructor mentioned. In addition it can include other Steps objects if you want to use them. More on them later.

#### Step 3 : Create a Test Flow Class - `LoginFlow.java` 

So, once we have all the interaction methods in Steps Class, we would want a wrapper around them to combine those unit steps into meaningful user-flows.

This Flows Class will contain all such methods which ofcourse will comprise of calling multiple "steps".

For example, one "flow" will be to login successfully. We have already defined 4 steps above, lets define a wrapper flow method to combine the above to create a flow - 

````java
    public class LoginFlow extends LoginSteps {

        private LogUtility logUtility = new LogUtility(LoginFlow.class);

        public LoginFlow(SessionManager sessionManager) {
    	    super(sessionManager);
            logUtility.setExtentLogger(sessionManager.getExtentReporter().getExtentlogger());
        }
    
        public void loginWithEmail(String email, String passowrd) {
    	    clickOnLoginWithEmailButton();
            enterUserName(email);
            enterPassword(passowrd);
            clickOnLoginButton();
        }
        
    }
````

As you can see, we have created a similar constructor for `LoginFlow` class as we did for `LoginSteps`. We created a method `loginWithEmail` method to log in a user. You can include an additional assertion to verify whether the login is successful but you get the idea.

#### Step 4 : Create a Test Class - `LoginTest.java`

So, now we have to finally add our TestNG class to put everything together. We create a `LoginTest` class which can contain multiple `@Test` methods for each of our test case.

````java
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
````
        
        
The Test Class will extend `BaseClass` from where we are gonna receive an instance of `SessionManager` containing initialized `AppiumDriver` (as per the configuration specified).

You notice, we have `initializeTestObjects` method which overrides it for the one in parent `BaseClass`. This is necessary to assign `SessionManager` instance (initialized in @BeforeTest) to pass into your flows classes.

And then we have our `@Test` method to call all the flows we want to and log the steps and other info along the way.


## Useful Classes/Utilities

* #### AppiumLibrary - 

    You would use appium interaction utility methods defined in `AppiumLibrary` in your Steps class. It supports `clickOnElement`, `clickOnElementIfPresent`, `getAttribute`, `getText`, `scrollToElement`, `bringElementToTop`, `hideKeyboard`, `pressKey`, `enterText`, `scrollUp`, `scrollDown`, `swipeOnElement` and lot more actions!

* #### CommonFlow -

    Many times in an application, some steps and flows are common for all/most parts of the app. For example, menu navigation panel could remain same no matter the screen user is on. Or pressing on notifications icon which is present on many/all screens of the app. So it makes sense to write interaction methods for these in a CommonFlow class and use it in any of your module Flows class.

* #### LogUtility -

    For every class, declare a variable of `LogUtility` and in constructor, (As seen in examples above) initialize by passing instance of extent logger from sessionmanager like this -

````java
        @Override
        public void initializeTestObjects() {
            loginFlow = new LoginFlow(sessionManager);
            logUtility.setExtentLogger(sessionManager.getExtentReporter().getExtentlogger());
        }
````

   It contains various helpful logging methods to log on different levels as well as different markups in the TestNG and Extent Reports like -

````java
    logUtility.logTestTitle("Testing Login functionality");

    logUtility.logStep("Step " + (++n) + " : Clicking on login button");

    logUtility.logWarning("Element not found, trying again.");
 
    logUtility.logTestInfo("Current device is " + sessionManager.getCurrentDevice());
````

* #### WebLibrary -

    Similar to AppiumLibrary, WebLibrary contains interaction utility methods for Selenium like click, enterText, switchToWindow, scrollUp, scrollDown and many more!
 
 
* #### Constants & Test_Data -

    Classes with public final static fields to store project directory paths, test data like user credentials, etc. Anything you might need to access globally in your tests!
 
* #### AndroidUtility & iOSUtility -

    You can find methods related to installing/uninstalling apps on your phone, some adb, idevice utility commands [Not finished yet]
 
* #### DBUtility (In progress) -

    `DBUtility` class establishes a MySQL connection whenever its constructor is called. Set your dbUrl, username and password in `src/main/resources/{environment}.properties` file. To execute query -

````java
    DBUtility dbUtility = new DBUtility();
    dbUtility.executeQuery(your_query);
    dbUtility.closeDbConnection();
````
   
* #### JSONFileUtility -

    Normally you would store your api requests in json files. This class will help you read those files to json objects or arrays -
 
 ````java
        JSONObject loginRequest = JSONFileUtility.getJsonFileAsJsonObject(LoginRequest.json);
 ````
   
Well, that's okay you ask but how to actually call an API?

* ### How to test APIs -

We have written a wrapper around HttpClient called `ApiUtility`. Below is an example to get a json object from a file, replace variables in the json with their actual intended values in the code and then send a post request and then retrieve a value from the response and return it -

Lets say you have loginRequest.json(in apiRequests folder) with `{{email}}` and `{{password}}` as variables -
 
````ruby
 {
   "email" : "{{email}}",
   "password" : "{{password}}"
 }
````
    
Calling login api to login with passed email & password -

````java
    public String loginWithEmailViaApi(User user) {
         String baseUrl = TestConfiguration.getBaseUrl() + "/api/v5/login/";
         Map<String, String> headers = apiUtility.getCommonHeaders(); //You can define whatever common headers like Content-Type = application/json
         //get the json file
         JSONObject requestBody = JSONFileUtility.getJsonFileAsJsonObject(Constants.API_REQUESTS_PATH + Constants.slash + "loginRequest.json");
         
         //prepare a map for the variables you defined in your json file and their values
         Map<String, Object> variablesMap = new HashMap<>();
         variablesMap.put("email", user.getEmail());
         variablesMap.put("password", user.getPassword());
         
         //now prepare the final json body by replacing all the values of variables
         JSONObject finalBody = JSONFileUtility.replaceVariablesInJsonWithTheirValuesFromMap(requestBody, variablesMap);
         
         //call the api and convert the HttpResponse object to JSONObject
         JSONObject my_api_response_json = apiUtility.convertResponseToJsonObject(apiUtility.sendRequest("post", baseUrl, headers, finalBody));
         
         //retrieve token from the response and return it
         JSONObject dataJson = (JSONObject) my_api_response_json.get("data");
         String token = dataJson.get("token").toString();
         String userId = dataJson.get("id").toString();
         user.setToken(token);
         user.setUserId(userId);
         return token;
     }
````
 
Similarly, you can call your get, put apis with whatever arguments you want to. Simple, right?


#### Retry Listener - (In Progress)

#### How to run tests under different configurations from command line - (In Progress)
 
 Run on android(or ios) on whatever devices connected -
            
  ````ruby   
     mvn clean test -DisTestRunOnSpecificDevice = false -DplatformOs = android (or ios) -DtestSuites=Login,Ocb -DtestEnvironment=staging
  ````   

 Run on android(or ios) on specific devices connected -
        
   ````ruby   
     mvn clean test -DisTestRunOnSpecificDevice = true -DplatformOs = android (or ios) -DexecutionDevicesList = "Samsung Galaxy J6, Nokia 3" -DtestSuites=Login,Ocb -DtestEnvironment=staging
   ````
     
 Running via testng.xml is exactly how it should be - Just add a testng xml file with your tests & classes and you are good to go.
 

### Internal Architecture 

Detailed Explanation & Architecture Diagram coming soon...

### Reports Configuration :

* How to configure default reports in IntelliJ -
Go to Run -> Edit Congurations.

Under TestNG configuration, go to Listeners tab.

Add org.testng.reporters.TestHTMLReporter listener and save it.

Click on Use default reporters. 
 



