package chess.moves;

import chess.*;

import java.util.ArrayList;

public class RookMoves {

    public ArrayList<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moveList = new ArrayList<>();

        int[][] moves = {
                {1,0}
                ,{-1,0}
                ,{0,-1}
                ,{0,1}
        };

        for (int[] move : moves) {
            moveList.addAll(new MoveAdder().addMoves(board, myPosition, move[0], move[1]));
        }

        return moveList;
    }
}
