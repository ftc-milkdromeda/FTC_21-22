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
    protected Task(Clock clock) {
        this(null, clock);
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
    public abstract void run();
    @Override
    public final synchronized void start() {
        super.start();
    }

    public final Error getExitState() {
        return this.exitState;
    }
    protected void setExitState(Error exitState) {
        this.exitState = exitState;
    }

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

        this.currentCycle = this.clock == null ? -1 : this.clock.getCurrentCycle();

        error = this.init();
        if(error != GeneralError.NO_ERROR) {
            TaskManager.removeTask(this.taskID);
            return error;
        }

        this.exitState = GeneralError.NO_ERROR;

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

    protected int getClockCycle() {
        return this.currentCycle;
    }
    protected Error waitForClockCycle() throws InterruptedException {
        if(this.taskClockID == -1)
            return TaskError.NO_CLOCK_DEFINED;

        this.exitState = this.clock.taskReady(this);

        if(this.exitState != GeneralError.NO_ERROR)
            return this.exitState;

        while(this.clock.nextCycleReady(this.currentCycle)) {
            if(super.isInterrupted())
                throw new InterruptedException();
        }

        this.currentCycle = this.clock.getCurrentCycle();

        return this.exitState;
    }

    private TaskManager.DriverList driverList;
    private Clock clock;

    private Error exitState;

    int taskID;

    int taskClockID;
    int currentCycle;
    boolean taskReady;
    boolean taskIsPaused;

    protected Driver[] boundDrivers;
    protected Driver[] unboundDrivers;
}
