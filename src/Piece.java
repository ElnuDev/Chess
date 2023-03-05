import java.awt.*;
import java.awt.image.ImageObserver;
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
    public void draw(Graphics graphics, ImageObserver observer, int x, int y) {
        graphics.drawImage(image, x - DIMENSION / 2, y - DIMENSION / 2, observer);
    }

    public void draw(Graphics graphics, ImageObserver observer, ScreenCoordinate coordinate) {
        draw(graphics, observer, coordinate.x, coordinate.y);
    }
}
