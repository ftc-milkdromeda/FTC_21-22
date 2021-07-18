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
import RobotCode.Drivers.TestDriver1;

public class TestTask1 extends Task {
    public TestTask1(Clock clock) {
        super(TestTask1.driverList, clock);
    }

    @Override
    protected Error init() {
        this.driver1 = (TestDriver1) super.unboundDrivers[0];
        return TaskError.NO_ERROR;
    }

    @Override
    public Error loop() {
        Driver.InterruptHandler handler = this.driver1.new InterruptHandler(this) {
            @Override
            protected Error programLogic() {
                driver1.doSomething(super.task);
                return DriverError.NO_ERROR;
            }
        };

        handler.run();

        return TaskError.NO_ERROR;
    }

    private static TaskManager.DriverList driverList = new TaskManager.DriverList(
            null,
            new DriverType[] {DriverDirectory.TEST_DRIVER_1}
    );

    private TestDriver1 driver1;
}
