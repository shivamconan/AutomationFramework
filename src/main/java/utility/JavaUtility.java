package utility;

import com.google.inject.internal.cglib.core.$DefaultGeneratorStrategy;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shivam mishra
 */

public class JavaUtility {

    //methods to convert string to integer, etc


    public static List<String> getListFromStringSplitVia(String inputString, String demiliter) {
        return new LinkedList<>(Arrays.asList(inputString.split(demiliter)));
    }

    public static LinkedList<String> getListFromStringArray(String[] strings) {
        return  new LinkedList<>(Arrays.asList(strings));
    }

    public static void randomSleep(){
        Random rand = new Random();
        int n = rand.nextInt(10) + 10;
        sleep(n);
    }

    public static String getFirstMatchRegex(String inputString, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(inputString);
        String firstMatch = null;
        while (m.find()) {
            firstMatch = m.group(0);
        }
        return firstMatch;
    }

    public static int getIntegerFromString(String input) {
        String numberOnly= input.replaceAll("[^0-9]", "");
        return Integer.parseInt(numberOnly);
    }

    public static int addNumberInStringForm(String input1, String input2) {
        return getIntegerFromString(input1) + getIntegerFromString(input2);
    }

    public static long getLongFromString(String input) {
        String numberOnly= input.replaceAll("[^0-9]", "");
        return Long.parseLong(numberOnly);
    }

    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getCurrentDateTime() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String currentDateTime = now.format(format);
        return currentDateTime;
    }

    public static String get5SecondsEarlierDateTime() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        now = now.minusSeconds(5);
        String currentDateTime = now.format(format);
        return currentDateTime;
    }

    public static String get4DaysEarlierDateTime() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        now = now.minusDays(4);
        String currentDateTime = now.format(format);
        return currentDateTime;
    }

    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String currentDate = (dateFormat.format(date));
        return currentDate;
    }

}
