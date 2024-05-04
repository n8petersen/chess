package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class RookMoves {

    public ArrayList<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moveList = new ArrayList<ChessMove>();

        // TODO:    Will look similar to previous moves we've done, checking straights
        //          Use a while loop, incrementing the move in the same direction until there is a piece blocking, or edge of board
        //          If piece is blocking, check color:
        //          If own color, don't add the move and stop loop
        //          If other color, add move and stop loop

        return moveList;
    }
}
