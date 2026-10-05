import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

/**
 * The test class SlotMachineContestTest.
 */
public class SlotMachineContestTest {
    private SlotMachineContest solver;
    
    @Before
    public void setup(){
        solver = new SlotMachineContest();
    }   
    
    @Test
    public void testSimulate(){
        assertTrue(solver.simulate(1) >= 0);
    }
    
    @Test
    public void testSolve(){
        assertTrue(solver.solve(2) >= 0);  
    }
    
    @Test
    public void testSolvePositive(){
        // Prueba con un valor válido mayor o igual a 1
        assertTrue(solver.solve(3) >= 0);
    }
    
    @Test
    public void testSimulatePositive(){
        // Prueba con otro valor válido
        assertTrue(solver.simulate(2) >= 0);
    }
}