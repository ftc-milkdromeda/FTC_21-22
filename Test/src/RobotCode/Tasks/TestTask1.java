package RobotCode.Tasks;

import Framework.Drivers.Driver;
import Framework.Drivers.DriverType;
import Framework.Error;
import Framework.Tasks.Clock;
import Framework.Tasks.TaskLoop;
import Framework.Tasks.TaskManager;
import RobotCode.Drivers.DriverDirectory;
import RobotCode.Drivers.TestDriver1;
import Framework.GeneralError;

public class TestTask1 extends TaskLoop {
    public TestTask1(Clock clock) {
        super(TestTask1.driverList, clock);
    }

    @Override
    protected Error init() {
        this.driver1 = (TestDriver1) super.unboundDrivers[0];
        return GeneralError.NO_ERROR;
    }

    @Override
    public Error loop() {
        Driver.InterruptHandler handler = this.driver1.new InterruptHandler(this) {
            @Override
            protected Error programLogic() {
                driver1.doSomething(super.task);
                return GeneralError.NO_ERROR;
            }
        };

        handler.run();

        return GeneralError.NO_ERROR;
    }

    private static TaskManager.DriverList driverList = new TaskManager.DriverList(
            null,
            new DriverType[] {DriverDirectory.TEST_DRIVER_1}
    );

    private TestDriver1 driver1;
}
