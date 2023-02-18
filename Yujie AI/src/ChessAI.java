public class ChessAI {
    private final int MAX_DEPTH = 4;

    public Move findBestMove(Board board) {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;

        for (Move move : board.getAllLegalMoves()) {
            board.makeMove(move);
            int score = minimax(board, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            board.undoMove();

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private int minimax(Board board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || board.isGameOver()) {
            return evaluateBoard(board);
        }

        int score;
        if (maximizingPlayer) {
            score = Integer.MIN_VALUE;
            for (Move move : board.getAllLegalMoves()) {
                board.makeMove(move);
                score = Math.max(score, minimax(board, depth - 1, alpha, beta, false));
                board.undoMove();
                alpha = Math.max(alpha, score);
                if (beta <= alpha) {
                    break;
                }
            }
        } else {
            score = Integer.MAX_VALUE;
            for (Move move : board.getAllLegalMoves()) {
                board.makeMove(move);
                score = Math.min(score, minimax(board, depth - 1, alpha, beta, true));
                board.undoMove();
                beta = Math.min(beta, score);
                if (beta <= alpha) {
                    break;
                }
            }
        }

        return score;
    }

    private int evaluateBoard(Board board) {
        int score = 0;
        for (Piece piece : board.getPieces()) {
            if (piece.getColor() == Color.WHITE) {
                score += piece.getValue();
            } else {
                score -= piece.getValue();
            }
        }
        return score;
    }
}

