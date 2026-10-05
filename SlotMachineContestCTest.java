
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * The test class SlotMachineContestCTest.
 */
public class SlotMachineContestCTest {

    @Test
    public void accordingNaRbShouldSolve(){
        SlotMachineContest solver = new SlotMachineContest();
        assertTrue(solver.solve(3) >= 0);
    }
    
    @Test 
    public void accordingNaRbShouldSolveAnother(){
        SlotMachineContest solver = new SlotMachineContest();
        // Valida otro escenario con un número de ruedas válido (ej. 4)
        assertTrue(solver.solve(4) >= 0);
    }
}