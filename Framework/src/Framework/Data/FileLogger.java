package Framework.Data;

import Framework.Error;
import Framework.GeneralError;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileLogger
{
    File logFile;
    private Error status;

    public FileLogger(String filename)
    {
        try {
            logFile = new File(filename + ".txt");
            if (logFile.createNewFile()) {
                status = GeneralError.NO_ERROR;
            } else {
                status = DataError.FILE_ALREADY_EXISTS_ERROR;
            }
        } catch (IOException e) {

            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public Error getStatus()
    {
        return status;
    }
    public Error AddEntry(DataObjects d)
    {
        try {
            FileWriter logWriter = new FileWriter(logFile.getAbsoluteFile(), true);
            logWriter.write(d.toString());
            logWriter.flush();
            logWriter.close();
            return GeneralError.NO_ERROR;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return DataError.FILE_WRITING_ERROR;

        }

    }

}
