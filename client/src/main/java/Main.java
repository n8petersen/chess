import ui.ChessClient;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Main {
    public static void main(String[] args) throws Exception {

        var server = "localhost";
        int port = 8080;

        ChessClient chessClient = new ChessClient(server, port);
        System.out.println("♕ CS240 Chess Client");
        System.out.println("Type 'Help' to get started");
        Scanner scanner = new Scanner(System.in);

        var result = "";
        while (!result.equals("quit")) {
            chessClient.writePrompt();
            String input = scanner.nextLine();
            result = chessClient.readInput(input);
            System.out.println(RESET_TEXT_COLOR + result);
        }
    }
}