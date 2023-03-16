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

        {
            BoardCoordinate rightRookPosition = new BoardCoordinate(position.x + 3, position.y);
            Piece rightRook = board.get(rightRookPosition);
            if (!moved &&
                    rightRook instanceof Rook &&
                    !rightRook.moved &&
                    board.get(position.x + 1, position.y) == null &&
                    board.get(position.x + 2, position.y) == null // &&
                    // !isInCheck(board)
            ) {
                // TODO: Does not take into account squares in castling path being threatened
                Move rightCastle = new Move(position, new BoardCoordinate(position.x + 2, position.y));
                rightCastle.submove = new Move(rightRookPosition, new BoardCoordinate(rightRookPosition.x - 2, rightRookPosition.y));
                possibleMoves.add(rightCastle);
            }
        }

        {
            BoardCoordinate leftRookPosition = new BoardCoordinate(position.x - 4, position.y);
            Piece leftRook = board.get(leftRookPosition);
            if (!moved &&
                    leftRook instanceof Rook &&
                    !leftRook.moved &&
                    board.get(position.x - 1, position.y) == null &&
                    board.get(position.x - 2, position.y) == null &&
                    board.get(position.x - 3, position.y) == null // &&
                    // !isInCheck(board)
            ) {
                // TODO: Does not take into account squares in castling path being threatened
                Move leftCastle = new Move(position, new BoardCoordinate(position.x - 2, position.y));
                leftCastle.submove = new Move(leftRookPosition, new BoardCoordinate(leftRookPosition.x + 3, leftRookPosition.y));
                possibleMoves.add(leftCastle);
            }
        }

        return possibleMoves;
    }
}