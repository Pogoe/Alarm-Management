package plc;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONStringer;

public class Solution
{
    private int errorCode;
    private String description;
    
    public Solution(int errorCode, String description)
    {
        this.errorCode = errorCode;
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
    
    public String toJSONString()
    {
        JSONStringer s = new JSONStringer();
        try
        {
            s.object();
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
