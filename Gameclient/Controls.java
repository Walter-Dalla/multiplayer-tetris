import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

public class Controls implements KeyListener {
    Server server;

    LinkedList<Integer> keysPressed = new LinkedList<Integer>();
    
    Controls(Server server){
        super();
        this.server = server;
    }


    public void keyTyped(KeyEvent e) {
    }
    
    public void keyPressed(KeyEvent e) {
        keysPressed.add(e.getKeyCode());
        System.out.println("Send key pressed");
    }

    void sendNextCommand(){
        if(keysPressed.size() == 0){
            return;
        }
        
        Integer command = keysPressed.getFirst();

        server.sendControl(command);
        keysPressed.clear();
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}