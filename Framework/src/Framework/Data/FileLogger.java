package Framework.Data;

import Framework.Error;
import Framework.GeneralError;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileLogger
{
    //instance variables
    File logFile;
    private Error status;

    /**
     * constructors
     * @param filename the name of the file logs will be put in.
     */
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

    /**
     * write a dataObject to a file.
     * @param d the data to be written.
     * @return any Errors that crop up.
     */
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

    /**
     * getter for status.
     * @return
     */
    public Error getStatus()
    {
        return status;
    }

}
