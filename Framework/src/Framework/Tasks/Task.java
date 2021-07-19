package Framework.Tasks;

import Framework.Drivers.Driver;
import Framework.Error;
import Framework.GeneralError;

public abstract class Task extends Thread{
    protected Task(TaskManager.DriverList driverList, Clock clock) {
        this.driverList = driverList;
        this.taskID = -1;
        this.clock = clock;
        this.taskClockID = -1;
        this.taskIsPaused = false;
        this.taskReady = false;
        this.exitState = null;
    }
    protected Task() {
        this(new TaskManager.DriverList(null, null), null);
    }
    protected Task(TaskManager.DriverList driverList) {
        this(driverList, null);
    }


    public final int getTaskID() {
        return taskID;
    }
    public final int getTaskClockID() {
        return this.taskClockID;
    }

    @Override
    public final void run() {
        exitState = GeneralError.NO_ERROR;

        while(!super.isInterrupted() && this.exitState == GeneralError.NO_ERROR) {
            int currentCycle = this.clock == null ? 0 : this.clock.getCurrentCycle();

            exitState = loop();

            if(this.taskClockID != -1 && this.exitState == GeneralError.NO_ERROR) {
                exitState = clock.taskReady(this);

                while(this.clock.nextCycleReady(currentCycle) && GeneralError.NO_ERROR == this.exitState && !super.isInterrupted());
            }
        }
    }
    @Override
    public final synchronized void start() {
        super.start();
    }
    public final Error getExitState() {
        return this.exitState;
    }

    public abstract Error loop();

    final Error startTask() {
        Error error = TaskManager.addTask(this);
        if (error != GeneralError.NO_ERROR)
            return error;

        if (this.clock != null) {
            error = this.clock.addTask(this);
            if (error != GeneralError.NO_ERROR) {
                TaskManager.removeTask(this.taskID);
                return error;
            }
        }

        TaskManager.RequestedDrivers output = new TaskManager.RequestedDrivers();
        error = TaskManager.driverRequest(this, driverList, output);
        if (error != GeneralError.NO_ERROR) {
            TaskManager.removeTask(this.taskID);
            return error;
        }

        this.boundDrivers = output.getBoundDrivers();
        this.unboundDrivers = output.getUnboundDrivers();

        error = this.init();
        if(error != GeneralError.NO_ERROR) {
            TaskManager.removeTask(this.taskID);
            return error;
        }

        super.start();

        return GeneralError.NO_ERROR;
    }
    final Error endTask() {
        Error error = this.destructor();
        if(error != GeneralError.NO_ERROR)
            return error;

        if(this.clock != null) {
            error = this.clock.removeTask(this.taskClockID);
            if(error != GeneralError.NO_ERROR)
                return error;
        }

        if(TaskManager.unbindDrivers(this, driverList) == TaskError.DRIVER_NOT_BOUND_TO_TASK)
            return TaskError.DRIVER_NOT_BOUND_TO_TASK;

        return TaskManager.removeTask(taskID);
    }

    protected Error init() {
        return GeneralError.NO_ERROR;
    }
    protected Error destructor() {
        return GeneralError.NO_ERROR;
    }

    private TaskManager.DriverList driverList;
    private Clock clock;

    private Error exitState;

    int taskID;

    int taskClockID;
    boolean taskReady;
    boolean taskIsPaused;

    protected Driver[] boundDrivers;
    protected Driver[] unboundDrivers;
}

//todo add linear tasks (FUTURE CHANGE)
