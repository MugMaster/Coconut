/** Required package class namespace */
package code.utilities;

import code.body.Start;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


/**
 * ImageHandler - Contains functions that involve images
 * 
 * @author Teddy Macdonald
 * @since Feb 3, 2025
 */
public abstract class ImageHandler 
{
    
    /**
     * Checks if two images match
     * 
     * @param referenceImageFile the file to reference to
     * @param checkedImageFile the file file to check
     * @return if the reference image matches the reference image
     */
    public static boolean imageMatch(File refrenceImagefile, File checkedImageFile) {
        final int TOLERANCE = 0;
        
        if (refrenceImagefile == null) return false;
        if (checkedImageFile == null) return false;
        
        try {
            BufferedImage checkedImage = ImageIO.read(checkedImageFile);
            BufferedImage refrenceImage = ImageIO.read(refrenceImagefile);
            
            int checkedWidth = checkedImage.getWidth();
            int refrencedWidth = refrenceImage.getWidth();
            int checkedHeight = checkedImage.getHeight();
            int refrencedHeight = refrenceImage.getHeight();
            
            if ((checkedWidth != refrencedWidth) 
                    || (checkedHeight != refrencedHeight)) return false;
            
            long difference = 0;

            for (int y = 0; y < checkedHeight; y++) {
 
                for (int x = 0; x < checkedWidth; x++) {

                    int rgbA = checkedImage.getRGB(x, y);
                    int rgbB = refrenceImage.getRGB(x, y);
                    int redA = (rgbA >> 16) & 0xff;
                    int greenA = (rgbA >> 8) & 0xff;
                    int blueA = (rgbA) & 0xff;
                    int redB = (rgbB >> 16) & 0xff;
                    int greenB = (rgbB >> 8) & 0xff;
                    int blueB = (rgbB) & 0xff;

                    difference += Math.abs(redA - redB);
                    difference += Math.abs(greenA - greenB);
                    difference += Math.abs(blueA - blueB);
                }
            }

            double total_pixels = checkedWidth * checkedHeight * 3;
            
            double avg_different_pixels
                    = difference / total_pixels;

            double percentage
                    = (avg_different_pixels / 255) * 100;
            
            if (percentage > TOLERANCE) return false;
            
        } catch (IOException ex) {
            Logger.getLogger
            (ImageHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }
    
    /**
     * Gets a buffered image from classPath
     * 
     * @param path the path of the image
     * @return the image as a BufferedImage
     */
    public static BufferedImage getBufferedImmageFromJar(String path) {
        try {
            return ImageIO.read(Start.class.getClassLoader().getResource(path));
        } catch (IOException ex) {
            Printer.printErr("Immage get err: " + ex);
            Logger.getLogger(ImageHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    
}
