package Main;

import Framework.Images.Image;
import Framework.QRCode.QR;

public class QRCode {
    public static void main(String[] args) {
        Image image = new Image();
        System.out.println(image.readImage("test1.png"));
        QR.testQR(image);
        image.saveImage("OutputImage1.png");

        System.out.println(image.readImage("test2.png"));
        QR.testQR(image);
        image.saveImage("OutputImage2.png");

        System.out.println(image.readImage("test3.png"));
        QR.testQR(image);
        image.saveImage("OutputImage3.png");

        System.out.println(image.readImage("test4.png"));
        QR.testQR(image);
        image.saveImage("OutputImage4.png");

        System.out.println(image.readImage("test5.png"));
        QR.testQR(image);
        image.saveImage("OutputImage5.png");

        image.readImage("QRCodev3.png");
        QR.testQR(image);
        image.saveImage("OUTPUT.png");

    }
}
