import java.util.ArrayList;

public class Move {
    public BoardCoordinate from;
    public BoardCoordinate to;
    public Piece captured;
    // e.g. castling, castle move. Done after.
    public Move submove;

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