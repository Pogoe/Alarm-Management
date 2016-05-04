package plc;

import gui.GUIController;
import java.util.BitSet;
import java.util.Collections;
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
        }
        return instance;
    }

    public void initialize()
    {

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
        synchronized (greenhouses)
        {
            greenhouses.parallelStream().forEach((g) ->
            {
                BitSet bs = g.ReadErrors();
                for(int i = 0; i < bs.length(); i++)
                {
                    if(bs.get(i))
                    {
                        //addError(g, new Error(i, new Date()));
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
}
