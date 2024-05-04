package chess.moves;

import chess.*;

import java.util.ArrayList;

public class KnightMoves {

    public ArrayList<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moveList = new ArrayList<ChessMove>();

        // TODO:    This one will be a little unique, but similar to the King, as there are a set of specific moves
        //          Doesn't need to check for blocking, or loop
        //          Just check for edge of board, and for own pieces for invalid moves to NOT add
        //          Any moves that are in the board with null pieces, or opposite color pieces add as valid moves

        moveList.addAll(addMoves(board, myPosition, 2,-1));
        moveList.addAll(addMoves(board, myPosition, 2,1));

        moveList.addAll(addMoves(board, myPosition, 1,2));
        moveList.addAll(addMoves(board, myPosition, -1,2));

        moveList.addAll(addMoves(board, myPosition, -2,-1));
        moveList.addAll(addMoves(board, myPosition, -2,1));

        moveList.addAll(addMoves(board, myPosition, 1,-2));
        moveList.addAll(addMoves(board, myPosition, -1,-2));


        return moveList;
    }

    private ArrayList<ChessMove> addMoves(ChessBoard board, ChessPosition position, int moveRow, int moveCol) {
        ArrayList<ChessMove> addedMoves = new ArrayList<ChessMove>();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        int newRow = position.getRow() + moveRow;
        int newCol = position.getColumn() + moveCol;

        // check if out of edge of board
        if (newRow < 9 && newRow > 0 && newCol < 9 && newCol > 0) {
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
