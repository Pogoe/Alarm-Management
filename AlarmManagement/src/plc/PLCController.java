package plc;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PLCController
{
    private static PLCController instance;
    private List<Greenhouse> greenhouses;
    private Map<Greenhouse, Set<Error>> errors;
    
    private PLCController()
    {
        this.greenhouses = new LinkedList<>();
        this.errors = new ConcurrentHashMap<>();
    }
    
    public static PLCController get()
    {
        if(instance == null)
        {
            instance = new PLCController();
        }
        return instance;
    }
    
    public void addGreenhouse(Greenhouse g)
    {
        greenhouses.add(g);
        errors.put(g, new HashSet<>());
    }
    
    public void removeGreenhouse(Greenhouse g)
    {
        greenhouses.remove(g);
        errors.remove(g);
    }
    
    public void checkForErrors()
    {
        for(Greenhouse g : greenhouses)
        {
            //Check for an error at the given greenhouse.
            //if(error): add error to the map of errors and start handling.
            //else: recurse.
        }
        checkForErrors();
    }
    
    public void addError(Greenhouse g, Error e)
    {
        errors.get(g).add(e);
    }
    
    public void removeError(Greenhouse g, Error e)
    {
        errors.get(g).remove(e);
    }
    
    public Set<Error> getErrors(Greenhouse g)
    {
        return errors.get(g);
    }
}