package Framework.Units;

public enum TimeUnits {
    SEC(1),
    MIN(60),
    HOUR(3600),
    mSEC(1.0 / 1000);

    TimeUnits(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversion() {
        return this.conversionFactor;
    }

    public double toBase(double value) {
        return value * this.conversionFactor;
    }

    public double toUnit(double value) {
        return value / conversionFactor;
    }

    private final double conversionFactor;
}
