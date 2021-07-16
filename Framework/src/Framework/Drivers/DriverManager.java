package Framework.Drivers;

import Framework.Error;
import Framework.Tasks.TaskManager;

import java.util.Arrays;

public final class DriverManager {
    public DriverManager(int driverAmount) {
        this.driverList = new Driver[driverAmount];
        this.driverAmount = driverAmount;

         Arrays.fill(driverList, null);
    }

    public Error addDriver(Driver driver) {
        if(driver.isActive())
            return DriverError.DRIVERS_ALREADY_INIT;

        if(this.driverList[driver.getType().getID()] != null)
            return DriverError.DRIVER_ALREADY_EXISTS;

        if(driver.isActive())
            return DriverError.DRIVER_ALREADY_ACTIVE;

        this.driverList[driver.getType().getID()] = driver;

        return DriverError.NO_ERROR;
    }
    public Error initDrivers() {
        if(this.isDriversInit)
            return DriverError.DRIVERS_ALREADY_INIT;

        for (Driver driver : this.driverList) {
            if(driver != null) {
                driver.start();
            }
        }

        this.isDriversInit = true;

        return DriverError.NO_ERROR;
    }
    public Error swapDriver(Driver driver) {
        if(driver.isActive())
            return DriverError.DRIVER_ALREADY_ACTIVE;

        if(!this.isDriversInit)
            return DriverError.DRIVERS_NOT_INIT;

        Driver currentDriver = driverList[driver.getType().getID()];

        currentDriver.terminate();

        driverList[driver.getType().getID()] = driver;
        driver.start();

        return DriverError.NO_ERROR;
    }
    public Error terminateDriver() {
        if (!this.isDriversInit)
            return DriverError.DRIVERS_NOT_INIT;

        for (Driver driver : this.driverList) {
            if (driver != null)
                driver.terminate();
        }

        Arrays.fill(driverList, null);
        this.isDriversInit = false;

        return DriverError.NO_ERROR;
    }

    public Driver getDriver(DriverType driver) {
        return driverList[driver.getID()];
    }

    private boolean isDriversInit;
    private Driver[] driverList;
    private int driverAmount;
}
