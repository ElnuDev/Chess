import java.util.ArrayList;

public class Pawn extends Piece {
    public Pawn(boolean black, DrawingPanel panel) {
        super(black, panel, "black-pawn.png", "white-pawn.png");
    }

    public int getValue() {
        return 1;
    }

    public ArrayList<BoardCoordinate> getPossibleMoves(BoardCoordinate position, Board board) {
        ArrayList<BoardCoordinate> possibleMoves = new ArrayList<>();
        if (this.black) {
            possibleMoves.add(new BoardCoordinate(position.x, position.y + 1));
            if (position.y == 1) possibleMoves.add(new BoardCoordinate(position.x, position.y + 2));
        } else {
            possibleMoves.add(new BoardCoordinate(position.x, position.y - 1));
            if (position.y == 6) possibleMoves.add(new BoardCoordinate(position.x, position.y - 2));
        }
        return possibleMoves;
    }
}
