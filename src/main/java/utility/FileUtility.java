package utility;

import constants.Constants;
import library.SessionManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtility {

    private LogUtility logUtility = new LogUtility(FileUtility.class);

    public FileUtility(SessionManager sessionManager)
    {
        logUtility.setExtentLogger(sessionManager.getExtentReporter().getExtentlogger());
    }

    /*
        By default surefire plugin stores TestNG reports in target/surefire-reports. Copying
        them to test-output folder for easy accessibility.
     */

    public void copyReportsFromTargetToTestOutput() {
        logUtility.logDebug("Copying the files from target to test-output");
        try {
            FileUtils.copyDirectory(new File("/target/surefire-reports"), new File("test-output"));
        } catch (IOException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
    }

    public static void deleteOldExtentReportIfPresent() {
        try {
            Files.deleteIfExists(new File(Constants.EXTENT_REPORTS_PATH).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
