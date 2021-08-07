import java.awt.Color;
import java.io.Serializable;

public class GameData implements Serializable {
    public static final long serialVersionUID = 1L;
	
    public Color[][] map;
    public int score = 0;
    public boolean gameOver = false;

    public GameData(){}

    public GameData(Color[][] map, int score, boolean gameOver){
        this.map = map;
        this.score = score;
        this.gameOver = gameOver;
    }
}
