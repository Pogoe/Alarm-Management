package plc;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONStringer;

public class Solution
{
    private int id;
    private int errorCode;
    private String description;
    
    public Solution(int id, int errorCode, String description)
    {
        this.id = id;
        this.errorCode = errorCode;
        this.description = description;
    }

    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }

    public int getErrorCode()
    {
        return errorCode;
    }
    
    public String toJSONString()
    {
        JSONStringer s = new JSONStringer();
        try
        {
            s.object();
            s.key("id");
            s.value(id);
            s.key("errorCode");
            s.value(errorCode);
            s.key("description");
            s.value(description);
            s.endObject();
        } catch (JSONException ex)
        {
            Logger.getLogger(Solution.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return s.toString();
    }

    @Override
    public String toString()
    {
        return description;
    }
}
