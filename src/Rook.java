import java.util.ArrayList;

public class Rook extends Piece {
    public Rook(boolean black, DrawingPanel panel) {
        super(black, panel, "black-rook.png", "white-rook.png");
    }

    public int getValue() {
        return 5;
    }

    public ArrayList<Move> getPossibleMoves(BoardCoordinate position, Board board) {
        ArrayList<Move> possibleMoves = new ArrayList<>();

        getPossibleMovesInDirection(0, 1, position, board, possibleMoves);
        getPossibleMovesInDirection(0, -1, position, board, possibleMoves);
        getPossibleMovesInDirection(1, 0, position, board, possibleMoves);
        getPossibleMovesInDirection(-1, 0, position, board, possibleMoves);

        return possibleMoves;
    }
}