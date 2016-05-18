package plc;

import java.util.Date;
import java.util.Objects;
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
    
    public Error(Date date, ErrorType type)
    {
        this.errorCode = type.getErrorCode();
        this.description = type.getDescription();
        this.time = date;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.errorCode;
        hash = 83 * hash + Objects.hashCode(this.description);
        hash = 83 * hash + Objects.hashCode(this.source);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Error other = (Error) obj;
        if (this.errorCode != other.errorCode) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString()
    {
        return "Error " + errorCode + ": " + description;
    }
}