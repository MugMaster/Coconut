package code.body;

import code.utilities.ImageHandler;
import code.utilities.FileManager;
import code.utilities.Format;
import code.utilities.Printer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URI;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CoconutKiller - description
 *
 * @author Teddy Macdonald
 * @since Jan 21, 2025
 */
public class Start {

    public static String user;
    public static String publicDesktop;
    public static File coconut;
    public static File refrenceCoconut;
    public static NoCoconutForm noCoconutForm;
    public static File permanentMemory;

    public static void main(String[] args) {
        Printer.setPrintMethod(Printer.SYSTEM_PRINT);

        Printer.printBox(" = Running Silly = ", Printer.STANDARD_BOX_BUFFER);

//        try {
//            System.out.println(Inet4Address.getLocalHost().getHostAddress());
//        } catch (UnknownHostException ex) {
//            Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
//        }
        //fileManager = new FileManager();

        
        //getPublicDesktop();
        //testGetInputStream();
        
        Printer.printBuff("Setup");
        user = FileManager.getUser();
        getMyDesktop(); // Don`t overide coputer desktop just yet ;)
        getPermanentMemory();
        
//        Printer.list(FileManager.read(permanentMemory), "lines");
//        Printer.print(FileManager.read(permanentMemory, 0));
        
        //testFileGet();
        
        System.out.println();
        
        getRefrenceCoconut();
        
        Printer.printBuff("Loop");
        while (true) {
            try {
                check();
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //testCopy();
        //testGetChildren("C:\\");
    }

    public static void check() {
        boolean match = checkCoconut();
        Printer.print(" - - - - - -  Found Match: " + match);
        if (!match && noCoconutForm == null) {
            Printer.print("Coconut not valid!");
            noCoconutForm = new NoCoconutForm();
        } else if (noCoconutForm != null) {
            noCoconutForm.tick();
        }
    }

    public static void getPublicDesktop() {
        final String publicDesktopString = "C:\\Users\\Public\\Desktop\\";

        if (Path.of(publicDesktopString) == null) {
            Printer.printErr("Could not find public desktop!");
            System.exit(1);
        } else {
            publicDesktop = publicDesktopString;
            Printer.print("Found desktop: " + publicDesktop);
        }
    }

    public static void getMyDesktop() {
        final String publicDesktopString = "C:\\Users\\" + user + "\\Desktop\\";
        if (Path.of(publicDesktopString) == null) {
            Printer.printErr("Could not find public desktop!");
            System.exit(1);
        } else {
            publicDesktop = publicDesktopString;
            Printer.print("Found desktop: " + publicDesktop);
        }
    }

    public static void generateCoconut() {
        FileManager.forceFileCopy(refrenceCoconut, publicDesktop, true);
    }

    public static void getRefrenceCoconut() {
        Printer.print("Getting refrence Coconut");
        
        refrenceCoconut = FileManager.getReasorseFile("media\\images\\coconut.png", "\\coconut.png", true);
        
        Printer.print("Got refrence Coconut: " + refrenceCoconut);
    }

    /**
     * Checks for COCONUT MATCH
     */
    public static boolean checkCoconut() {
        boolean foundCoconut = updateCoconut();
        if (!foundCoconut) {
            //System.out.println("No coconut found in " + publicDesktop);
            return false;
        }

        //System.out.println("refrenceCoconut.canRead(): " + refrenceCoconut.canRead());
        System.out.println("coconut.canRead(): " + coconut.canRead());

        boolean coconutMatch
                = ImageHandler.imageMatch(refrenceCoconut, coconut);

        return coconutMatch;
    }

    public static boolean updateCoconut() {
        final String coconutString = publicDesktop + "coconut.png";
        //Path coconutPath = Path.of(coconutString);
        //URI coconutURI = coconutPath.toUri();
        File cocounutFile = FileManager.getDirectFile(coconutString);

//        File cocounutFile
//                = Paths.get(coconutURI).toFile();
        if (!cocounutFile.exists()) {
            return false;
        }

        coconut = cocounutFile;
        Printer.print("Found a coconut at: "
                + cocounutFile.getAbsolutePath());

        //System.out.println("" + coconut);
        return true;
    }

    private static void testCopy() {
        String STARTUP_DIRECTORY = "C:\\Users\\"
                + System.getProperty("user.name")
                + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\"
                + "Programs\\Startup\\";
        URL TEST_FILE_URL = Start.class.getResource("\\media\\text\\note.txt");
        File TEST_FILE = null;

        try {
            URI TEST_FILE_URI = TEST_FILE_URL.toURI();
            TEST_FILE = new File(TEST_FILE_URI);
        } catch (URISyntaxException ex) {
            Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (TEST_FILE != null) {
            int status = FileManager.forceFileCopy(TEST_FILE, STARTUP_DIRECTORY, true);
            Printer.print("copy status: " + status);
        }
    }

    private static void testGetInputStream() {
        InputStream is = FileManager.getFileInputStream("media\\text\\fonts\\alarmClock.ttf", true);
        Printer.print("test InputStream: " + is);
    }

    private static void getPermanentMemory() {
        Printer.print("Found userFolderName: " + user);
        final String path = "C:\\Users\\" + user + "\\Documents\\perm.txt";
        
        Printer.print("Getting perm");
        permanentMemory = FileManager.createFile(path);
    }
    
    
}
