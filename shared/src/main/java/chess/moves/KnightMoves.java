package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class KnightMoves {

    public ArrayList<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moveList = new ArrayList<ChessMove>();

        // TODO:    This one will be a little unique, but similar to the King, as there are a set of specific moves
        //          Doesn't need to check for blocking, or loop
        //          Just check for edge of board, and for own pieces for invalid moves to NOT add
        //          Any moves that are in the board with null pieces, or opposite color pieces add as valid moves

        return moveList;
    }
}
