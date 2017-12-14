package rbadia.voidspace.main;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Asteroid;
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
public class Level3State extends Level2State {

	private static final long serialVersionUID = -2094575762243216079L;
	
	protected Asteroid asteroid2;
	
	protected Boss boss;
	protected Boss boss2;
	protected List<BigBullet> bossBullets;
	protected List<BigBullet> boss2Bullets;
	protected Rectangle bossExplosion;
	private boolean isBossGoingDown = true;
	private boolean isBoss2GoingDown = false;
	private boolean isBossSpawning = true;
	private boolean isBoss2Spawning = true;
	private int bossDamage = 0;
	private int boss2Damage = 0;
	
	private long lastBossBulletTime;
	private long lastBoss2BulletTime;
	private long lastCollisionTime;
	protected long lastAsteroid2Time;
	
	
	
	
	// Constructors
	public Level3State(int level, MainFrame frame, GameStatus status, 
			LevelLogic gameLogic, InputHandler inputHandler,
			GraphicsManager graphicsMan, SoundManager soundMan) {
		super(level, frame, status, gameLogic, inputHandler, graphicsMan, soundMan);
	}
	
	public Asteroid getAsteroid2() 				{ return asteroid2; 	}

	@Override
	public void doStart() {	
		super.doStart();
		setStartState(GETTING_READY);
		setCurrentState(getStartState());
		
		bossBullets = new ArrayList<BigBullet>();
		boss2Bullets = new ArrayList<BigBullet>();
		
		newAsteroid(this);
		newAsteroid2(this);
		newBoss(this);
		newBoss2(this);
		
		lastAsteroid2Time = -NEW_ASTEROID_DELAY;
	}
	
	@Override
	public boolean isLevelWon() {
		if(this.getInputHandler().isNPressed()){
			return true;
		}
		return levelAsteroidsDestroyed >= 5 && bossDamage >= 5;
	}
	
	@Override
	public void updateScreen(){
		Graphics2D g2d = getGraphics2D();
		GameStatus status = this.getGameStatus();

		// save original font - for later use
		if(this.originalFont == null){
			this.originalFont = g2d.getFont();
			this.bigFont = originalFont;
		}

		clearScreen();
		drawStars(50);
		drawFloor();
		drawPlatforms();
		drawMegaMan();
		drawAsteroid();
		drawBullets();
		drawBigBullets();
		drawBosses();
		drawBossBullets();
		checkBullletAsteroidCollisions();
		checkBigBulletAsteroidCollisions();
		checkMegaManAsteroidCollisions();
		checkAsteroidFloorCollisions();
		checkBossBulletMegaManCollisions();
		checkMegaManBossCollisions();
		checkBulletBossCollisions();
		checkBigBulletBossCollisions();

		// update asteroids destroyed (score) label  
		getMainFrame().getDestroyedValueLabel().setText(Long.toString(status.getAsteroidsDestroyed()));
		// update lives left label
		getMainFrame().getLivesValueLabel().setText(Integer.toString(status.getLivesLeft()));
		//update level label
		getMainFrame().getLevelValueLabel().setText(Long.toString(status.getLevel()));
	}

	@Override
	protected void drawAsteroid() {
		Graphics2D g2d = getGraphics2D();
		if((asteroid.getX() + asteroid.getPixelsWide() >  0)) {
			asteroid.translate(-asteroid.getSpeed() + rand.nextInt(asteroid.getSpeed()) - rand.nextInt(asteroid.getSpeed()), 
					asteroid.getSpeed()/2 + rand.nextInt(asteroid.getSpeed()) - rand.nextInt(asteroid.getSpeed()));
			getGraphicsManager().drawAsteroid(asteroid, g2d, this);	
		}
		else {
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){

				asteroid.setLocation(this.getWidth() - asteroid.getPixelsWide(),
						rand.nextInt(this.getHeight() - asteroid.getPixelsTall() - 32));
			}
			else {
				// draw explosion
				getGraphicsManager().drawAsteroidExplosion(asteroidExplosion, g2d, this);
			}
		}
		
		if((asteroid2.getX() + asteroid2.getPixelsWide() >  0)) {
			asteroid2.translate(-asteroid2.getSpeed() + rand.nextInt(asteroid2.getSpeed()) - rand.nextInt(asteroid2.getSpeed()), 
					asteroid2.getSpeed()/2 + rand.nextInt(asteroid2.getSpeed()) - rand.nextInt(asteroid2.getSpeed()));
			getGraphicsManager().drawAsteroid(asteroid2, g2d, this);	
		}
		else {
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastAsteroid2Time) > NEW_ASTEROID_DELAY){

				asteroid2.setLocation(this.getWidth() - asteroid2.getPixelsWide(),
						rand.nextInt(this.getHeight() - asteroid2.getPixelsTall() - 32));
			}
			else {
				// draw explosion
				getGraphicsManager().drawAsteroidExplosion(asteroidExplosion, g2d, this);
			}
		}	
	}
	
	protected void drawBosses(){
		Graphics2D g2d = getGraphics2D();
		if(isBossSpawning){
			boss.setLocation(this.getWidth() - boss.getPixelsWide(), 0);
			getGraphicsManager().drawBoss(boss, g2d, this);
			isBossSpawning = false;
		}
		
		if(this.getHeight() - boss.getPixelsTall() > boss.getY() && isBossGoingDown){
			boss.translate(0, boss.getDefaultSpeed());
			getGraphicsManager().drawBoss(boss, g2d, this);
		}
		else{
			isBossGoingDown = false;
		}
		
		if(boss.getY() > 0 && !isBossGoingDown){
			boss.translate(0, -boss.getDefaultSpeed());
			getGraphicsManager().drawBoss(boss, g2d, this);
		}
		else if(!isBossGoingDown){
			isBossGoingDown = true;
			boss.translate(0, boss.getDefaultSpeed());
			getGraphicsManager().drawBoss(boss, g2d, this);
		}
		
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
		for(int i=0; i<bossBullets.size(); i++){
			BigBullet bigBullet = bossBullets.get(i);
			if(megaMan.intersects(bigBullet)){
				// increase asteroids destroyed count
				status.setLivesLeft(status.getLivesLeft() - 1);
				bossBullets.remove(i);
				break;
			}
		}
	}
	
	//if MegaMan touches the enemy he loses one life point
	protected void checkMegaManBossCollisions() {
		GameStatus status = getGameStatus();
		if(boss.intersects(megaMan)){
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastCollisionTime) > 1000){
				lastCollisionTime = currentTime;
				status.setLivesLeft(status.getLivesLeft() - 1);
			}
		}
		
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
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if(boss.intersects(bullet)){
				bossDamage++;
				if(bossDamage >= 5){
					status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 500);
					removeBoss(boss);
				}
				// remove bullet
				bullets.remove(i);
				break;
			}
			
			if(boss2.intersects(bullet)){
				boss2Damage++;
				if(boss2Damage >= 5){
					status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 500);
					removeBoss(boss2);
				}
				// remove bullet
				bullets.remove(i);
				break;
			}
		}
	}
	
	protected void checkBigBulletBossCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			if(boss.intersects(bigBullet)){
				bossDamage += 3;
				if(bossDamage >= 5){
					status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 500);
					removeBoss(boss);
				}
				// remove big bullet
				bigBullets.remove(i);
				break;
			}
			
			if(boss2.intersects(bigBullet)){
				boss2Damage += 3;
				if(boss2Damage >= 5){
					status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 500);
					removeBoss(boss2);
				}
				// remove big bullet
				bigBullets.remove(i);
				break;
			}
		}
	}
	
	protected void drawBossBullets() {
		Graphics2D g2d = getGraphics2D();
		
		long currentTime = System.currentTimeMillis();
		if((currentTime - lastBossBulletTime) > 1000){
			lastBossBulletTime = currentTime;
			fireBossBullet();
		}
		for(int i=0; i<bossBullets.size(); i++){
			BigBullet bigBullet = bossBullets.get(i);
			getGraphicsManager().drawBigBullet(bigBullet, g2d, this);

			boolean remove = this.moveBossBullet(bigBullet);
			if(remove){
				bossBullets.remove(i);
				i--;
			}
		}
		
		currentTime = System.currentTimeMillis();
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
	
	public void fireBossBullet(){
		int xPos = boss.x;
		int yPos = boss.y + boss.width/2 - BigBullet.HEIGHT + 4;
		BigBullet  bossBullet = new BigBullet(xPos, yPos);
		bossBullets.add(bossBullet);
		this.getSoundManager().playBulletSound();
	}
	
	public void fireBoss2Bullet(){
		int xPos = boss2.x;
		int yPos = boss2.y + boss.width/2 - BigBullet.HEIGHT + 4;
		BigBullet  bossBullet = new BigBullet(xPos, yPos);
		boss2Bullets.add(bossBullet);
		this.getSoundManager().playBulletSound();
	}
	
	public boolean moveBossBullet(BigBullet bossBullet){
		if(bossBullet.getY() - bossBullet.getSpeed() >= 0){
			bossBullet.translate(-bossBullet.getSpeed(), 0);
			return false;
		}
		else{
			return true;
		}
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
	
	public Boss newBoss(Level3State screen){
		int xPos = (int) (screen.getWidth() - Boss.WIDTH);
		int yPos = 0;
		boss = new Boss(xPos, yPos);
		return boss;
	}
	
	public Boss newBoss2(Level3State screen){
		int xPos = 0;
		int yPos = (int) (screen.getWidth() - Boss.HEIGHT);
		boss2 = new Boss(xPos, yPos);
		return boss2;
	}
	
	public void removeBoss(Boss boss){
		// "remove" boss
		bossExplosion = new Rectangle(
				boss.x,
				boss.y,
				boss.getPixelsWide(),
				boss.getPixelsTall());
		boss.setLocation(-boss.getPixelsWide(), -boss.getPixelsTall());
		// play boss explosion sound
		this.getSoundManager().playAsteroidExplosionSound();
	}
	
	protected void checkAsteroidFloorCollisions() {
		for(int i=0; i<9; i++){
			if(asteroid.intersects(floor[i])){
				removeAsteroid(asteroid);
			}
			
			if(asteroid2.intersects(floor[i])){
				removeAsteroid(asteroid2);
			}
		}
	}
	
	protected void checkMegaManAsteroidCollisions() {
		GameStatus status = getGameStatus();
		if(asteroid.intersects(megaMan)){
			status.setLivesLeft(status.getLivesLeft() - 1);
			removeAsteroid(asteroid);
		}
		
		if(asteroid2.intersects(megaMan)){
			status.setLivesLeft(status.getLivesLeft() - 1);
			removeAsteroid(asteroid2);
		}
	}
	
	protected void checkBigBulletAsteroidCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			if(asteroid.intersects(bigBullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid);
				damage=0;
			}
			
			if(asteroid2.intersects(bigBullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid2);
				damage=0;
			}
		}
	}
	
	protected void checkBullletAsteroidCollisions() {
		GameStatus status = getGameStatus();
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if(asteroid.intersects(bullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid);
				levelAsteroidsDestroyed++;
				damage=0;
				// remove bullet
				bullets.remove(i);
				break;
			}
			
			if(asteroid2.intersects(bullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid2);
				levelAsteroidsDestroyed++;
				damage=0;
				// remove bullet
				bullets.remove(i);
				break;
			}
		}
	}
	
	public void removeAsteroid(Asteroid asteroid){
		// "remove" asteroid
		asteroidExplosion = new Rectangle(
				asteroid.x,
				asteroid.y,
				asteroid.getPixelsWide(),
				asteroid.getPixelsTall());
		asteroid.setLocation(-asteroid.getPixelsWide(), -asteroid.getPixelsTall());
		this.getGameStatus().setNewAsteroid(true);
		lastAsteroidTime = System.currentTimeMillis();
		lastAsteroid2Time = System.currentTimeMillis();
		// play asteroid explosion sound
		this.getSoundManager().playAsteroidExplosionSound();
	}
	
	public Asteroid newAsteroid2(Level1State screen){
		int xPos = (int) (screen.getWidth() - Asteroid.WIDTH);
		int yPos = rand.nextInt((int)(screen.getHeight() - Asteroid.HEIGHT- 32));
		asteroid2 = new Asteroid(xPos, yPos);
		return asteroid2;
	}
}