import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameCluster implements IJogo{
    private static final int MAX_CLIENTS = 1;

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

                DataInputStream is = new DataInputStream(socket.getInputStream());
                inputList.add(is);
            }
            
            for(int i = 0; i < clients.size(); i++){
                
                ObjectOutputStream dos = outputList.get(i);
                GameLogic logic = new GameLogic();
                logics.add(logic);
                
                DefaultGameLogic dgl = new DefaultGameLogic();
                dgl.setScore(logic.score);
                dgl.setGameMap(logic.gameMap);
                
                dos.writeObject(dgl);
                System.out.println("Sending object");
                dos.flush();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        iniciaThreadJogoEnvio();
    }
    public void iniciaThreadJogoEnvio() {
        new Thread(new Runnable() {
          public void run() {
            while (true) {
                try {
                    for(int i = 0; i < clients.size(); i++){
                        ObjectOutputStream dos = outputList.get(i);
                        GameLogic logic = logics.get(i);
                        logic.fallBlock();
                        logic.beforePaintComponent();
                        logic.setScore(33);
                        
                        DefaultGameLogic dgl = new DefaultGameLogic();
                        dgl.setScore(logic.score);
                        dgl.setGameMap(logic.gameMap);
                        
                        dos.writeObject(dgl);
                        System.out.println("Sending object");
                        dos.flush();
                        
                    }
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
              try {
                Thread.sleep(1);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          }
        }).start();
      }


    public void getCommand(int playerNumber) {
        try {
            inputList.get(playerNumber).readInt();
            System.out.println(playerNumber);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}