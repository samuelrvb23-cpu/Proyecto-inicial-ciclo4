import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

/**
 * Suite unificada de pruebas para el Ciclo 4 (Robustez, Avanzadas y Reglas Especiales).
 * Agrupa todas las validaciones de ruedas, símbolos, excepciones y límites de la SlotMachine.
 */
public class SlotMachineCycle4Test {

    private SlotMachine machine;

    @Before
    public void setUp() {
        // Inicializa una máquina estándar con 3 ruedas para cada prueba
        machine = new SlotMachine(3);
        machine.makeInvisible();
    }

    // 1. PRUEBAS DE RUEDAS Y SÍMBOLOS ESPECIALES

    @Test
    public void testAddSpecialWheelsOk() {
        machine.addWheel("lefty", 2);
        assertTrue(machine.ok());
        
        machine.addWheel("rebel", 4);
        assertTrue(machine.ok());
    }

    @Test
    public void testAddWheelInvalidPosition() {
        machine.addWheel("rebel", 0);
        assertFalse(machine.ok());

        machine.addWheel("lefty", 10);
        assertFalse(machine.ok());
    }

    @Test
    public void testRebelWheelConstraints() {
        machine.addWheel("rebel", 2); // Rueda 2 es rebelde
        
        machine.lock(2);
        assertFalse("Una rueda rebelde rechaza el bloqueo", machine.ok());

        machine.delWheel(2);
        assertFalse("Una rueda rebelde no se puede eliminar", machine.ok());

        machine.swap(1, 2);
        assertFalse("Una rueda rebelde rechaza el intercambio", machine.ok());
    }

    @Test
    public void testAddSpecialSymbols() {
        machine.addSymbol("ephemeral", 1, "red");
        assertTrue(machine.ok());

        machine.addSymbol("shy", 1, "blue");
        assertTrue(machine.ok());

        machine.addSymbol("rainbow", 1, "green");
        assertTrue(machine.ok());
    }

    @Test
    public void testAddSymbolInvalidWheel() {
        machine.addSymbol("ephemeral", 99, "red");
        assertFalse(machine.ok());

        machine.addSymbol("shy", 0, "blue");
        assertFalse(machine.ok());
    }

    @Test
    public void testLeftyWheelBehavior() {
        machine.addWheel("lefty", 2);
        machine.spin(1, 2);
        machine.spin(2, 1); 
        assertTrue(machine.ok());
    }

    @Test
    public void testSpinSpecialSymbolsActions() {
        machine.addSymbol("ephemeral", 1, "magenta");
        machine.addSymbol("rainbow", 1, "yellow");
        
        for (int i = 0; i < 5; i++) {
            machine.spin(1, 1);
        }
        assertTrue(machine.ok());
    }

    // 2. PRUEBAS AVANZADAS Y DE CONFIGURACIÓN
    
    @Test
    public void testSymbolsAndConfigurationArrays() {
        String[] symbols = machine.symbols();
        assertNotNull("El arreglo de símbolos no debe ser nulo", symbols);
        assertTrue("Debe haber al menos un símbolo base", symbols.length > 0);

        String[] config = machine.configuration();
        assertEquals("La configuración debe coincidir con el número de ruedas", 3, config.length);
        assertTrue(machine.ok());
    }

    @Test
    public void testSpinSpecificSymbolsArrayOkAndFail() {
        String[] target = machine.configuration();
        machine.spin(target);
        assertTrue("Debería aceptar un arreglo de símbolos válido", machine.ok());
        assertArrayEquals(target, machine.configuration());

        machine.spin(new String[]{"red", "yellow"});
        assertFalse("Debe fallar si el arreglo es más pequeño que las ruedas", machine.ok());

        machine.spin(new String[]{"red", "yellow", "colorInexistenteXYZ"});
        assertFalse("Debe fallar si el símbolo no existe en la rueda", machine.ok());
    }

    @Test
    public void testDeleteSymbolEdgeCases() {
        machine.delSymbol("colorInexistenteXYZ");
        assertFalse("Debe registrar fallo al intentar borrar un símbolo ausente", machine.ok());
    }

    @Test
    public void testJackpotAndDistinctSymbolsLogic() {
        int distinct = machine.distinctSymbols();
        assertTrue("Los símbolos distintos deben estar entre 1 y el total de ruedas", distinct >= 1 && distinct <= 3);

        boolean isJ = machine.isJackpot();
        assertEquals("isJackpot e isjackpot deben retornar el mismo valor", isJ, machine.isjackpot());
        assertTrue(machine.ok());
    }

    @Test
    public void testAdvancedWheelSwapAndRebelInteraction() {
        machine.swap(1, 3);
        assertTrue("Debe permitir intercambiar ruedas normales", machine.ok());

        machine.addWheel("rebel", 2);
        
        machine.swap(1, 2);
        assertFalse("Las ruedas rebeldes no permiten operaciones de swap", machine.ok());

        machine.swap(0, 5);
        assertFalse("Debe rechazar índices fuera de rango", machine.ok());
    }
}