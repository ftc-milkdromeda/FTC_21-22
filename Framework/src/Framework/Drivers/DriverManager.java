package Framework.Drivers;

public class DriverManager {
    public static DriverManager getInstance() {
        if(hasInstance)
            return null;

        return new DriverManager();
    }

    private DriverManager() {
        this.driverList = new Driver[DriverCounter.getCount()];

        for(Driver driver : this.driverList)
            driver = null;
    }

    public DriverError addDriver(Driver driver) {
        if(driver.isActive())
            return DriverError.DRIVERS_ALREADY_INIT;

        if(this.driverList[driver.getType().getID()] != null)
            return DriverError.DRIVER_ALREADY_EXISTS;

        if(driver.isActive())
            return DriverError.DRIVER_ALREADY_ACTIVE;

        this.driverList[driver.getType().getID()] = driver;

        return DriverError.NO_ERROR;
    }
    public DriverError initDrivers() {
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
    public DriverError swapDriver(Driver driver) {
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
    public DriverError terminateDriver() {
        if (!this.isDriversInit)
            return DriverError.DRIVERS_NOT_INIT;

        for (Driver driver : this.driverList) {
            if (driver != null)
                driver.terminate();
        }

        for(int i = 0; i < this.driverList.length; i++)
            this.driverList[i] = null;

        this.isDriversInit = false;

        return DriverError.NO_ERROR;
    }

    public Driver getDriver(DriverType driver) {
        return driverList[driver.getID()];
    }

    private boolean isDriversInit;
    private Driver[] driverList;

    private static boolean hasInstance = false;
}
