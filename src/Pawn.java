import java.util.ArrayList;

public class Pawn extends Piece {
    public Pawn(boolean black, DrawingPanel panel) {
        super(black, panel, "black-pawn.png", "white-pawn.png");
    }

    public int getValue() {
        return 1;
    }

    public ArrayList<Move> getPossibleMoves(BoardCoordinate position, Board board) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        if (this.black) {
            possibleMoves.add(new Move(position, new BoardCoordinate(position.x, position.y + 1)));
            if (!moved) possibleMoves.add(new Move(position, new BoardCoordinate(position.x, position.y + 2)));
        } else {
            possibleMoves.add(new Move(position, new BoardCoordinate(position.x, position.y - 1)));
            if (!moved) possibleMoves.add(new Move(position, new BoardCoordinate(position.x, position.y - 2)));
        }
        return possibleMoves;
    }
}
