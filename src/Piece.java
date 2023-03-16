import java.awt.*;
import java.awt.image.ImageObserver;
import java.lang.reflect.Array;
import java.util.ArrayList;

public abstract class Piece {
    // Width and height of placeholder rectangle graphic
    static final int DIMENSION = 48;
    Image image;
    public boolean black;
    public boolean moved = false;

    public abstract int getValue();

    public Piece(boolean black, DrawingPanel panel, String blackImagePath, String whiteImagePath) {
        this.black = black;
        image = panel.loadImage(black ? blackImagePath : whiteImagePath);
    }

    public abstract ArrayList<Move> getPossibleMoves(BoardCoordinate position, Board board);

    public ArrayList<Move> getLegalMoves(BoardCoordinate position, Board board) {
        return getLegalMoves(position, board, true);
    }

    public ArrayList<Move> getLegalMoves(BoardCoordinate position, Board board, boolean doCheckChecks) {
        ArrayList<Move> legalMoves = getPossibleMoves(position, board);
        for (int i = 0; i < legalMoves.size(); i++) {
            Move possibleMove = legalMoves.get(i);
            Piece targetPiece = board.get(possibleMove.to);
            if (
                    // other piece of same color
                    (targetPiece != null && targetPiece.black == black) ||

                    // outside of bounds of board
                    possibleMove.to.x < 0 ||
                    possibleMove.to.y < 0 ||
                    possibleMove.to.x >= Board.BOARD_SIZE ||
                    possibleMove.to.y >= Board.BOARD_SIZE ||

                    // is in check
                    (doCheckChecks && isInCheck(new Move(position, possibleMove.to), board))
            ) {
                legalMoves.remove(i);
                i--;
            }
            // TODO: puts us into check
        }
        return legalMoves;
    }

    boolean isInCheck(Move move, Board board) {
        boolean isInCheck = false;
        board.move(move);
        outer: for (int y = 0; y < Board.BOARD_SIZE; y++) {
            for (int x = 0; x < Board.BOARD_SIZE; x++) {
                Piece piece = board.get(x, y);
                if (piece == null || piece.black == black) continue;
                ArrayList<Move> legalMoves = piece.getLegalMoves(new BoardCoordinate(x, y), board, false);
                for (Move legalMove : legalMoves) {
                    Piece pieceAtMove = board.get(legalMove.to);
                    if (pieceAtMove instanceof King) {
                        isInCheck = true;
                        break outer;
                    }
                }
            }
        }
        board.undoMove();
        return isInCheck;
    }

    // The Piece class doesn't store position,
    // so when drawing we need to be provided this along with a graphics context when drawing
    public void draw(Graphics graphics, ImageObserver observer, int x, int y) {
        graphics.drawImage(image, x - DIMENSION / 2, y - DIMENSION / 2, observer);
    }

    public void draw(Graphics graphics, ImageObserver observer, ScreenCoordinate coordinate) {
        draw(graphics, observer, coordinate.x, coordinate.y);
    }

    void getPossibleMovesInDirection(int dx, int dy, BoardCoordinate position, Board board, ArrayList<Move> possibleMoves) {
        for (
                int x = position.x + dx, y = position.y + dy;
                !board.outOfBounds(x, y);
                x += dx, y += dy) {
            BoardCoordinate coordinate = new BoardCoordinate(x, y);
            possibleMoves.add(new Move(position, coordinate));
            if (board.get(coordinate) != null) break;
        }
    }
}
