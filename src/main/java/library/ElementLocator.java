package library;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.testng.Assert;

/**
 * @author shivam mishra
 */

public class ElementLocator {

    public By by;

    public By elements(String locator) {
        String prefix = locator.substring(0, 2);
        switch (prefix) {
            case "ID":
                by = MobileBy.id(locator.substring(3, locator.length()));
                break;
            case "XP":
                by = MobileBy.xpath(locator.substring(3, locator.length()));
                break;
            case "CS":
                by = MobileBy.cssSelector(locator.substring(3, locator.length()));
                break;
            case "NM":
                by = MobileBy.name(locator.substring(3, locator.length()));
                break;
            case "CN":
                by = MobileBy.className(locator.substring(3, locator.length()));
                break;
            case "LT":
                by = MobileBy.linkText(locator.substring(3, locator.length()));
                break;
            case "TN":
                by = MobileBy.tagName(locator.substring(3, locator.length()));
                break;
            case "PL":
                by = MobileBy.partialLinkText(locator.substring(3, locator.length()));
                break;
            case "AS":
                by = MobileBy.AccessibilityId(locator.substring(3, locator.length()));
                break;
            default:
                Assert.fail("Invalid choice of element identifier- " + locator + ". Please select appropriate element identfier or make sure you have used correct abbreviation in property file.");
        }
        return (by);
    }
}
