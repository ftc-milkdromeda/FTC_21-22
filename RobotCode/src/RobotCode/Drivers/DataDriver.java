package RobotCode.Drivers;

import Framework.Drivers.Driver;
import Framework.Error;
import Framework.GeneralError;
import Framework.Tasks.Task;
import Framework.Data.DataObjects;
import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;


public class DataDriver extends Driver {
    public DataDriver()
    {
        super(DataDriver.driverType);
        TelemetryImpl tel;
        //tel = new TelemetryImpl();
    }

    private static DriverDirectory driverType = DriverDirectory.DATA_DRIVER;

    public Error doSomething(Task task, DataObjects data) {
        Error error = super.validateCall(task);
        return error;
    }

}
