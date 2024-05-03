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
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        switch (currentType) {
            case KING:      possibleMoves = new KingMoves().getMoves();
                            break;
            case QUEEN:     possibleMoves = new QueenMoves().getMoves();
                            break;
            case BISHOP:    possibleMoves = new BishopMoves().getMoves();
                            break;
            case KNIGHT:    possibleMoves = new KnightMoves().getMoves();
                            break;
            case ROOK:      possibleMoves = new RookMoves().getMoves();
                            break;
            case PAWN:      possibleMoves = new PawnMoves().getMoves();
                            break;
        }

        return possibleMoves;
    }
}
