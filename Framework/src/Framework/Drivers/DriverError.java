package Framework.Drivers;

public enum DriverError {
    //enum value declaration
    DRIVERS_NOT_INIT,
    DRIVERS_ALREADY_INIT,

    DRIVER_ALREADY_EXISTS,
    DRIVER_DOES_NOT_EXIST,

    DRIVER_ALREADY_ACTIVE,
    DRIVER_NOT_ACTIVE,

    NO_ERROR;

    //enum function declaration
    public int getValue() {
        return this.value;
    }

    DriverError() {
        this.value = DriverErrorCounter.addMember();
    }

    private abstract static class DriverErrorCounter {
        synchronized static int addMember() {
            return DriverErrorCounter.count++;
        }
        static int count = 0;
    }

    private int value;

    private static int count = 0;
}

//todo make into interface so client code can add custom error entries.
