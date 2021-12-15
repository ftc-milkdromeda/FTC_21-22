package RobotCode.ArmController;

import Framework.Drivers.Driver;
import Framework.Drivers.DriverType;
import Framework.Error;
import Framework.Units.AngleUnits;
public abstract class ArmDriver extends Driver {
    int ticksPerRadian;
    AngleUnits radUnits;
    protected ArmDriver(int ticksPerRadian, AngleUnits radUnits)
    {
        super(new DriverType() {
            @Override
            public int getID() {
                return 17;
            }
        });
        this.ticksPerRadian = ticksPerRadian;
        this.radUnits = radUnits;

    }

    public abstract Error doSomething(double amount);
}
