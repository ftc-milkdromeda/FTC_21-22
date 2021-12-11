package RobotCode.Drivers;

import Framework.Drivers.Driver;
import Framework.Tasks.Task;
import Framework.Error;
import Framework.GeneralError;

public class TestDriver1 extends Driver {
    public TestDriver1() {
        super(TestDriver1.driverType);
    }

    private static DriverDirectory driverType = DriverDirectory.TEST_DRIVER_1;

    public Error doSomething(Task task) {
        Error error = super.validateCall(task);
        if(error != GeneralError.NO_ERROR)
            return error;

        System.out.println("TestDriver1 did something.");

        return GeneralError.NO_ERROR;
    }
}
