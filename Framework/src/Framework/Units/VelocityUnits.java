package Framework.Units;

public enum VelocityUnits {
    MET_SEC(1),
    MIL_HOUR(1609.344 / 3600);

    VelocityUnits(double conversionFactor) {
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
