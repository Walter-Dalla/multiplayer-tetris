import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameCluster implements IGame{
    private static final int MAX_CLIENTS = 1;

    private List<Socket> clients = new ArrayList<Socket>(MAX_CLIENTS);
    private List<GameLogic> logics = new ArrayList<GameLogic>(MAX_CLIENTS);
    private List<ObjectOutputStream> outputList = new ArrayList<ObjectOutputStream>(MAX_CLIENTS);
    private List<DataInputStream> inputList = new ArrayList<DataInputStream>(MAX_CLIENTS);
    private ServerSocket serverSocket;

    private boolean isGameOver = false;

    public GameCluster(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public int getMaxPlayers(){
        return MAX_CLIENTS;
    }

    public void addNewPlayer(Socket clientSocket){
        if(clients.size() < MAX_CLIENTS){
            clients.add(clientSocket);
        }
    }

    public void startGame() {
        System.out.println("Starting game...");
        
        setUpCluster();
        startLogic();

    }

    private void setUpCluster(){
        try{
            for(int i = 0; i < clients.size(); i++){
                Socket socket = clients.get(i);
                ObjectOutputStream dos = new ObjectOutputStream(socket.getOutputStream());
                outputList.add(dos);

                DataInputStream is = new DataInputStream(socket.getInputStream());
                inputList.add(is);
                
                GameLogic logic = new GameLogic();
                logics.add(logic);

                sendGameIdToClient(i);
                sendPlayerNumberToClient(clients.size(), i);

                System.out.println("Sending setup to client - on start");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void startLogic() {
        startServerThreads();
    }


    public void startServerThreads() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    while (!isGameOver) {
                        handleClientCommands();
                    }
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {  
                    while (!isGameOver) {
                        handleGameTick();
                        Thread.sleep(1000);
                    }
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {   
                    while (!isGameOver) {
                        sendGamesToClient();
                        Thread.sleep(1000);
                    }
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }).start();
    }
    int cont = 0;
    private void handleGameTick() {
        cont ++;
        System.out.println(cont);
        if(cont == 3){
            logics.get(0).setGameOver();

        }
    
        for(int i = 0; i < clients.size(); i++){
            GameLogic logic = logics.get(i);
            logic.fallBlock();
            logic.beforePaintComponent();
        }
        
    }

    private void handleClientCommands() throws IOException{
        for(int i = 0; i < clients.size(); i++){
            
            DataInputStream input = inputList.get(i);
            
            if(input.available() > 0){
                GameLogic logic = logics.get(i);
                
                int command = input.readInt();
                logic = ControlHandler.handleCommand(logic, command);
                logic.beforePaintComponent();
                
                System.out.println("command " + command + " --->" + logic.score);
                
                sendGamesToClient();
            }
        }
    }
    

    private void sendGamesToClient() throws IOException {
        LinkedList<GameData> games = new LinkedList<GameData>();

        for (int i = 0; i < clients.size(); i++) {
            GameLogic logic = logics.get(i);
            GameData gameData = logic.generaGameData();
            games.add(gameData);
        }

        for (int i = 0; i < clients.size(); i++) {
            sendDataToClient(games, i);
            
        }

        for (int i = 0; i < logics.size(); i++) {
            
            if(logics.get(i).gameIsOver()){
                isGameOver = true;
            }
        }

    }

    private synchronized void sendDataToClient(LinkedList<GameData> gameDatas, Integer playerIndex) throws IOException {

        ObjectOutputStream dos = outputList.get(playerIndex);
        dos.reset();
        dos.writeObject(gameDatas);
        dos.flush();

        System.out.println("Sending object - game");
    }

    private synchronized void sendGameIdToClient(Integer playerIndex) throws IOException {

        ObjectOutputStream dos = outputList.get(playerIndex);
        dos.reset();
        dos.writeInt(playerIndex);
        dos.flush();

        System.out.println("Sending game id - start");
    }

    private synchronized void sendPlayerNumberToClient(Integer playerNumber, Integer playerIndex) throws IOException {

        ObjectOutputStream dos = outputList.get(playerIndex);
        dos.reset();
        dos.writeInt(playerNumber);
        dos.flush();

        System.out.println("Sending game id - start");
    }



    public void getCommand(int playerNumber) {
        try {
            inputList.get(playerNumber).readInt();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public synchronized void endGame() {
        System.out.println("Ending game...");
        int winnerId = -1;

        try {
            serverSocket.close();
            
        } catch (Exception e) {
            
        }
    }
    
    public synchronized boolean isGameOver(){
        return isGameOver;
    }

}