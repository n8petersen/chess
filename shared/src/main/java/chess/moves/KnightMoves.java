package chess.moves;

import chess.*;

import java.util.ArrayList;

public class KnightMoves {

    public ArrayList<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moveList = new ArrayList<>();

        int[][] moves = {
                {2,-1}
                ,{2,1}
                ,{1,2}
                ,{-1,2}
                ,{-2,-1}
                ,{-2,1}
                ,{1,-2}
                ,{-1,-2}
        };

        for (int[] move : moves) {
            moveList.addAll(new MoveAdder().addMoves(board, myPosition, move[0], move[1]));
        }

        return moveList;
    }

}
