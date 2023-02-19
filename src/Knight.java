import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(boolean black) {
        super(black);
    }

    @Override
    public ArrayList<BoardCoordinate> getPossibleMoves(BoardCoordinate position) {
        ArrayList<BoardCoordinate> possibleMoves = new ArrayList<>();
        possibleMoves.add(new BoardCoordinate(position.x, position.y - 1)); // to up
        return possibleMoves;
    }
}
