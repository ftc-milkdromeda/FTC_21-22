package RobotCode.Drivers;

import Framework.Drivers.Driver;
import Framework.Tasks.Task;
import Framework.GeneralError;

public class TestDriver2 extends Driver {
    public TestDriver2() {
        super(TestDriver2.driverType);
    }

    private static DriverDirectory driverType = DriverDirectory.TEST_DRIVER_2;

    public void doSomething(Task task) {
        if(super.validateCall(task) != GeneralError.NO_ERROR)
            return;

        System.out.println("TestDriver2 did something.");
    }
}
