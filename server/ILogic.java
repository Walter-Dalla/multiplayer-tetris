interface ILogic {
    public void newFallingShape();
    public void fallBlock();
    public void move(int command);
    public void rotate(Boolean isLeftRotation);
    public void beforePaintComponent();
    public GameData generaGameData();
    public boolean gameIsOver();
} 