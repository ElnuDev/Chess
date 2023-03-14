import java.util.ArrayList;

public class Rook extends Piece {
    public Rook(boolean black, DrawingPanel panel) {
        super(black, panel, "black-rook.png", "white-rook.png");
    }

    public ArrayList<BoardCoordinate> getPossibleMoves(BoardCoordinate position, Board board) {
        ArrayList<BoardCoordinate> possibleMoves = new ArrayList<>();

        getPossibleMovesInDirection(0, 1, position, board, possibleMoves);
        getPossibleMovesInDirection(0, -1, position, board, possibleMoves);
        getPossibleMovesInDirection(1, 0, position, board, possibleMoves);
        getPossibleMovesInDirection(-1, 0, position, board, possibleMoves);


        return possibleMoves;
    }
}