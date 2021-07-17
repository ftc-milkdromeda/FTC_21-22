package Framework.Tasks;

import Framework.Drivers.Driver;
import Framework.Drivers.DriverError;
import Framework.Error;

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

    @Override
    public final void run() {
        exitState = TaskError.NO_ERROR;
        int currentCycle = this.clock.getCurrentCycle();

        while(!super.isInterrupted() && this.exitState == TaskError.NO_ERROR) {
            exitState = loop();

            if(this.taskClockID != -1 && this.exitState == TaskError.NO_ERROR) {
                exitState = clock.taskReady(this);

                while(this.clock.nextCycleReady(currentCycle) && TaskError.NO_ERROR == this.exitState);
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
        if(TaskManager.driverRequest(this, driverList, this.boundDrivers, this.unboundDrivers) == TaskError.DRIVER_ALREADY_BOUND)
            return TaskError.DRIVER_ALREADY_BOUND;

        Error error = TaskManager.addTask(this);
        if(error != TaskError.NO_ERROR)
            return error;

        if(this.clock != null) {
            error = this.clock.addTask(this);
            if(error != TaskError.NO_ERROR)
                return error;
        }

        error = this.init();
        if(error != TaskError.NO_ERROR)
            return  error;

        super.start();

        return TaskError.NO_ERROR;
    }
    final Error endTask() {
        Error error = this.destructor();
        if(error != TaskError.NO_ERROR)
            return error;

        if(this.clock != null) {
            error = this.clock.removeTask(this.taskClockID);
            if(error != TaskError.NO_ERROR)
                return error;
        }

        if(TaskManager.unbindDrivers(this, driverList) == TaskError.DRIVER_NOT_BOUND_TO_TASK)
            return TaskError.DRIVER_NOT_BOUND_TO_TASK;

        return TaskManager.removeTask(taskID);
    }

    protected Error init() {
        return TaskError.NO_ERROR;
    }
    protected Error destructor() {
        return TaskError.NO_ERROR;
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
