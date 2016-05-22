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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import plc.Error;
import plc.ErrorType;
import plc.Solution;

public class ConnectionController implements Observer
{
    private static ConnectionController instance;
    private URL sendErrorURL;
    private URL getErrorCodesURL;
    private URL updateDatabaseURL;
    
    private ConnectionController() throws MalformedURLException
    {
        sendErrorURL = new URL("http://127.0.0.1:8080/errorHandler");
        getErrorCodesURL = new URL("http://127.0.0.1:8080/getErrorsToJava");
        updateDatabaseURL = new URL("http://127.0.0.1:8080/updateDatabase");
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
    
    public Map<Integer, ErrorType> getErrorTypes()
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
    
    public void update(List<ErrorType> types, List<Solution> solutions)
    {
        List<ErrorType> newTypes = new LinkedList<>();
        List<Solution> newSolutions = new LinkedList<>();
        
        types.parallelStream().filter((type) ->
                (type.getErrorCode() != 0 && !type.getDescription().equals(""))).forEach((type) ->
        {
            newTypes.add(type);
        });
        solutions.parallelStream().filter((solution) ->
                (solution.getErrorCode() != 0 && solution.getId() != 0 && !solution.getDescription().equals(""))).forEach((solution) ->
        {
           newSolutions.add(solution);
        });
        
        String typesJson = new JSONArray(newTypes).toString();
        String solutionsJson = new JSONArray(newSolutions).toString();
        
        try
        {
            HttpURLConnection conn = (HttpURLConnection) updateDatabaseURL.openConnection();
            
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestProperty("errorTypes", URLEncoder.encode(typesJson));
            conn.setRequestProperty("solutions", URLEncoder.encode(solutionsJson));
            conn.setDefaultUseCaches(false);
            DataOutputStream output = new DataOutputStream(conn.getOutputStream());
            
            conn.connect();
            output.writeBytes("errortypes=" + URLEncoder.encode(typesJson, "UTF-8"));
            output.writeBytes("solutions=" + URLEncoder.encode(solutionsJson, "UTF-8"));
            output.flush();
        } catch (MalformedURLException ex)
        {
            Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Solution> getSolutions()
    {
        List<Solution> l = new LinkedList<>();
        return l;
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