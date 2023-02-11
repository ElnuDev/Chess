// Generic abstract Coordinate class for storing an (x, y) pair.
// BoardCoordinate and ScreenCoordinate inherit from this.
public abstract class Coordinate {
    public int x;
    public int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
}