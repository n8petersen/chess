package ui;

import chess.ChessBoard;
import chess.ChessPosition;
import model.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class Draw {

    private final PrintStream out;

    public Draw() {
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(SET_TEXT_BOLD);
    }

    public void drawBoard(GameData gameData, boolean whitePosition) {
       ChessBoard board = gameData.game().getBoard();

        var stringBuilder = new StringBuilder();
        try {
            boolean currentSquareWhite = true;
            int[] rowNums;
            int[] colNums;
            String colLetters;
            if (whitePosition) {
                rowNums = new int[] {7,6,5,4,3,2,1,0};
                colNums = new int[] {0,1,2,3,4,5,6,7};
                colLetters = "    a  b  c  d  e  f  g  h    ";
            } else {
                rowNums = new int[] {1,2,3,4,5,6,7};
                colNums = new int[] {7,6,5,4,3,2,1,0};
                colLetters = "    h  g  f  e  d  c  b  a    ";
            }
            stringBuilder.append(SET_BG_COLOR_DARK_GREY).append(colLetters).append(RESET_BG_COLOR).append('\n');
            for (var row : rowNums) {
                String printRow = " " + (row + 1) + " ";
                stringBuilder.append(SET_BG_COLOR_DARK_GREY).append(printRow).append(RESET_BG_COLOR);
                for (var col : colNums) {
                    var piece = board.getPiece(new ChessPosition(row+1, col+1));
                }
            }
        } catch (Exception ex) {
            out.print("Error drawing board");
        }
        out.print(stringBuilder);
    }

    /**
     * drawBoard() will just draw the starter board
     */
    public void drawBoard() {
        drawStarter();
    }

    private void drawMiddleRow(String[] items, String initialColor, boolean whiteOrientation) {
        String color = initialColor;
        if (!whiteOrientation) {
            for (int i = 0; i < 10; i++) {
                if (i == 0 || i == 9) {
                    out.print(SET_BG_COLOR_DARK_GREY);
                    out.print(items[i]);
                } else {
                    out.print(color);
                    out.print(items[i]);
                    color = (color.equals(SET_BG_COLOR_BLACK) ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK);
                }
            }
        } else {
            for (int i = 9; i >= 0; i--) {
                if (i == 0 || i == 9) {
                    out.print(SET_BG_COLOR_DARK_GREY);
                    out.print(items[i]);
                } else {
                    out.print(color);
                    out.print(items[i]);
                    color = (color.equals(SET_BG_COLOR_BLACK) ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK);
                }
            }
        }
        out.print(RESET_BG_COLOR + "\n");
    }

    private void drawEdgeRow(String[] items, boolean whiteOrientation) {
        out.print(SET_BG_COLOR_DARK_GREY);
        if (!whiteOrientation) {
            for (int i = 0; i < 10; i++) {
                out.print(items[i]);
            }
        } else {
            for (int i = 9; i >= 0; i--) {
                out.print(items[i]);
            }
        }
        out.print(RESET_BG_COLOR + "\n");
    }

    private void drawStarter() {
        String[] edgeRow = {EMPTY," h "," g ", " f ", " e ", " d ", " c "," b "," a ", EMPTY};
        String[] whiteRow1 = {" 1 ",WHITE_ROOK,WHITE_KNIGHT,WHITE_BISHOP,WHITE_KING,WHITE_QUEEN,WHITE_BISHOP,WHITE_KNIGHT,WHITE_ROOK," 1 "};
        String[] whiteRow2 = {" 2 ",WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN," 2 "};
        String[] rowMid1 = {" 3 ",EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY," 3 "};
        String[] rowMid2 = {" 4 ",EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY," 4 "};
        String[] rowMid3 = {" 5 ",EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY," 5 "};
        String[] rowMid4 = {" 6 ",EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY," 6 "};
        String[] blackRow2 = {" 7 ",BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN," 7 "};
        String[] blackRow1 = {" 8 ",BLACK_ROOK,BLACK_KNIGHT,BLACK_BISHOP,BLACK_KING,BLACK_QUEEN,BLACK_BISHOP,BLACK_KNIGHT,BLACK_ROOK," 8 "};

        // black down
        drawEdgeRow(edgeRow, false);
        drawMiddleRow(whiteRow1, SET_BG_COLOR_WHITE, false);
        drawMiddleRow(whiteRow2, SET_BG_COLOR_BLACK, false);
        drawMiddleRow(rowMid1, SET_BG_COLOR_WHITE, false);
        drawMiddleRow(rowMid2, SET_BG_COLOR_BLACK, false);
        drawMiddleRow(rowMid3, SET_BG_COLOR_WHITE, false);
        drawMiddleRow(rowMid4, SET_BG_COLOR_BLACK, false);
        drawMiddleRow(blackRow2, SET_BG_COLOR_WHITE, false);
        drawMiddleRow(blackRow1, SET_BG_COLOR_BLACK, false);
        drawEdgeRow(edgeRow, false);

        out.println("\n");

        // white down
        drawEdgeRow(edgeRow, true);
        drawMiddleRow(blackRow1, SET_BG_COLOR_WHITE, true);
        drawMiddleRow(blackRow2, SET_BG_COLOR_BLACK, true);
        drawMiddleRow(rowMid1, SET_BG_COLOR_WHITE, true);
        drawMiddleRow(rowMid2, SET_BG_COLOR_BLACK, true);
        drawMiddleRow(rowMid3, SET_BG_COLOR_WHITE, true);
        drawMiddleRow(rowMid4, SET_BG_COLOR_BLACK, true);
        drawMiddleRow(whiteRow2, SET_BG_COLOR_WHITE, true);
        drawMiddleRow(whiteRow1, SET_BG_COLOR_BLACK, true);
        drawEdgeRow(edgeRow, true);
    }
}
