import java.awt.*;

public class Piece {
    public static final int DIMENSION = 32;
    public boolean black;

    public Piece() { }

    public Piece(boolean black) {
        this.black = black;
    }

    public void draw(Graphics graphics, int x, int y) {
        graphics.setColor(black ? Color.BLACK : Color.WHITE);
        graphics.fillRect(x - DIMENSION / 2, y - DIMENSION / 2, DIMENSION, DIMENSION);
    }
}
