import java.util.ArrayList;

public class RebelWheel extends Wheel {
    public RebelWheel(ArrayList<Symbol> initialSymbols, int x, int y) {
        super(initialSymbols, x, y);
    }
    @Override 
    protected String getframeColor(){
        return "black";

    }
    @Override
    public boolean canBeLocked(){
        return false;
    }
    @Override
    public boolean canBeDeleted(){
        return false;
    }
    @Override
    public boolean canBeSwapped(){
        return false;
    }
    @Override
    public void lock() {
        // Rebelde: se niega a ser bloqueada
        this.isLocked = false; 
    }

    @Override
    public boolean delSymbol(String color) {
        // Rebelde: se niega a eliminar símbolos
        return false; 
    }
}