package chess.moves;

import chess.*;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class PawnMoves {

    public ArrayList<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moveList = new ArrayList<ChessMove>();

        // TODO: add promotions
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        int dir = (color == ChessGame.TeamColor.WHITE ? 1 : -1);

        // add fwd moves
        moveList.addAll(addMoves(board, myPosition, dir, 0, false));
        // add attacking moves
        moveList.addAll(addMoves(board, myPosition, dir, 1, true));
        moveList.addAll(addMoves(board, myPosition, dir, -1, true));
        // add starter row moves
        ChessPosition checkPosition = new ChessPosition(myPosition.getRow() + dir, myPosition.getColumn());
        ChessPiece checkPiece = board.getPiece(checkPosition);
        if ((myPosition.getRow() == 2 || myPosition.getRow() == 7) && checkPiece == null) {
            moveList.addAll(addMoves(board, myPosition, 2 * dir, 0, false));
        }

        return moveList;
    }

    private ArrayList<ChessMove> addMoves(ChessBoard board, ChessPosition position, int moveRow, int moveCol, boolean attack) {
        ArrayList<ChessMove> addedMoves = new ArrayList<ChessMove>();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        int newRow = position.getRow() + moveRow;
        int newCol = position.getColumn() + moveCol;

        // check if out of edge of board
        if (newRow < 9 && newRow > 0 && newCol < 9 && newCol > 0) {
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            ChessPiece checkPiece = board.getPiece(newPosition);

            // if attack mode and the spot is taken
            if (attack && checkPiece != null) {
                // if piece is opposite color, add the move
                if (checkPiece.getTeamColor() != color) {
                    addedMoves.add(new ChessMove(position, newPosition, null));
                }
            }
            // if not attack mode, and spot is empty, add the move
            if (!attack && checkPiece == null) {
                addedMoves.add(new ChessMove(position, newPosition, null));
            }
        }
        return addedMoves;
    }
}
