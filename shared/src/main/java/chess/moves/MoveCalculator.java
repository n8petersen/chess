package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class MoveCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece currentPiece = board.getPiece(myPosition);

        if (currentPiece == null) {
            return new ArrayList<>();
        }

        return switch (currentPiece.getPieceType()) {
            case KING -> new KingMoves().getMoves(board, myPosition);
            case QUEEN -> new QueenMoves().getMoves(board, myPosition);
            case BISHOP -> new BishopMoves().getMoves(board, myPosition);
            case KNIGHT -> new KnightMoves().getMoves(board, myPosition);
            case ROOK -> new RookMoves().getMoves(board, myPosition);
            case PAWN -> new PawnMoves().getMoves(board, myPosition);
        };
    }
}
