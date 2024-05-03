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
        ChessPiece.PieceType currentType = currentPiece.getPieceType();

        return switch (currentType) {
            case KING -> new KingMoves().getMoves(board, myPosition);
            case QUEEN -> new QueenMoves().getMoves();
            case BISHOP -> new BishopMoves().getMoves();
            case KNIGHT -> new KnightMoves().getMoves();
            case ROOK -> new RookMoves().getMoves();
            case PAWN -> new PawnMoves().getMoves();
        };
    }
}
