package Framework.Opmodes;

import Framework.Drivers.DriverManager;
import Framework.Error;
import Framework.GeneralError;

public abstract class OpModeLoop extends OpMode{
    public OpModeLoop(DriverManager driverManager) {
        super(driverManager);
    }

    @Override
    protected Error opMode() {
        do {
            Error output = this.loop();
            if (output != GeneralError.NO_ERROR)
                return output;
        } while(super.opModeIsActive());

        return GeneralError.NO_ERROR;
    }

    public abstract Error loop();
}
