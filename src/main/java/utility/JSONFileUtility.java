package utility;

import com.mongodb.util.JSON;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import org.testng.Reporter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class JSONFileUtility {

    private static LogUtility logUtility = new LogUtility(FileUtility.class);

    public static JSONArray getJsonFileAsJsonArray(String fileName) {
        FileReader reader = null;
        JSONArray jsonArray = null;
        JSONParser jsonParser = new JSONParser();
        try {
            reader = new FileReader(fileName);
            Object obj = jsonParser.parse(reader);
            jsonArray = (JSONArray) obj;
        } catch (ParseException | IOException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        } finally {
            closeFile(reader);
        }
        return jsonArray;
    }

    public static JSONObject getJsonFileAsJsonObject(String fileName) {
        JSONObject jsonObject = null;
        FileReader reader = null;
        JSONParser jsonParser = new JSONParser();
        try {
            reader = new FileReader(fileName);
            Object obj = jsonParser.parse(reader);
            jsonObject = (JSONObject) obj;
        } catch (ParseException | IOException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        } finally {
            closeFile(reader);
        }
        return jsonObject;
    }

    public static void closeFile(FileReader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
    }

    public static JSONObject getJsonObjectFromString(String jsonString) {
        JSONObject json = null;
        try {
            JSONParser parser = new JSONParser();
            json = (JSONObject) parser.parse(jsonString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject replaceVariablesInJsonWithTheirValuesFromMap(JSONObject requestJson, Map<String,Object> variablesMap) {
        String requestJsonAsString = requestJson.toJSONString();
        String variableInRequestJson = JavaUtility.getFirstMatchRegex(requestJsonAsString, "\\{\\{(.*?)\\}\\}");
        while (variableInRequestJson != null) {
            String variableInRequestJsonTrimmed = variableInRequestJson.replace("{{", "").replace("}}", "");
            requestJsonAsString = requestJsonAsString.replace(variableInRequestJson, String.valueOf(variablesMap.get(variableInRequestJsonTrimmed)));
            variableInRequestJson = JavaUtility.getFirstMatchRegex(requestJsonAsString, "\\{\\{(.*?)\\}\\}");
        }

        return getJsonObjectFromString(requestJsonAsString);
    }
}
