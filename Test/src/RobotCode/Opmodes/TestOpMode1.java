package RobotCode.Opmodes;

import Framework.Drivers.DriverManager;
import Framework.Error;
import Framework.GeneralError;
import Framework.Opmodes.OpMode;
import Framework.Opmodes.OpModeLoop;

public class TestOpMode1 extends OpModeLoop {
    public TestOpMode1(DriverManager manager) {
        super(manager, .5);
    }

    @Override
    protected Error loop() {
        System.out.println("Functioning");
        System.out.println(super.opModeIsActive());
        return GeneralError.NO_ERROR;
    }

    @Override
    protected Error startSequence() {
        System.out.println("Starting");
        return GeneralError.NO_ERROR;
    }

    @Override
    protected Error destructor() {
        System.out.println("Ending");
        return GeneralError.NO_ERROR;
    }
}
