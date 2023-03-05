import java.awt.*;
import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(boolean black, DrawingPanel panel) {
        super(black, panel, "black-knight.png", "white-knight.png");
    }

    public ArrayList<BoardCoordinate> getPossibleMoves(BoardCoordinate position) {
        ArrayList<BoardCoordinate> possibleMoves = new ArrayList<>();
        possibleMoves.add(new BoardCoordinate(position.x - 2, position.y - 1));
        possibleMoves.add(new BoardCoordinate(position.x - 1, position.y - 2));
        possibleMoves.add(new BoardCoordinate(position.x + 1, position.y - 2));
        possibleMoves.add(new BoardCoordinate(position.x + 2, position.y - 1));
        possibleMoves.add(new BoardCoordinate(position.x + 2, position.y + 1));
        possibleMoves.add(new BoardCoordinate(position.x + 1, position.y + 2));
        possibleMoves.add(new BoardCoordinate(position.x - 1, position.y + 2));
        possibleMoves.add(new BoardCoordinate(position.x - 2, position.y + 1));
        // test
        return possibleMoves;
    }
}
