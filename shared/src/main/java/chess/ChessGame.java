package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turnColor;
    private ChessBoard board;

    public ChessGame() {
        // Create a new board, and set the current color to White since White starts the game.
        this.turnColor = TeamColor.WHITE;
        this.board = new ChessBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turnColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turnColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        // Takes as input a position on the chessboard and returns all moves the piece there can legally make.
        // If there is no piece at that location, this method returns null.
        ArrayList<ChessMove> moveList = new ArrayList<ChessMove>();
        ChessPiece piece = board.getPiece(startPosition);

        // only add moves if the piece is null
        if (piece != null) {
            // get all the possible moves for the current piece
            Collection<ChessMove> checkMoves = piece.pieceMoves(board, startPosition);

            // TODO: we need to add a way to make sure the moves are actually valid and legal.
            //  Perhaps a helper function is necessary. For now we will just add them all.
            moveList.addAll(checkMoves);
        }

        return moveList;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // Receives a given move and executes it, provided it is a legal move.
        // If the move is illegal, it throws an InvalidMoveException.
        // A move is illegal if the chess piece cannot move there, if the move leaves the team’s king in danger,
        // or if it’s not the corresponding team's turn.
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // Returns true if the specified team’s King could be captured by an opposing piece.
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // Returns true if the given team has no way to protect their king from being captured.
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //Returns true if the given team has no legal moves and it is currently that team’s turn.
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        throw new RuntimeException("Not implemented");
    }

    // TODO: Add extra moves:
    //  Castling:
    //    This is a special move where the King and a Rook move simultaneously.
    //    The castling move can only be taken when 4 conditions are met:
    //     1. Neither the King nor Rook have moved since the game started
    //     2. There are no pieces between the King and the Rook
    //     3. The King is not in Check
    //     4. Both your Rook and King will be safe after making the move (cannot be captured by any enemy pieces).
    //    To Castle, the King moves 2 spaces towards the Rook, and the Rook "jumps" the king moving to the position next to and on the other side of the King.
    //    This is represented in a ChessMove as the king moving 2 spaces to the side.
    //  En Passant
    //   This is a special move taken by a Pawn in response to your opponent double moving a Pawn.
    //   If your opponent double moves a pawn so it ends next to yours (skipping the position where your pawn could have captured their pawn),
    //   then on your immediately following turn your pawn may capture their pawn as if their pawn had only moved 1 square.
    //   This is as if your pawn is capturing their pawn mid motion, or In Passing.
}
