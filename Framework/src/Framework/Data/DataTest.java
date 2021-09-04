package Framework.Data;

public class DataTest {
    public static void main(String [] args) throws InterruptedException {
        double data = 0;
        DataObjects testData = new DataObjects("Test data", data);
        LogMaker logs = new LogMaker("Test");
        while(true)
        {
            data++;
            testData.addData("Test data", data);
            logs.AddEntry(testData);
            Thread.sleep(1000);
        }


    }
}
