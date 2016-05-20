package business;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import plc.Error;
import plc.ErrorType;

public class ConnectionController implements Observer
{
    private static ConnectionController instance;
    private URL sendErrorURL;
    private URL getErrorCodesURL;
    
    private ConnectionController() throws MalformedURLException
    {
        sendErrorURL = new URL("http://127.0.0.1:8080/errorHandler");
        getErrorCodesURL = new URL("http://127.0.0.1:8080/getErrorsToJava");
    }
        
    public static ConnectionController get()
    {
        if(instance == null)
        {
            try {
                instance = new ConnectionController();
            } catch (MalformedURLException ex) {
                Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return instance;
    }
    
    public void sendError(Error e)
    {
        String json = e.toJSONString() + "\n\r";
        try
        {    
            HttpURLConnection conn = (HttpURLConnection) sendErrorURL.openConnection();
            
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestProperty("json", URLEncoder.encode(json));
            conn.setDefaultUseCaches(false);
            DataOutputStream output = new DataOutputStream(conn.getOutputStream());
            
            conn.connect();
            output.writeBytes("json=" + URLEncoder.encode(json, "UTF-8"));
            output.flush();
            
            System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());
        } catch (MalformedURLException ex)
        {
            Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException exc)
        {
            Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, exc);
        }
    }
    
    public Map<Integer, ErrorType> getErrors()
    {
        Map<Integer, ErrorType> errors = new HashMap<>();
        try
        {
            HttpURLConnection conn = (HttpURLConnection) getErrorCodesURL.openConnection();
            
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setDefaultUseCaches(false);
            conn.connect();
            
            BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = input.readLine()) != null)
            {
                sb.append(line);
            }
            JSONArray ja = new JSONArray(sb.toString());
            
            for(int i = 0; i < ja.length(); i++)
            {
                JSONObject o = ja.getJSONObject(i);
                errors.put(i, new ErrorType(o.getInt("errorcode"), o.getString("description")));
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | JSONException ex) {
            Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return errors;
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