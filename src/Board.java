import java.awt.*;

public class Board {
    static final int TILE_SIZE = 64;
    static final int BOARD_SIZE = 8;
    static final int DIMENSION = TILE_SIZE * BOARD_SIZE;
    // Colors from Lost Century 24
    // https://lospec.com/palette-list/lost-century-24
    static final Color BLACK = new Color(0x6c595c);
    static final Color WHITE = new Color(0xab9b8e);
    final DrawingPanel panel;
    final Graphics graphics;

    Piece[][] board;
    BoardCoordinate dragging = null;

    public Board() {
        panel = new DrawingPanel(DIMENSION, DIMENSION);
        panel.onMouseDown(this::handleMouseDown);
        panel.onMouseDrag(this::draw);
        panel.onMouseUp(this::handleMouseUp);
        graphics = panel.getGraphics();
        board = new Piece[BOARD_SIZE][BOARD_SIZE];
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                set(x, y, new Piece(true));
                set(x, y + 6, new Piece(false));
            }
        }
    }

    public Piece get(int x, int y) {
        return board[y][x];
    }

    public Piece get(BoardCoordinate coordinate) {
        return get(coordinate.x, coordinate.y);
    }

    public void set(int x, int y, Piece piece) {
        board[y][x] = piece;
    }

    public void set(BoardCoordinate coordinate, Piece piece) {
        set(coordinate.x, coordinate.y, piece);
    }

    public void move(int fromX, int fromY, int toX, int toY) {
        set(toX, toY, get(fromX, fromY));
        set(fromX, fromY, null);
    }

    public void move(BoardCoordinate from, BoardCoordinate to) {
        move(from.x, from.y, to.x, to.y);
    }

    void handleMouseDown(int x, int y) {
        BoardCoordinate coordinate = new ScreenCoordinate(x, y).toBoard();
        if (get(coordinate) == null) return;
        dragging = coordinate;
        draw(x, y);
    }

    void handleMouseUp(int x, int y) {
        BoardCoordinate newCoordinate = new ScreenCoordinate(x, y).toBoard();
        if (!dragging.equals(newCoordinate)) {
            Piece capturedPiece = get(newCoordinate);
            if (capturedPiece == null || capturedPiece.black != get(dragging).black) {
                move(dragging, newCoordinate);
            }
        }
        dragging = null;
        draw();
    }

    public void draw() {
        draw(0, 0);
    }

    public void draw(int mouseX, int mouseY) {
        // Draw board
        graphics.setColor(WHITE);
        graphics.fillRect(0, 0, DIMENSION, DIMENSION);
        graphics.setColor(BLACK);
        for (int y = 0; y < BOARD_SIZE; y++)
            for (int x = y % 2; x < BOARD_SIZE; x += 2)
                graphics.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        // Draw pieces

        forEachPiece((coordinate, piece) -> {
            int x, y;
            if (coordinate.equals(dragging)) {
                x = mouseX;
                y = mouseY;
            } else {
                x = coordinate.x * TILE_SIZE + TILE_SIZE / 2;
                y = coordinate.y * TILE_SIZE + TILE_SIZE / 2;
            }
            piece.draw(graphics, x, y);
        });
    }

    @FunctionalInterface
    interface PieceActionXY {
        void forEachTile(int x, int y, Piece piece);
    }

    @FunctionalInterface
    interface PieceActionCoordinate {
        void forEachTile(BoardCoordinate coordinate, Piece piece);
    }

    public void forEachPiece(PieceActionXY tileAction) {
        for (int y = 0; y < BOARD_SIZE; y++)
            for (int x = 0; x < BOARD_SIZE; x++) {
                Piece piece = board[y][x];
                if (piece == null) continue;
                tileAction.forEachTile(x, y, piece);
            }
    }

    public void forEachPiece(PieceActionCoordinate tileAction) {
        forEachPiece((x, y, piece) -> tileAction.forEachTile(new BoardCoordinate(x, y), board[y][x]));
    }
}

