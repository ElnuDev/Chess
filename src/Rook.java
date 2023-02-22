import java.util.ArrayList;

public class Rook extends Piece {
    public Rook(boolean black) {
        super(black);
    }

    public ArrayList<BoardCoordinate> getPossibleMoves(BoardCoordinate position) {
        ArrayList<BoardCoordinate> possibleMoves = new ArrayList<>();
        for(int i = 1; i <= 7; i++) {
            possibleMoves.add(new BoardCoordinate(position.x - i, position.y));
            possibleMoves.add(new BoardCoordinate(position.x + i, position.y));
            possibleMoves.add(new BoardCoordinate(position.x, position.y - i));
            possibleMoves.add(new BoardCoordinate(position.x, position.y + i));
        }
        return possibleMoves;
    }
}