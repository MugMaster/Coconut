package code.utilities;

import java.util.LinkedList;

/**
 * Can format values to common formats
 * 
 * @author Teddy
 */
public abstract class Format {

    /**
     * Formats a Long is standard time notation
     * 
     * @param time the time in seconds to convert
     * @return the time displayed as a String
     */
    public static String time(long time) {
        final int ONE_MINUET = 60;

        long minutes = (long) (time / ONE_MINUET);
        time -= minutes * ONE_MINUET;
        long seconds = time;

        String msg = "";

        msg += minutes + ":";
        if (seconds < 10) {
            msg += "0";
        }
        msg += seconds;
        
        return msg;
    }
    
    /**
     * Fills a String list with empty Strings to achieve desired length
     * 
     * @param list the list for filling
     * @param end the length to achieve
     */
    public static LinkedList<String> reverseFillList(LinkedList<String> list, int end) {
        int lastPos = list.size() - 1;
        int spacesToFill = end - lastPos;
        for (int i = 0; i < spacesToFill; i++) list.add("");
        return list;
    }
    
}
