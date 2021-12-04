package Framework.Data;

import Framework.Error;
import Framework.GeneralError;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileLogger
{
    //instance variables
    private File logFile;
    private Error status;
    private String fileExtension = "txt";

    /**
     * Changes the file extension, which defaults to .txt
     * @param fe the file type that the log will be saved as
     */
    public void setFileExtension(String fe)
    {
        fileExtension = fe;
    }
    /**
     * constructors
     * @param filename the name of the file logs will be put in.
     */
    public FileLogger(String filename)
    {
        try {
            logFile = new File(filename + "." + fileExtension);
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
     * @return the current status, which may include errors and the like.
     */
    public Error getStatus()
    {
        return status;
    }

}
