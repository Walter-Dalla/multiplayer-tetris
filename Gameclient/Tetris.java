import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;




public class Tetris extends JPanel {

	private static final long serialVersionUID = -8715353373678321308L;

	public static void main(String[] args) {
		JFrame f = new JFrame("Tetris");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(900, 900);
    
        Server s = new Server();
        
		
		final Screen game = new Screen();
		final Screen game2 = new Screen();

		GridLayout gridLayout = new GridLayout(0, 2);
        f.setLayout(gridLayout);
        gridLayout.layoutContainer(f);

		f.add(game);
		f.add(game2);
		f.setVisible(true);
		
		f.addKeyListener(new Controls(s));
		
		// Make the falling piece drop every second
		new Thread() {
			@Override public void run() {
				do{
					while (true) {
						
						try {
							Thread.sleep(600);
							DefaultGameLogic gameLogic1 = s.receiveLogic();
							
							game.paintGame(gameLogic1);
						} catch ( InterruptedException e ) {
							System.out.println(e.getMessage());
						}
					}
				} while (game.endGame);
			}
		}.start();
	}   
}