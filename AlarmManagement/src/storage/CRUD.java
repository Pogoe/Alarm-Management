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
    private PreparedStatement getErrorSt;
    private PreparedStatement getSolutionSt;
    private PreparedStatement storeErrorSt;
    private PreparedStatement storeSolutionSt;
    
    protected CRUD()
    {
        connect();
    }
    
    private void connect()
    {
        try
        {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/AMDB";
            String user = "postgres";
            String pass = "u7e98d22";
            conn = DriverManager.getConnection(url, user, pass);
            getErrorSt = conn.prepareStatement("SELECT * FROM Error WHERE errorCode = ?;");
            getSolutionSt = conn.prepareStatement("SELECT * FROM Solution WHERE errorId = ?;");
            storeErrorSt = conn.prepareStatement("INSERT INTO Error(errorCode, description) VALUES (?, ?) RETURNING id;");
            storeSolutionSt = conn.prepareStatement("INSERT INTO Solution(errorId, description) VALUES (?, ?) RETURNING id;");
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
            getSolutionSt.setString(1, errorCode);
            ResultSet rs = getSolutionSt.executeQuery();
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
            getErrorSt.setString(1, errorCode);
            ResultSet rs = getErrorSt.executeQuery();
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
