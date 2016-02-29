package storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import plc.Solution;

public class CRUD
{
    private Connection conn;
    private PreparedStatement errorSt;
    private PreparedStatement solutionSt;
    
    protected CRUD()
    {
        connect();
    }
    
    private void connect()
    {
        try
        {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/RecipeDB";
            String user = "postgres"; //TODO: Change to the right value.
            String pass = "u7e98d22"; //TODO: Change to the right value.
            conn = DriverManager.getConnection(url, user, pass);
            errorSt = conn.prepareStatement("SELECT * FROM Error WHERE errorCode = ?;");
            solutionSt = conn.prepareStatement("SELECT * FROM Solution WHERE errorId = ?;");
        } catch (ClassNotFoundException | SQLException ex)
        {
            System.err.println(ex);
        }
    }
    
    protected List<Solution> getSolutions(String errorCode)
    {
        try
        {
            solutionSt.setString(1, errorCode);
        } catch (SQLException ex)
        {
            System.err.println(ex);
        }
        
        return null;
    }
}
