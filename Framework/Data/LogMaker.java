package Framework.Data;
import java.io.*;
public class LogMaker
{
    File logFile;
    LogMaker(String filename)
    {
        try {
            logFile = new File(filename + ".txt");
            if (logFile.createNewFile()) {
                System.out.println("File created: " + logFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    void AddEntry(DataObjects d)
    {
        try {
            FileWriter logWriter = new FileWriter(logFile.getAbsoluteFile(), true);
            logWriter.write(d.toString());
            logWriter.flush();
            logWriter.close();
            System.out.println("Successfully wrote to the file.");
        }
        catch(IOException e)
        {
            System.out.println("An error occurred");
            e.printStackTrace();
        }

    }

}
