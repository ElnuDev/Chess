public class BoardCoordinate extends Coordinate {
    public BoardCoordinate(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof BoardCoordinate other)) return false;
        return other.x == x && other.y == y;
    }
}
