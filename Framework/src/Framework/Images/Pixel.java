package Framework.Images;

public interface Pixel {
    double getRed();
    double getGreen();
    double getBlue();

    double getLuminance();

    void setRed(double value);
    void setGreen(double value);
    void setBlue(double value);

    Pixel copy();

    void curveAll(Curve curve);
    void curveR(Curve curve);
    void curveG(Curve curve);
    void curveB(Curve curve);

    boolean isGray();

    static void RGB888ToPixel_converter(int pixel, Pixel output) {
        output.setRed((pixel >> 16 & 0xff) / 255.0);
        output.setGreen((pixel >> 8 & 0xff) / 255.0);
        output.setBlue((pixel & 0xff) / 255.0);
    }
    static int PixelToRGB888_converter(Pixel pixel) {
         return (int)(Math.round(pixel.getRed() * 255) << 16 | Math.round(pixel.getGreen() * 255) << 8 | Math.round(pixel.getBlue() * 255));
    }

    interface Curve {
        double evaluate(double input);
    }
}
