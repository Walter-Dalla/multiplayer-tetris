import java.awt.event.KeyEvent;

public class ControlHandler {
    
    public static GameLogic handleCommand(GameLogic gameLogic, int command) {
        
        switch (command) {
            case KeyEvent.VK_LEFT:
                gameLogic.move(-1);
                break;
            case KeyEvent.VK_UP:
                gameLogic.rotate(true);
                break;
            case KeyEvent.VK_RIGHT:
                gameLogic.move(+1);
                break;
            case KeyEvent.VK_DOWN:
                gameLogic.fallBlock();
                gameLogic.addScore(10);
                break;
        } 

        return gameLogic;
    }
}