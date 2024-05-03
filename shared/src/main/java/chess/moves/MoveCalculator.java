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

        switch (currentType) {
            case KING:
                            break;
            case QUEEN:
                            break;
            case BISHOP:
                            break;
            case KNIGHT:
                            break;
            case ROOK:
                            break;
            case PAWN:
                            break;
        }

        return new ArrayList<>();
    }
}
