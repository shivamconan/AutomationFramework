# Master automation framework - Web + App + Api

## Based on

1. Appium

2. Selenium

3. HttpClient

4. TestNG

## Supports

1. Parallel Execution of Android or iOS devices

2. Runtime devices detection and execution based on environment specified (android/ios)

3. Dynamic Suite Generator. Will set test classes on runtime based on tags passed.

4. Retry for failed cases.

5. Log4j for logging.

6. Extent Reports.



## Usage -

Every Functionality Test Suite you will create using this framework will have following 3 things- 

* Test Step Class

	Each Test Steps Class must have a constructor which intializes AppiumLibrary object with the driver passed in argument

* Test Flow Class- 
     
     This will contain abstracted navigation flows to be used in your test automation and assertions. 

* Test Class-

    This is the main Test Class for each functionality/suite. This will contain your @Test methods and is reponsible for calling the test code.
    
	Each and every Test Class must extend BaseClass which will provide driver and other session related fields.
    And override initializeTestObjects() method which will intialize helper objects to be used in the test case.
    
       
	
### Example of Login Functionality-


#### Step 1 : Create a properties file to store all the locators for Login functionality

  Separate file must be created each for android and ios in locators package -
  
  locators.android.loginPage.properties-
  
     nextBtn=XP_//android.widget.TextView[@text='NEXT']
     // android locators
     
  locators.ios.loginPage.properties-
  
     nextBtn=XP_//XCUIElement[@text='NEXT'] 
     // ios locators

#### Step 2 : Create a Test Steps Class - LoginTestSteps.java    

This Steps Class will contain all the individiual methods which performs various actions. One flow will combine multiple steps and one test will combine multiple flows.
 
Steps class uses the properties file created in Step 1 to locate the elements -

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

#### Step 3 : Create a Test Flow Class - LoginFlow.java    

This Steps Class will contain all the methods which performs various actions being used in Test Class. 
It uses the properties file created in Step 1 to locate the elements -

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
           


#### Step 4 : Create a Test Class - LoginTest.java    


 This Test Class will extend BaseClass and implement initializeTestObjects() method which initializes the LoginFlows object created in Step 3 
 and the logUtility logs onto TestNG default reports, Extent reports and on console using Log4j
as per the log method used. 
        


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

    
### How To's   

#### 1. Use Common Steps-

   There will be certain methods which are common throughout for DealerApp and are not specific to any functionality. 
   You can use it just like how you would use any Steps class in your Test Class just like the example shown above.

#### 2. Use Logger-

    LogUtility class contains all the logging methods. The methods logs using Log4j and also logs in the TestNG and Extent reports.
    
    int stepCount = 0;
    LogUtility logUtility = new LogUtility(LoginPage.class);
    
    logUtility.logTestTitle("Testing Login functionality");
    
    logUtility.logStep("Step " + (++n) + " : Clicking on login button");
    //login step
    
    logUtility.logWarning("Element not found, trying again.");
    
    logStep(), logTestTitle(), logTestInfo(), logException() logs in the Extent Report as well along with on console and TestNG reports.
    logInfo(), logWarning() does not log in the Extent Report and only logs on console and TestNG reports.
    
    Additionally, there is a logMessage() method -
    
    logMessage(message, true) -> logs in the Extent Report as well along with on console and TestNG reports.
    
    logMessage(message, false) -> does not log in the Extent Report and only logs on console and TestNG reports.
    
#### 3. Use Property files-

   utility.PropertyUtility.java Class helps to write and read properties file.
   You can either get the entire Properties object using the static method propertyFile() -

       Properties myPropertyObject = PropertyUtility.propertyFile(filePath);
       String property1 = myPropertyObject.get("property1");
       String property2 = myPropertyObject.get("property2");
    
   This is useful when you want all the properties in one single call and not want to write anything.
 
   Another way is to create an object of PropertyUtility with the provided file path-
 
       PropertyUtility myProperty = new PropertyUtility(filePath);
       String property1 = myProperty.getProperty("property1");
       myProperty.setProperty("property1");
    
   Mostly, you would want to have locators properties file, for that there is a special method getLocatorPropertyFile()-
   
       Properties loginPageLocator = PropertyFileUtility.getLocatorPropertyFile( "loginPage.properties");
         
   which is equivalent to writing-
   
      Properties loginPageLocator = PropertyFileUtility.propertyFile( Constants.LOCATORS_PATH + "/loginPage.properties");
      
   Each method closes the file after performing required action.
 
 
#### 4. Use JSON files-
 
   utility.JSONFileUtility Class helps to read JSON files.
 
 
   Get JSONObject from the file-
 
       JSONObject devicesJsonFile = JSONFileUtility.getJsonFileAsJsonObject(filePath);
 
 
   Get JSONArray from the file-
    
       JSONArray devicesJsonFile = JSONFileUtility.getJsonFileAsJsonArray(filePath);
 
   After that you can write your parser logic.

#### 5. Test your API's-

   ApiLibrary class contains all the helper methods you need for sending HTTP requests. You need to create a JSON object for your body and a hashmap for your headers -
      
      ApiLibrary apiLibrary = new ApiLibrary();
      String my_api_url = "https://test.com/test";
      Map<String, String> headers = new HashMap<>();
      headers.put("Content-type", "application/json");
      JSONObject jsonBody = new JSONObject();
      jsonBody.put("field1", "value1");
      jsonBody.put("field2", "value2");
      
      HttpResponse my_api_respone = apiLibrary.sendRequest("post", my_api_url, headers, jsonBody);
      JSONObject my_api_response_json = apiLibrary.convertResponseToJsonObject(my_api_respone);
   
   You should keep your json bodies in main/resources/apRequests directory. 
   Many times you would need to replace some parameters with custom variables. JSONUtility class contains a helper method to replace all your variables through a HashMap. Simply put {{}} around the values for the keys you would want to replace in your code 
   and pass a map providing the values to be replaced with. For example, if your json is like this -
   
      {
        "parameter1" : "{{value1}}",
        "parameter2" : "{{value2}}"
      }
      
   Code to generate final json request -
   
     JSONObject requestBody = JSONFileUtility.getJsonFileAsJsonObject(Constants.API_REQUESTS_PATH + Constants.slash + "myRequest.json");
     Map<String, Object> variablesMap = new HashMap<>();
     variablesMap.put("value1", "test123");
     JSONObject finalBody = JSONFileUtility.replaceVariablesInJsonWithTheirValuesFromMap(requestBody, variablesMap);
     
 
#### 6. DB connection -

   DBUtility class establishes a MySQL connection whenever its constructor is called. Set your dbUrl, username and password in src/main/resources/{environment}.properties file.
   To execute query -
   
      DBUtility dbUtility = new DBUtility();
      dbUtility.executeQuery(your_query);
      dbUtility.closeDbConnection();
   
#### 7. Get the device being used for the current test -

   SessionManager contains getCurrentTestDevice() returns a Device object which stores various info about the device being used.
   
      public class LoginPage extends BaseClass
      {
        Device currentTestDevice = sessionManager.getCurrentTestDevice();
        String currentTestDeviceName = currentTestDevice.getDeviceName();
        logUtility.logTestInfo(currentTestDeviceName);
      }
       
       
#### 8. Run on android(or ios) on whatever devices connected -

      mvn clean test -DisTestRunOnSpecificDevice = false -DplatformOs = android (or ios) -DtestSuites=Login,Ocb -DtestEnvironment=staging
 
#### 9. Run on android(or ios) on specific devices connected -

      mvn clean test -DisTestRunOnSpecificDevice = true -DplatformOs = android (or ios) -DexecutionDevicesList = "Samsung Galaxy J6, Nokia 3" -DtestSuites=Login,Ocb -DtestEnvironment=staging
      
#### 10. Run tests locally without command line - 

Initially the testng.xml will look like-

      <!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">

      <suite name="Suite1" verbose="1" parallel="methods" thread-count="2">
       <listeners>
           <listener class-name="listeners.DynamicSuiteGenerator" />
           <listener class-name="listeners.AnnotationTransformer" />
       </listeners>
      </suite>

You can see the file only contains the listeners as tests will be added dynamically using DynamicSuiteGenerator class in listeners package.
On the runtime it checks whether the xml file contains any test tags before or not. If yes, it will pick up those tests, other wise it will
pickup from the -DtestSuites parameter in command line, if not specified from command line, it will pickup from defaultEnvironment.properties.

If you are running the xml file locally, you can add and specify your tests the usual way-

    <test name="Nopackage" >
        <classes>
            <class name="testClasses.LoginTest" />
        </classes>
    </test>
    
    
 Threads will be automatically created based on the number of devices (whether the connected ones or the ones passed from command line).
  

#### 11. Reports -
   This framework uses Extent Reports which is saved by the name extentReport.html in test-output folder. By default, surefire-plugin stores
   the TestNG reports in target folder, so in tear-down this framework copies the reports to the test-output folder for easy accessibility.

## Internal Architecture-
 
### SessionManager:
   Abstract class which contains various methods to start/service appium service. AndroidSessionManager and IosSessionManager extends this class
   and provides respective implementations for getting devices(android or ios), starting driver(AndroidDriver or IosDriver).
 
### BaseClass:
   Contains all the setup methods which calls SessionManager methods to initialize and maintain Appium session. On run time, it creates an AndroidSessionManager
   or IosSessionManager object on the basis of platformOs specified.
 
### locators :
   package contains two sub-packages - android and ios which contains respective locators for each functionality.
 
### ElementLocator :
   Class contains elements() method which returns By object based on the first two characters specified. 
   For ex, if passed XP_//a[@class='test'], it will return By.Xpath("//a[@class='test']").
   The various parameters are -
   XP - Xpath
   ID - Id
   CS - CSS
   NM - Name
   CN - Class Name
   LT - Link Text
   PL - Partial Link Text
   TN - Tag Name
 
### AppiumLibrary :
   Class contains various utility methods to perform common appium actions like click, swipe, scroll etc.
   It takes locator received from locators properties file. In each method, it receives MobileElement object with the help of ElementLocator
   as shown in example of Step 2 above. 
 
### WebLibrary :
   Similar to AppiumLibrary, contains all utility methods of Selenium to help you write automation scripts. Contains methods to click, select, drag and drop, scroll, etc.
   
### JavaUtility :
   Class contains several helper core Java methods to get current date, time, extract integers from string, etc. 
 
### resources :
   
 * defaultEnvironmentParameters.properties -
  
      stores default properties for environment. If you are running tests locally, you will specify details here.
 
  * devices.json -
  
      contains device details in this format-
 
        {
     
         "android" : [
      
           {  
        
             "name": "Samsung Galaxy J6",
          
             "udid": "udid1",
          
             "version": "8.0"
          
           },
        
           {
              //other devices  
           }
        
         ],
      
         "ios" : [

           {
        
             "name": "iPhone 6 Black",
          
             "udid" : "udid3",
             
             "version" : "12.3.1"
          
           },
        
           {
        
             //other devices
          
           }
        
         ]
      
       }

When deviceName is passed from command line, the framework will fetch udid of the passed device from this file.

### Constants :
Contains static fields majorly for all your paths -

       public static final String slash = System.getProperty("file.separator");
   
       public static final String PROJECT_DIRECTORY = System.getProperty("user.dir");
   
       public static final String MAIN_DIRECTORY = PROJECT_DIRECTORY + slash + "src" + slash + "main";
   
       public static final String TEST_DIRECTORY = PROJECT_DIRECTORY + slash + "src" + slash + "test";
   
       public static final String MAIN_RESOURCES_DIRECTORY = MAIN_DIRECTORY + slash + "resources";
   
       public static final String START_ACTIVITY_NAME = "com.test.yourActivity";
   
       public static final int DEFAULT_OBJECT_WAIT_TIME = 10;
   
       public static final String DEVICES_JSON_PATH = MAIN_DIRECTORY + slash + "resources" + slash + "devices.json";
   
       public static final String LOCATORS_PATH = MAIN_DIRECTORY + slash + "locators" + slash + EnvironmentParameters.getPlatformOs();


### Defaults :
Specifies static fields DEFAULT_ENVIRONMENT_PATH, DEALER_DEFAULT_APP_PATH, etc

### DynamicSuiteGenerator :
Class contains listener which creates dynamic suites based on specified test suites from command line or via xml file.

### Configuration : 
 

#### * How to configure default reports in IntelliJ -

  * Go to Run -> Edit Congurations.
  
  * Under TestNG configuration, go to Listeners tab.
  
  * Add org.testng.reporters.TestHTMLReporter listener and save it.
  
  * Click on Use default reporters.
  
  
Rest is described in comments in the codebase.




        

