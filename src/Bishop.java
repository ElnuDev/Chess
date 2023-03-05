import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(boolean black, DrawingPanel panel) {
        super(black, panel, "black-bishop.png", "white-bishop.png");
    }

    public ArrayList<BoardCoordinate> getPossibleMoves(BoardCoordinate position) {
        ArrayList<BoardCoordinate> possibleMoves = new ArrayList<>();
        for(int i = 1; i <= 7; i++) {
            possibleMoves.add(new BoardCoordinate(position.x - i, position.y - i));
            possibleMoves.add(new BoardCoordinate(position.x - i, position.y + i));
            possibleMoves.add(new BoardCoordinate(position.x + i, position.y - i));
            possibleMoves.add(new BoardCoordinate(position.x + i, position.y + i));
        }
        return possibleMoves;
    }
}
