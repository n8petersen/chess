package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

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
        this.board.resetBoard();
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

        // valid moves:
        //  1. given piece in move is not null
        //  2. given piece is the current team's color
        //  3. the provided rule is in the returned list from valid rules
        //  4. the move doesn't put team's king in check
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece piece = board.getPiece(startPosition);

        if (piece != null && piece.getTeamColor() == turnColor) {
            Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
            if (possibleMoves.contains(move) && checkMove(move)) {
                // actually move the piece on the board
                movePiece(board, move);

                // flip the turn color - if white, set to black, else set to white.
                turnColor = (turnColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);

                //return so we don't throw InvalidMoveException
                return;
            }
        }

        // after we make the move, swap the team color. But only if the move was valid and actually made.
        throw new InvalidMoveException("Invalid move: " + move);
    }

    /**
     * Actually moves the piece on a board
     *
     * @param board the board on which to move the piece
     * @param move the move to make
     */
    private void movePiece(ChessBoard board, ChessMove move) {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece piece = board.getPiece(startPosition);

        // check for promotion, and change piece if promoting
        if (move.getPromotionPiece() != null) {
            piece.setPieceType(move.getPromotionPiece());
        }
        board.removePiece(startPosition);
        board.addPiece(endPosition, piece);
    }

    /**
     * Determines if the given move is valid.
     * Move is valid if after the move, the king is not in check.
     *
     * @param move the move to test
     * @return True if the team's king is NOT in check after the move
     */
    private boolean checkMove(ChessMove move) {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece movePiece = board.getPiece(startPosition);
        TeamColor teamColor = movePiece.getTeamColor();

        // copy the board
        ChessBoard boardCopy = new ChessBoard(board);

        // make the move on the copied board
        movePiece(boardCopy, move);

        // check if king is in check
        boolean inCheck = isInCheck(teamColor);

        return !inCheck;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // Returns true if the specified team’s King could be captured by an opposing piece.
        // given color, we need to find that player's king
        // once we find the king, we need to find if that piece is being attacked

        // only return the first king found for team
        ChessPosition kingPiece = board.findPieces(teamColor, ChessPiece.PieceType.KING).iterator().next();

        return board.isUnderAttack(kingPiece);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // Returns true if the given team has no way to protect their king from being captured.
        // same as check, except there are no valid moves
        //   (move is valid when the move would not place the king on a square that is being attacked)

        //  to check this, we need to make a copy of the board
        //  on the copy of the board make the move and see check if king is attacked
        //  if we find any move that is valid, we can return false.
        //  else, after checking all possible king moves and none return false, then we return true.

        // TODO: Prereqs:
        //  Check if attacked
        //  Copy board
        //  Make moves

        if (!isInCheck(teamColor)) {
            return false;
        }



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

        // Only stalemate if king is not in check, so first check that and return false if in check
        // then, check if there are any valid moves.
        // if any moves are valid, we can return false
        // else, after checking all possible moves and none return false, then we return true.

        if (isInCheck(teamColor)) {
            return false;
        }

        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "turnColor=" + turnColor +
                ", board=" + board +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return turnColor == chessGame.turnColor && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turnColor, board);
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

