import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {

    DataOutputStream os = null;
    Socket socket = null;
    ObjectInputStream is = null;

    Server() {
        try {
            socket = new Socket("127.0.0.1", 12345);
            os = new DataOutputStream(socket.getOutputStream());
            is = new ObjectInputStream(socket.getInputStream());
        } catch (UnknownHostException e) {
            System.out.println("Unknown host");
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }

    public GameData receiveGameDataFromServer() {
        GameData data = null;

        try {
            data = (GameData) is.readObject();

            System.out.println("received data!" + data.score);

        } catch (Exception e) {
          Mensagem.erroFatalExcecao("Jogo terminado pelo servidor.", e);
        }

        return data; 
    }


    public void sendControl(int control) {
        try {
            os.writeInt(control);
            os.flush();
        } catch (IOException e) {
            Mensagem.erroFatalExcecao("A imagem não pode ser enviada!", e);
        }
    }

    

}
