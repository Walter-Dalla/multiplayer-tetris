import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controls implements KeyListener{
    Server server;
    
    Controls(Server server){
        super();
    }


    public void keyTyped(KeyEvent e) {
    }
    
    public void keyPressed(KeyEvent e) {
        server.sendControl(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}