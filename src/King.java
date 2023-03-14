import java.util.ArrayList;

public class King extends Piece {
    public King(boolean black, DrawingPanel panel) {
        super(black, panel, "black-king.png", "white-king.png");
    }

    public int getValue() {
        return 12;
    }

    public ArrayList<BoardCoordinate> getPossibleMoves(BoardCoordinate position, Board board) {
        ArrayList<BoardCoordinate> possibleMoves = new ArrayList<>();
        possibleMoves.add(new BoardCoordinate(position.x - 1, position.y - 1));
        possibleMoves.add(new BoardCoordinate(position.x - 1, position.y + 1));
        possibleMoves.add(new BoardCoordinate(position.x - 1, position.y));
        possibleMoves.add(new BoardCoordinate(position.x + 1, position.y - 1));
        possibleMoves.add(new BoardCoordinate(position.x + 1, position.y + 1));
        possibleMoves.add(new BoardCoordinate(position.x + 1, position.y));
        possibleMoves.add(new BoardCoordinate(position.x, position.y + 1));
        possibleMoves.add(new BoardCoordinate(position.x, position.y - 1));
        return possibleMoves;
    }
}