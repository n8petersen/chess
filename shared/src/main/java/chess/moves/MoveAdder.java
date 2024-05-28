package chess.moves;

import chess.*;

import java.util.ArrayList;

public class MoveAdder {

    public ArrayList<ChessMove> addStraightMoves(ChessBoard board, ChessPosition position, int moveRow, int moveCol) {
        ArrayList<ChessMove> addedMoves = new ArrayList<>();
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

    public ArrayList<ChessMove> addSpecialMoves(ChessBoard board, ChessPosition position, int moveRow, int moveCol) {
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
