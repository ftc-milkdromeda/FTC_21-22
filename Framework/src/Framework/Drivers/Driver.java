package Framework.Drivers;

import Framework.Error;
import Framework.Tasks.Task;

public abstract class Driver {
    protected Driver(DriverType type) {
        this.isActive = false;
        this.driverType = type;
        this.attachedTask = null;
        this.interruptTask = null;
    }

    public String toString() {
        return "Driver: " + this.driverType.getID() + " " + driverType;
    }
    public DriverType getType() {
        return this.driverType;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public Error bindToTask(Task task) {
        if(this.attachedTask != null)
            return DriverError.DRIVER_ALREADY_BOUND;

        this.attachedTask = task;
        return DriverError.NO_ERROR;
    }
    public Error unbindDriver(Task task) {
        if(this.attachedTask != task)
            return DriverError.DRIVER_NOT_BOUND_TO_TASK;
        this.attachedTask = null;

        return DriverError.NO_ERROR;
    }

    Error start() {
        if(isActive)
            return DriverError.DRIVER_ALREADY_ACTIVE;

        Error output = this.init();
        if(output != DriverError.NO_ERROR)
            return output;

        this.isActive = true;

        return DriverError.NO_ERROR;
    }
    Error terminate() {
        if(!isActive)
            return DriverError.DRIVER_NOT_ACTIVE;

        Error output = this.destructor();
        if(output != DriverError.NO_ERROR)
            return output;

        this.isActive = false;

        return DriverError.NO_ERROR;
    }

    protected Error destructor() {
        return DriverError.NO_ERROR;
    }
    protected Error init() {
        return DriverError.NO_ERROR;
    }


    protected Error validateCall(Task task) {
        if(!this.isActive)
            return DriverError.DRIVER_NOT_ACTIVE;

        if(this.interruptTask != null && this.attachedTask == task)
            return DriverError.DRIVER_IS_INTERRUPTED;

        if(task != this.attachedTask && task != this.interruptTask)
            return DriverError.DRIVER_NOT_BOUND_TO_TASK;

        return DriverError.NO_ERROR;
    }

    public abstract class InterruptHandler {
        public InterruptHandler(Task task) {
            this.task = task;
        }
        public final Error run() {
            if(Driver.this.interruptTask != null)
                return DriverError.DRIVER_IS_INTERRUPTED;

            Driver.this.interruptTask = this.task;

            Error error = this.programLogic();

            Driver.this.interruptTask = null;

            return error;
        }

        protected abstract Error programLogic();

        protected Task task;
    }

     protected DriverType driverType;
     private boolean isActive;
     private Task attachedTask;
     Task interruptTask;
 }

 //todo add taskless drivers (FUTURE CHANGE)


