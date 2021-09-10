package Framework.Tasks;

import Framework.Error;
import Framework.Drivers.Driver;
import Framework.Drivers.DriverError;
import Framework.Drivers.DriverManager;
import Framework.Drivers.DriverType;
import Framework.GeneralError;

import java.util.ArrayList;

/**
 * @author Tyler Wang
 * A static class that manages the {@link Task Tasks}.
 */
public final class TaskManager {
    /**
     * Finds and returns the index of a {@link Task} based on the id that is supplied.
     * @param taskID the id of the {@link Task} that is being requested.
     * @return the index of the {@link Task} in {@link TaskManager#taskList} that has been requested.
     */
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

    /**
     * Binds a {@link DriverManager} to the {@link TaskManager}.
     * @param manager the {@link DriverManager} that will be bound to the {@link TaskManager}.
     */
    public static void bindDriverManager(DriverManager manager) {
        TaskManager.manager = manager;
    }

    /**
     * A function that starts the {@link Task} passed into it and sets the {@link Task Task's} {@link Task#taskID}.
     * @param task An object of the {@link Task} that is to be started.
     * @return Returns errors associated with {@link Task#start()}.
     */
    public static Error startTask(Task task) {
        return task.startTask();
    }

    /**
     * A function that stops a {@link Task}.
     * @param taskID the id of the {@link Task} that is to be stopped.
     * @return {@link TaskError#TASK_ID_NOT_FOUND}: if the id that is supplied doesn't match with any {@link Task} that is stored in {@link TaskManager#taskList}. Also may return errors associated with {@link Task#endTask()}.
     */
    public static Error stopTask(int taskID) {
        int index = TaskManager.findTask(taskID);

        if(index == -1)
            return TaskError.TASK_ID_NOT_FOUND;

        return TaskManager.taskList.get(index).endTask();
    }

    /**
     * A function that stops all of the {@link Task Tasks} that are currently being
     * @return Returns errors associated with {@link Task#endTask()}.
     */
    public static Error stopTask() {
        Error error = GeneralError.NO_ERROR;

        for(Task task : TaskManager.taskList) {
            error = task.endTask();

            if(error != GeneralError.NO_ERROR)
                break;
        }

        return error;
    }

    /**
     * A function that returns the current size of the set of {@link Task Tasks}.
     * @return the number of {@link Task Tasks} that are currently being tracked by {@link TaskManager}.
     */
    public static int getNumOfTask() {
        return TaskManager.taskList.size();
    }

    /**
     * A function that allocates {@link Driver Drivers} that will be used in a {@link Task}.
     * @param callingTask the {@link Task} that is calling the function and also the {@link Task} that the function will allocate the {@link Driver Drivers} that it requests to.
     * @param list the list of {@link Driver} that will be allocated to the calling {@link Task}.
     * @param output an initialized object that will receive references to the objects of the {@Link Driver Drivers} allocated to the calling {@link Task}.
     * @return {@link TaskError#TASK_NOT_BOUND}: issued if the calling {@link Task} has not been added to the {@link TaskManager}. {@link TaskError#NO_DRIVER_MANAGER_BOUND}: issued if no {@link DriverManager} is bound to the {@link TaskManager}. {@link TaskError#NO_DRIVER_INIT}: Issued if the list of {@link Driver Drivers} contain a null element. {@link TaskError#DRIVER_ALREADY_BOUND}: Issued if {@link Driver} is currently allocated to another {@link Task}. {@link GeneralError#NO_ERROR}: Issued if function exited without errors.
     */
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

    /**
     * A function that unbinds all of the {@link Driver Drivers} that have previously been allocated the a {@link Task}.
     * @param callingTask the {@link Task} that has {@link Driver Drivers} to unbind.
     * @param drivers the list of {@link Driver Drivers} that are allocated to the {@link Task}.
     * @return {@link TaskError#DRIVER_NOT_BOUND_TO_TASK}: issued when a {@link Driver} in the list that is attempted to be unbound is not bound to the calling {@link Task}. {@link GeneralError#NO_ERROR}: Issued when the function exited without any errors.
     */
    static Error unbindDrivers(Task callingTask, DriverList drivers) {
        for(int i = 0; i < drivers.boundDrivers.length; i++) {
            if(DriverError.DRIVER_NOT_BOUND_TO_TASK == TaskManager.manager.getDriver(drivers.boundDrivers[i]).unbindDriver(callingTask))
                return TaskError.DRIVER_NOT_BOUND_TO_TASK;
        }

        return GeneralError.NO_ERROR;
    }

    /**
     * A function that adds a {@link Task} to {@link TaskManager#taskList}.
     * @param task The {@link Task} that is to be added to {@link TaskManager#taskList}.
     * @return {@link TaskError#TASK_ALREADY_BOUND}: when the {@link Task} that is attempted to be added has already been added. {@link GeneralError#NO_ERROR}: when the function exited without any errors.
     */
    static Error addTask(Task task) {
        if(task.taskID != -1)
            return TaskError.TASK_ALREADY_BOUND;

        task.taskID = currentID++;

        TaskManager.taskList.add(task);

        return GeneralError.NO_ERROR;
    }

    /**
     * A function that is used to remove a {@link Task} from {@link TaskManager#taskList}.
     * @param taskID the id of the {@link Task} that is to be removed.
     * @return {@link TaskError#TASK_ID_NOT_FOUND}: issued when the id provided does not correspond to a {@link Task}. {@link GeneralError#NO_ERROR}: Issued when function exited withtout errors.
     */
    static Error removeTask(int taskID) {
        int index = TaskManager.findTask(taskID);

        if(index == -1)
            return TaskError.TASK_ID_NOT_FOUND;

        TaskManager.taskList.remove(index);

        return GeneralError.NO_ERROR;
    }

    private static DriverManager manager = null;

    private static ArrayList<Task> taskList = new ArrayList<Task>();
    private static int currentID = 0;

    /**
     * A class that is used to define list of {@link Driver Drivers} when allocated {@link Driver Drivers} for {@link Task}.
     */
    public static class DriverList {
        /**
         * A constructor that initialize an list of {@link DriverType DriverTypes}.
         * @param boundDrivers the list of {@link Driver Drivers} that is explicitly bounded to the task.
         * @param unboundDrivers the list of {@link Driver Drivers} that is not explicitly bound to the task and must be used with the help of a {@link Driver.InterruptHandler}.
         */
        public DriverList(DriverType[] boundDrivers, DriverType[] unboundDrivers) {
            this.boundDrivers = boundDrivers;
            this.unboundDrivers = unboundDrivers;
        }

        private DriverType[] boundDrivers;
        private DriverType[] unboundDrivers;
    }

    /**
     * A class that stores a list of {@link Driver Drivers} that will be returned to the calling {@link Task} that requested the {@link Driver Drivers}.
     */
    public static class RequestedDrivers {
        private Driver[] boundDrivers;
        private Driver[] unboundDrivers;

        /**
         * Returns the list of {@link Driver Drivers} that have been explicitly bound to the {@link Task}.
         * @return a list of bound {@link Driver Drivers}.
         */
        public Driver[] getBoundDrivers() {
            return this.boundDrivers;
        }

        /**
         * Returns the list of {@link Driver Drivers} that have not been explicitly bound to the driver and must be used with the help of an {@link Driver.InterruptHandler}.
         * @return A list of unbounded {@link Driver Drivers}.
         */
        public Driver[] getUnboundDrivers() {
            return this.unboundDrivers;
        }
    }

    private TaskManager() {}
}

//todo make taskList into a more efficient data structure.