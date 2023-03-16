import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(boolean black, DrawingPanel panel) {
        super(black, panel, "black-bishop.png", "white-bishop.png");
    }

    public int getValue() {
        return 3;
    }

    public ArrayList<Move> getPossibleMoves(BoardCoordinate position, Board board) {
        ArrayList<Move> possibleMoves = new ArrayList<>();

        getPossibleMovesInDirection(1, 1, position, board, possibleMoves);
        getPossibleMovesInDirection(-1, -1, position, board, possibleMoves);
        getPossibleMovesInDirection(1, -1, position, board, possibleMoves);
        getPossibleMovesInDirection(-1, 1, position, board, possibleMoves);

        return possibleMoves;
    }
}