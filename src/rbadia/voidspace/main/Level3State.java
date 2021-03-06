package rbadia.voidspace.main;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import rbadia.voidspace.graphics.NewGraphicsManager;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Boss;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.sounds.SoundManager;

/**
 * Level very similar to LevelState1.  
 * Platforms arranged in triangular form. 
 * Asteroids travel at 225 degree angle
 */
public class Level3State extends NewLevel2State {

	private static final long serialVersionUID = -2094575762243216079L;
		
	protected Boss boss2;
	protected List<BigBullet> boss2Bullets;
	private boolean isBoss2GoingDown = false;

	private boolean isBoss2Spawning = true;
	private int boss2Damage = 0;
	
	private long lastBoss2BulletTime;
	private long lastCollisionTime;
	
	
	
	
	// Constructors
	public Level3State(int level, MainFrame frame, GameStatus status, 
			NewLevelLogic gameLogic, InputHandler inputHandler,
			NewGraphicsManager graphicsMan, SoundManager soundMan) {
		super(level, frame, status, gameLogic, inputHandler, graphicsMan, soundMan);
	}
	
	//public Asteroid getAsteroid2() { return asteroid2; }

	@Override
	public void doStart() {	
		super.doStart();
		setStartState(GETTING_READY);
		setCurrentState(getStartState());
		
		boss2Bullets = new ArrayList<BigBullet>();
		
		newBoss2(this);
	}
	
	@Override
	public boolean isLevelWon() {
		if(this.getInputHandler().isNPressed()){
			return true;
		}
		return levelAsteroidsDestroyed >= 5 && boss2Damage >=5;
	}
	
	protected void drawBoss(){
		Graphics2D g2d = getGraphics2D();
		if(isBoss2Spawning){
			boss2.setLocation(0, this.getHeight() - boss2.getPixelsTall());
			getGraphicsManager().drawBoss(boss2, g2d, this);
			isBoss2Spawning = false;
		}
		
		if(this.getHeight() - boss2.getPixelsTall() > boss2.getY() && isBoss2GoingDown){
			boss2.translate(0, boss2.getDefaultSpeed());
			getGraphicsManager().drawBoss(boss2, g2d, this);
		}
		else{
			isBoss2GoingDown = false;
		}
		
		if(boss2.getY() > 0 && !isBoss2GoingDown){
			boss2.translate(0, -boss2.getDefaultSpeed());
			getGraphicsManager().drawBoss(boss2, g2d, this);
		}
		else if(!isBoss2GoingDown){
			isBoss2GoingDown = true;
			boss2.translate(0, boss2.getDefaultSpeed());
			getGraphicsManager().drawBoss(boss2, g2d, this);
		}
	}
	
	protected void checkBossBulletMegaManCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<boss2Bullets.size(); i++){
			BigBullet bigBullet = boss2Bullets.get(i);
			if(megaMan.intersects(bigBullet)){
				status.setLivesLeft(status.getLivesLeft() - 1);
				boss2Bullets.remove(i);
				break;
			}
		}
	}
	
	//if MegaMan touches the enemy he loses one life point
	protected void checkMegaManBossCollisions() {
		GameStatus status = getGameStatus();
		if(boss2.intersects(megaMan)){
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastCollisionTime) > 1000){
				lastCollisionTime = currentTime;
				status.setLivesLeft(status.getLivesLeft() - 1);
			}
		}
	}
	
	protected void checkBulletBossCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bulletsL.size(); i++){
			Bullet bullet = bulletsL.get(i);
			if(boss2.intersects(bullet)){
				boss2Damage++;
				if(boss2Damage >= 5){
					status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 500);
					removeBoss(boss2);
				}
				// remove bullet
				bulletsL.remove(i);
				break;
			}
		}
	}
	
	protected void checkBigBulletBossCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bigBulletsL.size(); i++){
			BigBullet bigBullet = bigBulletsL.get(i);
			if(boss2.intersects(bigBullet)){
				boss2Damage += 3;
				if(boss2Damage >= 5){
					status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 500);
					removeBoss(boss2);
				}
				// remove big bullet
				bigBulletsL.remove(i);
				break;
			}
		}
	}
	
	protected void drawBossBullets() {
		Graphics2D g2d = getGraphics2D();
		long currentTime = System.currentTimeMillis();		
		if((currentTime - lastBoss2BulletTime) > 1000){
			lastBoss2BulletTime = currentTime;
			fireBoss2Bullet(); 
		}
		for(int i=0; i<boss2Bullets.size(); i++){
			BigBullet bigBullet = boss2Bullets.get(i);
			getGraphicsManager().drawBigBullet(bigBullet, g2d, this);

			boolean remove = this.moveBoss2Bullet(bigBullet); 
			if(remove){
				boss2Bullets.remove(i);
				i--;
			}
		}
	}
	
	public void fireBoss2Bullet(){
		int xPos = boss2.x;
		int yPos = boss2.y + boss.width/2 - BigBullet.HEIGHT + 4;
		BigBullet  bossBullet = new BigBullet(xPos, yPos);
		boss2Bullets.add(bossBullet);
		this.getSoundManager().playBulletSound();
	}
	
	public boolean moveBoss2Bullet(BigBullet bossBullet){
		if(bossBullet.getY() - bossBullet.getSpeed() >= 0){
			bossBullet.translate(bossBullet.getSpeed(), 0);
			return false;
		}
		else{
			return true;
		}
	}


	@Override
	public Platform[] newPlatforms(int n){
		platforms = new Platform[n];
		for(int i=0; i<n; i++){
			this.platforms[i] = new Platform(0,0);
			if(i<4)	platforms[i].setLocation(200, getHeight()/2 + 140 - i*40);
			if(i==4) platforms[i].setLocation(50 +i*50, getHeight()/2 + 140 - 3*40);
			if(i>4){	
				int k=4;
				platforms[i].setLocation(250, getHeight()/2 + 20 + (i-k)*40 );
				k=k+2;
			}
		}
		return platforms;
	}
	
	public Boss newBoss2(Level3State screen){
		int xPos = 0;
		int yPos = (int) (screen.getWidth() - Boss.HEIGHT);
		boss2 = new Boss(xPos, yPos);
		return boss2;
	}
	
	@Override
	public void removeBoss(Boss boss){
		// "remove" boss
		bossExplosion = new Rectangle(
				boss.x,
				boss.y,
				boss.getPixelsWide(),
				boss.getPixelsTall());
		boss.setLocation(boss.getPixelsWide() + this.getWidth(), -boss.getPixelsTall());
		// play boss explosion sound
		this.getSoundManager().playAsteroidExplosionSound();
	}
}