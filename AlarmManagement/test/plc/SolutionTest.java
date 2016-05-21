/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plc;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Knudsen
 */
public class SolutionTest
{
    private Solution instance;
    
    @Before
    public void initialize()
    {
        instance = new Solution(1, 1, "Hej");
    }
    /**
     * Test of getId method, of class Solution.
     */
    @Test
    public void testGetId()
    {
        System.out.println(this.getClass() + ": getId");
        int expResult = 1;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class Solution.
     */
    @Test
    public void testSetId()
    {
        System.out.println(this.getClass() + ": setId");
        int id = 0;
        instance.setId(id);
        assertEquals(id, instance.getId());
    }

    /**
     * Test of getErrorCode method, of class Solution.
     */
    @Test
    public void testGetErrorCode()
    {
        System.out.println(this.getClass() + ": getErrorCode");
        int expResult = 1;
        int result = instance.getErrorCode();
        assertEquals(expResult, result);
    }
}
