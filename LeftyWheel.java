import java.util.ArrayList;

public class LeftyWheel extends Wheel {
    private Wheel leftWheel;

    public LeftyWheel(ArrayList<Symbol> initialSymbols, int x, int y, Wheel leftWheel) {
        super(initialSymbols, x, y);
        this.leftWheel = leftWheel;
    }
    @Override
    protected String getframeColor(){
        return "yellow";
    }
    public void setLeftWheel(Wheel left){
        this.leftWheel = left;
    }
    @Override
    public void spin() {
        if (!isLocked && symbols.size() > 1) {
            super.spin();
            // Si tiene una rueda a la izquierda, copia su color/símbolo visible
            if (leftWheel != null) {
                Symbol leftSymbol = leftWheel.getVisibleSymbol();
                if (leftSymbol != null) {
                    this.placesymbol(leftSymbol.getColor());
                }
            }
        }
    }
}