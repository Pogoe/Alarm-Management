package plc;

import gui.GUIController;
import java.util.BitSet;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;

public class PLCController extends Observable
{
    private static PLCController instance;
    private List<Greenhouse> greenhouses;
    private Map<Greenhouse, List<Error>> errors;

    private PLCController()
    {
        this.greenhouses = Collections.synchronizedList(new LinkedList<>());
        this.errors = new ConcurrentHashMap<>();
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
    }

    public void addGreenhouse(Greenhouse g)
    {
        greenhouses.add(g);
        errors.put(g, new LinkedList<>());
        //notifyObservers(g);
    }

    public void removeGreenhouse(Greenhouse g)
    {
        greenhouses.remove(g);
        errors.remove(g);
        //notifyObservers(g);
    }

    public List<Greenhouse> getGreenhouses()
    {
        return greenhouses;
    }

    public void checkForErrors()
    {
        synchronized (greenhouses)
        {
            greenhouses.stream().forEach((g) ->
            {
                System.out.println("Checking greenhouse " + g.toString());
                BitSet bs = g.ReadErrors();
                System.out.println(bs.length());
                for(int i = 0; i < bs.length(); i++)
                {
                    if(bs.get(i))
                    {
                        System.out.println("Found error " + i + "!");
                        addError(g, new Error(i + 1, new Date(), "We have made a new error! Cheers!"));
                    }
                }
            });
        }
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
    
    public static void main(String[] args)
    {
        PLCConnection con1 = new UDPConnection(5000, "192.168.0.20");
        PLCConnection con2 = new UDPConnection(5000, "192.168.0.30");
        PLCConnection con3 = new UDPConnection(5000, "192.168.0.40");
        
        PLCController.get().addGreenhouse(new Greenhouse(con1));
        PLCController.get().addGreenhouse(new Greenhouse(con2));
        PLCController.get().addGreenhouse(new Greenhouse(con3));
        
        PLCController.get().checkForErrors();
    }
}