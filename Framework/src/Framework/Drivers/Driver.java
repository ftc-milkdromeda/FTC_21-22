package Framework.Drivers;

import Framework.Error;
import Framework.Tasks.Task;
import Framework.GeneralError;

/**
 * @author Tyler Wang
 * A abstract class which can be extended to create {@link Driver Drivers}, which are interfaces between {@link Task Tasks} and the mechanical components
 * of the robot.
 */
public abstract class Driver {
    /**
     * A simple constructor which takes a {@link DriverType} and sets up this {@link Driver}.
     * @param type which part of the robot this Driver is for.
     */
    protected Driver(DriverType type) {
        this.isActive = false;
        this.driverType = type;
        this.attachedTask = null;
        this.interruptTask = null;
    }

    /**
     * A function that returns all the basic information for this {@link Driver}.
     * @return the string "Driver: [ID][{@link DriverType}]".
     */
    public String toString() {
        return "Driver: " + this.driverType.getID() + " " + driverType;
    }

    /**
     * A simple getter for the {@link DriverType}.
     * @return the type of this {@link Driver}.
     */
    public DriverType getType() {
        return this.driverType;
    }

    /**
     * A method that returns wheter current {@link Driver} is or is not initialized.
     * @return if this {@link Driver} is active or not.
     */
    public boolean isActive() {
        return this.isActive;
    }

    /**
     * A method that binds this {@link Driver} to a {@link Task}, so that a {@link Task} can use this {@link Driver} without interrupting first.
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
     * Unbinds this {@link Driver} from a {@link Task}.
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
     * Initializes the {@link Thread}.
     * @return DRIVER_ALREADY_ACTIVE error if this Driver is, and any errors that init() throws.
     */
    Error start() {
        if(isActive)
            return DriverError.DRIVERS_NOT_INIT;

        Error output = this.init();
        if(output != GeneralError.NO_ERROR)
            return output;

        this.isActive = true;

        return GeneralError.NO_ERROR;
    }

    /**
     * A function that terminates the {@link Driver}.
     * @return {@link DriverError#DRIVERS_NOT_INIT}: issued if the {@link Driver} has not been initialized.
     */
    Error terminate() {
        if(!isActive)
            return DriverError.DRIVERS_NOT_INIT;

        Error output = this.destructor();
        if(output != GeneralError.NO_ERROR)
            return output;

        this.isActive = false;

        return GeneralError.NO_ERROR;
    }

    /**
     * An optional class that is defined to aid int he process of destroying an instance of its implementation.
     * @return returns the errors associated with the specific implementation.
     */
    protected Error init() {
        return GeneralError.NO_ERROR;
    }

    /**
     * An optional class that is defined to aid in the process of destroy an instance of its implementation.
     * @return returns the errors associated with the specific implementation.
     */
    protected Error destructor() {
        return GeneralError.NO_ERROR;
    }

    /**
     * Checks if a call to this {@link Driver} can actually be made, returning errors if not.
     * @param task the {@link Task} whose call will be validated.
     * @return {@link DriverError#DRIVERS_NOT_INIT}: Issued when the {@link Driver} has not been initialized. {@link DriverError#DRIVER_IS_INTERRUPTED}: Issued when the current {@link Driver} is being interrupted. {@link DriverError#DRIVER_NOT_BOUND_TO_TASK}: Issued when the calling {@link Task} doesn't have access to the current {@link Driver}. {@link GeneralError#NO_ERROR}: issued when the call from the {@link Task} is valid.
     */
    protected Error validateCall(Task task) {
        if(!this.isActive)
            return DriverError.DRIVERS_NOT_INIT;

        if(this.interruptTask != null && this.attachedTask == task)
            return DriverError.DRIVER_IS_INTERRUPTED;

        if(task != this.attachedTask && task != this.interruptTask)
            return DriverError.DRIVER_NOT_BOUND_TO_TASK;

        return GeneralError.NO_ERROR;
    }

    /**
     * A class that is used to run instructions for a {@link Task} when the {@link Driver} is not explicitly allocated to the {@link Task}.
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


