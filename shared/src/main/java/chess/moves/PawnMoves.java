package chess.moves;

import chess.*;

import java.util.ArrayList;

public class PawnMoves {

    public ArrayList<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moveList = new ArrayList<>();

        //      Pawn Movement:
        //      -----------------
        //      | | |*| | | | | |
        //      | | |P| | | |^| |
        //      | | | | | | |P| |
        //      | | | | | | | | |
        //      |^| | |x| |x| | |
        //      |^| | | |P| | | |
        //      |P| | | | | | | |
        //      | | | | | | | | |
        //      -----------------

        // Movement rules:
        // 1. If in starter position, it can move 2 forward, or 1 forward, if not blocked by ANY color piece
        // 2. If not in starter position, it can move forward 1, if not blocked by ANY color piece
        // 3. In any position, it can attack on the diagonals, if there is an opposite color piece there
        // 4. In any move, if the end position is on the last row, it can be promoted to: Queen, Bishop, Rook, Knight (add 4 moves)

        // change move direction based on color
        int dir = (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1);

        // add starter row moves, but only if piece in front of pawn is empty
        ChessPosition checkPosition = new ChessPosition(myPosition.getRow() + dir, myPosition.getColumn());
        ChessPiece checkPiece = board.getPiece(checkPosition);
        if ((myPosition.getRow() == 2 || myPosition.getRow() == 7) && checkPiece == null) {
            moveList.addAll(addMoves(board, myPosition, 2 * dir, 0, false));
        }

        // add fwd moves
        moveList.addAll(addMoves(board, myPosition, dir, 0, false));

        // add attacking moves
        moveList.addAll(addMoves(board, myPosition, dir, 1, true));
        moveList.addAll(addMoves(board, myPosition, dir, -1, true));

        return moveList;
    }

    private ArrayList<ChessMove> addMoves(ChessBoard board, ChessPosition position, int moveRow, int moveCol, boolean attack) {
        ArrayList<ChessMove> addedMoves = new ArrayList<>();
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
                    addedMoves.addAll(pawnCheckPromotion(position, newPosition));
                }
            }
            // if not attack mode, and spot is empty, add the move
            if (!attack && checkPiece == null) {
                addedMoves.addAll(pawnCheckPromotion(position, newPosition));
            }
        }
        return addedMoves;
    }

    private ArrayList<ChessMove> pawnCheckPromotion(ChessPosition oldPosition, ChessPosition newPosition) {
        ArrayList<ChessMove> addedMoves = new ArrayList<>();

        // if the newPosition is on the last row, add 4 moves, one of each promotion piece type
        if (newPosition.getRow() == 1 || newPosition.getRow() == 8) {
            addedMoves.add(new ChessMove(oldPosition, newPosition, ChessPiece.PieceType.KNIGHT));
            addedMoves.add(new ChessMove(oldPosition, newPosition, ChessPiece.PieceType.BISHOP));
            addedMoves.add(new ChessMove(oldPosition, newPosition, ChessPiece.PieceType.ROOK));
            addedMoves.add(new ChessMove(oldPosition, newPosition, ChessPiece.PieceType.QUEEN));
        }
        // otherwise, just add one move with promotion as null
        else {
            addedMoves.add(new ChessMove(oldPosition, newPosition, null));
        }

        return addedMoves;
    }
}
