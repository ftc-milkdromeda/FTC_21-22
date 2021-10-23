package Framework.Data;

import Framework.GeneralError;
import Framework.Error;
import java.io.Console;

/**
 * A class for sending DataObjects to a console. By default, it sends it to .out
 */
public class ConsoleLogger {
    Console console;

    /**
     * Default constructor
     */
    public ConsoleLogger()
    {
        this(System.console());
    }

    /**
     *
     * Constructor that allows you to set a Console for logs to go to.
     * @param c whatever console your heart desires.
     */
    public ConsoleLogger(Console c)
    {
        console = c;
    }

    /**
     *Sends a packet of information to the console.
     * @param d The information that will be logged.
     * @return Any Errors that crop up, like no consoles existing.
     */
    public Error addEntry(DataObjects d)
    {
        if(console != null)
        {
            console.printf("s",d);
            return GeneralError.NO_ERROR;
        }
        System.out.println(d);
        return DataError.CONSOLE_IS_NULL_ERROR;
    }
}
