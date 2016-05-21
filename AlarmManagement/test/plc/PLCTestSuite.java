package plc;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
{
    plc.SolutionTest.class, plc.ErrorTest.class, plc.ErrorTypeTest.class
})
public class PLCTestSuite
{
}
