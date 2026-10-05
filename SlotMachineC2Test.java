
/**
 * Write a description of class SlotMachineC2Test here.
 * 
 * @Samuel Rojas
 * @ Juan Miguel Nope
 * @version (a version number or a date)
 */
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class SlotMachineC2Test
{
    // instance variables - replace the example below with your own
    private SlotMachine machine;
    @Before
    public void setUp(){
        machine = new SlotMachine();
    }
    @Test
    public void testeswapOk(){
        machine.spin();
        machine.swap(1,2);
        assertTrue(machine.ok());
        assertArrayEquals(new String[]{"red","yellow","red"}, machine.configuration());
    }
    @Test
    public void testswapFail() {
        machine.swap(1,1);
        assertFalse(machine.ok());
        machine.swap(1,99);
        assertFalse(machine.ok());
    }
    @Test
    public void testLockOk(){
        machine.lock(1);
        assertTrue(machine.ok());
        machine.spin();
        assertEquals("red",machine.configuration()[0]);
    }
    @Test
    public void testlockfail(){
        machine.lock(0);
        assertFalse(machine.ok());
        machine.unlock(10);
        assertFalse(machine.ok());
    }
    @Test
    public void testSpinOk(){
        machine.spin(1,2);
        assertTrue(machine.ok());
        assertEquals("blue",machine.configuration()[0]);
    }
    @Test
    public void testspinfail(){
        machine.spin(1,-3);
        assertFalse(machine.ok());
        machine.spin(50,2);
        assertFalse(machine.ok());
    }
    @Test
    public void testSpinconfigOk(){
        String[] target = new String[]{"yellow","blue","red"};
        machine.spin(target);
        assertTrue(machine.ok());
        assertArrayEquals(target,machine.configuration());
    }
    @Test
        public void testSpinconfigFail(){
        machine.spin(new String[]{"red","yellow"});
        assertFalse(machine.ok());
        machine.spin(new String[]{"green","red","blue"});
        assertFalse(machine.ok());
    }
    @Test
    public void testJackpot(){
        assertTrue(machine.isJackpot());
        machine.spin();
        assertFalse(machine.isJackpot());
    }
}