package Framework.Tasks;

import Framework.Error;
import Framework.Drivers.Driver;
import Framework.Drivers.DriverError;
import Framework.Drivers.DriverManager;
import Framework.Drivers.DriverType;

import java.util.ArrayList;

public final class TaskManager {
    static int findTask(int taskID) {
        int upperBound = TaskManager.taskList.size() - 1;
        int lowerBound = 0;
        int median = (lowerBound + upperBound) / 2;
        int index = -1;

        while(median != lowerBound && index == -1) {
            if(TaskManager.taskList.get(median).taskID < taskID)
                lowerBound = median;
            else if(TaskManager.taskList.get(median).taskID > taskID)
                upperBound = median;
            else
                index = median;

            median = (lowerBound + upperBound) / 2;
        }

        if(index == -1 && TaskManager.taskList.get(median).taskID == taskID)
            index = median;
        else if (index == -1 && TaskManager.taskList.get(upperBound).taskID == taskID)
            index = upperBound;

        return index;
    }

    public static void bindDriverManager(DriverManager manager) {
        TaskManager.manager = manager;
    }

    public static Error startTask(int taskID) {
        int index = TaskManager.findTask(taskID);

        if(index == -1)
            return TaskError.TASK_ID_NOT_FOUND;

        return TaskManager.taskList.get(index).startTask();
    }
    public static Error stopTask(int taskID) {
        int index = TaskManager.findTask(taskID);

        if(index == -1)
            return TaskError.TASK_ID_NOT_FOUND;

        return TaskManager.taskList.get(index).endTask();
    }
    public static Error stopTask() {
        Error error = TaskError.NO_ERROR;

        for(Task task : TaskManager.taskList) {
            error = task.endTask();

            if(error != TaskError.NO_ERROR)
                break;
        }

        return error;
    }

    public static int getNumOfTask() {
        return TaskManager.taskList.size();
    }

    static Error driverRequest(Task callingTask, DriverList list, Driver[] boundDrivers, Driver[] unboundDrivers) {
        if(callingTask.taskID == -1)
            return TaskError.TASK_NOT_BOUND;

        if(list.boundDrivers != null) {
            boundDrivers = new Driver[list.boundDrivers.length];
            for (int i = 0; i < boundDrivers.length; i++) {
                boundDrivers[i] = TaskManager.manager.getDriver(list.boundDrivers[i]);

                if(boundDrivers[i].bindToTask(callingTask) == DriverError.DRIVER_ALREADY_BOUND)
                    return TaskError.DRIVER_ALREADY_BOUND;
            }
        }
        else boundDrivers = null;

        if(list.unboundDrivers != null) {
            unboundDrivers = new Driver[list.unboundDrivers.length];
            for(int i = 0; i < unboundDrivers.length; i++) {
                unboundDrivers[i] = TaskManager.manager.getDriver(list.unboundDrivers[i]);
            }
        }
        else unboundDrivers = null;

        return TaskError.NO_ERROR;
    }
    static Error unbindDrivers(Task callingTask, DriverList drivers) {
        for(int i = 0; i < drivers.boundDrivers.length; i++) {
            if(DriverError.DRIVER_NOT_BOUND_TO_TASK == TaskManager.manager.getDriver(drivers.boundDrivers[i]).unbindDriver(callingTask))
                return TaskError.DRIVER_NOT_BOUND_TO_TASK;
        }

        return TaskError.NO_ERROR;
    }

    static Error addTask(Task task) {
        if(task.taskID != -1)
            return TaskError.TASK_ALREADY_BOUND;

        task.taskID = currentID++;

        TaskManager.taskList.add(task);

        return TaskError.NO_ERROR;
    }
    static Error removeTask(int taskID) {
        if(taskID == -1)
            return TaskError.TASK_NOT_BOUND;

        int index = TaskManager.findTask(taskID);

        if(index == -1)
            return TaskError.TASK_ID_NOT_FOUND;

        TaskManager.taskList.remove(index);

        return TaskError.NO_ERROR;
    }

    private static DriverManager manager = null;

    private static ArrayList<Task> taskList = new ArrayList<Task>();
    private static int currentID = 0;

    public static class DriverList {
        public DriverList(DriverType[] boundDrivers, DriverType[] unboundDrivers) {
            this.boundDrivers = boundDrivers;
            this.unboundDrivers = unboundDrivers;
        }

        private DriverType[] boundDrivers;
        private DriverType[] unboundDrivers;
    }

    private TaskManager() {}
}
