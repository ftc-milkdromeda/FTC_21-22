package Framework.Tasks;

import Framework.Error;

/**
 * @author Tyler Wang
 * A enum that lists errors for {@link Task Tasks}. An implemtation of {@link Error}
 */
public enum TaskError implements Error {
    /**
     * Issued when the {@link Framework.Drivers.Driver} that is attempted to be bound to a {@link Task} is already bound to another {@link Task}.
     */
    DRIVER_ALREADY_BOUND,
    /**
     * Issued when the {@link Framework.Drivers.Driver} that is attempted to be accessed is not bound the the current {@link Task}.
     */
    DRIVER_NOT_BOUND_TO_TASK,

    /**
     * Issued when the {@link Task} that is attempted to be added to {@link TaskManager} has already been added.
     */
    TASK_ALREADY_BOUND,

    /**
     * Issued when {@link Clock} or {@link Task} functions are attempted to be used on {@link Task Tasks} that have not been initialized.
     */
    TASK_NOT_BOUND,

    /**
     * When the {@link Task} ID used in {@link TaskManager#findTask(int)} has not been defined.
     */
    TASK_ID_NOT_FOUND,

    /**
     * Issued when there is no {@link Framework.Drivers.DriverManager} bound when trying to request {@link Framework.Drivers.Driver Drivers} from {@link TaskManager}
     */
    NO_DRIVER_MANAGER_BOUND,

    /**
     * Issued when the {@link Framework.Drivers.Driver} that is being requested has not been defined in the {@link Framework.Drivers.DriverManager}.
     */
    NO_DRIVER_INIT,

    /**
     * Issued when a function requiring a {@link Clock} is used on a {@link Task} without a {@link Clock}
     */
    NO_CLOCK_DEFINED;


    /**
     * A method that returns the string of the {@link Framework.Error} message.
     * @return the error message in the form of a string.
     */
    @Override
    public String toString() {
        return this.source + ": " + super.toString();
    }

    /**
     * Returns the source of the {@link Framework.Error}.
     * @return the source of the {@link Framework.Error} in a string format.
     */
    @Override
    public String getSource() {
        return source;
    }

    private String source = "FRAMEWORK_TASKS";
}
