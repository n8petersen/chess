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

            //  TODO: add promotions. When adding the move, the "NULL" object should be the PieceType promotionPiece.
            //  TODO: Basically, if the end position is resulting in a promotion, we need to add a new move for each of the advanced piece types.
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
        ArrayList<ChessMove> addedMoves = new ArrayList<ChessMove>();

        // at first I had this split into black and white,
        // but I realized that pawns can't move backwards to their own first row,
        // so I combined it into one if statement.
        if (newPosition.getRow() == 1 || newPosition.getRow() == 8) {
            addedMoves.add(new ChessMove(oldPosition, newPosition, ChessPiece.PieceType.KNIGHT));
            addedMoves.add(new ChessMove(oldPosition, newPosition, ChessPiece.PieceType.BISHOP));
            addedMoves.add(new ChessMove(oldPosition, newPosition, ChessPiece.PieceType.ROOK));
            addedMoves.add(new ChessMove(oldPosition, newPosition, ChessPiece.PieceType.QUEEN));
        }
        else {
            addedMoves.add(new ChessMove(oldPosition, newPosition, null));
        }

        return addedMoves;
    }
}
