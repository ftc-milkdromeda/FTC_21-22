package Framework.Drivers;

import Framework.Error;
import Framework.GeneralError;

import java.util.Arrays;

/**
 * @author Tyler Wang
 * Class which stores and manages implementations of different {@link Driver}.
 */
public final class DriverManager {
    /**
     * A constructor that initializes local variables.
     * @param driverAmount The total amount of {@link Driver} types in a given implementation.
     */
    public DriverManager(int driverAmount) {
        this.driverList = new Driver[driverAmount];
        this.driverAmount = driverAmount;

         Arrays.fill(driverList, null);
    }

    /**
     * Adds the {@link Driver} passed in to function to a set of {@link Driver Drivers}.
     * @param driver The {@link Driver} that is to be added to the set.
     * @return DRIVER_ALREADY_INIT: the {@link Driver} to be added has already been initialized elsewhere. {@link DriverError#DRIVER_ALREADY_EXISTS}: the type of {@link Driver} to be added has already been added to the set. {@link GeneralError#NO_ERROR}: if function returns with no errors
     */
    public Error addDriver(Driver driver) {
        if(driver.isActive())
            return DriverError.DRIVERS_ALREADY_INIT;

        if(this.driverList[driver.getType().getID()] != null)
            return DriverError.DRIVER_ALREADY_EXISTS;

        this.driverList[driver.getType().getID()] = driver;

        return GeneralError.NO_ERROR;
    }

    /**
     * Initializes all {@link Driver Drivers} in the set.
     * @return {@link DriverError#DRIVERS_ALREADY_INIT}: drivers in the set have already been initialized. {@link GeneralError#NO_ERROR}: function exits without any errors.
     */
    public Error initDrivers() {
        if(this.isDriversInit)
            return DriverError.DRIVERS_ALREADY_INIT;

        for (Driver driver : this.driverList) {
            if(driver != null) {
                driver.start();
            }
        }

        this.isDriversInit = true;

        return GeneralError.NO_ERROR;
    }

    /**
     * Performs a swap for the specific type of {@link Driver} that the {@link Driver} is passed into the function.
     * @param driver The {@link Driver} that will replace the old {@link Driver}.
     * @return {@link DriverError#DRIVERS_ALREADY_INIT}: if {@link Driver} input is already activated. {@link DriverError#DRIVERS_NOT_INIT}: the set of {@link Driver Drivers} has not been initialized. {@link GeneralError#NO_ERROR}: program exited without any errors.
     */
    public Error swapDriver(Driver driver) {
        if(driver.isActive())
            return DriverError.DRIVERS_ALREADY_INIT;

        if(!this.isDriversInit)
            return DriverError.DRIVERS_NOT_INIT;

        Driver currentDriver = driverList[driver.getType().getID()];

        currentDriver.terminate();

        driverList[driver.getType().getID()] = driver;
        driver.start();

        return GeneralError.NO_ERROR;
    }

    /**
     * Terminates all {@link Driver Drivers} in the set.
     * @return {@link DriverError#DRIVERS_NOT_INIT}: when the set of {@link Driver Drivers} has not been initialized. {@link GeneralError#NO_ERROR}: Function exited without any errors.
     */
    public Error terminateDriver() {
        if (!this.isDriversInit)
            return DriverError.DRIVERS_NOT_INIT;

        for (Driver driver : this.driverList) {
            if (driver != null)
                driver.terminate();
        }

        Arrays.fill(driverList, null);
        this.isDriversInit = false;

        return GeneralError.NO_ERROR;
    }

    /**
     * Returns the {@link Driver} of type {@link Driver} that is found in the set.
     * @param driver The type of {@link Driver} that is to be returned.
     * @return A {@link Driver} that is of type driver. If null, {@link Driver} is not initialized in this set.
     */
    public Driver getDriver(DriverType driver) {
        return driverList[driver.getID()];
    }

    private boolean isDriversInit;
    private Driver[] driverList;
    private int driverAmount;
}
