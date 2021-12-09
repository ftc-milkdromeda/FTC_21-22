package Framework.Opmodes;

import Framework.Drivers.DriverManager;
import Framework.GeneralError;
import Framework.Tasks.TaskManager;

import Framework.Error;

public abstract class OpMode extends Thread {
    public OpMode(DriverManager drivers) {
        TaskManager.bindDriverManager(drivers);

        this.thisOpModeRunning = false;
    }

    protected abstract Error opMode();
    protected abstract Error startSequence();
    protected abstract Error destructor();

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
    public final Error end() {
        if(OpMode.opModeRunning == true) {
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

        return OpModeError.NO_OP_MODE_INIT;
    }
    public final void run() {
        this.opMode();
    }

    protected boolean opModeIsActive() {
        return !super.isInterrupted();
    }

    private static boolean opModeRunning = false;

    private boolean thisOpModeRunning;
}
