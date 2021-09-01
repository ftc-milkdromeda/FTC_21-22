package Framework.Opmodes;

import Framework.Drivers.DriverManager;
import Framework.Error;
import Framework.GeneralError;

public abstract class OpModeLoop extends OpMode{
    public OpModeLoop(DriverManager driverManager) {
        super(driverManager);

        this.maximumRefresh = -1;
    }
    public OpModeLoop(DriverManager driverManager, double maximumRefresh) {
        super(driverManager);

        this.maximumRefresh = maximumRefresh;
    }

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

    protected abstract Error loop();

    private double maximumRefresh;
}

//todo add opmode refresh monitor (FUTURE CHANGE)
