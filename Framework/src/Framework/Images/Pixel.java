package Framework.Images;

public class Pixel {
    //instance variables
    private int red, green, blue;

    //constructor
    public Pixel(int r, int g, int b)
    {
        red = r;
        green = g;
        blue = b;
    }
    public Pixel(Pixel other)
    {
        this(other.getRed(), other.getGreen(), other.getBlue());
    }
    public Pixel()
    {
        this(0,0,0);
    }
    //setters
    public void setPixel(int r, int g, int b)
    {
        red = r;
        green = g;
        blue = b;
    }

    /**
     * sets this pixel to another one, individually changing the color values to match
     * @param other the pixel it's going to be set to.
     */
    public void setPixel(Pixel other)
    {
        red = other.getRed();
        green = other.getGreen();
        blue = other.getBlue();
    }

    /**
     * add the values of this pixel and other.
     * @param other the pixel whose value will be added to this one.
     */
    public void addPixel(Pixel other)
    {
        this.red += other.getRed();
        this.green += other.getGreen();
        this.blue += other.getBlue();
    }
    //instance variable getters
    public int getRed()
    {
        return red;
    }
    public int getGreen()
    {
        return green;
    }
    public int getBlue()
    {
        return blue;
    }

    //instance variable setters
    public void setRed(int r){
        if(r > 255)
        {
            r = 255;
        }
        if(r < 0)
        {
            r = 0;
        }
        red = r; }
    public void setGreen(int g){
        if(g > 255)
        {
            g = 255;
        }
        if(g < 0)
        {
            g = 0;
        }
        green = g;}
    public void setBlue(int b){
        if(b > 255)
        {
            b = 255;
        }
        if(b < 0)
        {
            b = 0;
        }
        blue = b;}


}
