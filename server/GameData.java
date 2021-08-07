import java.awt.Color;
import java.io.Serializable;

public class GameData implements Serializable {
    public static final long serialVersionUID = 1L;
	public Color[][] map;
    public int score = 0;

    public GameData(){}

    public GameData(Color[][] map, int score){
        this.map = map;
        this.score = score;
    }
}
