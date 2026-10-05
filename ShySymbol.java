public class ShySymbol extends Symbol {
    public ShySymbol(String color, int value) {
        super(color, value);
    }

    @Override
    public void spinAction() {
        // Alterna el estado de visible a invisible cada vez que se selecciona/gira
        setVisible(!isVisible());
    }
}