import java.awt.GridLayout;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JPanel {
    

    private final long serialVersionUID = -8715353373678321308L;
    boolean isGameOver = false;
    Server s;

	public Game() {
        
        s = new Server();
        
        int gameId = s.getGameId();
        int playerNumbers = s.getPlayerNumbers();
		
        JFrame f = new JFrame("Tetris - " + gameId);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(900, 900);
        
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
        
		new Thread() {
			@Override public void run() {
				while (!isGameOver) {
                    try {
                        LinkedList<GameData> gameLogic1 = s.receiveGameDataFromServer();
                        renderGameTick(enemies, gameLogic1, gameId);
						Thread.sleep(100);
					} catch ( Exception e ) {
						System.out.println(e.getMessage());
					}
				}
			}
		}.start();

        new Thread() {
			@Override public void run() {
				while (!isGameOver) {
					try {
                        control.sendNextCommand();
						Thread.sleep(100);
					} catch ( Exception e ) {
						System.out.println(e.getMessage());
					}
				}
			}
		}.start();
	}

    int winnerId = -1;

    void renderGameTick(LinkedList<Screen> enemies, LinkedList<GameData> gameLogic1, int gameId){
        boolean isGameOver = false;

        for(int i = 0; i < gameLogic1.size(); i++) {
            isGameOver = gameLogic1.get(i).gameOver;
            if(isGameOver){
                this.isGameOver = isGameOver;
                winnerId = i;
                System.out.println("Game over!");
                System.out.println("Winner: "+winnerId);
            }
        }

        if(this.isGameOver){
            enemies.get(0).paintGame(gameLogic1.get(gameId), gameId == winnerId);
        }
        else{
            enemies.get(0).paintGame(gameLogic1.get(gameId));
        }


        for(int i = 0; i < enemies.size(); i++) {
            int gameLogicIndex =  i;
            int screenIndex = i;
            

            if(i == 0) {
                screenIndex = gameId;
                gameLogicIndex = 0;
            }
            
            if(gameId == i){
                continue;
            }

            if(this.isGameOver){
                enemies.get(screenIndex).paintGame(gameLogic1.get(gameLogicIndex), i == winnerId);
            }
            else{
                enemies.get(screenIndex).paintGame(gameLogic1.get(gameLogicIndex));

            }
            
        }
    }

}
