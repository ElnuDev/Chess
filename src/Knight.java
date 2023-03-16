import java.awt.*;
import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(boolean black, DrawingPanel panel) {
        super(black, panel, "black-knight.png", "white-knight.png");
    }

    public int getValue() {
        return 3;
    }

    public ArrayList<Move> getPossibleMoves(BoardCoordinate position, Board board) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        possibleMoves.add(new Move(position, new BoardCoordinate(position.x - 2, position.y - 1)));
        possibleMoves.add(new Move(position, new BoardCoordinate(position.x - 1, position.y - 2)));
        possibleMoves.add(new Move(position, new BoardCoordinate(position.x + 1, position.y - 2)));
        possibleMoves.add(new Move(position, new BoardCoordinate(position.x + 2, position.y - 1)));
        possibleMoves.add(new Move(position, new BoardCoordinate(position.x + 2, position.y + 1)));
        possibleMoves.add(new Move(position, new BoardCoordinate(position.x + 1, position.y + 2)));
        possibleMoves.add(new Move(position, new BoardCoordinate(position.x - 1, position.y + 2)));
        possibleMoves.add(new Move(position, new BoardCoordinate(position.x - 2, position.y + 1)));
        return possibleMoves;
    }
}
