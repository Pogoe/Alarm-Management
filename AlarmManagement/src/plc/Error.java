package plc;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class Error
{
    private int id;
    private int errorCode;
    private String description;
    private Date time;
    private Greenhouse source;
    
    public Error(int errorCode, Date time, String description)
    {
        this.errorCode = errorCode;
        this.time = time;
        this.description = description;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public String getDescription()
    {
        return description;
    }

    public Date getTime()
    {
        return time;
    }
    
    public Greenhouse getSource()
    {
        return source;
    }
    
    public void setSource(Greenhouse g)
    {
        source = g;
    }
    
    public String toJSONString()
    {
        JSONObject o = new JSONObject();
        try
        {
            o = o.put("errorCode", errorCode);
            o = o.put("description", description);
            o = o.put("date", time);
            o = o.put("source", source);
        } catch (JSONException ex)
        {
            Logger.getLogger(Error.class.getName()).log(Level.SEVERE, null, ex);
        }
        return o.toString();
    }
}