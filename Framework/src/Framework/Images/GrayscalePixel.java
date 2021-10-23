package Framework.Images;
import java.awt.Image;
public class GrayscalePixel extends Pixel{

    public GrayscalePixel(int c){
        super(c,c,c);

    }
    public GrayscalePixel(Pixel other){

        int c = (other.getRed() + other.getBlue() + other.getGreen())/3;
        setPixel(c,c,c);

    }
    public void setPixel(int r,int g,int b)
    {
        int c =  (r + g + b) / 3;
        super.setPixel(c,c,c);
    }
}
