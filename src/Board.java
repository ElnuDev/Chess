import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class Board {
    // Width and height of each board tile
    static final int TILE_SIZE = 64;
    // Width and height of board in tiles
    static final int BOARD_SIZE = 8;
    // Width and height of window in pixels
    static final int DIMENSION = TILE_SIZE * BOARD_SIZE;
    // Colors from Lost Century 24
    // https://lospec.com/palette-list/lost-century-24
    // new Color() takes in an integer representing the color
    // Colors are represented in hexadecimal, so we can write the hex literal by prefixing the color code with 0x
    static final Color BLACK = new Color(0x6c595c);
    static final Color WHITE = new Color(0xab9b8e);
    King blackKing;
    King whiteKing;
    final DrawingPanel panel;
    final Graphics graphics;

    // The board is a two-dimensional array of nullable pieces
    Piece[][] board;
    // The current board coordinate that's being dragged
    BoardCoordinate dragging = null;
    ArrayList<BoardCoordinate> legalMoves = null;
    Move lastMove;
    Piece captured;
    public boolean isGameOver;

    public Board() {
        // Initialize DrawingPanel
        panel = new DrawingPanel(DIMENSION, DIMENSION);
        graphics = panel.getGraphics();

        // Connect up event handlers
        panel.onMouseDown(this::handleMouseDown);
        panel.onMouseDrag((x, y) -> {
            // We want to re-render with new mouse position when dragging pieces
            if (dragging != null) draw(x, y);
        });
        panel.onMouseUp(this::handleMouseUp);

        // Initialize board
        board = new Piece[BOARD_SIZE][BOARD_SIZE];

        // Initialize pieces
        for(int i = 0; i <= 7; i++){
            if(i == 0 || i == 7){
                for(int j = 0; j <= 7; j++){
                    if(j == 0 || j == 7){
                        set(j, i, new Rook(i==0, panel));
                    } else if(j == 1 || j == 6){
                        set(j, i, new Knight(i==0, panel));
                    } else if(j == 2 || j == 5){
                        set(j, i, new Bishop(i==0, panel));
                    } else if(j == 4){
                        if (i == 0) {
                            blackKing = new King(true, panel);
                            set(j, i, blackKing);
                        } else {
                            whiteKing = new King(false, panel);
                            set(j, i, whiteKing);
                        }
                    } else {
                        set(j, i, new Queen(i==0, panel));
                    }
                }
            }
            if(i == 1 || i == 6){
                for(int j = 0; j <= 7; j++){
                    set(j, i, new Pawn(i==1, panel));
                }
            }
        }
    }

    public boolean outOfBounds(int x, int y) {
        return x < 0 || y < 0 || x >= BOARD_SIZE || y >= BOARD_SIZE;
    }

    public boolean outOfBounds(BoardCoordinate coordinate) {
        return outOfBounds(coordinate.x, coordinate.y);
    }

    public Piece get(int x, int y) {
        if (outOfBounds(x, y)) return null;
        return board[y][x];
    }

    public Piece get(BoardCoordinate coordinate) {
        if (coordinate == null) return null;
        return get(coordinate.x, coordinate.y);
    }

    public void set(int x, int y, Piece piece) {
        board[y][x] = piece;
    }

    public void set(BoardCoordinate coordinate, Piece piece) {
        set(coordinate.x, coordinate.y, piece);
    }

    public void move(int fromX, int fromY, int toX, int toY) {
        lastMove = new Move(new BoardCoordinate(fromX, fromY), new BoardCoordinate(toX, toY));
        captured = get(toX, toY);
        set(toX, toY, get(fromX, fromY));
        set(fromX, fromY, null);
    }

    public void move(BoardCoordinate from, BoardCoordinate to) {
        move(from.x, from.y, to.x, to.y);
    }

    public void move(Move move) {
        move(move.from, move.to);
        draw(new ScreenCoordinate(0, 0));
    }

    public void undoMove() {
        if (lastMove == null) return;
        set(lastMove.from, get(lastMove.to));
        set(lastMove.to, captured);
        lastMove = null;
    }

    // Mouse down event handler
    // This sets the currently dragging piece
    void handleMouseDown(int x, int y) {
        // Get board coordinate of mouse click
        BoardCoordinate coordinate = new ScreenCoordinate(x, y).toBoard();
        // If there's no piece there, return
        Piece piece = get(coordinate);
        if (piece == null || piece.black) return;
        // Set currently dragging piece to that coordinate
        dragging = coordinate;
        legalMoves = piece.getLegalMoves(coordinate, this);
        // Redraw with dragging
        draw(x, y);
    }

    void handleMouseUp(int x, int y) {
        // Get board coordinate of mouse release
        BoardCoordinate newCoordinate = new ScreenCoordinate(x, y).toBoard();
        // Only do something if new coordinate is different from the originating coordinate
        if (dragging != null && !newCoordinate.equals(dragging)) {
            // dragging is BoardCoordinate of piece being dragged
            Piece piece = get(dragging);
            move(dragging, newCoordinate);
            move(ChessAI.findBestMove(this));
            /*
            ArrayList<BoardCoordinate> legalMoves = piece.getLegalMoves(dragging, this);
            for (BoardCoordinate legalMove : legalMoves) {
                if (newCoordinate.equals(legalMove)) {
                    move(dragging, newCoordinate);
                    // QUICK TESTING CODE
                    Piece movedPiece = get(newCoordinate);
                    King oppositeKing = movedPiece.black ? whiteKing : blackKing;
                    BoardCoordinate oppositeKingPosition = null;
                    boolean inCheck = false;
                    for (BoardCoordinate move : movedPiece.getLegalMoves(newCoordinate, this)) {
                        if (get(move) == oppositeKing) {
                            oppositeKingPosition = move;
                            inCheck = true;
                            break;
                        }
                    }
                    if (inCheck && oppositeKing.getLegalMoves(oppositeKingPosition, this).size() == 0)
                        isGameOver = true;


                    move(ChessAI.findBestMove(this));
                    break;
                }
            }
            */
        }
        // Clear dragging
        dragging = null;
        // Redraw without dragging
        draw();
    }

    public void draw() {
        draw(null);
    }

    public void draw(int mouseX, int mouseY) {
        draw(new ScreenCoordinate(mouseX, mouseY));
    }

    void drawRect(int x, int y) {
        graphics.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    void drawRect(BoardCoordinate coordinate) {
        drawRect(coordinate.x, coordinate.y);
    }

    public void draw(ScreenCoordinate mousePosition) {
        // Draw board
        graphics.setColor(BLACK);
        graphics.fillRect(0, 0, DIMENSION, DIMENSION);
        graphics.setColor(WHITE);
        for (int y = 0; y < BOARD_SIZE; y++)
            for (int x = y % 2; x < BOARD_SIZE; x += 2)
                drawRect(x, y);
        if (dragging != null) {
            graphics.setColor(new Color(0, 128, 0, 128));
            for (BoardCoordinate legalMove : legalMoves)
                drawRect(legalMove);
            if (mousePosition != null) {
                BoardCoordinate hovering = mousePosition.toBoard();
                if (legalMoves.contains(hovering)) {
                    graphics.setColor(get(hovering) == null ? new Color(0, 0, 255, 128) : new Color(255, 0, 0, 128));
                    drawRect(mousePosition.toBoard());
                }
            }
        }

        // Draw pieces
        forEachPiece((boardCoordinate, piece) -> {
            // If piece is the one being dragged, render it at the mouse position
            // Otherwise, render it at the center of the board tile
            piece.draw(graphics, panel, boardCoordinate.equals(dragging) ? mousePosition : boardCoordinate.toScreen());
        });
    }

    // Functional interfaces for forEachPiece
    // Provide ability to run code for each piece on board without having to duplicate nested for loop boilerplate

    @FunctionalInterface
    interface PieceActionXY {
        void forEachTile(int x, int y, Piece piece);
    }

    @FunctionalInterface
    interface PieceActionCoordinate {
        void forEachTile(BoardCoordinate coordinate, Piece piece);
    }

    // Run code on each tile on board
    // Usage:
    // forEachPiece((x, y, piece) -> {
    //     // do something with piece
    // });
    public void forEachPiece(PieceActionXY tileAction) {
        for (int y = 0; y < BOARD_SIZE; y++)
            for (int x = 0; x < BOARD_SIZE; x++) {
                Piece piece = get(x, y);
                if (piece == null) continue;
                tileAction.forEachTile(x, y, piece);
            }
    }

    // Run code on each tile on board
    // Usage:
    // forEachPiece((coordinate, piece) -> {
    //     // do something with piece
    // });
    public void forEachPiece(PieceActionCoordinate tileAction) {
        forEachPiece((x, y, piece) -> tileAction.forEachTile(new BoardCoordinate(x, y), get(x, y)));
    }

    public ArrayList<Move> getAllLegalMoves() {
        ArrayList<Move> allLegalMoves = new ArrayList<>();
        forEachPiece((from, piece) -> {
            if (!piece.black) return;
            ArrayList<BoardCoordinate> legalTiles = piece.getLegalMoves(from, this);
            for (BoardCoordinate to : legalTiles) {
                allLegalMoves.add(new Move(from, to));
            }
        });
        return allLegalMoves;
    }
}