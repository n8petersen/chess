package ui;

import chess.ChessBoard;
import model.GameData;

import static ui.EscapeSequences.*;

public class Draw {

    public Draw() {
    }

    public String drawBoard(GameData gameData) {

        // TODO: Draw chessboard.
        //  For Phase 5, it just has to draw the current board, but we cannot actually play the game yet
        //  https://github.com/softwareconstruction240/softwareconstruction/blob/main/instruction/console-ui/console-ui.md

        ChessBoard board = gameData.game().getBoard();


        drawStarter();
        return "";
    }

    private void drawMiddleRow(String[] items, String initialColor, boolean whiteOrientation) {
        String color = initialColor;
        if (!whiteOrientation) {
            for (int i = 0; i < 10; i++) {
                if (i == 0 || i == 9) {
                    color = SET_BG_COLOR_DARK_GREY;
                    System.out.print(color);
                    System.out.print(items[i]);
                    color = initialColor;
                } else {
                    System.out.print(color);
                    System.out.print(items[i]);
                    color = (color.equals(SET_BG_COLOR_BLACK) ? SET_BG_COLOR_LIGHT_GREY : SET_BG_COLOR_BLACK);
                }
            }
        } else {
            for (int i = 9; i >= 0; i--) {
                if (i == 0 || i == 9) {
                    color = SET_BG_COLOR_DARK_GREY;
                    System.out.print(color);
                    System.out.print(items[i]);
                    color = initialColor;
                } else {
                    System.out.print(color);
                    System.out.print(items[i]);
                    color = (color.equals(SET_BG_COLOR_BLACK) ? SET_BG_COLOR_LIGHT_GREY : SET_BG_COLOR_BLACK);
                }
            }
        }
        System.out.print(RESET_BG_COLOR + "\n");
    }

    private void drawEdgeRow(String[] items, boolean whiteOrientation) {
        if (!whiteOrientation) {
            for (int i = 0; i < 10; i++) {
                System.out.print(SET_BG_COLOR_DARK_GREY);
                System.out.print(items[i]);
            }
        } else {
            for (int i = 9; i >= 0; i--) {
                System.out.print(SET_BG_COLOR_DARK_GREY);
                System.out.print(items[i]);
            }
        }
        System.out.print(RESET_BG_COLOR + "\n");
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
        drawMiddleRow(whiteRow1, SET_BG_COLOR_LIGHT_GREY, false);
        drawMiddleRow(whiteRow2, SET_BG_COLOR_BLACK, false);
        drawMiddleRow(rowMid1, SET_BG_COLOR_LIGHT_GREY, false);
        drawMiddleRow(rowMid2, SET_BG_COLOR_BLACK, false);
        drawMiddleRow(rowMid3, SET_BG_COLOR_LIGHT_GREY, false);
        drawMiddleRow(rowMid4, SET_BG_COLOR_BLACK, false);
        drawMiddleRow(blackRow2, SET_BG_COLOR_LIGHT_GREY, false);
        drawMiddleRow(blackRow1, SET_BG_COLOR_BLACK, false);
        drawEdgeRow(edgeRow, false);

        System.out.println("\n");

        // white down
        drawEdgeRow(edgeRow, true);
        drawMiddleRow(blackRow1, SET_BG_COLOR_LIGHT_GREY, true);
        drawMiddleRow(blackRow2, SET_BG_COLOR_BLACK, true);
        drawMiddleRow(rowMid1, SET_BG_COLOR_LIGHT_GREY, true);
        drawMiddleRow(rowMid2, SET_BG_COLOR_BLACK, true);
        drawMiddleRow(rowMid3, SET_BG_COLOR_LIGHT_GREY, true);
        drawMiddleRow(rowMid4, SET_BG_COLOR_BLACK, true);
        drawMiddleRow(whiteRow2, SET_BG_COLOR_LIGHT_GREY, true);
        drawMiddleRow(whiteRow1, SET_BG_COLOR_BLACK, true);
        drawEdgeRow(edgeRow, true);
    }
}
