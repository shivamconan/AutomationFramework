package library;

import devices.Device;
import io.appium.java_client.*;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utility.EnvironmentParameters;
import utility.LogUtility;

import java.util.List;

/**
 * @author shivam mishra
 */

public class AppiumLibrary {

    private AppiumDriver driver;
    private Device device;

    private ElementLocator elementLocator = new ElementLocator();
    private LogUtility logUtility = new LogUtility(AppiumLibrary.class);

    public AppiumLibrary(SessionManager sessionManager) {
        this.driver = sessionManager.getDriver();
        this.device = sessionManager.getCurrentTestDevice();
        this.logUtility.setExtentLogger(sessionManager.getExtentReporter().getExtentlogger());
    }

    /*
        Element related helper methods
     */

    public void clickOnMobileElement(String locator) {
        isElementPresent(locator, 10);
        MobileElement element = getMobileElement(locator);
        element.click();
    }

    public void clickOnMobileElement(MobileElement mobileElement) {
        mobileElement.click();
    }

    public void clickOnMobileElementIfPresent(String locator) {
        if (isElementPresent(locator, 10)) {
            logUtility.logInfo("Element " + locator + " present and clicking on it.");
            MobileElement element = getMobileElement(locator);
            element.click();
        } else {
            logUtility.logWarning("Element " + locator + " not present so not clicking on it.");
        }
    }

    public boolean isElementPresent(String locator, int timeout) {
        boolean found = false;
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            wait.until(ExpectedConditions.visibilityOfElementLocated(elementLocator.elements(locator)));
            found = true;
        } catch (TimeoutException timeoutException) {
            logUtility.logWarning("Element " + locator + " not present.");
            logUtility.logWarning(ExceptionUtils.getStackTrace(timeoutException));
        }
        return found;
    }

    public String getText(String locator) {
        return getMobileElement(locator).getAttribute("text");
    }

    public String getText(MobileElement element) {
        return element.getAttribute("text");
    }

    public boolean areElementsPresent(String locator, int timeout) {
        boolean found = false;
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(elementLocator.elements(locator)));
            found = true;
        } catch (TimeoutException timeoutException) {
            logUtility.logWarning("Element " + locator + " not present.");
            logUtility.logWarning(ExceptionUtils.getStackTrace(timeoutException));
        }
        return found;
    }

    public MobileElement getMobileElement(String locator) {
        isElementPresent(locator, 10);
        return (MobileElement) driver.findElement(elementLocator.elements(locator));
    }

    public List<MobileElement> getListOfMobileElements(String locator) {
        areElementsPresent(locator, 10);
        return driver.findElements(elementLocator.elements(locator));
    }

    public MobileElement scrollToElement(String locator) {
        int retryCount = 0;
        MobileElement element = null;
        while (retryCount < 15) {
            if(!isElementPresent(locator, 10)) {
                scrollDownHalfScreen();
                retryCount++;
            }
            else {
                element = getMobileElement(locator);
                break;
            }
        }
        Assert.assertNotNull(element);
        return element;
    }

    public MobileElement bringElementToTop(String locator) {
        MobileElement element = getMobileElement(locator);
        scroll(element.getCenter().getX(), element.getLocation().getY(), element.getCenter().getX(), 80);
        return element;
    }

    public boolean isElementTextCorrect(String locator, String expectedText) {
        boolean result = false;
        if(isElementPresent(locator, 10)) {
            MobileElement element = getMobileElement(locator);
            result = element.getText().equals(expectedText);
        }
        else {
            Assert.fail("Element " + locator + " not present.");
        }
        return result;
    }

    /*
        Keyboard related helper methods
     */

    public void closeKeyboard() {
        driver.hideKeyboard();
    }

    public void pressSearchOnKeyboard() {
        if (EnvironmentParameters.getPlatformOs().equalsIgnoreCase("android")) {
            driver.getKeyboard().pressKey(Keys.ENTER);
        }
        else {
            clickOnMobileElement("AS_Search");
        }
    }

    public void pressBack() {
        driver.navigate().back();
    }

    public void enterText(String locator, String text) {
        clickOnMobileElement(locator);
        getMobileElement(locator).sendKeys(text);
    }

    public String getTextOfElement(String locator) {
        return getMobileElement(locator).getAttribute("text");
    }

    public void scroll(int startX, int startY, int endX, int endY) {
        TouchAction action = new TouchAction(driver);
        action.longPress(PointOption.point(startX, startY)).moveTo(PointOption.point(endX, endY)).release().perform();
    }

    public void scrollFullScreen() {
        int startX = getScreenWidth() / 2;
        int startY = getScreenHeight() * 8/9;
        int endY = getScreenHeight() / 9;
        scroll(startX, startY, startX, endY);
    }

    public void scrollUpHalfScreen() {
        int startX = getScreenWidth() / 2;
        int startY = driver.manage().window().getSize().height /2;
        int endY = driver.manage().window().getSize().height * 7/9;
        scroll(startX, startY, startX, endY);
    }

    public void scrollDownHalfScreen() {
        int startX = getScreenWidth() / 2;
        int startY = driver.manage().window().getSize().height * 7/9;
        int endY = driver.manage().window().getSize().height /2;
        scroll(startX, startY, startX, endY);
    }

    public void scrollUpALittle() {
        int startX = getScreenWidth() / 2;
        int startY = getScreenHeight() * 2/3;
        int endY = getScreenHeight() * 8/9;
        scroll(startX, startY, startX, endY);
    }

    public void scrollDownALittle() {
        int startX = getScreenWidth() / 2;
        int startY = getScreenHeight() * 8/9;
        int endY = getScreenHeight() * 2/3;
        scroll(startX, startY, startX, endY);
    }

    public void swipeToRefresh() {
        int startX  = getScreenWidth() / 2;
        int endY = getScreenHeight() * 3/5;
        int startY = getScreenHeight() /2;
        scroll(startX, startY, startX, endY);
    }

    public void swipeToRefreshFromMiddle() {
        int endY;
        if (EnvironmentParameters.getPlatformOs().equalsIgnoreCase("android")) {
            endY = getScreenHeight() * 3/5;
        } else {
            endY = getScreenHeight() * 4/5;
        }
        int startY = getScreenHeight() /3;
        int startX = getScreenWidth() /2;

        scroll(startX, startY, startX, endY);
    }

    public void swipeLeftOnElementFromItsCenter(String locator) {
        MobileElement element = getMobileElement(locator);
        int startX = element.getCenter().getX();
        int startY = element.getCenter().getY();
        int endX = 20;
        scroll(startX, startY, endX, startY);
    }

    public void swipeLeftOnElementFromItsRightMostPoint(String locator) {
        MobileElement element = getMobileElement(locator);
        int startX = element.getLocation().getX() + element.getSize().getWidth() - 60;
        int startY = element.getCenter().getY();
        int endX = 40;
        scroll(startX, startY, endX, startY);
    }

    public void swipeRightOnElementFromItsCenter(String locator) {
        MobileElement element = getMobileElement(locator);
        int startX = element.getCenter().getX();
        int startY = element.getCenter().getY();
        int endX = getScreenWidth() - 20;
        scroll(startX, startY, endX, startY);
    }

    public void swipeRightOnElementFromItsLeftMostPoint(String locator) {
        MobileElement element = getMobileElement(locator);
        int startX = element.getLocation().getX() + 60;
        int startY = element.getCenter().getY();
        int endX = getScreenWidth() - 20;
        scroll(startX, startY, endX, startY);
    }

    public void setValue(String locator, String value) {
        MobileElement element = getMobileElement(locator);
        element.setValue(value);
    }

    public int getScreenWidth() {
        return driver.manage().window().getSize().width;
    }

    public int getScreenHeight() {
        return driver.manage().window().getSize().height;
    }
}
