package plc;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class ErrorTypeTest
{
    private ErrorType instance;
    
    @Before
    public void initialize()
    {
        instance = new ErrorType(1234, "Hej med dig");
    }
    
    /**
     * Test of getErrorCode method, of class ErrorType.
     */
    @Test
    public void testGetErrorCode()
    {
        System.out.println(this.getClass() + ": getErrorCode");
        int expResult = 1234;
        int result = instance.getErrorCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDescription method, of class ErrorType.
     */
    @Test
    public void testGetDescription()
    {
        System.out.println(this.getClass() + ": getDescription");
        String expResult = "Hej med dig";
        String result = instance.getDescription();
        assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class ErrorType.
     */
    @Test
    public void testHashCode()
    {
        System.out.println(this.getClass() + ": hashCode");
        ErrorType instance2 = new ErrorType(1234, "Hej med dig");
        assertEquals(instance.hashCode(), instance2.hashCode());
    }

    /**
     * Test of equals method, of class ErrorType.
     */
    @Test
    public void testEquals()
    {
        System.out.println(this.getClass() + ": equals");
        Object obj = new ErrorType(1234, "Hej med dig");
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
}
