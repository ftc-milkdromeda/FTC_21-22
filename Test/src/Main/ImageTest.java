package Main;

import Framework.Images.ColorCurves;
import Framework.Images.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import Framework.Images.Pixel;

public class ImageTest {

    /**
     * test method
     * @param args not used
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException
    {
        FileInputStream imgBitmap = new FileInputStream("/Users/llai/IdeaProjects/FTC_21-22/Test/src/Main/bmp_24.bmp");
        Image testImage = new Image(imgBitmap);

        //cropping test
        testImage.cropImage(0,1,0,69);
        testImage.fileImage("test3");

        //color curve test
        testImage.curveImage(ColorCurves.INVERT);
        testImage.fileImage("test4");

        //resizing tests
        testImage.scaleImage(10,10);
        testImage.fileImage("test1");
        testImage.scaleImage(1000,1000);
        testImage.fileImage("test2");





    return;
    }
}
