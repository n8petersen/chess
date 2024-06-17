package ui;

import chess.ChessBoard;
import chess.ChessPosition;
import model.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

import static ui.EscapeSequences.*;

public class Draw {

    private final PrintStream out;
    private static final Map<String, String> PIECE_MAP = Map.ofEntries(
            Map.entry("WHITE_KING", WHITE_KING),
            Map.entry("WHITE_QUEEN", WHITE_QUEEN),
            Map.entry("WHITE_BISHOP", WHITE_BISHOP),
            Map.entry("WHITE_KNIGHT", WHITE_KNIGHT),
            Map.entry("WHITE_ROOK", WHITE_ROOK),
            Map.entry("WHITE_PAWN", WHITE_PAWN),
            Map.entry("BLACK_KING", BLACK_KING),
            Map.entry("BLACK_QUEEN", BLACK_QUEEN),
            Map.entry("BLACK_BISHOP", BLACK_BISHOP),
            Map.entry("BLACK_KNIGHT", BLACK_KNIGHT),
            Map.entry("BLACK_ROOK", BLACK_ROOK),
            Map.entry("BLACK_PAWN", BLACK_PAWN)
    );

    public Draw() {
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(SET_TEXT_BOLD);
    }

    public void drawBoard(GameData gameData, boolean whiteOrientation, ArrayList<ChessPosition> highlights, ChessPosition currPos) {
       ChessBoard board = gameData.game().getBoard();

        var stringBuilder = new StringBuilder();
        try {
            boolean currentSquareWhite = true;
            int[] rowNums;
            int[] colNums;
            String colLetters;
            if (whiteOrientation) {
                rowNums = new int[] {7,6,5,4,3,2,1,0};
                colNums = new int[] {0,1,2,3,4,5,6,7};
                colLetters = "    a  b  c  d  e  f  g  h    ";
            } else {
                rowNums = new int[] {0,1,2,3,4,5,6,7};
                colNums = new int[] {7,6,5,4,3,2,1,0};
                colLetters = "    h  g  f  e  d  c  b  a    ";
            }
            stringBuilder.append(SET_BG_COLOR_DARK_GREY).append(colLetters).append(RESET_BG_COLOR).append('\n');
            for (var row : rowNums) {
                String printRow = " " + (row + 1) + " ";
                stringBuilder.append(SET_BG_COLOR_DARK_GREY).append(printRow).append(RESET_BG_COLOR);
                for (var col : colNums) {
                    var piece = board.getPiece(new ChessPosition(row + 1, col + 1));
                    var squareColor = (currentSquareWhite) ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK;
                    if (highlights != null && highlights.contains(new ChessPosition(row + 1, col + 1))) {
                        squareColor = (squareColor.equals(SET_BG_COLOR_WHITE)) ? SET_BG_COLOR_GREEN : SET_BG_COLOR_DARK_GREEN;
                        if (currPos != null && currPos.equals(new ChessPosition(row + 1, col + 1))) {
                            squareColor = (squareColor.equals(SET_BG_COLOR_GREEN)) ? SET_BG_COLOR_YELLOW : SET_BG_COLOR_DARK_YELLOW;
                        }
                    }
                    if (piece != null) {
                        var pieceColor = piece.getTeamColor().toString().toUpperCase();
                        var pieceType = piece.getPieceType().toString().toUpperCase();
                        var pieceText = PIECE_MAP.get(pieceColor + "_" + pieceType);
                        stringBuilder.append(squareColor).append(pieceText).append(RESET_BG_COLOR).append(RESET_TEXT_COLOR);
                    } else {
                        stringBuilder.append(squareColor).append(EMPTY).append(RESET_BG_COLOR);
                    }
                    currentSquareWhite = !currentSquareWhite;
                }
                stringBuilder.append(SET_BG_COLOR_DARK_GREY).append(printRow).append(RESET_BG_COLOR).append('\n');
                currentSquareWhite = !currentSquareWhite;
            }
            stringBuilder.append(SET_BG_COLOR_DARK_GREY).append(colLetters).append(RESET_BG_COLOR).append('\n');
        } catch (Exception ex) {
            out.print("Error drawing board");
        }
        out.print(stringBuilder);
    }

    public void drawBoard(GameData gameData, boolean whiteOrientation) {
        drawBoard(gameData, whiteOrientation, null, null);
    }
}
