package RobotCode.Drivers;

import Framework.Drivers.Driver;

public class TestDriver1 extends Driver {
    public TestDriver1() {
        super(TestDriver1.driverType);
    }

    private static DriverDirectory driverType = DriverDirectory.TEST_DRIVER_1;

    public void doSomething() {
        if(!super.isActive())
            return;
        System.out.println("TestDriver1 did something.");
    }
}
