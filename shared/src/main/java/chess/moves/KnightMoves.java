package chess.moves;

import chess.*;

import java.util.ArrayList;

public class KnightMoves {

    public ArrayList<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moveList = new ArrayList<>();

        //      Knight Movement:
        //      -----------------
        //      | | | | | | | | |
        //      | | | |x| |x| | |
        //      | | |x| | | |x| |
        //      | | | | |N| | | |
        //      | | |x| | | |x| |
        //      | | | |x| |x| | |
        //      | | | | | | | | |
        //      | | | | | | | | |
        //      -----------------

        moveList.addAll(new MoveAdder().addSpecialMoves(board, myPosition, 2,-1));
        moveList.addAll(new MoveAdder().addSpecialMoves(board, myPosition, 2,1));
        moveList.addAll(new MoveAdder().addSpecialMoves(board, myPosition, 1,2));
        moveList.addAll(new MoveAdder().addSpecialMoves(board, myPosition, -1,2));
        moveList.addAll(new MoveAdder().addSpecialMoves(board, myPosition, -2,-1));
        moveList.addAll(new MoveAdder().addSpecialMoves(board, myPosition, -2,1));
        moveList.addAll(new MoveAdder().addSpecialMoves(board, myPosition, 1,-2));
        moveList.addAll(new MoveAdder().addSpecialMoves(board, myPosition, -1,-2));

        return moveList;
    }

}
