import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameCluster implements IJogo{
    private static final int MAX_CLIENTS = 2;

    private List<Socket> clients = new ArrayList<Socket>(MAX_CLIENTS);
    private List<GameLogic> logics = new ArrayList<GameLogic>(MAX_CLIENTS);
    private List<ObjectOutputStream> outputList = new ArrayList<ObjectOutputStream>(MAX_CLIENTS);
    private List<DataInputStream> inputList = new ArrayList<DataInputStream>(MAX_CLIENTS);
    
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
        try{
            for(int i = 0; i < clients.size(); i++){
                Socket socket = clients.get(i);
                ObjectOutputStream dos = new ObjectOutputStream(socket.getOutputStream());
                outputList.add(dos);

                sendGameIdToClient(i);
                sendPlayerNumberToClient(clients.size(), i);

                DataInputStream is = new DataInputStream(socket.getInputStream());
                inputList.add(is);

                GameLogic logic = new GameLogic();
                logics.add(logic);
                System.out.println("Sending object - on start");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        iniciaThreadJogoEnvio();
    }

    public void iniciaThreadJogoEnvio() {
        new Thread(new Runnable() {
            public void run() {
                Boolean gameOver = false;
                while (!gameOver) {
                    try {
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
                    catch (Exception e) {
                        gameOver = true;
                        System.out.println("Error" + e.getMessage());
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        for(int i = 0; i < clients.size(); i++){
                            GameLogic logic = logics.get(i);
                            logic.fallBlock();
                            logic.beforePaintComponent();
                        }
                        Thread.sleep(1000);
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                       
                        sendGamesToClient();
                        Thread.sleep(1000);
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }).start();
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
    

}