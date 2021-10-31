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
        FileInputStream imgBitmap = new FileInputStream("/Users/llai/IdeaProjects/FTC_21-22/Test/src/Main/tryfile.bmp");
        Image testImage = new Image(imgBitmap);




        //resizing tests

        testImage.scaleImage(0.5,0.5);
        testImage.fileImage("test0");

        testImage.scaleImage(2.0,2.0);
        testImage.fileImage("test1");

        testImage.scaleImage(1.567,1.567);
        testImage.fileImage("test2");

        testImage.scaleImage(3.9,0.14);
        testImage.fileImage("test3");


        testImage.scaleImage(0.5,0.5);
        testImage.fileImage("test4");





    return;
    }
}
