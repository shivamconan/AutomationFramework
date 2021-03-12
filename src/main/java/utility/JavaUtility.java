package utility;

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
        int n = getRandomNumberUpto(10)+ 10;
        sleep(n);
    }

    public static int getRandomNumberUpto(int bound) {
        Random rand = new Random();
        return rand.nextInt(bound) ;
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
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        LocalDateTime now = getCurrentDateTimeObject();
        String currentDateTime = now.format(format);
        return currentDateTime;
    }

    public static String get3MinutesEarlierDateTime() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        LocalDateTime now = getCurrentDateTimeObject();
        now = now.minusMinutes(3);
        String currentDateTime = now.format(format);
        return currentDateTime;
    }

    public static LocalDateTime getCurrentDateTimeObject() {
        LocalDateTime now = LocalDateTime.now();
        return now;
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

    public static long getUnixTimestamp() {
        return System.currentTimeMillis() / 1000L;
    }

    public static String getCurrentTimeStamp() {
    	DateFormat dateFormat = new SimpleDateFormat("MM/DD/yyyy");
    	Date today = Calendar.getInstance().getTime();
    	return dateFormat.format(today);
    }
    
    public static int getPreviousYear() {
    	Calendar c = Calendar.getInstance();
    	return c.get(Calendar.YEAR)-1;
    }
    
    public static int getCurrentYear() {
    	Calendar c = Calendar.getInstance();
    	return c.get(Calendar.YEAR);
    }
    
    public static String getCurrentMonth() {
    	DateFormat df1 = new SimpleDateFormat("MMM");
    	Calendar c = Calendar.getInstance();
    	return df1.format(c.getTime());
    }
    
    public static String getNextMonth() {
    	DateFormat df1 = new SimpleDateFormat("MMM");
    	Calendar c = Calendar.getInstance();
    	c.add((Calendar.MONTH),1);
    	return df1.format(c.getTime());
    }
    
    public static String getPreviousDay() {
    	DateFormat df1 = new SimpleDateFormat("DD");
    	Calendar c = Calendar.getInstance();
    	c.add((Calendar.DAY_OF_MONTH),-1);
    	return df1.format(c.getTime());
    }
    
    public static String getPreviousToCurrentYear() {
    	return getNextMonth()+" "+getPreviousYear()+" - "+getCurrentMonth()+" "+getCurrentYear();
    }

}
