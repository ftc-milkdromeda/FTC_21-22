package Framework.Drivers;

import Framework.Error;

/**
 * @author Tyler Wang
 * Defines error for {@link Driver Drivers}. An implementation of {@link Error}
 */
public enum DriverError implements Error {
    /**
     * Given when a specific {@link Driver} is used without initialization.
     */
    DRIVERS_NOT_INIT,

    /**
     * Given when an attempt to reinitialize is found.
     */
    DRIVERS_ALREADY_INIT,

    /**
     * When a {@link Driver} of the specific type is already bound to the specific DriverManger.
     */
    DRIVER_ALREADY_EXISTS,

    /**
     * When a {@link Driver} of a specific type is being requested but is not currently bound in the {@link DriverManager}.
     */
    DRIVER_DOES_NOT_EXIST,

    /**
     * When a {@link Driver} being bound already is bound to a {@link Framework.Tasks.Task}.
     */
    DRIVER_ALREADY_BOUND,

    /**
     * When a {@link Driver} being accessed in a {@link Framework.Tasks.Task} that doesn't have authority to.
     */
    DRIVER_NOT_BOUND_TO_TASK,

    /**
     * When a {@link Driver} is being called in the {@link Framework.Tasks.Task} that it is allocated to, but another {@link Framework.Tasks.Task} is currently interrupting the process.
     */
    DRIVER_IS_INTERRUPTED;

    /**
     * Constructor for enum elements.
     */
    DriverError() {
        this.source = "FRAMEWORK_DRIVERS";
    }

    /**
     * A function that returns the source of a specific enum value.
     * @return source of the specific enum value.
     */
    @Override
    public String getSource() {
        return this.source;
    }

    /**
     * A toString method that prints out the specific error message for each of the enum entries.
     * @return A string in format: "FRAMEWORK_DRIVERS: [enum identifier]".
     */
    @Override
    public String toString() {
        return this.source + ": " + super.toString();
    }

    private String source;
}

