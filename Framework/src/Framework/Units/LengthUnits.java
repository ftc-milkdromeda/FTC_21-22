package Framework.Units;

public enum LengthUnits {
    NORMALIZEDUNIT(1),
    METER(1),
    cMETER(1.0 / 100),
    mMETER(1.0 / 1000),
    IN(0.3048006096012 / 12),
    FEET(0.3048006096012);

    LengthUnits(double conversionFactor) {
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
