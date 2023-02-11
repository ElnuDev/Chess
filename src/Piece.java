import java.awt.*;

public class Piece {
    // Width and height of placeholder rectangle graphic
    public static final int DIMENSION = 32;
    public boolean black;

    // If no parameter, default to white
    public Piece() { }

    public Piece(boolean black) {
        this.black = black;
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
