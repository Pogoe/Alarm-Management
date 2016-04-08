package business;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import plc.Error;

public class ConnectionController implements Observer
{
    private static ConnectionController instance;
    private HttpURLConnection conn;
    private URL url;
    private DataOutputStream output;
    
    public static ConnectionController get()
    {
        if(instance == null)
        {
            instance = new ConnectionController();
        }
        return instance;
    }
    
    public synchronized void sendError(Error e)
    {
        String json = e.toJSONString();
        try
        {
            if(url == null)
            {
                url = new URL("http://www.rstougaard.dk/errorHandler");
            }
            if(conn == null)
            {
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("json", URLEncoder.encode(json));
            }
            output = new DataOutputStream(conn.getOutputStream());
            
            conn.connect();
            output.write(conn.getURL().toString().getBytes());
            output.flush();
            output.close();
            
            System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());
        } catch (MalformedURLException ex)
        {
            Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException exc)
        {
            Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, exc);
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
    
    public static void main(String[] args)
    {
        ConnectionController c = new ConnectionController();
        c.sendError(new Error(13, 1234, new Date(), "This is a test"));
    }
}