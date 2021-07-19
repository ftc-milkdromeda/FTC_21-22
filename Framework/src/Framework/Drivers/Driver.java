package Framework.Drivers;

import Framework.Error;
import Framework.GeneralError;

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

    Error start() {
        if(isActive)
            return DriverError.DRIVER_ALREADY_ACTIVE;

        Error output = this.init();
        if(output != GeneralError.NO_ERROR)
            return output;

        this.isActive = true;

        return GeneralError.NO_ERROR;
    }
    Error terminate() {
        if(!isActive)
            return DriverError.DRIVER_NOT_ACTIVE;

        Error output = this.destructor();
        if(output != GeneralError.NO_ERROR)
            return output;

        this.isActive = false;

        return GeneralError.NO_ERROR;
    }

    protected Error destructor() {
        return GeneralError.NO_ERROR;
    }
    protected Error init() {
        return GeneralError.NO_ERROR;
    }

    protected DriverType driverType;
    private boolean isActive;
}
