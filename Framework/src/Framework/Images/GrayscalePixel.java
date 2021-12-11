package Framework.Images;
import java.awt.Image;

/**
 * Extends the Pixel class to only have one color, for grayscale and memory saving purposes.
 */
public class GrayscalePixel extends Pixel{

    /**
     * instance variables
     */
    int color;

    /**
     *constructor, takes a single pixel value.
     * @param c
     */
    public GrayscalePixel(int c){
        color = c;

    }

    /**
     * Constructor that converts another pixel to grayscale.
     * @param other
     */
    public GrayscalePixel(Pixel other){

        int c = (other.getRed() + other.getBlue() + other.getGreen())/3;
        color = c;

    }

    /**
     * takes the average of three input colors, and sets this pixel to that average.
     * @param r first input
     * @param g second input
     * @param b last input
     */
    public void setPixel(int r,int g,int b)
    {
        int c =  (r + g + b) / 3;
        color = c;
    }

    /**
     * converts another pixel to grayscale, then sets this pixel to that.
     * @param other the pixel it's going to be set to.
     */
    public void setPixel(Pixel other)
    {
        int c =  (other.getRed() + other.getBlue() + other.getGreen()) / 3;
        color = c;
    }

    //getters for the values of each pixel. It'll be the same for each,
    // so this is just in case.
    public int getRed()
    {
        return color;
    }
    public int getGreen()
    {
        return color;
    }
    public int getBlue()
    {
        return color;
    }

    //setters. Since there's only one color in the pixel,
    //all of these will just set the grayness.
    public void setRed(int r){
        if(r > 255)
        {
            r = 255;
        }
        if(r < 0)
        {
            r = 0;
        }
        color = r; }
    public void setGreen(int g){
        if(g > 255)
        {
            g = 255;
        }
        if(g < 0)
        {
            g = 0;
        }
        color = g;}
    public void setBlue(int b){
        if(b > 255)
        {
            b = 255;
        }
        if(b < 0)
        {
            b = 0;
        }
        color = b;}
}
