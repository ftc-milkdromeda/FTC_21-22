package Main;

import Framework.Error;
import Framework.GeneralError;
import Framework.Images.Image;
import Framework.Images.Pixel;
import Framework.Images.RGBPixel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImageTest {

    /**
     * test method
     * @param args not used
     */
    public static void main(String[] args) {
        Image testImage = new Image();
        Error error = testImage.readImage("test.png");

        if(error != GeneralError.NO_ERROR)
            System.out.println(error);


        long startTime = System.currentTimeMillis();

        /*
        testImage.scaleImage(0.5,0.5);
        testImage.saveImage("test0");

        testImage.scaleImage(2.0,2.0);
        testImage.saveImage("test1");

        testImage.scaleImage(1.567,1.567);
        testImage.saveImage("test2");

        testImage.scaleImage(3.9,0.14);
        testImage.saveImage("test3");

        testImage.scaleImage(0.5,0.5);
        testImage.saveImage("test4");


         */

        //testImage.cropImage(350, 750, 700, 100);

        testImage.curveAdjAll(x -> 1/(1+Math.exp(-23*(x-0.4))));

        testImage.saveImage("croptest.png");


    }

}
