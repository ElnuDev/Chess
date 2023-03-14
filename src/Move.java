public class Move {
    public BoardCoordinate from;
    public BoardCoordinate to;
    public Piece captured;

    public Move(BoardCoordinate from, BoardCoordinate to) {
        this.from = from;
        this.to = to;
    }

    public Move(BoardCoordinate from, BoardCoordinate to, Piece captured) {
        this.from = from;
        this.to = to;
        this.captured = captured;
    }
}
