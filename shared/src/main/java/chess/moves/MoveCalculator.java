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

        int[][] kingMoves = {
                {1,-1} ,{0,-1} ,{-1,-1} ,{1,0} ,{-1,0} ,{1,1} ,{0,1} ,{-1,1}
        };

        int[][] queenMoves = {
                {1,-1} ,{1,1} ,{-1,-1} ,{-1,1} ,{1,0} ,{-1,0} ,{0,-1} ,{0,1}
        };

        int[][] bishopMoves = {
                {1,-1}, {1,1} ,{-1,-1} ,{-1,1}
        };

        int[][] knightMoves = {
                {2,-1},{2,1},{1,2},{-1,2},{-2,-1},{-2,1},{1,-2},{-1,-2}
        };

        int[][] rookMoves = {
                {1,0} ,{-1,0} ,{0,-1} ,{0,1}
        };

        return switch (currentPiece.getPieceType()) {
            case KING -> new MoveAdder().findMoves(board, myPosition, kingMoves);
            case QUEEN -> new MoveAdder().findMoves(board, myPosition, queenMoves);
            case BISHOP -> new MoveAdder().findMoves(board, myPosition, bishopMoves);
            case KNIGHT -> new MoveAdder().findMoves(board, myPosition, knightMoves);
            case ROOK -> new MoveAdder().findMoves(board, myPosition, rookMoves);
            case PAWN -> new PawnMoves().getMoves(board, myPosition);
            case null -> new ArrayList<>();
        };
    }
}
