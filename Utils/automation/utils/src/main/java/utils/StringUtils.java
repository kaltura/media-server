package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by asher.saban on 3/8/2015.
 */
public class StringUtils {

    private static final Calendar calendarInstance = Calendar.getInstance();

    public static String generateRandomSuffix() {
        return new SimpleDateFormat("yyMMdd_HHmmss").format(calendarInstance.getTime());
    }
}
