package Framework.Drivers;

public abstract class Driver {
    protected Driver(DriverType type) {
        this.isActive = false;
        this.driverType = type;
    }

    public String toString() {
        return "Driver: " + this.driverType.getID() + " " + driverType;
    }
    public DriverType getType() {
        return this.driverType;
    }

    public boolean isActive() {
        return this.isActive;
    }

    DriverError start() {
        if(isActive)
            return DriverError.DRIVER_ALREADY_ACTIVE;

        DriverError output = this.init();
        if(output != DriverError.NO_ERROR)
            return output;

        this.isActive = true;

        return DriverError.NO_ERROR;
    }
    DriverError terminate() {
        if(!isActive)
            return DriverError.DRIVER_NOT_ACTIVE;

        DriverError output = this.destructor();
        if(output != DriverError.NO_ERROR)
            return output;

        this.isActive = false;

        return DriverError.NO_ERROR;
    }

    protected DriverError destructor() {
        return DriverError.NO_ERROR;
    }
    protected DriverError init() {
        return DriverError.NO_ERROR;
    }

    protected DriverType driverType;
    private boolean isActive;
}
