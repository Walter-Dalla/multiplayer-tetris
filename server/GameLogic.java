import java.awt.Color;
import java.awt.Point;
import java.util.Random;


public class GameLogic extends DefaultGameLogic implements ILogic {
   private Random rand = new Random();
    
	private Shape fallingShape;
	private Point centerPoint = new Point(5, 2);
    private boolean gameOver = false;
    
    int ROW_DEFAULT_VALUE = 100;

    GameLogic(){
        super();
        newFallingShape();
    }


	public void newFallingShape(){
		
		int randomNumber = rand.nextInt(6);
		Shape newShape;
		centerPoint = new Point(5, 2);
		
		switch (randomNumber) {
			case 0:
				newShape = new StripShape();
				break;

			case 1:
				newShape = new JShape();
				break;

			case 2:
				newShape = new LShape();
				break;

			case 3:
				newShape = new OShape();
				break;
				
			case 4:
				newShape = new SShape();
				break;

			case 5:
				newShape = new TShape();
				break;

			case 6:
				newShape = new ZShape();
				break;
			default:
				newShape = new LShape();
		}

		
		fallingShape = newShape;
        System.out.println("new falling shape");
		if(willCollide(0, 1)) {
			gameOver = true;
		}
	}

	public void fallBlock(){

		int fallMovementValue = 1;

		if(willCollide(0, fallMovementValue)) {
			fixToMap();
		}
		else {
			centerPoint.y += fallMovementValue;
		}
	}

	
	public void move(int command){
		
		if(!willCollide(command, 0)) {
			centerPoint.x += command;
		}
	}
	
	private Boolean willCollide(Point[] shape) {
		return willCollide(shape, 0, 0);
	}

	private Boolean willCollide(int xModifier, int yModifier) {
		return willCollide(fallingShape.getShape(), xModifier, yModifier);
	}

	private Boolean willCollide(Point[] shape, int xModifier, int yModifier){
		clearLastShapeRender();

		for (Point point : shape) {
			int x = point.x + centerPoint.x + xModifier;
			int y = point.y + centerPoint.y + yModifier;

			Color gameColor = gameMap.getMapBlock(x, y);

			Boolean collide = gameColor != gameMap.getMapBackgroundColor();

			if(collide) {
				return true;
			}
		}
		
		return false;
	}

	private void fixToMap() {
		
		for (Point point : fallingShape.getShape()) {
			gameMap.setMapBlock(point.x + centerPoint.x, point.y + centerPoint.y, fallingShape.color);
		}

		isFull();
		newFallingShape();
	}
	
	private void clearLastShapeRender(){
		for (Point point : fallingShape.getShape()) {
			int newX = point.x + centerPoint.x;
			int newY = point.y + centerPoint.y;
	
			gameMap.setMapBlock(newX, newY, gameMap.getMapBackgroundColor());
		}
	}

	private void renderShapes(){
		for (Point point : fallingShape.getShape()) {
			int newX = point.x + centerPoint.x;
			int newY = point.y + centerPoint.y;
            
            gameMap.setMapBlock(newX, newY, fallingShape.color);
		}
	}

	public void rotate(Boolean isLeftRotation) {
		clearLastShapeRender();
		
		Point[] newPosition = fallingShape.preRotate(fallingShape.getShape(), isLeftRotation);
		
		if(!willCollide(newPosition)) {
			fallingShape.rotate(isLeftRotation);
		}
	}

	private void isFull(){
		int rowStrick = 0;
		int mapOffset = 1;

		for(int rowIndex = mapOffset; rowIndex < gameMap.getHeight() -mapOffset; rowIndex++) {
			Boolean isRowFull = gameMap.checkIfRowIsFull(rowIndex);
			
			
			if(isRowFull){
				rowStrick++;
				gameMap.clearRow(rowIndex);
				gameMap.applyGravity(rowIndex);
			}
		}

		calculatePoints(rowStrick);
	}

	private void calculatePoints(int rowStrick){
        addScore(ROW_DEFAULT_VALUE * rowStrick);
	}
	
	public void beforePaintComponent()
	{
        if(gameOver) {
            return;
        }
		clearLastShapeRender();
		renderShapes();
		
	}

    public GameData generaGameData(){
        System.out.println(gameOver);
        return new GameData(gameMap.getMap(), score++, gameOver);
    }

    public boolean gameIsOver() {
        return gameOver;
    }

    public void setGameOver() {
        gameOver = true;
    }
}