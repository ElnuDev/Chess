import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(boolean black, DrawingPanel panel) {
        super(black, panel, "black-queen.png", "white-queen.png");
    }

    public int getValue() {
        return 9;
    }

    public ArrayList<BoardCoordinate> getPossibleMoves(BoardCoordinate position, Board board) {
        ArrayList<BoardCoordinate> possibleMoves = new ArrayList<>();

        getPossibleMovesInDirection(0, 1, position, board, possibleMoves);
        getPossibleMovesInDirection(0, -1, position, board, possibleMoves);
        getPossibleMovesInDirection(1, 1, position, board, possibleMoves);
        getPossibleMovesInDirection(1, 0, position, board, possibleMoves);
        getPossibleMovesInDirection(1, -1, position, board, possibleMoves);
        getPossibleMovesInDirection(-1, 1, position, board, possibleMoves);
        getPossibleMovesInDirection(-1, 0, position, board, possibleMoves);
        getPossibleMovesInDirection(-1, -1, position, board, possibleMoves);

        return possibleMoves;
    }
}
