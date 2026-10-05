import java.util.ArrayList;

/**
 * Clase base Wheel para el manejo de las ruedas de la SlotMachine.
 * 
 * @Samuel Ricardo Rojas Barragán
 * @Juan Miguel Nope Ascencio
 */
public class Wheel {
    protected ArrayList<Symbol> symbols;
    protected int xposition;
    protected int yposition;
    protected boolean isvisible;
    protected int currentindex;
    protected Circle symbolshape;
    protected Rectangle background;
    protected boolean isLocked = false;

    public Wheel(ArrayList<Symbol> initialSymbols, int x, int y) {
        this.symbols = new ArrayList<>();
        int i = 1;
        for (Symbol s : initialSymbols) {
            this.symbols.add(new Symbol(s.getColor(), i)); 
            i++;
        }
        this.xposition = x;
        this.yposition = y;
        this.isvisible = false;
        if (!this.symbols.isEmpty()){
            this.currentindex = (int)(Math.random() * this.symbols.size());
        } else {
            this.currentindex = 0;
        }
        initvisuals();
    }

    protected void initvisuals(){
        background = new Rectangle();
        background.changeSize(110, 60);
        background.changeColor(getframeColor());
        background.moveHorizontal(xposition - 60);
        background.moveVertical(yposition - 50);
        
        symbolshape = new Circle();
        symbolshape.changeSize(40);
        symbolshape.moveHorizontal(xposition);
        updatevisualSymbol();
        symbolshape.moveVertical(yposition - 15);
    }
    // requisito de usabilidad 
    protected String getframeColor(){
        return "white";
    }
    public void movewheel(int newX, int newY) {
        makeInvisible();
        this.xposition = newX;
        this.yposition = newY;
        initvisuals();
        if (isvisible){
            makeVisible();
        }
    }
    public boolean canBeLocked() { 
        return true; 
    }
    public boolean canBeDeleted() { 
        return true; 
    }
    public boolean canBeSwapped() { 
        return true; 
    }
    
    public void makeVisible(){
        isvisible = true;
        background.makeVisible();
        symbolshape.makeVisible();
    }

    public void makeInvisible(){
        isvisible = false;
        background.makeInvisible();
        symbolshape.makeInvisible();
    }

    public void addSymbol(String color){
        symbols.add(new Symbol(color, symbols.size() + 1));
        updatevisualSymbol();
    }
    public void addSymbol(Symbol symbol) {
        symbols.add(symbol);
        updatevisualSymbol();
    }
    public boolean delSymbol(String color){
        if (symbols.size() <= 1 || !containsSymbol(color)) {
            return false;
        }
        for (int i = 0; i < symbols.size(); i++) {
            if (symbols.get(i).getColor().equalsIgnoreCase(color)) {
                symbols.remove(i);
                break;
            }
        }
        if (currentindex >= symbols.size()){
            currentindex = 0;
        }
        updatevisualSymbol();
        return true;
    }

    public boolean containsSymbol(String color) {
        for (Symbol s : symbols) {
            if (s.getColor().equalsIgnoreCase(color)) 
                return true;
        }
        return false;
    }

     public void updatevisualSymbol() {
        if (!symbols.isEmpty() && symbolshape != null) {
            Symbol current = symbols.get(currentindex);
            symbolshape.changeSize(current.getSize());
            symbolshape.changeColor(current.getColor());

            if (isvisible && current.isVisible()) {
                symbolshape.makeVisible();
            } else {
                symbolshape.makeInvisible();
            }
        }
    }
    public int getSymbolCount() {
        return symbols.size();
    }

    public boolean placesymbol(String color) {
       for (int i = 0; i < symbols.size(); i++) {
           if (symbols.get(i).getColor().equalsIgnoreCase(color)){
               currentindex = i;
               updatevisualSymbol();
               return true;
           }
       }
       return false;
    }

    /**
     * Método spin virtual/polimórfico para ser sobrescrito en las ruedas hijas.
     */
    public void spin(){
        if (symbols.size() > 1 && !isLocked){
            currentindex = (currentindex + 1) % symbols.size();
            updatevisualSymbol();
            // Ejecutar la acción polimórfica del símbolo actual
            Symbol currentSymbol = getVisibleSymbol();
            if (currentSymbol != null) {
                currentSymbol.spinAction();
                updatevisualSymbol(); // Refresca por si cambió color o estado
            }
        }
    }
    public Symbol getVisibleSymbol(){
        if (symbols.isEmpty()){
            return null;
        }
        return symbols.get(currentindex);
    }

    public String[] getSymbolsArray(){
        String[] arr = new String[symbols.size()];
        for (int i = 0; i < symbols.size(); i++) {
            arr[i] = symbols.get(i).getColor();
        }
        return arr;
    }

    public void lock(){
        this.isLocked = true;
    }

    public void unlock(){
        this.isLocked = false;
    }

    public boolean islocked(){
        return this.isLocked;
    }
}