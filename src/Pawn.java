import java.util.ArrayList;

public class Pawn extends Piece {
    public Pawn(boolean black, DrawingPanel panel) {
        super(black, panel, "black-pawn.png", "white-pawn.png");
    }

    public ArrayList<BoardCoordinate> getPossibleMoves(BoardCoordinate position, Board board) {
        ArrayList<BoardCoordinate> possibleMoves = new ArrayList<>();
        if (this.black) {
            possibleMoves.add(new BoardCoordinate(position.x, position.y + 1));
        } else {
            possibleMoves.add(new BoardCoordinate(position.x, position.y - 1));
        }
        return possibleMoves;
    }
}
