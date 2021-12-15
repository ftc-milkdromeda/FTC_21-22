package RobotCode.ArmController;

public class ArmController {

    public static void moveArm(int ticks, double power, ArmDriver driver)
    {
        int steps = 50;
        double distance = ticks/(double)driver.ticksPerRadian;
        distance = distance/steps;
        for(int i = 0; i < steps; i++)
        {
            try {
                driver.doSomething(distance);
            }
            catch(Exception e)
            {
                return;
            }


        }
    }

}
