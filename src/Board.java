import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

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
    static final Color WHITE = new Color(0x847875);
    static final Color HIGHLIGHT = new Color(0xab9b8e7f, true);
    static final Color MOVE_HIGHLIGHT = new Color(0xafb381);
    static final Color CAPTURE_HIGHLIGHT = new Color(0xb65c5f);
    King blackKing;
    King whiteKing;
    final DrawingPanel panel;
    final Graphics graphics;
    public boolean aiThinking = false;

    // The board is a two-dimensional array of nullable pieces
    Piece[][] board;
    // The current board coordinate that's being dragged
    BoardCoordinate dragging = null;
    ArrayList<Move> legalMoves = null;
    Stack<Move> moveHistory;
    public boolean isGameOver;
    public boolean isStalemate;
    public boolean victor;
    Image youWin;
    Image youLose;
    Image stalemate;

    public Board() {
        moveHistory = new Stack<>();

        // Initialize DrawingPanel
        panel = new DrawingPanel(DIMENSION, DIMENSION);
        youWin = panel.loadImage("you-win.png");
        youLose = panel.loadImage("you-lose.png");
        stalemate = panel.loadImage("stalemate.png");
        graphics = panel.getGraphics();

        // Connect up event handlers
        panel.onMouseDown(this::handleMouseDown);
        panel.onMouseDrag((x, y) -> {
            // We want to re-render with new mouse position when dragging pieces
            if (dragging != null) draw(x, y);
        });
        panel.onMouseUp(this::handleMouseUp);

        // Initialize board
        setup();
    }

    public void setup() {
        // Clear move history
        moveHistory.clear();

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
        moveHistory.add(new Move(new BoardCoordinate(fromX, fromY), new BoardCoordinate(toX, toY), get(toX, toY)));
        set(toX, toY, get(fromX, fromY));
        set(fromX, fromY, null);
    }

    public void move(BoardCoordinate from, BoardCoordinate to) {
        move(from.x, from.y, to.x, to.y);
    }

    public void move(Move move) {
        if (move == null) return;
        // White pieces disappear if new move isn't created, don't know why
        Move copiedMove = new Move(new BoardCoordinate(move.from.x, move.from.y), new BoardCoordinate(move.to.x, move.to.y), get(move.to.x, move.to.y));
        copiedMove.submove = move.submove;
        copiedMove.isPromotion = move.isPromotion;
        moveHistory.add(copiedMove);
        // moveHistory.add(move);
        Piece movedPiece = get(move.from);
        set(move.to, move.isPromotion ? new Queen(movedPiece.black, panel) : movedPiece);
        set(move.from, null);
        move(move.submove);
        if (move.submove != null) moveHistory.pop();
    }

    public void undoMove() {
        if (moveHistory.isEmpty()) return;
        Move lastMove = moveHistory.pop();
        undoMove(lastMove);
        undoMove(lastMove.submove);
    }

    void undoMove(Move move) {
        if (move == null) return;
        Piece movedPiece = get(move.to);
        set(move.from, move.isPromotion ? new Pawn(movedPiece.black, panel) : movedPiece);
        set(move.to, move.captured);
    }

    // Mouse down event handler
    // This sets the currently dragging piece
    void handleMouseDown(int x, int y) {
        if (isGameOver) {
            isGameOver = false;
            isStalemate = false;
            setup();
            return;
        }
        if (aiThinking) return;
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

    void setLastMovedPieceAsMoved() {
        get(moveHistory.peek().to).moved = true;
    }

    void handleMouseUp(int x, int y) {
        if (aiThinking) return;
        // Get board coordinate of mouse release
        BoardCoordinate newCoordinate = new ScreenCoordinate(x, y).toBoard();
        // Only do something if new coordinate is different from the originating coordinate
        if (dragging != null && !newCoordinate.equals(dragging)) {
            // dragging is BoardCoordinate of piece being dragged
            Piece piece = get(dragging);
            ArrayList<Move> legalMoves = piece.getLegalMoves(dragging, this);
            for (Move legalMove : legalMoves) {
                if (newCoordinate.equals(legalMove.to)) {
                    move(legalMove);
                    setLastMovedPieceAsMoved();
                    checkForCheckmate();
                    // Clear dragging
                    dragging = null;
                    // Redraw without dragging
                    draw();
                    if (!isGameOver) {
                        try {
                            ChessAI.move(this);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        return;
                    }
                }
            }
        }
        // Clear dragging
        dragging = null;
        // Redraw without dragging
        draw();
    }

    public void checkForCheckmate() {
        BoardCoordinate movedCoordinate = moveHistory.peek().to;
        Piece movedPiece = get(movedCoordinate);
        King oppositeKing = movedPiece.black ? whiteKing : blackKing;
        BoardCoordinate oppositeKingPosition = null;
        boolean inCheck = false;
        for (Move move : movedPiece.getLegalMoves(movedCoordinate, this)) {
            if (get(move.to) == oppositeKing) {
                oppositeKingPosition = move.to;
                inCheck = true;
                break;
            }
        }
        if (inCheck) {
            isGameOver = true;
            for (Move move : getAllLegalMoves(!movedPiece.black)) {
                if (!oppositeKing.isInCheck(move, this)) {
                    isGameOver = false;
                    break;
                }
            }
        } else if (getAllLegalMoves(!movedPiece.black).isEmpty()) {
            isGameOver = true;
            isStalemate = true;
        }
        victor = movedPiece.black;
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
            graphics.setColor(HIGHLIGHT);
            for (Move legalMove : legalMoves)
                drawRect(legalMove.to);
            if (mousePosition != null) {
                BoardCoordinate hovering = mousePosition.toBoard();
                for (Move legalMove : legalMoves) {
                    if (legalMove.to.equals(hovering)) {
                        graphics.setColor(get(hovering) == null ? MOVE_HIGHLIGHT : CAPTURE_HIGHLIGHT);
                        drawRect(mousePosition.toBoard());
                        break;
                    }
                }
            }
        }

        // Draw pieces
        forEachPiece((boardCoordinate, piece) -> {
            // If piece is the one being dragged, render it at the mouse position
            // Otherwise, render it at the center of the board tile
            if (piece instanceof King && piece.isInCheck(this)) {
                graphics.setColor(CAPTURE_HIGHLIGHT);
                drawRect(boardCoordinate);
            }
            piece.draw(graphics, panel, boardCoordinate.equals(dragging) ? mousePosition : boardCoordinate.toScreen());
        });

        // Draw game over text
        if (isGameOver) {
            graphics.drawImage(isStalemate ? stalemate : (victor ? youLose : youWin), 0, 0, panel);
        }
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

    public ArrayList<Move> getAllLegalMoves(boolean black) {
        ArrayList<Move> allLegalMoves = new ArrayList<>();
        forEachPiece((from, piece) -> {
            if (piece.black != black) return;
            ArrayList<Move> legalTiles = piece.getLegalMoves(from, this);
            allLegalMoves.addAll(legalTiles);
        });
        return allLegalMoves;
    }
}