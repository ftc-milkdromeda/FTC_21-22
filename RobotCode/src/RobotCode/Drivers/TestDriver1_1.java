package RobotCode.Drivers;

import Framework.Drivers.Driver;
import Framework.Tasks.Task;
import Framework.GeneralError;

public class TestDriver1_1 extends Driver {
    public TestDriver1_1() {
        super(TestDriver1_1.driverType);
    }

    private static DriverDirectory driverType = DriverDirectory.TEST_DRIVER_1;

    public void doSomething(Task task) {
        if(super.validateCall(task) != GeneralError.NO_ERROR)
            return;
        System.out.println("TestDriver1 did nothing.");
    }
}
