package Framework.Units;

public enum AngularVelocityUnits {
    NORMALIZEDUNIT(1),
    RAD_SEC(1),
    REV_MIN(2 * Math.PI / 60);

    AngularVelocityUnits(double conversionFactor) {
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
