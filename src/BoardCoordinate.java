// Board coordinate class.
// Provides tile coordinates for squares on the chess board.
public class BoardCoordinate extends Coordinate {
    public BoardCoordinate(int x, int y) {
        super(x, y); // Execute superclass constructor
    }

    // TODO: reimplement as casting
    // Convert to screen coordinate by getting center pixel of tile
    public ScreenCoordinate toScreen() {
        return new ScreenCoordinate(
                x * Board.TILE_SIZE + Board.TILE_SIZE / 2,
                y * Board.TILE_SIZE + Board.TILE_SIZE / 2
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true; // Equals if same object
        if (!(obj instanceof BoardCoordinate other)) return false; // If other is not BoardCoordinate, not equals
        return other.x == x && other.y == y; // If is BoardCoordinate and x- and y-values match, equals
    }
}
