import java.util.ArrayList;
import java.util.Collections;

public class ChessAI {
    private static final int MAX_DEPTH = 3;

    public static Move findBestMove(Board board) {
        int bestScore = Integer.MIN_VALUE;

        ArrayList<Move> legalMoves = board.getAllLegalMoves(true);

        Collections.shuffle(legalMoves);
        Move bestMove = legalMoves.get(0);
        for (Move move : legalMoves) {
            board.move(move);
            int score = minimax(board, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            board.undoMove();

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private static int minimax(Board board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || board.isGameOver) {
            return evaluateBoard(board);
        }

        int score;
        if (maximizingPlayer) {
            score = Integer.MIN_VALUE;
            for (Move move : board.getAllLegalMoves(true)) {
                board.move(move);
                score = Math.max(score, minimax(board, depth - 1, alpha, beta, false));
                board.undoMove();
                alpha = Math.max(alpha, score);
                if (beta <= alpha) {
                    break;
                }
            }
        } else {
            score = Integer.MAX_VALUE;
            for (Move move : board.getAllLegalMoves(true)) {
                board.move(move);
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

    private static int evaluateBoard(Board board) {
        Score score = new Score();
        board.forEachPiece((coordinate, piece) -> {
            if (!piece.black) {
                score.add(-piece.getValue());
            } else {
                score.add(piece.getValue());
            }
        });
        return score.score;
    }
}

class Score {
    public int score;
    public void add(int value) {
        score += value;
    }
}