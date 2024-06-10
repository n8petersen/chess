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

    private void drawRow(String[] items, String color, boolean whiteOrientation) {
        if (!whiteOrientation) {
            for (int i = 0; i < 8; i++) {
                System.out.print(color);
                System.out.print(items[i]);
                color = (color.equals(SET_BG_COLOR_DARK_GREY) ? SET_BG_COLOR_LIGHT_GREY : SET_BG_COLOR_DARK_GREY);
            }
        } else {
            for (int i = 7; i >= 0; i--) {
                System.out.print(color);
                System.out.print(items[i]);
                color = (color.equals(SET_BG_COLOR_DARK_GREY) ? SET_BG_COLOR_LIGHT_GREY : SET_BG_COLOR_DARK_GREY);
            }
        }
        System.out.print(RESET_BG_COLOR + "\n");
    }

    private void drawStarter() {
        String[] whiteRow1 = {WHITE_ROOK,WHITE_KNIGHT,WHITE_BISHOP,WHITE_KING,WHITE_QUEEN,WHITE_BISHOP,WHITE_KNIGHT,WHITE_ROOK};
        String[] whiteRow2 = {WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN};
        String[] rowMid = {EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY};
        String[] blackRow2 = {BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN};
        String[] blackRow1 = {BLACK_ROOK,BLACK_KNIGHT,BLACK_BISHOP,BLACK_KING,BLACK_QUEEN,BLACK_BISHOP,BLACK_KNIGHT,BLACK_ROOK};

        // black down
        drawRow(whiteRow1, SET_BG_COLOR_LIGHT_GREY, false);
        drawRow(whiteRow2, SET_BG_COLOR_DARK_GREY, false);
        drawRow(rowMid, SET_BG_COLOR_LIGHT_GREY, false);
        drawRow(rowMid, SET_BG_COLOR_DARK_GREY, false);
        drawRow(rowMid, SET_BG_COLOR_LIGHT_GREY, false);
        drawRow(rowMid, SET_BG_COLOR_DARK_GREY, false);
        drawRow(blackRow2, SET_BG_COLOR_LIGHT_GREY, false);
        drawRow(blackRow1, SET_BG_COLOR_DARK_GREY, false);

        System.out.println("\n");

        // white down
        drawRow(blackRow1, SET_BG_COLOR_LIGHT_GREY, true);
        drawRow(blackRow2, SET_BG_COLOR_DARK_GREY, true);
        drawRow(rowMid, SET_BG_COLOR_LIGHT_GREY, true);
        drawRow(rowMid, SET_BG_COLOR_DARK_GREY, true);
        drawRow(rowMid, SET_BG_COLOR_LIGHT_GREY, true);
        drawRow(rowMid, SET_BG_COLOR_DARK_GREY, true);
        drawRow(whiteRow2, SET_BG_COLOR_LIGHT_GREY, true);
        drawRow(whiteRow1, SET_BG_COLOR_DARK_GREY, true);
    }
}
