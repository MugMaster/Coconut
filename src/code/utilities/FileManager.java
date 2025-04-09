package code.utilities;

import code.utilities.Printer;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * FileBuilder - description
 *
 * @author Teddy Macdonald
 * @since Jan 21, 2025
 */
public abstract class FileManager {

    /**
     * Copy command successful
     */
    public static final int COPY_SUCCESS = 0;
    /**
     * Copy command blocked by function
     */
    public static final int COPY_BLOCKED = 1;
    /**
     * Copy command failed (internal error)
     */
    public static final int COPY_FAILUARE = 2;

    /**
     * Overides existing value and adds new value to line
     */
    public final static int SET = 0;
    /**
     * Adds a value to a line and moves others back if they exist
     */
    public final static int ADD = 1;
    /**
     * Adds a value right after the last one, separated by a | character,
     * (useful for histories)
     */
    public final static int INSERT = 2;
    /**
     * Adds a value to line if no value is present
     */
    public final static int CHECK = 3;

    /**
     * Useful function that can override some Windows security features by
     * absolutely deep-frying the file input stream service
     *
     * @param file The file to copy to given path
     * @param target The path of the new directory
     * @param replace Weather or not to replace an existing file at that spot
     * @return an int that references FileManager copy messages
     */
    public static int forceFileCopy(
            File file,
            String target,
            boolean replace) {

        String name = file.getName();
        Path path = Path.of(target);

        // Check if the file already exists, if exist and replace are true
        // then retun that the copy command was blocked (not an error)
        final Path targetPath = Path.of(path + name);
        boolean exists = Files.exists(targetPath);
        if (exists && !replace) {
            return COPY_BLOCKED;
        }

        if (!exists) {
            try {
                Printer.print("[FileManager] No dir found for: " + targetPath
                        + ", made new dir");
                Files.createDirectories(targetPath.getParent()
                        .resolve(targetPath));
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName())
                        .log(Level.SEVERE, null, ex);
                Printer.printErr("[FileManager] Could not make new dir for: "
                        + targetPath);
                Printer.printErr("[FileManager] ^ " + ex);

                return COPY_FAILUARE;
            }
        }

        InputStream inputStream;

        try {
            inputStream = new FileInputStream(file);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName())
                    .log(Level.SEVERE, null, ex);
            Printer.printErr("[FileManager] Could not make inputStream for: "
                    + name);
            Printer.printErr("[FileManager] ^ " + ex);
            return COPY_FAILUARE;
        }

        // I don`t know why but this is the only way that works (/*o*)/ ~ |__|_|
        File outputF = new File(target + name);
        final Path OUTPUT_PATH = outputF.toPath();

        try {
            Files.copy(inputStream, OUTPUT_PATH, REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName())
                    .log(Level.SEVERE, null, ex);
            Printer.printErr("[FileManager] Could not copy inputStream: "
                    + inputStream + " to " + targetPath);
            Printer.printErr("[FileManager] ^ " + ex);
            return COPY_FAILUARE;
        }

        Printer.print("[FileManager] forceFileCopy() was able to place "
                + name + " in " + path);

        // If this is reached the copy command must have worked
        return COPY_SUCCESS;
    }

    /**
     * Gets children of a given directory
     *
     * @param dir the director to get children of
     * @param getSubDirChildren weather or not to get children of child folders
     * @return a Boolean that states if the check was successful
     */
    public static boolean getChildren(
            String dir,
            boolean getSubDirChildren,
            int depth) {

        Printer.print("[FileManager] Getting children of " + dir + ":");

        File file = new File(dir);

        if (dir == null) {
            Printer.printErr("[FileManager] getChildren() could not"
                    + " get children beacuse "
                    + dir + " is null!");
            return false;
        }

        if (!file.isDirectory()) {
            Printer.printErr("[FileManager] getChildren() could not"
                    + " get children beacuse "
                    + dir + " is not a directory!");
            return false;
        }

        String spaces = " -";
        for (int i = 0; i < depth; i++) {
            spaces += "-";
        }

        String[] names = file.list();
        if (names != null) {
            Printer.print("[FileManager] "
                    + spaces + " Found " + names.length + " files/directories");
        } else {
            Printer.print(spaces + dir + " is empty");
            return false;
        }

        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            File newFile = new File(dir + "\\" + name);
            Printer.print(spaces + "> " + newFile.getPath().toString());
            if (newFile.isDirectory() && getSubDirChildren) {

                depth++;
                Printer.print("[FileManager] "
                        + spaces + " A sub-directory was found: " + dir + name);

                getChildren(
                        newFile.getPath().toString(), getSubDirChildren, depth);
            }
        }

        return true;
    }

    /**
     * Create a file copy from a reasourse
     *
     * @param pathOfReasourse where the reasourse is in the reasourse path
     * @param newLocation is this is null the function will create a temp file
     * @return the new file
     */
    public static File getReasorseFile(String pathOfReasourse, String newLocation, boolean temp) {
        Printer.print("[FileManager] trying to get reasourse file  at: " + pathOfReasourse);
        InputStream is = getFileInputStream(pathOfReasourse, true);
        File file = convertInputStreamToFile(is, newLocation, temp);
        Printer.print("[FileManager] got new file as " + file + " at " + file.getAbsolutePath());
        return file;
    }

    /**
     * Gets a File at an established location in memory
     *
     * @param path the path of the File
     * @return File found
     */
    public static File getDirectFile(String path) {
        File file = Paths.get(path, "").toFile();

        if (file == null || !file.exists()) {
            Printer.print("[FileManager] did not find file at: " + path);
        } else {
            Printer.print("[FileManager] Found File: " + file);
        }

        return file;
    }

    /**
     * Makes a new file at a specified location. If one is already present it
     * will return the existing one
     *
     * @param path the path of the new file
     * @return the file created OR found
     */
    public static File createFile(String path) {
        Printer.print("[FileManager] making file: " + path);

        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Printer.printErr("[FileManager] Failed file creation!");
                Printer.printErr("[FileManager] ^ " + ex);
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return file;
    }

    /**
     * Gets a fileInSt at a given String path
     *
     * @param path the path as a String
     * @param isReasourse if the file is contained in the project or build
     * @return the file InSt found
     */
    public static InputStream getFileInputStream(String path, boolean isReasourse) {
        if (isReasourse) {

            InputStream fileInputStream = FileManager.class.getClassLoader().getResourceAsStream(path);
            if (fileInputStream == null) {
                Printer.printErr("[FileManager] fileInputStream is null");
            } else {
                Printer.print("[FileManager] fileInputStream: " + fileInputStream);
            }
            return fileInputStream;

        } else {
            Printer.printErr("[FileManager] sorry, this build does not allow"
                    + " non reasourse InputStream(s)");
            return null;
        }

    }

    /**
     * Converts a set of binary InputStream data to an existing file at a
     * specified location. If you wish to make a temp it true the function will
     * make a folder called temp, place the File in it, and use newLocation as
     * the NAME of the File.
     *
     * @param is the input stream to read from
     * @param newLocation the location or name of temp File
     * @param temp if the File is to be deleted on close
     * @return the File created
     */
    public static File convertInputStreamToFile(InputStream is, String newLocation, boolean temp) {
        if (temp) {
            Printer.print("[FileManager] requestet temp file: " + newLocation);
            createDirectory("C:\\temp", true);
            newLocation = "C:\\temp" + newLocation;

            Printer.print("[FileManager] placaing in: " + newLocation);
        }

        Path path = Paths.get(newLocation);

        try {
            Printer.print("[FileManager] found path: " + path);
            File file = getDirectFile(newLocation);
            if (file.exists()) {
                return file;
            }

            Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
            Printer.print("[FileManager] copy succsess!");
            file = getDirectFile(newLocation);
            boolean exists = file.exists();
            if (!exists) {
                Printer.printErr("[FileManager] file does not exist; Pray.");
            }

            Printer.print("[FileManager] file: " + file);
            if (temp) {
                file.deleteOnExit();
            }

            return file;
        } catch (IOException ex) {
            Printer.printErr("[FileManager] failed InputStreamConversion");
            Printer.printErr("[FileManager] ^ " + ex);
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    /**
     * Creates or finds a directory (folder)
     *
     * @param path the path of the new directory
     * @param temp if the directory is deleted on exit
     * @return the directory created or found
     */
    public static File createDirectory(String path, boolean temp) {
        Path pathPath = Paths.get(path);

        boolean exists = Files.exists(pathPath);
        Printer.print("[FileManager] path to: " + path + " exists = " + exists);
        if (exists == true) {
            File directory = getDirectFile(path);
            if (temp) {
                directory.deleteOnExit();
            }
            return directory;
        }

        Path newDirectory;
        try {
            newDirectory = Files.createDirectories(pathPath.getParent().resolve(path));
            Printer.print("[FileManager] created new directory: " + newDirectory);
            File directory = getDirectFile(path);
            if (temp) {
                directory.deleteOnExit();
            }
            return directory;
        } catch (IOException ex) {
            Printer.printErr("[FileManager] could not create new directory: " + path);
            Printer.printErr("[FileManager] ^ " + ex);
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    /**
     * Gets the name of the user`s folder (not name)
     *
     * @return the user folder name
     */
    public static String getUser() {
        String userDir = System.getProperty("user.dir");
        Printer.print("Found userDir: " + userDir);
        String split[] = userDir.split("\\" + "\\");
        String user = split[2];
        return user;
    }

    public static LinkedList<String> read(File target) {
        Printer.print("[File Manager] reading all lines in " + target.getName());
        
        LinkedList<String> data = new LinkedList();
        
        try {
            // Open the file
            FileInputStream fstream = new FileInputStream(target.getPath());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;

            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                // Print the content on the console
                data.add(strLine);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return data;
    }

    /**
     * Reads a specific file line
     *
     * @param target the target File
     * @param line the line to read
     * @return the line read as a String
     */
    public static String read(File target, int line) {
        LinkedList<String> list = read(target);
        String data = list.get(line);
        return data;
    }
    
    /**
     * evil function that destroys your code
     */
    public static void evilFunctionThatDestroysYourCode() {
        evilFunctionThatDestroysYourCode();
    }
}
