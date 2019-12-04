package library;

import constants.Constants;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utility.JavaUtility;
import utility.LogUtility;

import java.util.*;

public class WebLibrary {

    private WebDriver driver;

    private ElementLocator elementLocator = new ElementLocator();
    private LogUtility logUtility = new LogUtility(WebLibrary.class);

    public WebLibrary(SessionManager sessionManager) {
        this.logUtility.setExtentLogger(sessionManager.getExtentReporter().getExtentlogger());
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void initiateChromeDriver()  {
        System.setProperty("webdriver.chrome.driver", Constants.CHROME_DRIVER_PATH);
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    public void clickIfPresent(String element) {
        if(isElementPresentFastFail(element)) {
            click(element);
        }
    }

    public void click(String element) {
        try {
            new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(elementLocator.elements(element))).click();
            logUtility.logWarning("*****Performed click operation on element->" + element + "*****");
        } catch (ElementNotVisibleException env) {
            driver.findElement(elementLocator.elements(element)).click();
            logUtility.logWarning(env.getMessage());
            logUtility.logWarning("*****Element is present in DOM but not visible on the page*****" + env.getMessage());
        } catch (NoSuchElementException ne) {
            logUtility.logWarning(ne.getMessage());
            logUtility.logWarning("*****The element could not be located on the page.*****" + ne.getMessage());
        } catch (StaleElementReferenceException se) {
            logUtility.logWarning(se.getMessage());
            logUtility.logWarning(
                    "*****Either the element has been deleted entirely or the element is no longer attached to DOM.*****"
                            + se.getMessage());
        } catch (Exception e) {
            logUtility.logWarning(ExceptionUtils.getStackTrace(e));
            logUtility.logWarning("Could not perform click for first time, trying again");
            new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(elementLocator.elements(element))).click();
            logUtility.logWarning("*****Could not perform click. Please check!!! *****");
        }
    }

    public void fill(String element, String inputdata) {
        try {
            WebElement field = new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(elementLocator.elements(element)));
            field.clear();
            field.sendKeys(inputdata);
            JavaUtility.sleep(1);
            field.sendKeys(Keys.TAB);
            logUtility.logWarning("*****Performed fill operation on locator->" + element + "*****");
        } catch (InvalidElementStateException ie) {
            logUtility.logWarning(ie.getMessage());
            logUtility.logWarning("*****Element is either hidden or disabled*****");
        } catch (NoSuchElementException ne) {
            logUtility.logWarning(ne.getMessage());
            logUtility.logWarning("*****The element could not be located on the page.*****" + ne.getMessage());
        } catch (StaleElementReferenceException se) {
            logUtility.logWarning(
                    "*****Either the element has been deleted entirely or the element is no longer attached to DOM.*****"
                            + se.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logUtility.logWarning("*****Could not perform fill operation. Please check!!! *****" + e.getMessage());
        }
    }

    public void clear(String element) {
        try {
            driver.findElement(elementLocator.elements(element)).clear();
            logUtility.logWarning("*****Performed fill operation on locator->" + element + "*****");
        } catch (InvalidElementStateException ie) {
            logUtility.logWarning(ie.getMessage());
            logUtility.logWarning("*****Element is either hidden or disabled*****");
        } catch (NoSuchElementException ne) {
            logUtility.logWarning(ExceptionUtils.getStackTrace(ne));
            logUtility.logWarning("*****The element could not be located on the page.*****" + ne.getMessage());
        } catch (StaleElementReferenceException se) {
            logUtility.logWarning(
                    "*****Either the element has been deleted entirely or the element is no longer attached to DOM.*****"
                            + se.getMessage());
        } catch (Exception e) {
            logUtility.logWarning(e.getStackTrace().toString());
            logUtility.logWarning("*****Could not perform fill operation. Please check!!! *****" + e.getMessage());
        }
    }

    public String getText(String locator) {
        WebDriverWait wait = new WebDriverWait(driver, 90);
        WebElement element = wait
                .until(ExpectedConditions.visibilityOfElementLocated(elementLocator.elements(locator)));
        return element.getText();
    }

    public boolean isElementPresentFastFail(String locator) {
        boolean result = false;
        WebDriverWait wait = new WebDriverWait(driver, 5);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(elementLocator.elements(locator)));
            result = true;
        } catch (TimeoutException e) {
            logUtility.logWarning(e.getStackTrace().toString());
        }
        return result;
    }

    public boolean isElementPresent(String locator) {
        boolean result = false;
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(elementLocator.elements(locator)));
            result = true;
        } catch (TimeoutException e) {
            logUtility.logWarning(e.getStackTrace().toString());
        }
        return result;
    }

    public boolean isElementClickable(String locator) {
        boolean result = false;
        WebDriverWait wait = new WebDriverWait(driver, 5);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(elementLocator.elements(locator)));
            result = true;
        } catch (TimeoutException e) {
            logUtility.logWarning(e.getStackTrace().toString());
        }
        return result;
    }

    public void scrollToBottom(WebDriver driver) {
        JavascriptExecutor jsExecutor = ((JavascriptExecutor) driver);
        String jsToGetHeight = "return document.body.scrollHeight";
        String jsToScroll = "window.scrollTo(0, document.body.scrollHeight)";
        long currentHeight = (long) jsExecutor.executeScript(jsToGetHeight);
        long lastRecordedHeight = 0;
        do {
            lastRecordedHeight = currentHeight;
            jsExecutor.executeScript(jsToScroll);
            JavaUtility.sleep(1);
            currentHeight = (long) jsExecutor.executeScript(jsToGetHeight);
        } while (currentHeight != lastRecordedHeight);

    }

    public void refreshPage() {
        driver.navigate().refresh();
    }

    public boolean isElementDisplayed(String locator) {
        isElementPresent(locator);
        return getWebElement(locator).isDisplayed();
    }

    public WebElement getWebElement(String element) {
        isElementPresent(element);
        return driver.findElement(elementLocator.elements(element));
    }

    public void switchToPickupWindow() {
        Set<String> window = driver.getWindowHandles();
        Iterator<String> iterate = window.iterator();
        String defaukt = iterate.next();
        String pickupRequestWindow = iterate.next();
        driver.switchTo().window(pickupRequestWindow);
    }

    public void scrollElement(WebDriver driver) {
        try {
            JavascriptExecutor jsExecutor = ((JavascriptExecutor) driver);
            long lastHeight = (long) jsExecutor.executeScript("return document.body.scrollHeight");
            int y = 300;
            int x = 150;
            long initialOffset = 0;
            long finalOffset = 0;
            while (y < lastHeight) {
                jsExecutor.executeScript("window.scrollTo(" + x + "," + y + ");", "");
                //TODO not working for large number of cars
                y = y + 150;
                JavaUtility.sleep(1);
                finalOffset = getYOffset(driver);
                if (!isPageScrolling(initialOffset, finalOffset, lastHeight)) {
                    //TODO create a method to close any popup which is blocking scroll functionality
                }
                initialOffset = finalOffset;
                lastHeight = (long) jsExecutor.executeScript("return document.body.scrollHeight");
            }
        } catch (Exception e) {
            logUtility.logWarning(ExceptionUtils.getStackTrace(e));
        }

    }

    public boolean isPageScrolling(long initialOffset, long finalOffset, long lastHeight) {
        return ((initialOffset != finalOffset) && finalOffset < lastHeight);
    }

    public long getYOffset(WebDriver driver) {
        Long value = 0L;
        try {
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            value = (Long) executor.executeScript("return window.pageYOffset;");
        } catch (Exception e) {
            logUtility.logWarning(e.getStackTrace().toString());
        }
        return value;
    }

    public void moveToTop() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, -document.body.scrollHeight);");
    }

    public boolean isTextPresentOnElement(String locator, String expectedText) {
        boolean result = false;
        scrollToWebElement(locator);
        String actual = getText(locator);
        result = isStringcontains(actual, expectedText);
        return result;
    }

    boolean isStringcontains(String string1, String string2) {
        boolean result = false;
        if (string1.toLowerCase().contains(string2.toLowerCase())) {
            result = true;
        }
        return result;
    }

    public boolean verifyTitle(String title) {
        logUtility.logWarning(">>>>" + driver.getTitle());
        return isStringcontains(driver.getTitle(), title);
    }

    public boolean verifyUrl(String url) {
        return isStringcontains(driver.getCurrentUrl(), url);
    }

    public void enterTextUsingAction(String element, String text) {
        isElementPresent(element);
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(elementLocator.elements(element)));
        actions.click();
        actions.sendKeys(text);
        actions.sendKeys(Keys.ENTER);
        actions.build().perform();
    }

    public void openNewTabAndSwitchToIt()
    {
        String currentWindow = driver.getWindowHandle();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.open()");
        Set<String> handles = driver.getWindowHandles();
        for(String handle: handles)
        {
            if(!handle.equalsIgnoreCase(currentWindow))
            {
                driver.switchTo().window(handle);
            }
        }

    }

    public void scrollDown() {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("window.scrollBy(0,450)");
    }

    public String getTextFromValue(String locator) {
        WebDriverWait wait = new WebDriverWait(driver, 90);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(elementLocator.elements(locator)));
        return element.getAttribute("value");
    }

    //this function will work only for 2 tabs as of now
    public void closeTabAndSwitchToParent()
    {
        Set<String> handles = driver.getWindowHandles();
        String current_handle = driver.getWindowHandle();
        driver.close();
        for(String handle: handles)
        {
            if(!handle.equalsIgnoreCase(current_handle))
            {
                driver.switchTo().window(handle);
            }
        }
    }

    public void moveSliderTo(String sliderType, String filter, String element, long position) {
        WebElement slider = driver.findElement(elementLocator.elements(element));
        int offset = 0;

        if (filter.contains("Kilometers Driven")) {
            offset = 5;
        } else if (filter.contains("Model Year")) {
            offset = 10;
        } else {
            Assert.fail("Wrong slider provided");
        }
        //if left slider we need to move towards right (+5) else towards left (-5)
        if (sliderType.equalsIgnoreCase("right")) {
            offset = -offset;
        }

        //continuously moving slider till desired value is reached
        while (Long.parseLong(slider.getAttribute("aria-valuenow")) != (position)) {
            Actions move = new Actions(driver);
            move.click().dragAndDropBy(slider, offset, 0).build().perform();

        }
    }

    public String getAttributeValue(String element, String attribute)
    {
        isElementPresent(element);
        return driver.findElement(elementLocator.elements(element)).getAttribute(attribute);
    }

    public void waitForUrlToContain(String value)
    {
        //TODO For slider issue we will wait for url to update here once slider is moved
    }

    public List<WebElement> getWebelementsList(String element) {
        List<WebElement> elements = null;
        try {
            elements = driver.findElements(elementLocator.elements(element));
        } catch (Exception e) {
            logUtility.logWarning(ExceptionUtils.getStackTrace(e));
        }
        return elements;

    }

    public void goBack() {
        driver.navigate().back();
    }

    public void scrollToWebElement(String locator) {
        try {
            (new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOfElementLocated(elementLocator.elements(locator)));
            WebElement element = driver.findElement(elementLocator.elements(locator));
            Point point = element.getLocation();
            JavascriptExecutor jsExecutor = ((JavascriptExecutor) driver);
            int y = point.getY() - 200;
            jsExecutor.executeScript("window.scrollTo(" + point.getX() + "," + y + ");", "");
        } catch (Exception e) {
            logUtility.logWarning(e.getStackTrace().toString());
        }
    }

    public void scrollToTop(){
        try {
            JavascriptExecutor jsExecutor = ((JavascriptExecutor) driver);
            jsExecutor.executeScript("window.scrollTo(0,0);", "");
        } catch (Exception e) {
            logUtility.logWarning(e.getStackTrace().toString());
        }
    }

    public Select getSelect(String locator) {
        Select select = new Select(driver.findElement(elementLocator.elements(locator)));
        return select;
    }

    public void selectByVisibleText(String locator, String visibleText) {
        Select select = getSelect(locator);
        select.selectByVisibleText(visibleText);
    }

    public void loadUrl(String url) {
        driver.get(url);
    }

    public void closeDriver() {
        driver.close();
    }

    public void quitDriver() {
        driver.quit();
    }
}
