package chess.moves;

import chess.*;

import java.util.ArrayList;

public class QueenMoves {

    public ArrayList<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moveList = new ArrayList<>();

        moveList.addAll(new MoveAdder().addStraightMoves(board, myPosition, 1,-1));
        moveList.addAll(new MoveAdder().addStraightMoves(board, myPosition, 1,1));
        moveList.addAll(new MoveAdder().addStraightMoves(board, myPosition, -1,-1));
        moveList.addAll(new MoveAdder().addStraightMoves(board, myPosition, -1,1));
        moveList.addAll(new MoveAdder().addStraightMoves(board, myPosition, 1,0));
        moveList.addAll(new MoveAdder().addStraightMoves(board, myPosition, -1,0));
        moveList.addAll(new MoveAdder().addStraightMoves(board, myPosition, 0,-1));
        moveList.addAll(new MoveAdder().addStraightMoves(board, myPosition, 0,1));

        return moveList;
    }
}
