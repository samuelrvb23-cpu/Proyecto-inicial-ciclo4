/**
 * Clase base Symbol para el manejo de los símbolos en las ruedas de la SlotMachine.
 * 
 * @Samuel Ricardo Rojas Barragán
 * @Juan Miguel Nope Ascencio
 * @version 4.0 (2026)
 */ 
public class Symbol {
    private String color;
    private int value;
    private boolean isVisible;

    /**
     * Constructor para objetos de la clase Symbol.
     */
    public Symbol(String color, int value) {
        this.color = color;
        this.value = value;
        this.isVisible = true;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
    public int getSize(){
        return 40;
    }

    /**
     * Método que se ejecuta en cada giro de la rueda.
     * Permite que los símbolos especiales modifiquen su estado dinámicamente.
     */
    public void spinAction() {
        // Comportamiento por defecto para símbolos normales: no hace nada especial.
    }
}