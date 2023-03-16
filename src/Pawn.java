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
            if (board.get(position.x, position.y + 1) == null) {
                possibleMoves.add(new Move(position, new BoardCoordinate(position.x, position.y + 1)));
                if (!moved && board.get(position.x, position.y + 2) == null) {
                    possibleMoves.add(new Move(position, new BoardCoordinate(position.x, position.y + 2)));
                }
            }
            if (board.get(position.x - 1, position.y + 1) != null) {
                possibleMoves.add(new Move(position, new BoardCoordinate(position.x - 1,position.y + 1)));
            }
            if (board.get(position.x + 1, position.y + 1) != null) {
                possibleMoves.add(new Move(position, new BoardCoordinate(position.x + 1,position.y + 1)));
            }
            for (Move move : possibleMoves) move.isPromotion = position.y + 1 == 0;
        } else {
            if (board.get(position.x, position.y - 1) == null) {
                possibleMoves.add(new Move(position, new BoardCoordinate(position.x, position.y - 1)));
                if (!moved && board.get(position.x, position.y - 2) == null) {
                    possibleMoves.add(new Move(position, new BoardCoordinate(position.x, position.y - 2)));
                }
            }
            if (board.get(position.x - 1, position.y - 1) != null) {
                possibleMoves.add(new Move(position, new BoardCoordinate(position.x - 1,position.y - 1)));
            }
            if (board.get(position.x + 1, position.y - 1) != null) {
                possibleMoves.add(new Move(position, new BoardCoordinate(position.x + 1,position.y - 1)));
            }
            for (Move move : possibleMoves) move.isPromotion = position.y - 1 == 0;
        }
        return possibleMoves;
    }
}
