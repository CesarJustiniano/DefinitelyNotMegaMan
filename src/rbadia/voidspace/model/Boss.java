package rbadia.voidspace.model;

public class Boss extends GameObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2942644610003124564L;
	
	public static final int DEFAULT_SPEED = 5;
	
	public static final int WIDTH = 42;
	public static final int HEIGHT = 41;
	
	public Boss(int xPos, int yPos){
		super(xPos, yPos, WIDTH, HEIGHT);
		this.setSpeed(DEFAULT_SPEED);
	}
	
	
	public int getDefaultSpeed(){
		return DEFAULT_SPEED;
	}
}
