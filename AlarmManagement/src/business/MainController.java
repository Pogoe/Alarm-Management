package business;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import plc.PLCController;
import plc.Error;
import storage.CRUDController;

public class MainController implements Observer
{
    private static MainController instance;
    private PHPConnection server;
    
    private MainController() throws IOException
    {
        server = new PHPConnection();
    }
    
    public static MainController get()
    {
        if(instance == null)
        {
            try
            {
                instance = new MainController();
            } catch (IOException ex)
            {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return instance;
    }
    
    public void initialize()
    {
        PLCController.get();
        CRUDController.get();
    }
    
    public void sendError(Error e)
    {
        try
        {
            server.getOutputStream().write(e.toJSONString());
            server.getOutputStream().flush();
        } catch (IOException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if(arg instanceof Error)
        {
            sendError((Error) arg);
        }
    }
}