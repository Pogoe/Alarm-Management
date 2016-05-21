/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
        System.out.println("getErrorCode");
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
        System.out.println("getDescription");
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
        System.out.println("getTime");
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
        System.out.println("getSource");
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
        System.out.println("toJSONString");
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
        System.out.println("hashCode");
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
        System.out.println("equals");
        Object obj = new Error(1, 1, new Date(1), "Hello");
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
}
