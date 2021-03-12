package model;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

public class ProjectMobileBy extends By{

    protected By androidBy;
    protected By iOSXCUITBy;
    protected String androidLocatorStr;
    protected String iOSXCUITLocatorStr;

    public ProjectMobileBy(By androidBy, By iOSXCUITBy) {
        this.androidBy = androidBy;
        this.iOSXCUITBy = iOSXCUITBy;
    }

    public ProjectMobileBy(By mobileBy) {
        this.androidBy = mobileBy;
        this.iOSXCUITBy = mobileBy;
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        return this.findElements(context);
    }

    public By getMobileBy(RemoteWebDriver driver) {
        if(driver instanceof AndroidDriver) {
            return androidBy;
        }
        else {
            return iOSXCUITBy;
         }
    }
}
