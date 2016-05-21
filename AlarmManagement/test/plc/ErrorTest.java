package plc;

import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Knudsen
 */
public class ErrorTest
{
    private Error instance;
    
    @Before
    public void initialize()
    {
        instance = new Error(1, 1, new Date(1), "Hello");
    }

    /**
     * Test of getErrorCode method, of class Error.
     */
    @Test
    public void testGetErrorCode()
    {
        System.out.println(this.getClass() + ": getErrorCode");
        int expResult = 1;
        int result = instance.getErrorCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDescription method, of class Error.
     */
    @Test
    public void testGetDescription()
    {
        System.out.println(this.getClass() + ": getDescription");
        String expResult = "Hello";
        String result = instance.getDescription();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTime method, of class Error.
     */
    @Test
    public void testGetTime()
    {
        System.out.println(this.getClass() + ": getTime");
        Date expResult = new Date(1);
        Date result = instance.getTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSource method, of class Error.
     */
    @Test
    public void testGetSource()
    {
        System.out.println(this.getClass() + ": getSource");
        Greenhouse g = new Greenhouse(new UDPConnection());
        instance.setSource(g);
        Greenhouse result = instance.getSource();
        assertEquals(g, result);
    }

    /**
     * Test of toJSONString method, of class Error.
     */
    @Test
    public void testToJSONString()
    {
        System.out.println(this.getClass() + ": toJSONString");
        String expResult = "{\"date\":\"Thu Jan 01 01:00:00 CET 1970\",\"errorCode\":1,\"description\":\"Hello\"}";
        String result = instance.toJSONString();
        assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class Error.
     */
    @Test
    public void testHashCode()
    {
        System.out.println(this.getClass() + ": hashCode");
        Error instance1 = new Error(1, 1, new Date(1), "Hello");
        Error instance2 = new Error(1, 1, new Date(1), "Hello");
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    /**
     * Test of equals method, of class Error.
     */
    @Test
    public void testEquals()
    {
        System.out.println(this.getClass() + ": equals");
        Object obj = new Error(1, 1, new Date(1), "Hello");
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
}
