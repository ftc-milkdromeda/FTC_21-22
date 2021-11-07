package Framework.Tasks;

import Framework.Drivers.Driver;
import Framework.GeneralError;
import Framework.Error;

/**
 * @author Tyler Wang
 * An abstract class for a {@link Task} that can loop.
 */
public abstract class TaskLoop extends Task{
    /**
     * A constructor that neither defines a {@link Clock} nor a {@link TaskManager.DriverList}.
     */
    public TaskLoop() {
        super();
    }

    /**
     * Full constructor that allows the user to define both a {@link TaskManager.DriverList} and a {@link Clock}.
     * @param driverList The list of {@link Driver drivers} that the {@link Task} will request the {@link TaskManager} to allocate for it.
     * @param clock The {@link Clock} that the {@link Task} will be attached to and synchronized with.
     */
    public TaskLoop(TaskManager.DriverList driverList, Clock clock) {
        super(driverList, clock);
    }

    /**
     * A constructor that only defines a {@link TaskManager.DriverList} and no {@link Clock}.
     * @param driverList The list of {@link Driver Drivers}  that the task will request the {@link TaskManager} to supply the {@link Task} with.
     */
    public TaskLoop(TaskManager.DriverList driverList) {
        super(driverList);
    }

    /**
     * A constructor that that doesn't include a {@link TaskManager.DriverList}.
     * @param clock The {@link Clock} that the {@link Task} will be synchronized with.
     */
    public TaskLoop(Clock clock) {
        super(clock);
    }

    /**
     * An implementation of {@link Task#run()} that loops the {@link TaskLoop#loop()} method.
     */
    @Override
    public final void run() {
        while(!super.isInterrupted() && super.getExitState() == GeneralError.NO_ERROR) {
            super.setExitState(this.loop());
            try {
                super.waitForClockCycle();
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    /**
     * A abstract method that defines the logic for the {@link Task}.
     * @return the error of the specific implementation.
     */
    public abstract Error loop();
}
