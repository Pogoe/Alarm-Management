package storage;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CRUDTest
{    
    public CRUDTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
        CRUDController.get();
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    @Test
    public void storeError()
    {
        assertNotSame(CRUDController.get().storeError(1234, "This is a test"), 0);
    }
    
    @Test
    public void getAllErrors()
    {
        assertFalse(CRUDController.get().getAllErrors(1234).isEmpty());
    }
    
    @Test
    public void storeSolution()
    {
        assertNotSame(CRUDController.get().storeSolution(1234, "This is a test"), 0);
    }
    
    @Test
    public void getSolutions()
    {
        assertFalse(CRUDController.get().getSolutions(1234).isEmpty());
    }
    
    @Test
    public void threadSafeness()
    {
        for(int i = 0; i < 20; i++)
        {
            Thread t = new Thread(() -> {
                System.out.println("Thread is starting..");
                CRUDController.get().storeError(1234, "This is a test of Threading");
                System.out.println("Thread is finished!");
            });
            CRUDController.get().storeError(1234, "this is a test");
        }
    }
}
