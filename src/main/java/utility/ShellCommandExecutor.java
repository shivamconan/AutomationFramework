/**
 *
 */
package utility;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author shivam mishra
 */

public class ShellCommandExecutor {

    private static LogUtility logUtility = new LogUtility(ShellCommandExecutor.class);

    public static String executeCommands(String command) {
        logUtility.logInfo("executing terminal command- " + command);
        StringBuilder output = new StringBuilder();
        String terminalOutputAsString = null;
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line = "";
            while ((line = input.readLine()) != null) {
                output.append(line).append("\n");
            }

            while ((line = error.readLine()) != null) {
                output.append("Error Ocurred : \n").append(line).append("\n");
            }
            terminalOutputAsString = output.toString();
            logUtility.logDebug("terminal output -" + terminalOutputAsString);
        } catch (Exception e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
        return terminalOutputAsString;
    }

}
