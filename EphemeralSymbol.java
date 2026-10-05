public class EphemeralSymbol extends Symbol {
    private int size;

    public EphemeralSymbol(String color, int value) {
        super(color, value);
        this.size = 40;
    }

    @Override
    public void spinAction() {
        if (size > 8) {
            size -= 8; // Reduce su tamaño progresivamente hasta quedar como un punto
        }
    }

    public int getSize() {
        return size;
    }
}