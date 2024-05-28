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
        moveList.addAll(new MoveAdder().addSpecialMoves(board, myPosition, 1,-1));
        moveList.addAll(new MoveAdder().addSpecialMoves(board, myPosition, 0,-1));
        moveList.addAll(new MoveAdder().addSpecialMoves(board, myPosition, -1,-1));
        moveList.addAll(new MoveAdder().addSpecialMoves(board, myPosition, 1,0));
        moveList.addAll(new MoveAdder().addSpecialMoves(board, myPosition, -1,0));
        moveList.addAll(new MoveAdder().addSpecialMoves(board, myPosition, 1,1));
        moveList.addAll(new MoveAdder().addSpecialMoves(board, myPosition, 0,1));
        moveList.addAll(new MoveAdder().addSpecialMoves(board, myPosition, -1,1));
        return moveList;
    }

}
