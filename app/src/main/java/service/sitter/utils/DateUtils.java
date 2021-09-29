package service.sitter.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

public class DateUtils {
    public static boolean isValidDate(String inDate, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public static String getFormattedDate(LocalDate date) {
        return date.getDayOfMonth() + "/" +  date.getMonth().getValue() +"/" + date.getYear();
    }
    public static String getFormattedDateFromString(String date) {
        String[] dateArray = date.split("-");
        // dd/mm/yyyy
        return dateArray[2] + "/" + dateArray[1] + "/" + dateArray[0];
    }
}
