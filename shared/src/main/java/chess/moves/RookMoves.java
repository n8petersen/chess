package chess.moves;

import chess.*;

import java.util.ArrayList;

public class RookMoves {

    public ArrayList<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moveList = new ArrayList<ChessMove>();

        // TODO:    Will look similar to previous moves we've done, checking straights
        //          Use a while loop, incrementing the move in the same direction until there is a piece blocking, or edge of board
        //          If piece is blocking, check color:
        //          If own color, don't add the move and stop loop
        //          If other color, add move and stop loop

        moveList.addAll(addMoves(board, myPosition, 1,0));
        moveList.addAll(addMoves(board, myPosition, -1,0));
        moveList.addAll(addMoves(board, myPosition, 0,-1));
        moveList.addAll(addMoves(board, myPosition, 0,1));

        return moveList;
    }

    private ArrayList<ChessMove> addMoves(ChessBoard board, ChessPosition position, int moveRow, int moveCol) {
        ArrayList<ChessMove> addedMoves = new ArrayList<ChessMove>();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        int newRow = position.getRow() + moveRow;
        int newCol = position.getColumn() + moveCol;

        // loop while still in board
        while (newRow < 9 && newRow > 0 && newCol < 9 && newCol > 0) {
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            ChessPiece checkPiece = board.getPiece(newPosition);
            if (checkPiece == null || checkPiece.getTeamColor() != color) {
                addedMoves.add(new ChessMove(position, newPosition, null));
                newRow += moveRow;
                newCol += moveCol;
            }
            if (checkPiece != null) {
                break;
            }
        }

        return addedMoves;
    }
}
