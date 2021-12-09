package Framework.Drivers;

import Framework.Error;

public enum DriverError implements Error {
    DRIVERS_NOT_INIT,
    DRIVERS_ALREADY_INIT,

    DRIVER_ALREADY_EXISTS,
    DRIVER_DOES_NOT_EXIST,

    DRIVER_ALREADY_ACTIVE,
    DRIVER_NOT_ACTIVE,

    DRIVER_ALREADY_BOUND,
    DRIVER_NOT_BOUND_TO_TASK,

    DRIVER_IS_INTERRUPTED;

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

