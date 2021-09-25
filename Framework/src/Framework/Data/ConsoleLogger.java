package Framework.Data;

import Framework.GeneralError;
import Framework.Error;
import java.io.Console;

public class ConsoleLogger {
    Console console;
    public ConsoleLogger()
    {
        this(System.console());
    }
    public ConsoleLogger(Console c)
    {
        console = c;
    }

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
