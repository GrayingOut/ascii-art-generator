import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import javax.imageio.ImageIO;

/**
 * The main class
 */
public final class Main {

    /**
     * Characters representing brightness
     */
    private static char[] characters = { ' ', '.', ',', ':', ';', '!', '|', '\'', '^', '"', '-', '~', '=', '<', '+', '*', '?', '%', '&', '$', '#', '@' };

    /**
     * The main method
     * 
     * @param args Command line arguments
     * @throws Exception If an exception occurs
     */
    public static void main(String[] args) throws Exception {
        /* Uses the path on the command line */
        getImageAsASCII(args[0]);
    }

    /**
     * Creates a new ASCII art from the image file path provided
     * 
     * @param filename The image file path
     * @throws Exception If an exception occurs
     */
    public static final void getImageAsASCII(String filename) throws Exception {
        /* Open the output file for writing */
        File outputImage = new File("image.out");
        OutputStreamWriter fos = new OutputStreamWriter(new FileOutputStream(outputImage), "UTF-8");

        /* Read the image file */
        BufferedImage image = ImageIO.read(new File(filename));
        
        /* Get the image information */
        Raster raster = image.getRaster();
        int bandCount = raster.getNumBands();

        /* Stores the current pixel */
        double[] pixel = new double[bandCount];

        /* For each pixel */
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                /* Get the pixel at (x,y) */
                raster.getPixel(x, y, pixel);
                char c = 0;
                
                /* Depending on the band, get the brightness of the pixel */
                if (bandCount == 4) { // RGBA
                    c = characters[(int)(brightness(pixel[0], pixel[1], pixel[2]) * characters.length)];
                } else if (bandCount == 3) { // RGB
                    c = characters[(int)(brightness(pixel[0], pixel[1], pixel[2]) * characters.length)];
                } else if (bandCount == 1) { // Black & White
                    c = characters[(int)(brightness(pixel[0], pixel[0], pixel[0]) * characters.length)];
                } else {
                    fos.close();
                    throw new UnsupportedOperationException("Operation does not support "+bandCount+" bands");
                }

                /* Write the character */
                fos.write(c);
            }

            /* End of row of pixels */
            fos.write('\r');
            fos.write('\n');
        }

        /* End of writing */
        fos.close();
    }

    /**
     * Gets the brightness of an rgb color
     * 
     * @param r The red value
     * @param g The green value
     * @param b The blue value
     * @return The brightness (0-1)
     */
    private static final double brightness(double r, double g, double b) {
        return Math.min((0.2126*r + 0.7152*g + 0.0722*b)/255, 1);
    }
}
