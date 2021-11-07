package Framework.Opmodes;

import Framework.Drivers.DriverManager;
import Framework.GeneralError;
import Framework.Tasks.TaskManager;

import Framework.Error;

/**
 * @author Tyler Wang
 * A abstract class that allows the user to define operation modes ({@link OpMode}).
 */
public abstract class OpMode extends Thread {
    /**
     * Constructor that constructs an {@link OpMode}.
     * @param drivers the specific {@link DriverManager} that will be used within the {@link OpMode}.
     */
    public OpMode(DriverManager drivers) {
        TaskManager.bindDriverManager(drivers);

        this.thisOpModeRunning = false;
    }

    /**
     * The main part of the {@link OpMode} that is to be implemented.
     * @return returns error of the specific implementation.
     */
    protected abstract Error opMode();

    /**
     * The function that initializes the {@link OpMode} for operation.
     * @return returns error of the initial setup of the {@link OpMode}.
     */
    protected abstract Error startSequence();

    /**
     * Destroys the current {@link OpMode} so other {@link OpMode OpModes} can be ran.
     * @return returns the specific error of the specific implementation.
     */
    protected abstract Error destructor();

    /**
     * The default setup of every {@link OpMode} and runs {@link OpMode#startSequence()}.
     * @return {@link OpModeError#OP_MODE_ALREADY_INIT}: returned when {@link OpMode} has been attempted to be reinitialized. {@link OpModeError#ANOTHER_OP_MODE_INIT}: if another {@link OpMode} is already init while initializing current {@link OpMode}. {@link GeneralError#NO_ERROR}: function exited without error. Other errors from {@link Error startSequence()} may be returned.
     */
    public final Error init() {
        if(this.thisOpModeRunning)
            return OpModeError.OP_MODE_ALREADY_INIT;
        if(OpMode.opModeRunning)
            return OpModeError.ANOTHER_OP_MODE_INIT;

        Error error = this.startSequence();

        if(error != GeneralError.NO_ERROR)
            return error;

        super.start();

        this.thisOpModeRunning = true;
        OpMode.opModeRunning = true;

        return error;
    }

    /**
     * The default destructor of every {@link OpMode} and runs {@link OpMode#destructor()}.
     * @return {@link OpModeError#OP_MODE_NOT_INIT}: returned when an {@link OpMode} that hasn't been initialized is attempted to be destroyed. {@link GeneralError#NO_ERROR}: returned when function returned without errors.
     */
    public final Error end() {
        if(this.thisOpModeRunning == false)
            return OpModeError.OP_MODE_NOT_INIT;

        Error output = this.destructor();

        if(output != GeneralError.NO_ERROR)
            return output;

        super.interrupt();

        OpMode.opModeRunning = false;
        this.thisOpModeRunning = false;

        return output;
    }

    /**
     * Runs {@link OpMode#opMode()}
     */
    public final void run() {
        this.opMode();
    }

    /**
     * Returns the activity state of current {@link OpMode}.
     * @return the current state of the {@link OpMode}.
     */
    protected boolean opModeIsActive() {
        return !super.isInterrupted();
    }

    private static boolean opModeRunning = false;

    private boolean thisOpModeRunning;
}

//todo make drivers hot-swapable
