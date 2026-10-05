public class RainbowSymbol extends Symbol {
    private static final String[] COLORS = {"red", "yellow", "blue", "green", "magenta", "black"};
    private int colorIndex;

    public RainbowSymbol(int value) {
        super("red", value);
        this.colorIndex = 0;
    }

    @Override
    public void spinAction() {
        colorIndex = (colorIndex + 1) % COLORS.length;
        setColor(COLORS[colorIndex]);
    }
}