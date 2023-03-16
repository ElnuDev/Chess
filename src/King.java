import java.util.ArrayList;

public class King extends Piece {
    public King(boolean black, DrawingPanel panel) {
        super(black, panel, "black-king.png", "white-king.png");
    }

    public int getValue() {
        return 12;
    }

    public ArrayList<Move> getPossibleMoves(BoardCoordinate position, Board board) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        
        possibleMoves.add(new Move(position, new BoardCoordinate(position.x - 1, position.y - 1)));
        possibleMoves.add(new Move(position, new BoardCoordinate(position.x - 1, position.y + 1)));
        possibleMoves.add(new Move(position, new BoardCoordinate(position.x - 1, position.y)));
        possibleMoves.add(new Move(position, new BoardCoordinate(position.x + 1, position.y - 1)));
        possibleMoves.add(new Move(position, new BoardCoordinate(position.x + 1, position.y + 1)));
        possibleMoves.add(new Move(position, new BoardCoordinate(position.x + 1, position.y)));
        possibleMoves.add(new Move(position, new BoardCoordinate(position.x, position.y + 1)));
        possibleMoves.add(new Move(position, new BoardCoordinate(position.x, position.y - 1)));
        Piece rightRook = board.get(position.x + 3, position.y);

        return possibleMoves;
    }
}