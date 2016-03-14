package business;

import plc.PLCController;
import storage.CRUDController;

public class MainController
{
    private static MainController instance;
    
    private MainController()
    {
        
    }
    
    public static MainController get()
    {
        if(instance == null)
        {
            instance = new MainController();
        }
        return instance;
    }
    
    public void initialize()
    {
        PLCController.get();
        CRUDController.get();
    }
}