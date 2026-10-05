/**
 * Clase principal SlotMachine para la simulación de la máquina tragamonedas.
 * 
 * @Samuel Ricardo Rojas Barragán.
 * @Juan Miguel Nope Ascencio
 * @version 4.0 (2026)
 */
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.util.Collections;

public class SlotMachine {
    
    private ArrayList<Wheel> wheels;
    private ArrayList<Symbol> symboList;
    private boolean isvisible;
    private boolean lastOk;
    private Rectangle machineFrame;
    private Rectangle leverHorizontal;
    private Rectangle leververtical;
    private Circle lever;
    
    /**
     * Constructor para objetos de la clase SlotMachine (por defecto 3 ruedas).
     */
    public SlotMachine(){
        this.wheels = new ArrayList<>();
        this.symboList = new ArrayList<>();
        this.isvisible = false;
        this.lastOk = true;
        
        symboList.add(new Symbol("red", 1));
        symboList.add(new Symbol("yellow", 2));
        symboList.add(new Symbol("blue", 3));
        
        machineFrame = new Rectangle();
        machineFrame.changeSize(150, 260);
        machineFrame.changeColor("black");
        machineFrame.moveHorizontal(-20);
        machineFrame.moveVertical(-10);
        
        wheels.add(new Wheel(symboList, 60, 60));
        wheels.add(new Wheel(symboList, 140, 60));
        wheels.add(new Wheel(symboList, 220, 60));
                
        initlever();
    }

    /**
     * Constructor con 'n' ruedas y símbolos inicializados aleatoriamente.
     */
    public SlotMachine(int n){
        if (n < 1) {
            System.out.println("Numero ingresado no es valido, ingrese un numero entero positivo");
            lastOk = false;
            return;
        } else {
            this.wheels = new ArrayList<>();
            this.symboList = new ArrayList<>();
            this.isvisible = false;
            this.lastOk = true;
            String[] baseColors = {"red", "yellow", "blue", "green", "magenta", "black"};
            for (int i = 0; i < n; i++) {
                String colorName = (i < baseColors.length) ? baseColors[i] : ("color" + (i + 1));
                symboList.add(new Symbol(colorName, i + 1));
            }
            machineFrame = new Rectangle();
            machineFrame.changeSize(150, (n * 80) + 20);
            machineFrame.changeColor("black");
            machineFrame.moveHorizontal(-20);
            machineFrame.moveVertical(-10);            
            for(int i = 0; i < n; i++) {
                ArrayList<Symbol> wheelSymbols = new ArrayList<>(symboList);     
                Collections.shuffle(wheelSymbols);
                wheels.add(new Wheel(wheelSymbols, 60 + (i * 80), 60));
            }
            initlever();
        }
    }

    private void initlever() {
        int x = (wheels.size() * 80);
        leverHorizontal = new Rectangle();
        leverHorizontal.changeSize(6, 25);
        leverHorizontal.changeColor("black");
        leverHorizontal.moveHorizontal(x);
        leverHorizontal.moveVertical(50);
        
        leververtical = new Rectangle();
        leververtical.changeSize(45, 6);
        leververtical.changeColor("black");
        leververtical.moveHorizontal(x + 19);
        leververtical.moveVertical(10);
        
        lever = new Circle();
        lever.changeSize(24);
        lever.changeColor("gray");
        lever.moveHorizontal(x + 70);
        lever.moveVertical(-2);
    }
        
    public void makeVisible() {
        isvisible = true;
        machineFrame.makeVisible();
        leverHorizontal.makeVisible();
        leververtical.makeVisible();
        lever.makeVisible();
        for (Wheel w : wheels) {
            w.makeVisible();
        }
        lastOk = true;
    }

    public void makeInvisible() {
        isvisible = false;
        leverHorizontal.makeInvisible();
        machineFrame.makeInvisible();
        leververtical.makeInvisible();
        lever.makeInvisible();
        for (Wheel w : wheels) {
            w.makeInvisible();
        }
        lastOk = true;
    }

    public boolean ok() {
        return lastOk;
    }

    // --- CICLO 2 y 4: GESTIÓN DE RUEDAS ---
    public void addWheel(int pos){
        if (pos < 1 || pos > wheels.size() + 1){ 
            lastOk = false;
            return;
        }
        wheels.add(pos - 1, new Wheel(symboList, 60, 60));
        redraw();
        lastOk = true;  
    }

    /**
     * Ciclo 4: Adiciona una rueda de un tipo específico ("normal", "lefty", "rebel") en la posición indicada.
     */
    public void addWheel(String type, int pos) {
        if (pos < 1 || pos > wheels.size() + 1) {
            lastOk = false;
            return;
        }
        
        Wheel newWheel;
        if (type.equalsIgnoreCase("lefty")) {
            Wheel neighbor = (pos > 1) ? wheels.get(pos - 2) : null;
            newWheel = new LeftyWheel(symboList, 60, 60, neighbor);
        } else if (type.equalsIgnoreCase("rebel")) {
            newWheel = new RebelWheel(symboList, 60, 60);
        } else {
            newWheel = new Wheel(symboList, 60, 60);
        }
        
        wheels.add(pos - 1, newWheel);
        redraw();
        lastOk = true;
    }

    public void delWheel(int pos){
        if (wheels.size() <= 1){
            lastOk = false;
            if (isvisible) {
                JOptionPane.showMessageDialog(null, "No se puede borrar todas las ruedas");
            }
            return;
        }
        if (pos < 1 || pos > wheels.size()) {
            lastOk = false;
            return;
        }
        Wheel target = wheels.get(pos - 1);
        if (!target.canBeDeleted()) {
            lastOk = false;
            if (isvisible) {
                JOptionPane.showMessageDialog(null, "No se puede eliminar una rueda rebelde");
            }
            return;
        }

        Wheel w = wheels.remove(pos - 1);
        w.makeInvisible();
        redraw();
        lastOk = true;
    }

    private void redraw() {
        actualizarVecinosLefty();
        machineFrame.changeSize(150, (wheels.size() * 80) + 20);
        for (int i = 0; i < wheels.size(); i++) {
            wheels.get(i).movewheel(60 + (i * 80), 60);
        }
        leverHorizontal.makeInvisible();
        leververtical.makeInvisible();
        lever.makeInvisible();
        initlever();
        if (isvisible) {
            makeVisible();
        }
    }

    // --- CICLO 3 y 4: GESTIÓN DE SÍMBOLOS ---
    public void addSymbol(int wheel, String symbol){
        if (wheel < 1 || wheel > wheels.size()){
            lastOk = false;
            return;
        }
        wheels.get(wheel - 1).addSymbol(symbol);
        lastOk = true;  
    }

    /**
     * Ciclo 4: Adiciona un símbolo de un tipo específico ("normal", "ephemeral", "shy", "rainbow") a una rueda.
     */
    public void addSymbol(String type, int pos, String color) {
        if (pos < 1 || pos > wheels.size()) {
            lastOk = false;
            return;
        }
        Wheel targetWheel = wheels.get(pos - 1);
        int val = targetWheel.getSymbolCount() + 1;
        if (type.equalsIgnoreCase("ephemeral")) {
            targetWheel.addSymbol(new EphemeralSymbol(color, val)); // O se puede extender para aceptar objetos Symbol si se requiere mayor control
        } else if (type.equalsIgnoreCase("shy")) {
            targetWheel.addSymbol(new ShySymbol(color, val));
        } else if (type.equalsIgnoreCase("rainbow")) {
            targetWheel.addSymbol(new RainbowSymbol(val));
        } else {
            targetWheel.addSymbol(color);
        }
        lastOk = true;
    }

    public void delSymbol(String symbol){
        for (Wheel w: wheels) {
            if (w.containsSymbol(symbol) && w.getSymbolCount() <= 1) {
                lastOk = false;
                if (isvisible) {
                    JOptionPane.showMessageDialog(null, "No se puede eliminar el simbolo");
                }
                return;
            }
        } 
        boolean anyDeleted = false;
        for (Wheel w: wheels) {
            if (w.delSymbol(symbol)){
                anyDeleted = true;
            }
        }
        if (!anyDeleted) {
            lastOk = false;
            if (isvisible) {
                JOptionPane.showMessageDialog(null, "El simbolo no existe en ninguna rueda");
            }
            return;
        }
        lastOk = true;
    } 

    public void placeSymbol(int wheel, String symbol) {
        if (wheel < 1 || wheel > wheels.size()){
            lastOk = false;
            return;
        }
        boolean success = wheels.get(wheel - 1).placesymbol(symbol);
        if (!success) {
            lastOk = false;
            if (isvisible) {
                JOptionPane.showMessageDialog(null, "El simbolo no existe en la rueda indicada");
            }
            return;
        }
        lastOk = true;
    }

    public void spin(){
        boolean anyspin = false;
        for (Wheel w : wheels) {
            if (!w.islocked() && w.getSymbolCount() > 1) {
                int steps = (int)(Math.random() * 12) + 3; 
                for (int s = 0; s < steps; s++) {
                    w.spin();
                }
                anyspin = true;
            }
        }
        if (!anyspin) {
            lastOk = false;
            if (isvisible) {
                JOptionPane.showMessageDialog(null, "No se puede girar ya que ninguna rueda tiene suficientes simbolos o estan bloqueadas");
            }
            return;
        }
        lastOk = true;
    }

    public String[] configuration() {
        String[] config = new String[wheels.size()];
        for (int i = 0; i < wheels.size(); i++) {
            config[i] = wheels.get(i).getVisibleSymbol().getColor();
        }
        lastOk = true;
        return config;
    }

    public String[] symbols() {
        if (wheels.isEmpty()) {
            lastOk = false;
            return new String[0];
        }
        lastOk = true;
        return wheels.get(0).getSymbolsArray();
    }

    public int distinctSymbols() {
        ArrayList<String> distinct = new ArrayList<>();
        for (Wheel w : wheels){
            String sym = w.getVisibleSymbol().getColor();
            if (!distinct.contains(sym)) {
                distinct.add(sym);
            }
        }
        lastOk = true;
        return distinct.size();
    }

    public boolean isJackpot() {
        boolean jackpot = false;
        if (distinctSymbols() == 1) {
            jackpot = true;
            machineFrame.changeColor("yellow");
        } else {
            machineFrame.changeColor("black");
        }
        lastOk = true;
        return jackpot;
    }

    // Alias con minúscula por si las pruebas de la maratón lo invocan como isjackpot()
    public boolean isjackpot() {
        return isJackpot();
    }

    public void exit() {
        makeInvisible();
        System.exit(0);
    }

    public void swap(int wheel1, int wheel2){
        if (wheel1 < 1 || wheel1 > wheels.size() || wheel2 < 1 || wheel2 > wheels.size() || wheel1 == wheel2){
            lastOk = false;
            return;
        }
        Wheel w1 = wheels.get(wheel1 - 1);
        Wheel w2 = wheels.get(wheel2 - 1);

        if (!w1.canBeSwapped() || !w2.canBeSwapped()) {
            lastOk = false;
            if (isvisible) {
                JOptionPane.showMessageDialog(null, "No se puede intercambiar una rueda rebelde");
            }
            return;
        }

        Wheel temp = w1;
        wheels.set(wheel1 - 1, w2);
        wheels.set(wheel2 - 1, temp);
        
        redraw();
        lastOk = true;
    }

    public void lock(int wheel){
        if (wheel < 1 || wheel > wheels.size()){
            lastOk = false;
            return;
        }
        Wheel target = wheels.get(wheel - 1);
        if (!target.canBeLocked()) {
            lastOk = false;
            return; // La rueda rebelde rechaza el bloqueo
        }
        target.lock();
        lastOk = true;
    }

    public void unlock(int wheel){
        if (wheel < 1 || wheel > wheels.size()){
            lastOk = false;
            return;
        }
        wheels.get(wheel - 1).unlock();
        lastOk = true;
    }  
    public void spin(int wheel) {
        spin(wheel, 1);
    }
    public void spin (int wheel, int steps){
        if (wheel < 1 || wheel > wheels.size() || steps < 0){
            lastOk = false;
            if(isvisible) {
                JOptionPane.showMessageDialog(null, "Rueda o cantidad de pasos invalido"); 
            }
            return;
        }
        Wheel targetwheel = wheels.get(wheel - 1);
        
        if (targetwheel.islocked()){
            lastOk = false;
            if(isvisible) {
                JOptionPane.showMessageDialog(null, "La rueda esta bloqueada y no puede girar");
            }
            return;
        }
        if (targetwheel.getSymbolCount() <= 1){
            lastOk = false;
            if (isvisible){
                JOptionPane.showMessageDialog(null, "No se puede girar la rueda con un solo simbolo");
            }
            return;
        }
        for (int s = 0; s < steps; s++){
            targetwheel.spin(); 
        }
        lastOk = true;
    }

    public void spin(String[] setSymbols) {
        if (setSymbols == null || setSymbols.length != wheels.size()){
            lastOk = false;
            if (isvisible){
                JOptionPane.showMessageDialog(null, "El arreglo de simbolo no coincide con la cantidad de ruedas");
            }
            return;
        }
        for (int i = 0; i < wheels.size(); i++){
            if (!wheels.get(i).containsSymbol(setSymbols[i])) {
                lastOk = false;
                if (isvisible){
                    JOptionPane.showMessageDialog(null, "El simbolo " + setSymbols[i] + " no existe en la rueda " + (i + 1));
                }
                return;
            }
        }
        for (int i = 0; i < wheels.size(); i++){
            wheels.get(i).placesymbol(setSymbols[i]);
        }
        lastOk = true;
    }
    private void actualizarVecinosLefty() {
        for (int i = 0; i < wheels.size(); i++) {
            if (wheels.get(i) instanceof LeftyWheel) {
                Wheel left = (i > 0) ? wheels.get(i - 1) : null;
                ((LeftyWheel) wheels.get(i)).setLeftWheel(left);
            }
        }
    }
}