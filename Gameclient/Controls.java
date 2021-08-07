import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controls implements KeyListener{
    Server server;
    
    Controls(Server server){
        super();
        this.server = server;
    }


    public void keyTyped(KeyEvent e) {
    }
    
    public void keyPressed(KeyEvent e) {
        server.sendControl(e.getKeyCode());
        System.out.println("Send key pressed");
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}