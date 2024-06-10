import chess.*;
import ui.ChessClient;
import ui.EscapeSequences;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Main {
    public static void main(String[] args) {

        var server = "localhost";
        int port = 8080;

        ChessClient chessClient = new ChessClient(server, port);
        System.out.println("♕ CS240 Chess Client");
        System.out.println("Type 'Help' to get started");
        Scanner scanner = new Scanner(System.in);

        var result = "";
        while (!result.equals("quit")) {
            String input = scanner.nextLine();
            try {
                result = chessClient.readInput(input);
                System.out.println(RESET_TEXT_COLOR + result);
            } catch (Throwable e) {
                System.err.println(e.getMessage());
            }
        }
    }
}