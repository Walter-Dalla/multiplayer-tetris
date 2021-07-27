import java.net.Socket;

interface IJogo {
    
    int getMaxPlayers();
    void addNewPlayer(Socket clientSocket);
    void startGame();
    
}