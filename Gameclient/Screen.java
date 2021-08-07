import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;


public class Screen extends JPanel {
	private static final long serialVersionUID = -8715353373678321308L;

    private GameMap gameMap = new GameMap(12, 24);
	private int score = 100;

	public boolean endMessage = false;

    void setScore(int score){
        this.score = score;
    }

    void setGameMap(GameMap gameMap){
        this.gameMap = gameMap;
    }
	
	@Override 
	public void paintComponent(Graphics g)
	{
		
		g.fillRect(0, 0, 26*12, 26*24);
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 24; j++) {
				g.setColor(gameMap.getMap()[i][j]);
				g.fillRect(26*i, 26*j, 25, 25);
			}
		}
		
		// Display the score
		g.setColor(Color.WHITE);
		g.setFont(new Font("Roboto", 1, 15));
		g.drawString("" + score, 12*12, 10*65);

		if (endMessage){
			g.setColor(Color.GRAY);
			g.setFont(new Font("Roboto", 1, 30));
			g.drawString("FIM DE JOGO", 12*5, 10*30);
			
		}
		
	}

    public void paintGame(DefaultGameLogic logic){

        setGameMap(logic.getGameMap());
        setScore(logic.getScore());
        
        repaint();
    }
}