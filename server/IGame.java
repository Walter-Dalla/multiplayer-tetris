import java.net.Socket;

interface IGame {
    
    int getMaxPlayers();
    void addNewPlayer(Socket clientSocket);
    void startGame();
    void startLogic();

    boolean isGameOver();
    void endGame();
    
}