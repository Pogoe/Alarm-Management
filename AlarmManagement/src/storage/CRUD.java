package storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import plc.Solution;
import plc.Error;

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
            String url = "jdbc:postgresql://localhost:5432/RecipeDB"; //TODO: Set up the database and change name
            String user = "postgres";
            String pass = "u7e98d22";
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
        List<Solution> l = new ArrayList<>();
        try
        {
            solutionSt.setString(1, errorCode);
            ResultSet rs = solutionSt.executeQuery();
            while(rs.next())
            {
                l.add(new Solution(rs.getInt("id"), rs.getInt("errorId"), rs.getString("description")));
            }
        } catch (SQLException ex)
        {
            System.err.println(ex);
        }
        
        return l;
    }
    
    protected List<Error> getErrors(String errorCode)
    {
        List<Error> l = new ArrayList<>();
        
        try
        {
            errorSt.setString(1, errorCode);
            ResultSet rs = errorSt.executeQuery();
            while(rs.next())
            {
                l.add(new Error(rs.getInt("id"), rs.getInt("errorCode"), new Date(rs.getTimestamp("time").getTime()), rs.getString("description")));
            }
        } catch(SQLException ex)
        {
            System.err.println(ex);
        }
        
        return l;
    }
}
