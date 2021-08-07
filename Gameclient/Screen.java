import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;


public class Screen extends JPanel {
	private static final long serialVersionUID = -8715353373678321308L;

	private int score = 100;
    private Color[][] gameMap;

	public boolean endMessage = false;
	public boolean endgame = false;

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
		g.setFont(new Font("Roboto", 1, 15));
		g.drawString("" + score, 12*12, 10*65);

		if (endMessage){
			g.setColor(Color.GRAY);
			g.setFont(new Font("Roboto", 1, 30));
			g.drawString("FIM DE JOGO", 12*5, 10*30);
			
		}
		
	}

    public void paintGame(GameData gameData){

        setGameMap(gameData.map);
        setScore(gameData.score);
        
        repaint();
    }
}