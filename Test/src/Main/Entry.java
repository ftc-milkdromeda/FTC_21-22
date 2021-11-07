package Main;

import Framework.Drivers.DriverManager;
import Framework.Tasks.Clock;
import Framework.Tasks.TaskManager;
import RobotCode.Drivers.DriverDirectory;
import RobotCode.Drivers.TestDriver1;
import RobotCode.Drivers.TestDriver2;
import RobotCode.Tasks.TestTask1;
import RobotCode.Tasks.TestTask2;

public class Entry {
    public static void main(String args[]) {
        DriverManager drivers = new DriverManager(DriverDirectory.NULL_TYPE.getID());

        TestDriver1 driver1 = new TestDriver1();
        TestDriver2 driver2 = new TestDriver2();

        drivers.addDriver(driver1);
        drivers.addDriver(driver2);
        drivers.initDrivers();

        TaskManager.bindDriverManager(drivers);

        Clock clock = new Clock(1);

        TestTask1 task1 = new TestTask1(clock);
        TestTask2 task2 = new TestTask2(clock);

        System.out.println(TaskManager.startTask(clock));
        System.out.println(TaskManager.startTask(task1));
        System.out.println(TaskManager.startTask(task2));

        while(true);
    }
}

