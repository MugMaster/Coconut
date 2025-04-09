/** Required package class namespace */
package code.utilities;

import java.awt.Color;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;


/**
 * Printer - Nice little printing class, great for testing and
 * clean output formatting
 * 
 * @author Teddy Macdonald
 * @since Feb 26, 2025
 */
public abstract class Printer {
    
    public static final int SYSTEM_PRINT = 0;
    public static final int JOPTION_PANE = 1;
    private static final int PRINT_BUFF_LENGTH = 100;
    public static final int STANDARD_BOX_BUFFER = 3;
    private static int method = SYSTEM_PRINT;
    
    public static void setPrintMethod(int method) {
        Printer.method = method;
    }
    
    /**
     * Normal print function
     * 
     * @param msg the msg to print
     */
    public static void print(String msg) {
        masterPrint(msg, false, null, null);
    }
    
    /**
     * Prints a msg with a set color
     * 
     * @param msg the msg to print
     * @param background the background color
     * @param foreground the foreground color
     */
    public static void print(String msg, Color background, Color foreground) {
        masterPrint(msg, false, background, foreground);
    }
    
    /**
     * Prints an err msg
     * 
     * @param msg the msg to print
     */
    public static void printErr(String msg) {
        masterPrint(msg, true, null, null);
    }
    
    /**
     * Root printing function for the printer class
     * 
     * @param msg the msg to display
     * @param exeption if the msg is an err
     * @param background the background color
     * @param foreground the foreground color
     */
    private static void masterPrint(
            String msg, 
            boolean exeption, 
            Color background, 
            Color foreground) {
        
        if (method == SYSTEM_PRINT) {
            if (exeption) System.err.println(msg);
            else System.out.println(msg);
        } else if (method == JOPTION_PANE) {
            if (exeption) {
                foreground = Color.red;
                background = Color.black;
            }
            JOptionPane.showMessageDialog(
                    null, 
                    formatTextArea(msg, background, foreground));
        }
    }
    
    /**
     * Makes a text area with a background and foreground color
     * 
     * @param msg the msg to be contained
     * @param background the background color
     * @param foreground the foreground color
     * @return the new text area
     */
    private static JTextArea formatTextArea(
            String msg, 
            Color background, 
            Color foreground) {
        
        JTextArea area = new JTextArea();
        if (foreground != null) area.setForeground(foreground);
        if (background != null) area.setBackground(background);
        
        area.setText(msg);
        
        return area;
    }
    
    /**
     * Prints a buffer of periods
     */
    public static void printBuff() {
        if (method == SYSTEM_PRINT) {
            for (int i = 0; i < PRINT_BUFF_LENGTH; i++) {
                System.out.print(".");
            }
            System.out.println("\n");
        }
    }

    /**
     * Prints a buffer with a message in it
     *
     * @param msg the message to display
     */
    public static void printBuff(String msg) {
        int msgLength = msg.length();
        int repeatAmmount = ((PRINT_BUFF_LENGTH / 2) - (msgLength / 2));

        System.out.println("");
        if (method == SYSTEM_PRINT) {
            for (int i = 0; i < repeatAmmount; i++) {
                System.out.print(".");
            }

            System.out.print(msg);
            if (Numbers.isOdd(msgLength)) {
                repeatAmmount--;
            }

            for (int i = 0; i < repeatAmmount; i++) {
                System.out.print(".");
            }
            System.out.println("");
        }
    }
    
    /**
     * Prints a box with a message in it
     *
     * @param msg the message to display
     */
    public static void printBox(String msg, int horizontalBuffer) {

        int msgLength = msg.length();
        int lineLength = msgLength + (horizontalBuffer * 2);

        String boxMsg = "    ";
        for (int i = 0; i < lineLength; i++) {
            boxMsg += "_";
        }
        boxMsg += "\n";

        boxMsg += "   |";
        for (int i = 0; i < horizontalBuffer; i++) {
            boxMsg += " ";
        }
        boxMsg += msg;
        for (int i = 0; i < horizontalBuffer; i++) {
            boxMsg += " ";
        }
        boxMsg += "|\n";

        boxMsg += "   |";
        for (int i = 0; i < lineLength; i++) {
            boxMsg += "_";
        }
        boxMsg += "|";

        print(boxMsg);
    }
    
    /**
     * Prints LinkedList contents
     * 
     * @param list the list to print
     * @param name the name of the list
     */
    public static void list(LinkedList list, String name) {
        print(" ----- " + name + " ----- ");
        
        for (int i = 0; i < list.size(); i++) {
            print(" --- " + list.get(i).toString());
        }
    }
}
