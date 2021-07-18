package RobotCode.Tasks;

import Framework.Drivers.Driver;
import Framework.Drivers.DriverError;
import Framework.Drivers.DriverType;
import Framework.Error;
import Framework.Tasks.Clock;
import Framework.Tasks.Task;
import Framework.Tasks.TaskError;
import Framework.Tasks.TaskManager;
import RobotCode.Drivers.DriverDirectory;
import RobotCode.Drivers.TestDriver2;

public class TestTask2 extends Task {
    public TestTask2(Clock clock) {
        super(TestTask2.driverList, clock);
    }

    @Override
    protected Error init() {
        this.driver2 = (TestDriver2) super.boundDrivers[0];
        return TaskError.NO_ERROR;
    }

    @Override
    public Error loop() {
        driver2.doSomething(this);

        long waitTime = 5;
        long startTime = System.currentTimeMillis();
        while(!super.isInterrupted() && System.currentTimeMillis() - startTime <= waitTime * 1000);

        return TaskError.NO_ERROR;
    }

    private static TaskManager.DriverList driverList = new TaskManager.DriverList(
            new DriverType[] {DriverDirectory.TEST_DRIVER_2},
            null
    );

    private TestDriver2 driver2;
}
