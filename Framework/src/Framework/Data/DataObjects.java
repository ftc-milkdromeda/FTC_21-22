package Framework.Data;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
public class DataObjects {
    ArrayList<Object> data;
    ArrayList<String> messages;
    String time;
    DataObjects(String m, Object d)
    {
        data = new ArrayList<Object>();
        messages = new ArrayList<String>();
        data.add(d);
        messages.add(m);
        updateTime();
    }
    public void addData(String m, Object d)
    {
        data.add(d);
        messages.add(m);
        updateTime();
    }
    public void removeData(int index)
    {
        data.remove(index);
        messages.remove(index);
        updateTime();
    }
    public String getData(int index)
    {
        return messages.get(index) + ": " + data.get(index);
    }
    public void updateTime()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        time = dtf.format(now);
    }
    public String toString()
    {
        String str = "";
        str += "Data at " + time + ":\n";
        for(int i = 0; i < data.size(); i++)
        {
            str += "\t";
            str += getData(i);
            str += "\n";
        }
        return str;
    }

}
