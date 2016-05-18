package plc;

import business.ConnectionController;
import gui.GUIController;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PLCController extends Observable
{
    private static PLCController instance;
    private Map<Integer, ErrorType> errorTypes;
    private List<Greenhouse> greenhouses;
    private Map<Greenhouse, List<Error>> errors;
    private Thread t;

    private PLCController()
    {
        this.greenhouses = Collections.synchronizedList(new LinkedList<>());
        this.errors = new ConcurrentHashMap<>();
        this.errorTypes = new ConcurrentHashMap<>();
        addObserver(GUIController.get());
    }

    public static PLCController get()
    {
        if (instance == null)
        {
            instance = new PLCController();
            instance.initialize();
        }
        return instance;
    }

    public void initialize()
    {
        PLCConnection con1 = new UDPConnection(5000, "192.168.0.20");
        PLCConnection con2 = new UDPConnection(5000, "192.168.0.30");
        PLCConnection con3 = new UDPConnection(5000, "192.168.0.40");
        
        PLCController.get().addGreenhouse(new Greenhouse(con1));
        PLCController.get().addGreenhouse(new Greenhouse(con2));
        PLCController.get().addGreenhouse(new Greenhouse(con3));
        
        errorTypes = ConnectionController.get().getErrors();
        checkForErrors();
    }

    public void addGreenhouse(Greenhouse g)
    {
        greenhouses.add(g);
        errors.put(g, new LinkedList<>());
        notifyObservers(g);
    }

    public void removeGreenhouse(Greenhouse g)
    {
        greenhouses.remove(g);
        errors.remove(g);
        notifyObservers(g);
    }

    public List<Greenhouse> getGreenhouses()
    {
        return greenhouses;
    }

    public void checkForErrors()
    {
        t = new Thread(() ->{
            while(true)
            {
                greenhouses.parallelStream().forEach((g) ->
                {
                    System.out.println("Checking: " + g.toString());
                    byte[] errorArray = g.ReadErrors();
                    for(int i = 0; i < errorArray.length; i++)
                    {
                        if(errorArray[i] != 0)
                        {
                            ErrorType type = errorTypes.get(i);
                            Error e = new Error(new Date(), type);
                            if(!getErrors(g).contains(e))
                            {
                                addError(g, e);
                                System.out.println("Found error: " + type.toString());
                            }
                        }
                    }
                });
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PLCController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        t.start();
    }

    public void addError(Greenhouse g, Error e)
    {
        e.setSource(g);
        errors.get(g).add(e);
        notifyObservers(e);
    }

    public void removeError(Greenhouse g, Error e)
    {
        errors.get(g).remove(e);
        notifyObservers(e);
    }

    public List<Error> getErrors(Greenhouse g)
    {
        return errors.get(g);
    }
}