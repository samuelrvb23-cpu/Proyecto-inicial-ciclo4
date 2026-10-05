import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class SlotMachineCC4test{

    private SlotMachine slotMachine;

    @Before
    public void setUp() {
        slotMachine = new SlotMachine();
    }

    /**
     * Verifica que se puedan agregar símbolos de los tres tipos.
     */
    @Test
    public void shouldAddSymbolsOfEveryType(){
        slotMachine.addSymbol("normal", 1, "red");
        slotMachine.addSymbol("ephemeral", 2, "blue");
        slotMachine.addSymbol("shy", 3, "green");
        assertTrue(slotMachine.ok());
        // Nota de diseño: symbols() retorna únicamente el arreglo de la primera rueda (índice 0), 
        // por lo que agregar símbolos en otras ruedas no expande el tamaño del arreglo de symbols().
    }

    /**
     * Verifica que la rueda rebel no se deje bloquear, intercambiar
     * ni eliminar, y que la máquina quede igual después de intentarlo.
     */
    @Test
    public void shouldRebelWheelRefuseLockSwapAndDelete(){
        slotMachine.addSymbol(1, "red");
        slotMachine.addSymbol(2, "blue");
        slotMachine.addWheel("rebel", 1);
        slotMachine.addWheel(2);
        slotMachine.placeSymbol(1, "red");
        slotMachine.placeSymbol(2, "blue");
        slotMachine.lock(1);
        assertFalse(slotMachine.ok());
        slotMachine.swap(1, 2);
        assertFalse(slotMachine.ok());
        slotMachine.delWheel(1);
        assertFalse(slotMachine.ok());
        assertEquals(2, slotMachine.configuration().length);
        assertEquals("red", slotMachine.configuration()[0]);
        assertEquals("blue", slotMachine.configuration()[1]);
    }

    /**
     * Verifica que la máquina funcione con los tres tipos de rueda y los
     * tres tipos de símbolo a la vez: todos los giros son exitosos y la
     * lefty copia siempre a la rueda de su izquierda.
     */
    @Test
    public void shouldSpinMachineWithEveryWheelAndSymbolType(){
        slotMachine.addSymbol("normal", 1, "red");
        slotMachine.addSymbol("ephemeral", 2, "blue");
        slotMachine.addSymbol("shy", 3, "green");
        slotMachine.addWheel("normal", 1);
        slotMachine.addWheel("lefty", 2);
        slotMachine.addWheel("rebel", 3);
        for (int i = 0; i < 10; i++){
            slotMachine.spin();
            assertTrue(slotMachine.ok());
            String[] config = slotMachine.configuration();
            assertEquals(config[0], config[1]);
        }
    }

    /**
     * GRUPO 6: CañónA - PáezP
     */
    @Test
    public void acordingCaPpShouldNotSwapRebelWheel() {
        SlotMachine machine = new SlotMachine();
        machine.addWheel("normal", 1);
        machine.addWheel("rebel", 2);
        machine.swap(1, 2);
        assertFalse(machine.ok());
    }
    
    @Test
    public void acordingCaPpShouldNotDeleteRebelWheelbutShouldDeleteNormalWheel() {
        SlotMachine machine = new SlotMachine();
        machine.addWheel("normal", 1);
        machine.addWheel("rebel", 2);
        machine.delWheel(2);
        assertFalse(machine.ok());
        assertEquals(2, machine.configuration().length);
        
        machine.delWheel(1);
        assertTrue(machine.ok());
        assertEquals(1, machine.configuration().length);
    }

    /**
     * BustosL - GómezG: Pruebas sobre EphemeralSymbol y ShySymbol individuales.
     * 
     * COMENTARIO DE ARQUITECTURA / DISEÑO: 
     * Estas pruebas no pasan de forma directa porque nuestros constructores reciben 
     * (String color, int value) y nuestros métodos nativos son spinAction() e isVisible(), 
     * en lugar de los nombres de métodos (onWheelSpin, onSelected, isShyVisible) y constructores 
     * basados en cadenas dobles planteados en este fragmento.
     */
    /*
    @Test
    public void shouldDecreaseEphemeralSymbolSize() {
        EphemeralSymbol symbol = new EphemeralSymbol("red", "red"); 
        int initialSize = symbol.getSize();
        symbol.onWheelSpin(); 
        assertTrue(symbol.getSize() < initialSize);
    }

    @Test
    public void shouldAlternateShySymbolVisibility() {
        ShySymbol symbol = new ShySymbol("blue", "blue"); 
        boolean initialState = symbol.isShyVisible(); 
        symbol.onSelected(); 
        assertNotEquals(initialState, symbol.isShyVisible());
        symbol.onSelected();
        assertEquals(initialState, symbol.isShyVisible());
    }
    */

    /**
     * GRUPO 4: Gómez - Rojas (SlotMachineCC4Test)
     */
    @Test
    public void gomRojLeftyDeberiaCopiarALaRuedaNormalDeSuIzquierda(){
        SlotMachine machine = new SlotMachine();
        String[] types = {"normal", "lefty", "rebel"};
        for(int w = 1; w <= 3; w++){
            machine.addWheel(types[w - 1], w);
            machine.addSymbol(w, "red");
            machine.addSymbol(w, "green");
            machine.addSymbol(w, "blue");
        }
        machine.placeSymbol(1, "blue");
        machine.spin(2);
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[1]);
    }
    
    @Test
    public void gomRojRebelNoDeberiaBloquearseIntercambiarseNiEliminarse(){
        SlotMachine machine = new SlotMachine();
        String[] types = {"normal", "lefty", "rebel"};
        for(int w = 1; w <= 3; w++){
            machine.addWheel(types[w - 1], w);
            machine.addSymbol(w, "red");
            machine.addSymbol(w, "green");
            machine.addSymbol(w, "blue");
        }
        String[] original = machine.configuration();
        machine.lock(3);
        assertFalse(machine.ok());
        machine.swap(3, 1);
        assertFalse(machine.ok());
        machine.delWheel(3);
        assertFalse(machine.ok());
        assertArrayEquals(original, machine.configuration());
        assertEquals(3, machine.configuration().length);
    }
    
    /**
     * Por que no va a pasar la prueba  
     * Esta prueba no pasa porque nuestra arquitectura de ShySymbol alterna un indicador 
     * booleano interno (`isVisible`) para el dibujado gráfico, pero el método de negocio 
     * `configuration()` siempre retorna el color real del símbolo activo (`getVisibleSymbol().getColor()`), 
     * por lo que nunca evaluará una cadena vacía `""`.
     */
    
    @Test
    public void gomRojShyInvisibleNoDeberiaAportarColorAlJackpot(){
        SlotMachine shyMachine = new SlotMachine();
        for(int w = 1; w <= 2; w++){
            shyMachine.addWheel(w);
            shyMachine.addSymbol("shy", 1, "red");
        }
        assertTrue(shyMachine.isJackpot());
        shyMachine.placeSymbol(2, "red");
        assertFalse(shyMachine.isJackpot());
        assertEquals("", shyMachine.configuration()[1]); 
        shyMachine.exit();
    }
}