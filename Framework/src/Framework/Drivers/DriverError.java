package Framework.Drivers;

import Framework.Error;

public enum DriverError implements Error {
    DRIVERS_NOT_INIT,
    DRIVERS_ALREADY_INIT,

    DRIVER_ALREADY_EXISTS,
    DRIVER_DOES_NOT_EXIST,

    DRIVER_ALREADY_ACTIVE,
    DRIVER_NOT_ACTIVE,

    NO_ERROR;

    DriverError() {
        this.source = "FRAMEWORK_DRIVERS";
    }

    @Override
    public String getSource() {
        return this.source;
    }

    @Override
    public String toString() {
        return this.source + ": " + super.toString();
    }

    private String source;
}

//todo make into interface so client code can add custom error entries.
