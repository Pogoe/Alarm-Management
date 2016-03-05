package storage;

import java.util.List;
import plc.Solution;
import plc.Error;

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
            return crud.getSolutions(errorCode);
        }
    }
    
    public List<Error> getAllErrors(int errorCode)
    {
        synchronized(crud)
        {
            return crud.getAllErrors(errorCode);
        }
    }
    
    public int storeError(int errorCode, String description)
    {
        synchronized(crud)
        {
            return crud.storeError(errorCode, description);
        }
    }
    
    public int storeSolution(int errorCode, String description)
    {
        synchronized(crud)
        {
            return crud.storeSolution(errorCode, description);
        }
    }
}