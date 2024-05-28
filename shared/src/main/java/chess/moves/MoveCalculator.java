package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class MoveCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece currentPiece = board.getPiece(myPosition);

        int[][] rookMoves   =   {{1,0},{-1,0},{0,-1},{0,1}};
        int[][] bishopMoves =   {{1,-1},{1,1},{-1,-1},{-1,1}};
        int[][] queenMoves  =   {{1,-1},{1,1},{-1,-1},{-1,1},{1,0},{-1,0},{0,-1},{0,1}};
        int[][] knightMoves =   {{2,-1},{2,1},{1,2},{-1,2},{-2,-1},{-2,1},{1,-2},{-1,-2}};
        int[][] kingMoves   =   {{1,-1},{0,-1},{-1,-1},{1,0},{-1,0},{1,1},{0,1},{-1,1}};

        return switch (currentPiece.getPieceType()) {
            case KING ->    findMoves(board, myPosition, kingMoves);
            case QUEEN ->   findMoves(board, myPosition, queenMoves);
            case BISHOP ->  findMoves(board, myPosition, bishopMoves);
            case KNIGHT ->  findMoves(board, myPosition, knightMoves);
            case ROOK ->    findMoves(board, myPosition, rookMoves);
            case PAWN ->    new PawnMoves().getMoves(board, myPosition);
            case null ->    new ArrayList<>();
        };
    }

    private ArrayList<ChessMove> findMoves(ChessBoard board, ChessPosition position, int[][] moves) {
        ArrayList<ChessMove> moveList = new ArrayList<>();
        ChessPiece.PieceType pieceType = board.getPiece(position).getPieceType();

        if (pieceType == ChessPiece.PieceType.BISHOP || pieceType == ChessPiece.PieceType.QUEEN || pieceType == ChessPiece.PieceType.ROOK) {
            for (int[] move : moves) {
                moveList.addAll(addStraightMoves(board, position, move[0], move[1]));
            }
        } else {
            for (int[] move : moves) {
                moveList.addAll(addSpecialMoves(board, position, move[0], move[1]));
            }
        }
        return moveList;
    }

    private ArrayList<ChessMove> addStraightMoves(ChessBoard board, ChessPosition position, int moveRow, int moveCol) {
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

    private ArrayList<ChessMove> addSpecialMoves(ChessBoard board, ChessPosition position, int moveRow, int moveCol) {
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
