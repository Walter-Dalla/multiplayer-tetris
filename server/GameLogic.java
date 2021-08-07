import java.awt.Color;
import java.awt.Point;
import java.util.Random;

public class GameLogic extends DefaultGameLogic {
	private Random rand = new Random();

	public boolean endGame = false;
	public boolean endMessage = false;

	private Shape fallingShape;
	private Point centerPoint = new Point(5, 2);

	GameLogic() {
		super();
		newFallingShape();
	}

	public void newFallingShape() {

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
		System.out.println("falling shape");
		if (willCollide(0, 1)) {
			System.out.printf("Finish him");
			endGame = false;
			endMessage = true;
		}
	}

	public void fallBlock() {

		int fallMovementValue = 1;

		if (willCollide(0, fallMovementValue)) {
			fixToMap();
		} else {
			centerPoint.y += fallMovementValue;
		}
	}

	public void move(int command) {

		if (!willCollide(command, 0)) {
			centerPoint.x += command;
		}
	}

	private Boolean willCollide(Point[] shape) {
		return willCollide(shape, 0, 0);
	}

	private Boolean willCollide(int xModifier, int yModifier) {
		return willCollide(fallingShape.getShape(), xModifier, yModifier);
	}

	private Boolean willCollide(Point[] shape, int xModifier, int yModifier) {
		clearLastShapeRender();

		for (Point point : shape) {
			int x = point.x + centerPoint.x + xModifier;
			int y = point.y + centerPoint.y + yModifier;

			Color gameColor = gameMap.getMapBlock(x, y);

			Boolean collide = gameColor != gameMap.getMapBackgroundColor();

			if (collide) {
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

	private void clearLastShapeRender() {
		for (Point point : fallingShape.getShape()) {
			int newX = point.x + centerPoint.x;
			int newY = point.y + centerPoint.y;

			gameMap.setMapBlock(newX, newY, gameMap.getMapBackgroundColor());
		}
	}

	public void renderShapes() {
		for (Point point : fallingShape.getShape()) {
			int newX = point.x + centerPoint.x;
			int newY = point.y + centerPoint.y;

			gameMap.setMapBlock(newX, newY, fallingShape.color);
		}
	}

	public void rotate(Boolean isLeftRotation) {
		clearLastShapeRender();

		Point[] newPosition = fallingShape.preRotate(fallingShape.getShape(), isLeftRotation);

		if (!willCollide(newPosition)) {
			fallingShape.rotate(isLeftRotation);
		}
	}

	public void isFull() {
		int rowStrick = 0;
		int mapOffset = 1;

		for (int rowIndex = mapOffset; rowIndex < gameMap.getHeight() - mapOffset; rowIndex++) {
			Boolean isRowFull = gameMap.checkIfRowIsFull(rowIndex);

			if (isRowFull) {
				rowStrick++;
				gameMap.clearRow(rowIndex);
				gameMap.applyGravity(rowIndex);
			}
		}

		calculatePoints(rowStrick);
	}

	public void calculatePoints(int rowStrick) {

		switch (rowStrick) {
			case 1:
				score += 100;
				break;
			case 2:
				score += 300;
				break;
			case 3:
				score += 500;
				break;
			case 4:
				score += 800;
				break;
		}
	}

	public void beforePaintComponent() {
		clearLastShapeRender();
		renderShapes();

	}

	public int getMaxPlayers() {
		return 2;
	}
}