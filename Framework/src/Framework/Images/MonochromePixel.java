package Framework.Images;

/**
 * Extends the Pixel class to only have one color, for grayscale and memory saving purposes.
 */
public class MonochromePixel implements Pixel {
    public MonochromePixel() {}
    public MonochromePixel(double luminance) {
        this.value = luminance;
    }
    public MonochromePixel(MonochromePixel o) {
        this.value = o.value;
    }

    @Override
    public double getRed() {
        return value;
    }
    @Override
    public double getGreen() {
        return value;
    }
    @Override
    public double getBlue() {
        return value;
    }
    @Override
    public double getLuminance() {
        return value;
    }
    @Override
    public void setRed(double value) {
        this.value = value;
    }
    @Override
    public void setGreen(double value) {
        this.value = value;
    }
    @Override
    public void setBlue(double value) {
        this.value = value;
    }

    @Override
    public Pixel copy() {
        return new MonochromePixel(this);
    }

    @Override
    public void curveAll(Curve curve) {
        this.value = curve.evaluate(this.value);
    }
    @Override
    public void curveR(Curve curve) {
        this.value = curve.evaluate(this.value);
    }
    @Override
    public void curveG(Curve curve) {
        this.value = curve.evaluate(this.value);
    }
    @Override
    public void curveB(Curve curve) {
        this.value = curve.evaluate(this.value);
    }

    @Override
    public boolean isGray() {
        return true;
    }

    public void setLuminance(double value) {
        this.value = value;
    }

    public RGBPixel toRGB() {
        return new RGBPixel(value, value, value);
    }

    double value = 0;
}
