import java.io.Serializable;

public class DefaultGameLogic implements Serializable {
    private static final long serialVersionUID = -299482035708790407L;

    public GameMap gameMap = new GameMap(12, 24);
    
	public int score = 100;

    public void setScore(int score){
        this.score = score;
    }

    public int getScore(){
        return score;
    }

    public GameMap getGameMap(){
        return gameMap;
    }

    public void setGameMap(GameMap gameMap){
        this.gameMap = gameMap;
    }


    public int getMaxPlayers(){
        return 2;
    }
}