package RobotCode.ArmController;

import Framework.GeneralError;
import Framework.Error;
import Framework.Units.AngleUnits;

public class ArmTest {
    public static void main(String [] args)
    {
        AngleUnits ang = AngleUnits.DEG;
        ArmDriver armsy = new ArmDriver(68, ang) {
            @Override
            public Error doSomething(double amount) {
                System.out.println("Arm: +" + amount + " Bucket: -" + amount);
                return GeneralError.NO_ERROR;
            }
        };
        ArmController.moveArm(4567, 16.9, armsy);

    }
}
