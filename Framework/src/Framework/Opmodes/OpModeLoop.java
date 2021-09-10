package Framework.Opmodes;

import Framework.Drivers.DriverManager;
import Framework.Error;
import Framework.GeneralError;

/**
 * @author Tyler Wang
 * An implementation of {@link OpMode} class that allows user to create an opmode that loops.
 */
public abstract class OpModeLoop extends OpMode{
    /**
     * A constructor for an {@link OpModeLoop} that doesn't have a refresh rate cap.
     * @param driverManager The {@link DriverManager} that will be used within the {@link OpMode}.
     */
    public OpModeLoop(DriverManager driverManager) {
        super(driverManager);

        this.maximumRefresh = -1;
    }

    /**
     * A constructor for an {@link OpModeLoop} that caps its refresh rate.
     * @param driverManager The {@link DriverManager} that will be used within the {@link OpMode}.
     * @param maximumRefresh The maximum rate that the {@link OpMode} will loop at given in Hz.
     */
    public OpModeLoop(DriverManager driverManager, double maximumRefresh) {
        super(driverManager);

        this.maximumRefresh = maximumRefresh;
    }

    /**
     * An implementation of the {@link Error opMode()} function that will loop {@link Error loop()}.
     * @return return error that {@link Error loop()} returns.
     */
    @Override
    protected Error opMode() {
        do {
            long startTime = System.currentTimeMillis();
            Error output = this.loop();

            if (output != GeneralError.NO_ERROR)
                return output;

            if(this.maximumRefresh == -1)
                continue;

            while(super.opModeIsActive() && System.currentTimeMillis() - startTime < (long)(1000 / this.maximumRefresh));
        } while(super.opModeIsActive());

        return GeneralError.NO_ERROR;
    }

    /**
     * A abstract function that defines the logic that will be looped in the {@link OpMode} implementation
     * @return The error that the specific implementation returns.
     */
    protected abstract Error loop();

    private double maximumRefresh;
}

//todo add opmode refresh monitor (FUTURE CHANGE)
