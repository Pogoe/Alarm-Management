package plc;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import storage.CRUDController;

public class Error implements Serializable
{
    private int id;
    private int errorCode;
    private String description;
    private Date time;
    private Greenhouse source;
    private List<Solution> solutions;
    
    public Error(int id, int errorCode, Date time, String description)
    {
        this.id = id;
        this.errorCode = errorCode;
        this.time = time;
        this.description = description;
        //this.solutions = findSolutions();
    }
    
    private List<Solution> findSolutions()
    {
        return CRUDController.get().getSolutions(errorCode);
    }
    
    public List<Solution> getSolutions()
    {
        return solutions;
    }
    
    public Solution getSolution(int index)
    {
        return solutions.get(index);
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
            o = o.put("id", id);
            o = o.put("errorCode", errorCode);
            o = o.put("description", description);
            o = o.put("date", time);
            o = o.put("source", source);
            //o = o.put("solutions", new JSONArray(solutions).toString());
        } catch (JSONException ex)
        {
            Logger.getLogger(Error.class.getName()).log(Level.SEVERE, null, ex);
        }
        return o.toString();
    }
}