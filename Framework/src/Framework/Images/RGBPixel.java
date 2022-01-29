package Framework.Images;

public class RGBPixel implements Pixel{
    public RGBPixel(double r, double g, double b) {
        this.red = r;
        this.green = g;
        this.blue = b;
    }
    public RGBPixel(RGBPixel o) {
        this.red = o.red;
        this.green = o.green;
        this.blue = o.blue;
    }
    public RGBPixel(){}

    @Override
    public double getRed() {
        return this.red;
    }
    @Override
    public double getGreen() {
        return this.green;
    }
    @Override
    public double getBlue() {
        return this.blue;
    }
    @Override
    public double getLuminance() {
        return this.red * 0.299 + this.green * 0.587 + this.blue * 0.114;
    }

    @Override
    public void setRed(double value) {
        this.red = value;
    }
    @Override
    public void setGreen(double value ) {
        this.green = value;
    }
    @Override
    public void setBlue(double value) {
        this.blue = value;
    }

    @Override
    public Pixel copy() {
        return new RGBPixel(this);
    }

    @Override
    public void curveAll(Curve curve) {
        this.curveR(curve);
        this.curveG(curve);
        this.curveB(curve);
    }
    @Override
    public void curveR(Curve curve) {
        this.red = curve.evaluate(this.red);

        this.red = this.red > 1 || this.red < 0 ? this.red < 0 ? 0 : 1 : this.red;
    }
    @Override
    public void curveG(Curve curve) {
        this.green= curve.evaluate(this.green);
        this.green = this.green > 1 || this.green < 0 ? this.green < 0 ? 0 : 1 : this.green;
    }
    @Override
    public void curveB(Curve curve) {
        this.blue = curve.evaluate(this.blue);

        this.blue = this.blue > 1 || this.blue < 0 ? this.blue < 0 ? 0 : 1 : this.blue;

    }

    @Override
    public boolean isGray() {
        return false;
    }

    public MonochromePixel toMonochrome() {
        return new MonochromePixel(this.getLuminance());
    }

    double red = 0;
    double green = 0;
    double blue = 0;
}
