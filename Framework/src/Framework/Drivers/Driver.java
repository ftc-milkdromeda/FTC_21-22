package Framework.Drivers;

import Framework.Error;
import Framework.Tasks.Task;
import Framework.GeneralError;

/**
 * @author Tyler Wang
 * a abstract class which can be extended to create RobotCode.Drivers,
 * which are interfaces between Tasks and the mechanical components
 * of the robot.
 */
public abstract class Driver {
    /**
     * a simple constructor which takes the type of this driver,
     * and sets up this Driver as inactive.
     * @param type which part of the robot this Driver is for.
     */
    protected Driver(DriverType type) {
        this.isActive = false;
        this.driverType = type;
        this.attachedTask = null;
        this.interruptTask = null;
    }

    /**
     * a function that returns all the basic information for this driver.
     * @return the string "Driver: [driverID] [driverType]".
     */
    public String toString() {
        return "Driver: " + this.driverType.getID() + " " + driverType;
    }

    /**
     * a simple getter for the driverType.
     * @return the type of this driver.
     */
    public DriverType getType() {
        return this.driverType;
    }

    /**
     * a getter for the current state of this driver.
     * @return if this Driver is active or not.
     */
    public boolean isActive() {
        return this.isActive;
    }

    /**
     * a method that binds this driver to a task, so that task can
     * use this Driver without interrupting first.
     * It checks for the Driver already being bound first.
     * @param task the task that this Driver will be bound to.
     * @return an DRIVER_ALREADY_BOUND Error if that's the case, no error otherwise.
     */
    public Error bindToTask(Task task) {

        if(this.attachedTask != null)
            return DriverError.DRIVER_ALREADY_BOUND;

        this.attachedTask = task;
        return GeneralError.NO_ERROR;
    }

    /**
     * Unbinds this Driver from a task, so all tasks using this Driver must interrupt first.
     * @param task the task that this Driver will be unbound from.
     * @return DRIVER_NOT_BOUND_TO_TASK error if that's the case, no error otherwise.
     */
    public Error unbindDriver(Task task) {
        if(this.attachedTask != task)
            return DriverError.DRIVER_NOT_BOUND_TO_TASK;
        this.attachedTask = null;

        return GeneralError.NO_ERROR;
    }

    /**
     *runs the init() function, with some error handling.
     * @return DRIVER_ALREADY_ACTIVE error if this Driver is, and any errors that init() throws.
     */
    Error start() {
        if(isActive)
            return DriverError.DRIVER_ALREADY_ACTIVE;

        Error output = this.init();
        if(output != GeneralError.NO_ERROR)
            return output;

        this.isActive = true;

        return GeneralError.NO_ERROR;
    }

    /**
     * runs the destructor() function, with some error handling.
     * @return DRIVER_NOT_ACTIVE error if that's the case, and any errors that destructor() throws.
     */
    Error terminate() {
        if(!isActive)
            return DriverError.DRIVER_NOT_ACTIVE;

        Error output = this.destructor();
        if(output != GeneralError.NO_ERROR)
            return output;

        this.isActive = false;

        return GeneralError.NO_ERROR;
    }

    /**
     * an initializer, to be overridden in implementations of this class.
     * @return NO_ERROR
     */
    protected Error init() {
        return GeneralError.NO_ERROR;
    }
    /**
     * a destructor, to be overridden in implementations of this class.
     * @return NO_ERROR
     */
    protected Error destructor() {
        return GeneralError.NO_ERROR;
    }


    /**
     * checks if a call to this Driver can actually be run, throwing errors if not.
     * @param task the task whose call will be validated.
     * @return any errors, like this Driver not being active, Driver already being interrupted, or something else.
     */
    protected Error validateCall(Task task) {
        if(!this.isActive)
            return DriverError.DRIVER_NOT_ACTIVE;

        if(this.interruptTask != null && this.attachedTask == task)
            return DriverError.DRIVER_IS_INTERRUPTED;

        if(task != this.attachedTask && task != this.interruptTask)
            return DriverError.DRIVER_NOT_BOUND_TO_TASK;

        return GeneralError.NO_ERROR;
    }

    /**
     * a nested class used to run instructions from interrupting tasks.
     */
    public abstract class InterruptHandler {
        /**
         * a small constructor for this class.
         * @param task the task that is interrupting
         */
        public InterruptHandler(Task task) {
            this.task = task;
        }

        /**
         * runs programLogic(), which does what the interrupting task wants.
         * @return any errors that are thrown.
         */
        public final Error run() {
            if(Driver.this.interruptTask != null)
                return DriverError.DRIVER_IS_INTERRUPTED;

            Driver.this.interruptTask = this.task;

            Error error = this.programLogic();

            Driver.this.interruptTask = null;

            return error;
        }

        /**
         * a function that will be implemented, and will do what interrupting tasks want.
         * @return nothing
         */
        protected abstract Error programLogic();

        protected Task task;
    }


     protected DriverType driverType;
     private boolean isActive;
     private Task attachedTask;
     Task interruptTask;
 }

 //todo add taskless drivers (FUTURE CHANGE)


