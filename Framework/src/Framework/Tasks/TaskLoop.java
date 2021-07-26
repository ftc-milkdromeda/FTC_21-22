package Framework.Tasks;

import Framework.GeneralError;
import Framework.Error;

public abstract class TaskLoop extends Task{
    public TaskLoop() {
        super();
    }
    public TaskLoop(TaskManager.DriverList driverList, Clock clock) {
        super(driverList, clock);
    }
    public TaskLoop(TaskManager.DriverList driverList) {
        super(driverList);
    }
    public TaskLoop(Clock clock) {
        super(clock);
    }

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

    public abstract Error loop();
}
