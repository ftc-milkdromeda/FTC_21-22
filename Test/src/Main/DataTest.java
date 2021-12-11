package Main;

import Framework.Data.ConsoleLogger;
import Framework.Data.DataObjects;
import Framework.Data.FileLogger;
import Framework.Data.ConsoleLogger;

public class DataTest {
    public static void main(String [] args) throws InterruptedException {
        double data = 0;
        DataObjects testData = new DataObjects("Test data", data, "Test.DataTest.class");
        FileLogger logs;
        ConsoleLogger consoleLogs = new ConsoleLogger();
        logs = new FileLogger("Test");
        while(true)
        {
            data++;
            consoleLogs.addEntry(testData);
            testData.addData("Test data", data);
            logs.AddEntry(testData);
            Thread.sleep(1000);
        }


    }
}
