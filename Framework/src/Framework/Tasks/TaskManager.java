package Framework.Tasks;

import Framework.Error;
import Framework.Drivers.Driver;
import Framework.Drivers.DriverError;
import Framework.Drivers.DriverManager;
import Framework.Drivers.DriverType;
import Framework.GeneralError;

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

    public static Error startTask(Task task) {
        return task.startTask();
    }
    public static Error stopTask(int taskID) {
        int index = TaskManager.findTask(taskID);

        if(index == -1)
            return TaskError.TASK_ID_NOT_FOUND;

        return TaskManager.taskList.get(index).endTask();
    }
    public static Error stopTask() {
        Error error = GeneralError.NO_ERROR;

        for(Task task : TaskManager.taskList) {
            error = task.endTask();

            if(error != GeneralError.NO_ERROR)
                break;
        }

        return error;
    }

    public static int getNumOfTask() {
        return TaskManager.taskList.size();
    }

    static Error driverRequest(Task callingTask, DriverList list, RequestedDrivers output) {
        if(callingTask.taskID == -1)
            return TaskError.TASK_NOT_BOUND;

        if(TaskManager.manager == null)
            return TaskError.NO_DRIVER_MANAGER_BOUND;

        if(list.boundDrivers != null) {
            output.boundDrivers = new Driver[list.boundDrivers.length];
            for (int i = 0; i < output.boundDrivers.length; i++) {
                output.boundDrivers[i] = TaskManager.manager.getDriver(list.boundDrivers[i]);

                if(output.boundDrivers[i] == null)
                    return TaskError.NO_DRIVER_INIT;

                if(output.boundDrivers[i].bindToTask(callingTask) == DriverError.DRIVER_ALREADY_BOUND)
                    return TaskError.DRIVER_ALREADY_BOUND;
            }
        }
        else output.boundDrivers = null;

        if(list.unboundDrivers != null) {
            output.unboundDrivers = new Driver[list.unboundDrivers.length];
            for(int i = 0; i < output.unboundDrivers.length; i++) {
                output.unboundDrivers[i] = TaskManager.manager.getDriver(list.unboundDrivers[i]);

                if(output.unboundDrivers[i] == null)
                    return TaskError.NO_DRIVER_INIT;
            }
        }
        else output.unboundDrivers = null;

        return GeneralError.NO_ERROR;
    }
    static Error unbindDrivers(Task callingTask, DriverList drivers) {
        for(int i = 0; i < drivers.boundDrivers.length; i++) {
            if(DriverError.DRIVER_NOT_BOUND_TO_TASK == TaskManager.manager.getDriver(drivers.boundDrivers[i]).unbindDriver(callingTask))
                return TaskError.DRIVER_NOT_BOUND_TO_TASK;
        }

        return GeneralError.NO_ERROR;
    }

    static Error addTask(Task task) {
        if(task.taskID != -1)
            return TaskError.TASK_ALREADY_BOUND;

        task.taskID = currentID++;

        TaskManager.taskList.add(task);

        return GeneralError.NO_ERROR;
    }
    static Error removeTask(int taskID) {
        if(taskID == -1)
            return TaskError.TASK_NOT_BOUND;

        int index = TaskManager.findTask(taskID);

        if(index == -1)
            return TaskError.TASK_ID_NOT_FOUND;

        TaskManager.taskList.remove(index);

        return GeneralError.NO_ERROR;
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
    public static class RequestedDrivers {
        private Driver[] boundDrivers;
        private Driver[] unboundDrivers;

        public Driver[] getBoundDrivers() {
            return this.boundDrivers;
        }
        public Driver[] getUnboundDrivers() {
            return this.unboundDrivers;
        }
    }

    private TaskManager() {}
}
