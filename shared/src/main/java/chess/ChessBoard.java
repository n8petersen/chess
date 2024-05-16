package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] pieces = new ChessPiece[8][8];

    public ChessBoard() {
    }

    /**
     * Constructor to make a copy of the board
     *
     * @param originalBoard    the board to copy
     */
    public ChessBoard(ChessBoard originalBoard) {
        for (int i = 0; i < originalBoard.pieces.length; i++) {
            this.pieces[i] = Arrays.copyOf(originalBoard.pieces[i], originalBoard.pieces.length);
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.pieces[position.getRow() - 1][position.getColumn() - 1] = piece;
    }
    /**
     * Removes a chess piece from the chessboard
     *
     * @param position where to add the piece to
     */
    public void removePiece(ChessPosition position) {
        this.pieces[position.getRow() - 1][position.getColumn() - 1] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.pieces[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // set all pieces to null
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                pieces[i][j] = null;
            }
        }

        ChessPiece.PieceType[] specials = {
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK
        };

        // add pieces
        for (int i = 0; i < 2; i ++) {
            for (int j = 0; j < 8; j++) {
                // add pawns
                pieces[1][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
                pieces[6][j] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);

                // add specials
                pieces[0][j] = new ChessPiece(ChessGame.TeamColor.WHITE, specials[j]);
                pieces[7][j] = new ChessPiece(ChessGame.TeamColor.BLACK, specials[j]);
            }
        }
    }

    /**
     * Finds all piece of color and type on the chessboard
     *
     * @param color     team color to look for
     * @param pieceType piece type to look for
     */
    public Collection<ChessPosition> findPieces(ChessGame.TeamColor color, ChessPiece.PieceType pieceType) {
        // iterate through pieces, and if piece is the same color and type, return it
        Collection<ChessPosition> foundPieces = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pieces[i][j] != null && pieces[i][j].getPieceType() == pieceType && pieces[i][j].getTeamColor() == color) {
                    foundPieces.add(new ChessPosition(i + 1, j + 1));
                }
            }
        }
        return foundPieces;
    }

    /**
     * Checks if a position is under attack by enemy color
     *
     * @param position position to check if under attack
     */
    public boolean isUnderAttack(ChessPosition position) {
        Collection<ChessPiece> attackingPieces = this.getAttackingPieces(position);
        return !attackingPieces.isEmpty();
    }

    /**
     * Gets possible attackers of a position.
     * Since this is only used for checking status, such as check or checkmate, we break out after finding first attacker.
     *
     * @param targetPosition position to check for attackers
     */
    private Collection<ChessPiece> getAttackingPieces(ChessPosition targetPosition) {
        ChessGame.TeamColor targetColor = this.getPiece(targetPosition).getTeamColor();
        Collection<ChessPiece> attackingPieces = new ArrayList<>();
        // iterate through all pieces in board
        // if piece is not null and is opposite color,
        // iterate through attackingPiece's moves,
        // if endPosition of move is same as target position
        // add to attackers

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pieces[i][j] != null && pieces[i][j].getTeamColor() != targetColor) {
                    for (ChessMove move : pieces[i][j].pieceMoves(this, new ChessPosition(i+1,j+1))) {
                        if (move.getEndPosition().equals(targetPosition)) {
                            attackingPieces.add(pieces[i][j]);
                            break;
                            // we can break here because this function is only used to check status, which only requires 1 attacker.
                        }
                    }
                }
            }
        }
        return attackingPieces;
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "pieces=" + Arrays.toString(pieces) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(pieces, that.pieces);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(pieces);
    }
}
