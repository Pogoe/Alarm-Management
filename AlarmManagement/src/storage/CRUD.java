package storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import plc.Solution;
import plc.Error;

public class CRUD
{
    private Connection conn;
    private PreparedStatement getAllErrorSt;
    private PreparedStatement getErrorsBeforeSt;
    private PreparedStatement getErrorsAfterSt;
    private PreparedStatement getErrorsOnDaySt;
    private PreparedStatement getSolutionSt;
    private PreparedStatement storeErrorSt;
    private PreparedStatement storeSolutionSt;
    private PreparedStatement incrementSolutionSt;
    
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
            getAllErrorSt = conn.prepareStatement("SELECT * FROM LogError WHERE errorCode = ?;");
            getErrorsBeforeSt = conn.prepareStatement("SELECT * FROM LogError WHERE time < ?");
            getErrorsAfterSt = conn.prepareStatement("SELECT * FROM LogError WHERE time > ?");
            getErrorsOnDaySt = conn.prepareStatement("SELECT * FROM LogError WHERE time > ? AND time < ?");
            getSolutionSt = conn.prepareStatement("SELECT * FROM Solution WHERE errorId = ?;");
            storeErrorSt = conn.prepareStatement("INSERT INTO LogError(errorCode, description) VALUES (?, ?) RETURNING id;");
            storeSolutionSt = conn.prepareStatement("INSERT INTO Solution(errorId, description) VALUES (?, ?) RETURNING id;");
            incrementSolutionSt = conn.prepareStatement("UPDATE Solved SET timesSolved = timesSolved + 1 WHERE errorCode = ?;");
        } catch (ClassNotFoundException | SQLException ex)
        {
            Logger.getLogger(CRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected List<Solution> getSolutions(int errorCode)
    {
        List<Solution> l = new ArrayList<>();
        try
        {
            getSolutionSt.setInt(1, errorCode);
            ResultSet rs = getSolutionSt.executeQuery();
            while(rs.next())
            {
                l.add(new Solution(rs.getInt("id"), rs.getInt("errorId"), rs.getString("description")));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return l;
    }
        
    protected int storeSolution(int errorCode, String description)
    {
        int id = 0;
        
        try
        {
            storeSolutionSt.setInt(1, errorCode);
            storeSolutionSt.setString(2, description);
            ResultSet rs = storeSolutionSt.executeQuery();
            while(rs.next())
            {
                id = rs.getInt("id");
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return id;
    }
    
    protected void incrementSolution(Solution s)
    {
        try
        {
            incrementSolutionSt.setInt(1, s.getErrorCode());
            incrementSolutionSt.executeUpdate();
        } catch (SQLException ex)
        {
            Logger.getLogger(CRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected List<Error> getAllErrors(int errorCode)
    {
        List<Error> l = new ArrayList<>();
        
        try
        {
            getAllErrorSt.setInt(1, errorCode);
            ResultSet rs = getAllErrorSt.executeQuery();
            while(rs.next())
            {
                l.add(new Error(rs.getInt("id"), rs.getInt("errorCode"), new Date(rs.getTimestamp("time").getTime()), rs.getString("description")));
            }
        } catch(SQLException ex)
        {
            Logger.getLogger(CRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return l;
    }
    
    protected List<Error> getErrorBefore(Date date)
    {
        List<Error> l = new LinkedList<>();
        
        try
        {
            getErrorsBeforeSt.setTimestamp(1, new Timestamp(date.getTime()));
            ResultSet rs = getErrorsBeforeSt.executeQuery();
            while(rs.next())
            {
                l.add(new Error(rs.getInt("id"), rs.getInt("errorCode"), rs.getTimestamp("time"), rs.getString("description")));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return l;
    }
    
    protected List<Error> getErrorAfter(Date date)
    {
        List<Error> l = new LinkedList<>();
        
        try
        {
            getErrorsAfterSt.setTimestamp(1, new Timestamp(date.getTime()));
            ResultSet rs = getErrorsAfterSt.executeQuery();
            while(rs.next())
            {
                l.add(new Error(rs.getInt("id"), rs.getInt("errorCode"), rs.getTimestamp("time"), rs.getString("description")));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return l;
    }
    
    protected List<Error> getErrorOnDay(Date date)
    {
        List<Error> l = new LinkedList<>();
        
        try
        {
            getErrorsOnDaySt.setTimestamp(1, new Timestamp(date.getTime()));
            getErrorsOnDaySt.setTimestamp(2, new Timestamp(date.getTime()));
            ResultSet rs = getErrorsOnDaySt.executeQuery();
            while(rs.next())
            {
                l.add(new Error(rs.getInt("id"), rs.getInt("errorCode"), rs.getTimestamp("time"), rs.getString("description")));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return l;
    }
    
    protected List<Error> getErrorBetween(Date from, Date to)
    {
        List<Error> l = new LinkedList<>();
        
        try
        {
            getErrorsOnDaySt.setTimestamp(1, new Timestamp(to.getTime()));
            getErrorsOnDaySt.setTimestamp(2, new Timestamp(from.getTime()));
            ResultSet rs = getErrorsBeforeSt.executeQuery();
            while(rs.next())
            {
                l.add(new Error(rs.getInt("id"), rs.getInt("errorCode"), rs.getTimestamp("time"), rs.getString("description")));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return l;
    }
    
    protected int storeError(int errorCode, String description)
    {
        int id = 0;
        try
        {
            storeErrorSt.setInt(1, errorCode);
            storeErrorSt.setString(2, description);
            ResultSet rs = storeErrorSt.executeQuery();
            while(rs.next())
            {
                id = rs.getInt("id");
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return id;
    }
}
