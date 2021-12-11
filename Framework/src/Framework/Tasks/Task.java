package Framework.Tasks;

import Framework.Drivers.Driver;
import Framework.Error;
import Framework.GeneralError;

/**
 * @author Tyler Wang
 * An abstract class that is used to define a task.
 */
public abstract class Task extends Thread{
    /**
     * Full constructor that allows the user to define both a {@link TaskManager.DriverList} and a {@link Clock}.
     * @param driverList The list of {@link Driver drivers} that the {@link Task} will request the {@link TaskManager} to allocate for it.
     * @param clock The {@link Clock} that the {@link Task} will be attached to and synchronized with.
     */
    protected Task(TaskManager.DriverList driverList, Clock clock) {
        this.driverList = driverList;
        this.taskID = -1;
        this.clock = clock;
        this.taskClockID = -1;
        this.taskIsPaused = false;
        this.taskReady = false;
        this.exitState = null;
        this.currentCycle = -1;
    }

    /**
     * A constructor that that doesn't include a {@link TaskManager.DriverList}.
     * @param clock The {@link Clock} that the task will be synchronized with.
     */
    protected Task(Clock clock) {
        this(null, clock);
    }

    /**
     * A constructor that neither defines a {@link Clock} nor a {@link TaskManager.DriverList}.
     */
    protected Task() {
        this(new TaskManager.DriverList(null, null), null);
    }

    /**
     * A constructor that only defines a {@link TaskManager.DriverList} and no clock.
     * @param driverList The list of {@link Driver Drivers}  that the {@link Task} will request the {@link TaskManager} to supply the {@link Task} with.
     */
    protected Task(TaskManager.DriverList driverList) {
        this(driverList, null);
    }

    /**
     * Returns the {@link Task} id of the current {@link Task}.
     * @return returns the id of the {@link Task}.
     */
    public final int getTaskID() {
        return taskID;
    }

    /**
     * Returns the {@link Clock} id of the current {@link Task}.
     * @return the id given by the {@link Clock} of the current {@link Task}.
     */
    public final int getTaskClockID() {
        return this.taskClockID;
    }

    /**
     * An abstract class that defines the logic of the {@link Task}.
     */
    @Override
    public abstract void run();

    /**
     * An implementation of the {@link Thread#start()} method in {@link Thread}
     */
    @Override
    public final synchronized void start() {
        super.start();
    }

    /**
     * Returns the state of the {@link Task} after it exits.
     * @return the {@link Error} that the {@link Task} exited with.
     */
    public final Error getExitState() {
        return this.exitState;
    }

    /**
     * Sets the exit state of the {@link Task} after it exits.
     * @param exitState The state that the {@link Task} was in when it exited.
     */
    protected void setExitState(Error exitState) {
        this.exitState = exitState;
    }

    /**
     * A method that starts the {@link Task}.
     * @return {@link GeneralError#NO_ERROR}: function exited without errors. Also may return errors from {@link TaskManager#addTask(Task)} and {@link Task#init()}.
     */
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

    /**
     * A function that ends the current {@link Task}.
     * @return {@link GeneralError#NO_ERROR} if the function exited without errors. Also may return errors from {@link TaskManager#removeTask(int)} and {@link Task#destructor()}.
     */
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

    /**
     * An optional function that may be overridden when the class is extended. Function is called in {@link Task#startTask()} function when {@link Task} is initialized.
     * @return Returns errors from specific implementations.
     */
    protected Error init() {
        return GeneralError.NO_ERROR;
    }

    /**
     * An optional function that may be overridden when the class is extended. Function is called in {@link Task#endTask()} function when {@link Task} is destroyed.
     * @return Returns errors from specific implementations.
     */
    protected Error destructor() {
        return GeneralError.NO_ERROR;
    }

    /**
     * Get the current cycle that the {@link Clock} implemented in the current {@link Task} is on.
     * @return returns the current cycle of the {@link Clock}; returns -1 if no {@link Clock} is bound.
     */
    protected int getClockCycle() {
        return this.currentCycle;
    }

    /**
     * Method that will wait until the {@link Clock} is ready for next cycle.
     * @return {@link TaskError#NO_CLOCK_DEFINED}: returned if no {@link Clock} is defined for current method. {@link GeneralError#NO_ERROR}: if function exited without errors. Also may return errors associated with {@link Clock#taskReady(Task) Clock.taskReady()}.
     * @throws InterruptedException if {@link Task} is interrupted while function is waiting, an {@link InterruptedException} exception will be thrown.
     */
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

