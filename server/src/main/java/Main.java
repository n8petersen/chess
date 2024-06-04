import chess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {
        ChessPiece piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        int portNum = 8080;
        Server server = new Server();
        server.run(portNum);
        System.out.printf("Running server on port %d\n", portNum);
    }
}