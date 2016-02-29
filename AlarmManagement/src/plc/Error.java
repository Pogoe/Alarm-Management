package plc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import storage.CRUDController;

public class Error implements Serializable
{
    private int id;
    private int errorCode;
    private String description;
    private Date time;
    private List<Solution> solutions;
    
    public Error(int id, int errorCode, Date time, String description)
    {
        this.id = id;
        this.errorCode = errorCode;
        this.time = time;
        this.description = description;
        this.solutions = findSolutions();
    }
    
    private List<Solution> findSolutions()
    {
        return CRUDController.get().getSolutions(errorCode);
    }

    public int getId()
    {
        return id;
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
}
