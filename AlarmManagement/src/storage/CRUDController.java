package storage;

import java.util.List;
import plc.Solution;

public class CRUDController
{
    private static CRUDController instance;
    private CRUD crud;
    
    private CRUDController()
    {
        crud = new CRUD();
    }
    
    public static CRUDController get()
    {
        if(instance == null)
        {
            instance = new CRUDController();
        }
        return instance;
    }
    
    public List<Solution> getSolutions(int errorCode)
    {
        synchronized(crud)
        {
            return crud.getSolutions(String.valueOf(errorCode));
        }
    }
}
