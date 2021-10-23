package Framework.Data;

import Framework.Error;
import Framework.Drivers.Driver;
import Framework.Drivers.DriverType;

/**
 * A simple Driver for Data management within the robot.
 */
public abstract class DataDriver extends Driver {
    public DataDriver(DriverType type)
    {
        super(type);
    }
    public abstract Error updateData();

    public abstract Error getData();

}
