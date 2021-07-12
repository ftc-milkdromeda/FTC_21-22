package Main;

import Framework.Drivers.DriverManager;
import RobotCode.Drivers.DriverDirectory;
import RobotCode.Drivers.TestDriver1;
import RobotCode.Drivers.TestDriver1_1;
import RobotCode.Drivers.TestDriver2;

public class Entry {
    public static void main(String args[]) {
        DriverManager drivers = DriverManager.getInstance(DriverDirectory.NULL_TYPE.getID());

        TestDriver1 driver1 = new TestDriver1();
        TestDriver2 driver2 = new TestDriver2();
        TestDriver1_1 driver3 = new TestDriver1_1();

        System.out.println(drivers.addDriver(driver1));
        System.out.println(drivers.addDriver(driver2));
        System.out.println(drivers.addDriver(driver3));

        System.out.println("_________________________________________\n");

        driver1.doSomething();
        driver2.doSomething();
        driver3.doSomething();

        System.out.println("_________________________________________\n");

        System.out.println(drivers.initDrivers());

        driver1.doSomething();
        driver2.doSomething();
        driver3.doSomething();

        System.out.println("_________________________________________\n");

        System.out.println(drivers.swapDriver(driver3));

        driver1.doSomething();
        driver2.doSomething();
        driver3.doSomething();

        System.out.println("_________________________________________\n");

        System.out.println(drivers.terminateDriver());

        driver1.doSomething();
        driver2.doSomething();
        driver3.doSomething();

        System.out.println(drivers.addDriver(driver1));
        System.out.println(drivers.addDriver(driver2));
        System.out.println(drivers.addDriver(driver3));
    }
}

