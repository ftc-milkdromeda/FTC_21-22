package Framework.Units;

public enum AngleUnits {
    RAD(1),
    NORMALIZEDUNIT(1),
    DEG(Math.PI / 180);

    AngleUnits(double conversionFactor) {
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
