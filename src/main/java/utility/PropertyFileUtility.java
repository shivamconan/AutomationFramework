package utility;

import constants.Constants;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * @author shivam mishra
 */

public class PropertyFileUtility {

    private String fileName;
    private FileInputStream fileInputStream;
    private OutputStream outputStream;
    private static LogUtility logUtility = new LogUtility(PropertyFileUtility.class);

    public PropertyFileUtility(String fileName) {
        this.fileName = fileName;
    }

    /*
        returns a properties object in case you dont want to
        open and close a property file everytime you want
        to read a property
     */

    public static Properties getLocatorPropertyFile(String locatorFileName) {
        return propertyFile(Constants.LOCATORS_PATH + File.separator + locatorFileName);
    }

    public static Properties propertyFile(String fileName) {
        Properties temp = new Properties();
        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(fileName);
            temp.load(fileInput);
        } catch (Exception e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        } finally {
            try {
                if (fileInput != null) {
                    fileInput.close();
                }
            } catch (IOException e) {
                logUtility.logException(ExceptionUtils.getStackTrace(e));
            }
        }
        return temp;
    }
    
    public static OrderedProperties property_File(String fileName) {
    	OrderedProperties temp = new OrderedProperties();
        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(fileName);
            temp.load(fileInput);
        } catch (Exception e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        } finally {
            try {
                if (fileInput != null) {
                    fileInput.close();
                }
            } catch (IOException e) {
                logUtility.logException(ExceptionUtils.getStackTrace(e));
            }
        }
        return temp;
    }

    public void clearPropertiesFile() {
        System.out.println("file name-=" + fileName);
        try {
            outputStream = new FileOutputStream(fileName);
            Properties prop = new Properties();
            prop.clear();
            prop.store(outputStream, null);
        } catch (IOException io) {
            logUtility.logException(ExceptionUtils.getStackTrace(io));
        } finally {
            closeFile();
        }
    }

    public void setProperty(String key, String value) {
        //appends the property file
        PropertiesConfiguration conf = null;
        try {
            conf = new PropertiesConfiguration(fileName);
            conf.setProperty(key, value);
            conf.save();
        } catch (ConfigurationException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }

    }

    public String getProperty(String key) {
        String value = null;
        try {
            fileInputStream = new FileInputStream(fileName);
            Properties prop = new Properties();
            prop.load(fileInputStream);
            value = prop.getProperty(key);

        } catch (IOException ex) {
            logUtility.logException(ExceptionUtils.getStackTrace(ex));
        } finally {
            closeFile();
        }
        return value;
    }

    public void closeFile() {
        try {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
    }
    
    public List<Object> getProperties() {
        String value = null;
        try {
            fileInputStream = new FileInputStream(fileName);
            OrderedProperties prop = new OrderedProperties();
            prop.load(fileInputStream);
            return Collections.list(prop.keys());
        } catch (IOException ex) {
            logUtility.logException(ExceptionUtils.getStackTrace(ex));
        } finally {
            closeFile();
        }
        return Collections.emptyList();
    }
}
