import java.awt.GridLayout;
import java.util.LinkedList;

import java.util.ResourceBundle.Control;

import javax.swing.JFrame;
import javax.swing.JPanel;




public class Tetris extends JPanel {

	private static final long serialVersionUID = -8715353373678321308L;

	public static void main(String[] args) {
		JFrame f = new JFrame("Tetris");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(900, 900);
    
        Server s = new Server();

        int gameId = s.getGameId();
        int playerNumbers = s.getPlayerNumbers();
        
		final Screen game = new Screen();
		
        

		GridLayout gridLayout = new GridLayout(0, 2);
        f.setLayout(gridLayout);
        gridLayout.layoutContainer(f);

		f.setVisible(true);
		
        Controls control = new Controls(s);
		f.addKeyListener(control);
		
        LinkedList<Screen> enemies = new LinkedList<Screen>();

        for(int i = 0; i < playerNumbers; i++) {
            final Screen enemy = new Screen();
            enemies.add(enemy);
            f.add(enemy);
        }

		// Make the falling piece drop every second
		new Thread() {
			@Override public void run() {
				while (true) {
					try {
                        LinkedList<GameData> gameLogic1 = s.receiveGameDataFromServer();

                        for(int i = 0; i < playerNumbers; i++) {

                            enemies.get(i).paintGame(gameLogic1.get(i));
                            
                        }

						Thread.sleep(100);
					} catch ( InterruptedException e ) {
						System.out.println(e.getMessage());
					}
				}
			}
		}.start();

        new Thread() {
			@Override public void run() {
				while (true) {
					try {
                        control.sendNextCommand();
						Thread.sleep(100);
					} catch ( InterruptedException e ) {
						System.out.println(e.getMessage());
					}
				}
			}
		}.start();
	}   
}