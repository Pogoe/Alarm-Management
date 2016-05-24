package plc;

import business.ConnectionController;
import gui.GUIController;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PLCController extends Observable
{
    private static PLCController instance;
    private List<InetAddress> addresses;
    private Map<Integer, ErrorType> errorTypes;
    private List<Greenhouse> greenhouses;
    private Map<Greenhouse, List<Error>> errors;
    private Thread errorThread;
    private Thread connectionThread;

    private PLCController() throws UnknownHostException
    {
        this.greenhouses = Collections.synchronizedList(new LinkedList<>());
        this.errors = new ConcurrentHashMap<>();
        this.errorTypes = new ConcurrentHashMap<>();
        this.addresses = new CopyOnWriteArrayList<>();
        addresses.add(Inet4Address.getByName("192.168.0.20"));
        addresses.add(Inet4Address.getByName("192.168.0.30"));
        addresses.add(Inet4Address.getByName("192.168.0.40"));
        addObserver(GUIController.get());
    }

    public static PLCController get()
    {
        if (instance == null)
        {
            try
            {
                instance = new PLCController();
                instance.initialize();
            } catch (UnknownHostException ex)
            {
                Logger.getLogger(PLCController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return instance;
    }
    
    public void checkIPAdresses()
    {
        connectionThread = new Thread(() ->
        {
            while(true)
            {
                addresses.parallelStream().forEach((address) ->
                {
                    try
                    {
                        if(!address.isReachable(1000))                
                        {
                            greenhouses.removeIf((Greenhouse g) ->
                            {
                                return g.getConnection().getIp().equals(address.getHostName());
                            });
                        }
                        else if(address.isReachable(1000))
                        {
                            PLCConnection conn = new UDPConnection(5000, address.getHostName());

                            if(!greenhouses.contains(new Greenhouse(conn)))
                            {
                                greenhouses.add(new Greenhouse(conn));
                            }
                        }
                    } catch (IOException ex)
                    {
                        Logger.getLogger(PLCController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                
                try
                {
                    if(Thread.currentThread().equals(connectionThread))
                    {
                        Thread.sleep(5000);
                    }
                } catch(InterruptedException ex)
                {
                    Logger.getLogger(PLCController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
        });
        connectionThread.start();
    }

    public void initialize()
    {
        addresses.parallelStream().map((address) -> new UDPConnection(5000, address.getHostAddress())).forEach((conn) ->
        {
            addGreenhouse(new Greenhouse(conn));
        });
        
        errorTypes = ConnectionController.get().getErrorTypes();
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
        errorThread = new Thread(() -> {
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
                try
                {
                    if(Thread.currentThread().equals(errorThread))
                    {
                        Thread.sleep(5000);
                    }
                } catch (InterruptedException ex)
                {
                    Logger.getLogger(PLCController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        errorThread.start();
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