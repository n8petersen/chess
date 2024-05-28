package chess.moves;

import chess.*;

import java.util.ArrayList;

public class BishopMoves {

    public ArrayList<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moveList = new ArrayList<>();

        //      Bishop Movement:
        //      -----------------
        //      | |x| | | | | |x|
        //      | | |x| | | |x| |
        //      | | | |x| |x| | |
        //      | | | | |B| | | |
        //      | | | |x| |x| | |
        //      | | |x| | | |x| |
        //      | |x| | | | | |x|
        //      |x| | | | | | | |
        //      -----------------

        moveList.addAll(new MoveAdder().addStraightMoves(board, myPosition, 1,-1));
        moveList.addAll(new MoveAdder().addStraightMoves(board, myPosition, 1,1));
        moveList.addAll(new MoveAdder().addStraightMoves(board, myPosition, -1,-1));
        moveList.addAll(new MoveAdder().addStraightMoves(board, myPosition, -1,1));

        return moveList;
    }
}
