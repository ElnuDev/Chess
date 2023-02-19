import java.awt.*;
import java.util.ArrayList;

public abstract class Piece {
    // Width and height of placeholder rectangle graphic
    public static final int DIMENSION = 32;
    public boolean black;

    // If no parameter, default to white
    public Piece() { }

    public Piece(boolean black) {
        this.black = black;
    }

    public abstract ArrayList<BoardCoordinate> getPossibleMoves(BoardCoordinate position);

    public ArrayList<BoardCoordinate> getLegalMoves(BoardCoordinate position, Board board) {
        ArrayList<BoardCoordinate> legalMoves = getPossibleMoves(position);
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
                    possibleMove.y >= Board.BOARD_SIZE) {
                legalMoves.remove(i);
                i--;
            }
            // TODO: puts us into check
        }
        return legalMoves;
    }

    // The Piece class doesn't store position,
    // so when drawing we need to be provided this along with a graphics context when drawing
    public void draw(Graphics graphics, int x, int y) {
        graphics.setColor(black ? Color.BLACK : Color.WHITE);
        // Drawing is performed from the top-left corner.
        // We need the drawn rectangle to be offset by half of the width and height
        // so it is centered on the provided position.
        graphics.fillRect(x - DIMENSION / 2, y - DIMENSION / 2, DIMENSION, DIMENSION);
    }

    public void draw(Graphics graphics, ScreenCoordinate coordinate) {
        draw(graphics, coordinate.x, coordinate.y);
    }
}
