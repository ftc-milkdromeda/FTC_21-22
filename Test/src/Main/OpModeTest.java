package Main;

import Framework.Drivers.DriverManager;
import RobotCode.Drivers.DriverDirectory;
import RobotCode.Opmodes.TestOpMode;
import RobotCode.Opmodes.TestOpMode1;

public class OpModeTest {
    public static void main(String [] args) {
        DriverManager manager = new DriverManager(DriverDirectory.NULL_TYPE.getID());

        TestOpMode opmode = new TestOpMode(manager);
        TestOpMode1 opmode1 = new TestOpMode1(manager);

        System.out.println(opmode1.init());
        System.out.println(opmode.init());

        long startTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - startTime <= 5000) ;

        System.out.println(opmode1.end());
        System.out.println(opmode.end());
    }
}
