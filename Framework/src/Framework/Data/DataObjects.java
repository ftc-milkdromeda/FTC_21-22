package Framework.Data;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * stores a list of data, and a message for each piece of data. Also keeps track of the time of the most recent update.
 */
public class DataObjects {

    /**
     * Instance variables.
     */
    ArrayList<Object> data;
    ArrayList<String> messages;
    String time;
    String source;

    /**
     * A constructor that stores one piece of data.
     * @param m the message for this DataObject
     * @param d the data, which can be anything.
     * @param s the name of the program that instantiated this DataObject.
     */
    public DataObjects(String m, Object d, String s)
    {
        data = new ArrayList<Object>();
        messages = new ArrayList<String>();
        data.add(d);
        messages.add(m);
        updateTime();
        source = s;
    }

    /**
     * A constructor that does not include the source program.
     * @param m the message for this DataObject
     * @param d the data, which can be anything.
     */
    public void addData(String m, Object d)
    {
        data.add(d);
        messages.add(m);
        updateTime();
    }

    /**
     * deletes the data and message pair at the index.
     * @param index
     */
    public void removeData(int index)
    {
        data.remove(index);
        messages.remove(index);
        updateTime();
    }

    /**
     * returns the data and message at index, in the form "message: data"
     * @param index used to pick which piece of data to return.
     * @return some data
     */
    public String getData(int index)
    {
        return messages.get(index) + ": " + data.get(index);
    }

    /**
     *sets time(the instance variable) to the current time.
     */
    public void updateTime()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        time = dtf.format(now);
    }

    /**
     *
     * @return every piece of data in the object, concatenated into one String.
     */
    public String toString()
    {
        String str = "";
        str += "Data from " + source + " at " + time + ":\n";
        for(int i = 0; i < data.size(); i++)
        {
            str += "\t";
            str += getData(i);
            str += "\n";
        }
        return str;
    }

}
