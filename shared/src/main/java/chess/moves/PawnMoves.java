package chess.moves;

import chess.*;

import java.util.ArrayList;

public class PawnMoves {

    public ArrayList<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moveList = new ArrayList<ChessMove>();

        return moveList;
    }

    private ArrayList<ChessMove> addMoves(ChessBoard board, ChessPosition position, int moveRow, int moveCol, boolean attack) {
        ArrayList<ChessMove> addedMoves = new ArrayList<ChessMove>();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        int newRow = position.getRow() + moveRow;
        int newCol = position.getColumn() + moveCol;

        // check if out of edge of board
        if (newRow < 9 && newRow > 0 && newCol < 9 && newCol > 0) {
            // TODO after checking if edge of board, check:
            // TODO - If attack mode, check that there is an enemy on spot
            // TODO -   else, check that there is NO piece, regardless of color

            // check if piece is on new spot
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            ChessPiece checkPiece = board.getPiece(newPosition);
            // check if spot is empty, or other team's piece
            if (checkPiece == null || checkPiece.getTeamColor() != color) {
                addedMoves.add(new ChessMove(position, newPosition, null));
            }
        }
        return addedMoves;
    }
}
