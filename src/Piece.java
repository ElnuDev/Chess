import java.awt.*;
import java.awt.image.ImageObserver;
import java.lang.reflect.Array;
import java.util.ArrayList;

public abstract class Piece {
    // Width and height of placeholder rectangle graphic
    static final int DIMENSION = 48;
    Image image;
    public boolean black;

    public Piece(boolean black, DrawingPanel panel, String blackImagePath, String whiteImagePath) {
        this.black = black;
        image = panel.loadImage(black ? blackImagePath : whiteImagePath);
    }

    public abstract ArrayList<BoardCoordinate> getPossibleMoves(BoardCoordinate position, Board board);

    public ArrayList<BoardCoordinate> getLegalMoves(BoardCoordinate position, Board board) {
        return getLegalMoves(position, board, true);
    }

    public ArrayList<BoardCoordinate> getLegalMoves(BoardCoordinate position, Board board, boolean doCheckChecks) {
        ArrayList<BoardCoordinate> legalMoves = getPossibleMoves(position, board);
        for (int i = 0; i < legalMoves.size(); i++) {
            BoardCoordinate possibleMove = legalMoves.get(i);
            Piece targetPiece = board.get(possibleMove);
            if (
                    // other piece of same color
                    (targetPiece != null && targetPiece.black == black) ||

                    // outside of bounds of board
                    possibleMove.x < 0 ||
                    possibleMove.y < 0 ||
                    possibleMove.x >= Board.BOARD_SIZE ||
                    possibleMove.y >= Board.BOARD_SIZE ||

                    // is in check
                    (doCheckChecks && isInCheck(position, possibleMove, board))
            ) {
                legalMoves.remove(i);
                i--;
            }
            // TODO: puts us into check
        }
        return legalMoves;
    }

    boolean isInCheck(BoardCoordinate from, BoardCoordinate to, Board board) {
        boolean isInCheck = false;
        Piece captured = board.move(from, to);
        outer: for (int y = 0; y < Board.BOARD_SIZE; y++) {
            for (int x = 0; x < Board.BOARD_SIZE; x++) {
                Piece piece = board.get(x, y);
                if (piece == null || piece.black == black) continue;
                ArrayList<BoardCoordinate> legalMoves = piece.getLegalMoves(new BoardCoordinate(x, y), board, false);
                for (BoardCoordinate move : legalMoves) {
                    Piece pieceAtMove = board.get(move);
                    if (pieceAtMove instanceof King) {
                        isInCheck = true;
                        break outer;
                    }
                }
            }
        }
        board.move(to, from);
        board.set(to, captured);
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

    void getPossibleMovesInDirection(int dx, int dy, BoardCoordinate position, Board board, ArrayList<BoardCoordinate> possibleMoves) {
        for (
                int x = position.x + dx, y = position.y + dy;
                !board.outOfBounds(x, y);
                x += dx, y += dy) {
            BoardCoordinate coordinate = new BoardCoordinate(x, y);
            possibleMoves.add(coordinate);
            if (board.get(coordinate) != null) break;
        }
    }
}
