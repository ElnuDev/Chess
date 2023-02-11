// Screen coordinate class.
// Holds pixel coordinates relative to window.
public class ScreenCoordinate extends Coordinate {
    public ScreenCoordinate(int x, int y) {
        super(x, y); // Execute superclass constructor
    }

    // TODO: reimplement as casting
    public BoardCoordinate toBoard() {
        // We can convert to board coordinates by dividing by the tile size of each square on the board
        return new BoardCoordinate(x / Board.TILE_SIZE, y / Board.TILE_SIZE);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true; // Equals if same object
        if (obj instanceof BoardCoordinate) return obj == toBoard(); // If other is BoardCoordinate, we can see if they match
        if (!(obj instanceof ScreenCoordinate other)) return false; // If not BoardCoordinate or ScreenCoordinate, not equals
        return other.x == x && other.y == y; // If is ScreenCoordinate and x- and y-values match, equals
    }
}