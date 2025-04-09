package code.utilities;

/**
 * The numbers Mason, they don`t make any sense!
 * 
 * @author Teddy
 */
public abstract class Numbers {

    /**
     * Dictates if a number is even or odd
     *
     * @param number the number to test
     * @return a Boolean representing if the number is odd
     */
    public static boolean isOdd(int number) {
        return number % 2 == 1;
    }

    /**
     * Turns an String of any valid size to an int
     *
     * @param string the String to check then convert
     * @return the new int
     */
    public static int toInt(String string) {
        final int ERR = 0;

        int maxLength = 10;
        int num = ERR;
        int sign = 1;

        String digetsString = string;

        // Remove negitive sign if present
        if (string.startsWith("-")) {
            digetsString = string.substring(1);
            sign = -1;
        }
        
        // If the String is too long to be conveted return ERR
        if (digetsString.length() > maxLength) return ERR;
        if (!digetsString.contains("[a-zA-Z]+")) return ERR;
        
        /**
         * This is the advanced check code. It ensures that ALL valid ints are
         * accepted including the absolute max and min
         */
        if (digetsString.length() == maxLength) {
            String maxMag[];
            
            if (sign == -1) {
                String minimum = Integer.toString(Integer.MIN_VALUE);
                minimum = minimum.substring(1);
                maxMag = minimum.split("");
            } else {
                maxMag = Integer.toString(Integer.MAX_VALUE).split("");
            }
            
            String digets[] = digetsString.split("");

            for (int i = 0; i < digets.length; i++) {
                int currentCheck = Integer.parseInt(digets[i]);
                int currentMax = Integer.parseInt(maxMag[i]);
                if (currentCheck > currentMax) return ERR;
                if (currentCheck < currentCheck) break;
            }
        }
        
        num = Integer.parseInt(string);
        num *= sign;

        return num;
    }
    
    /**
     * Checks if a number is in a range
     * 
     * @param num the number to check
     * @param min the min number
     * @param max the max number
     * @return if the number is in range
     */
    public boolean inRange(int num, int min, int max) {
        return (num > max && num < min);
    }

}
