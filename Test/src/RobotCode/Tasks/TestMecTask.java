package RobotCode.Tasks;

import Framework.MecanumAlg.MecanumAlg;
import Framework.Tasks.Clock;
import RobotCode.Drivers.DriverDirectory;

public class TestMecTask extends DriveTrainTeleOp {
    public TestMecTask(Clock clock, MecanumAlg solver) {
        super(DriverDirectory.MEC_DRIVER, clock);
    }
}
