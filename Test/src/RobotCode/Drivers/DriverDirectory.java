package RobotCode.Drivers;

import Framework.Drivers.DriverType;

public enum DriverDirectory implements DriverType {
    TEST_DRIVER_1,
    TEST_DRIVER_2,
    MEC_DRIVER,
    NULL_TYPE;

    public int getID() {
        return driverID;
    }

    DriverDirectory() {
        driverID = Counter.addMember();
    }
    private int driverID;

    private abstract static class Counter {
        synchronized static int addMember() {
            return DriverDirectory.Counter.count++;
        }
        static int count = 0;
    }

}
