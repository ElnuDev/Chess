public class ScreenCoordinate extends Coordinate {
    public ScreenCoordinate(int x, int y) {
        super(x, y);
    }

    public BoardCoordinate toBoard() {
        return new BoardCoordinate(x / Board.TILE_SIZE, y / Board.TILE_SIZE);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof BoardCoordinate) return obj == toBoard();
        if (!(obj instanceof ScreenCoordinate other)) return false;
        return other.x == x && other.y == y;
    }
}