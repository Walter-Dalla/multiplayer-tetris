import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;


public class Screen extends JPanel {
	private static final long serialVersionUID = -8715353373678321308L;

	private int score = 100;
    private Color[][] gameMap;

    void setScore(int score){
        this.score = score;
    }

    void setGameMap(Color[][] gameMap){
        this.gameMap = gameMap;
    }
	
	@Override 
	public void paintComponent(Graphics g)
	{
		
        if(gameMap == null){
            return;
        }

		g.fillRect(0, 0, 26*12, 26*24);
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 24; j++) {
				g.setColor(gameMap[i][j]);
				g.fillRect(26*i, 26*j, 25, 25);
			}
		}
		
		// Display the score
		g.setColor(Color.WHITE);
		g.drawString("" + score, 19*12, 25);
		
	}

    public void paintGame(GameData gameData){

        setGameMap(gameData.map);
        setScore(gameData.score);
        
        repaint();
    }
}