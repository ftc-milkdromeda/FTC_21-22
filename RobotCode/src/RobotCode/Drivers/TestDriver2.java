package RobotCode.Drivers;

import Framework.Drivers.Driver;

public class TestDriver2 extends Driver {
    public TestDriver2() {
        super(TestDriver2.driverType);
    }

    private static DriverDirectory driverType = DriverDirectory.TEST_DRIVER_2;

    public void doSomething() {
        if(!super.isActive())
            return;

        System.out.println("TestDriver2 did something.");
    }
}
