package Main;

import Framework.Drivers.DriverManager;
import Framework.Error;
import Framework.GeneralError;
import Framework.Tasks.Clock;
import Framework.Tasks.TaskLoop;
import Framework.Tasks.TaskManager;

public class ClockTest {
    public static void main(String[] args) {
        Clock clock = new Clock(1);

        TaskLoop task = new TaskLoop(clock) {
            @Override
            public Error loop() {
                System.out.println("Iteration: " + ClockTest.counter++);
                return GeneralError.NO_ERROR;
            }

            @Override
            public Error destructor() {
                System.out.println("Ending task.");
                return GeneralError.NO_ERROR;
            }
        };
        TaskLoop task1 = new TaskLoop(clock) {
            @Override
            public Error loop() {
                System.out.println("ANOTHER TASK");
                return GeneralError.NO_ERROR;
            }

            public Error destructor() {
                System.out.println("Ending task1.");
                return GeneralError.NO_ERROR;
            }
        };

        DriverManager manager = new DriverManager(0);

        TaskManager.bindDriverManager(manager);

        TaskManager.startTask(clock);
        TaskManager.startTask(task);
        TaskManager.startTask(task1);


        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException e) {
            return;
        }

        clock.pauseTask(task.getTaskClockID());

        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException e) {
            return;
        }
        clock.resumeTask(task.getTaskClockID());

        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException e) {
            return;
        }

        TaskManager.stopTask(task.getTaskID());
        TaskManager.stopTask(clock.getTaskID());
        TaskManager.stopTask(task1.getTaskID());
    }

    static int counter = 0;
}
