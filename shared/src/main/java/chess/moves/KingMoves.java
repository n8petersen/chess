package chess.moves;

import chess.*;

import java.util.ArrayList;

public class KingMoves {

    public ArrayList<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moveList = new ArrayList<>();

        //      King Movement:
        //      -----------------
        //      | | | | | | | | |
        //      | | | | | | | | |
        //      | | | |x|x|x| | |
        //      | | | |x|K|x| | |
        //      | | | |x|x|x| | |
        //      | | | | | | | | |
        //      | | | | | | | | |
        //      | | | | | | | | |
        //      -----------------

        // add all the possible moves
        moveList.addAll(addMoves(board, myPosition, 1,-1));
        moveList.addAll(addMoves(board, myPosition, 0,-1));
        moveList.addAll(addMoves(board, myPosition, -1,-1));
        moveList.addAll(addMoves(board, myPosition, 1,0));
        moveList.addAll(addMoves(board, myPosition, -1,0));
        moveList.addAll(addMoves(board, myPosition, 1,1));
        moveList.addAll(addMoves(board, myPosition, 0,1));
        moveList.addAll(addMoves(board, myPosition, -1,1));
        return moveList;
    }

    private ArrayList<ChessMove> addMoves(ChessBoard board, ChessPosition position, int moveRow, int moveCol) {
        ArrayList<ChessMove> addedMoves = new ArrayList<>();
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
