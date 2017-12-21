package rbadia.voidspace.main;

import java.awt.Graphics2D;

import rbadia.voidspace.graphics.NewGraphicsManager;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.sounds.SoundManager;

public class Level4State extends Level3State {


	private static final long serialVersionUID = 8738640199070011353L;

	public Level4State(int level, MainFrame frame, GameStatus status, 
			NewLevelLogic gameLogic, InputHandler inputHandler,
			NewGraphicsManager graphicsMan, SoundManager soundMan) {
		super(level, frame, status, gameLogic, inputHandler, graphicsMan, soundMan);
	}
	
	protected void drawAsteroid() {
		Graphics2D g2d = getGraphics2D();
		GameStatus status = getGameStatus();
		if((asteroid.getX() + asteroid.getWidth() >  0)){
			asteroid.translate(-asteroid.getSpeed() + rand.nextInt(asteroid.getSpeed()) - 
					rand.nextInt(asteroid.getSpeed()), rand.nextInt(asteroid.getSpeed()) - 
					rand.nextInt(asteroid.getSpeed()));
			getGraphicsManager().drawAsteroid(asteroid, g2d, this);	
		}
		else {
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){
				// draw a new asteroid
				lastAsteroidTime = currentTime;
				status.setNewAsteroid(false);
				asteroid.setLocation((int) (this.getWidth() - asteroid.getPixelsWide()),
						(rand.nextInt((int) (this.getHeight() - asteroid.getPixelsTall() - 32))));
			}

			else{
				// draw explosion
				getGraphicsManager().drawAsteroidExplosion(asteroidExplosion, g2d, this);
			}
		}
		
		if((asteroid2.getX() + asteroid2.getWidth() >  0)){
			asteroid2.translate(-asteroid2.getSpeed() + rand.nextInt(asteroid2.getSpeed()) - 
					rand.nextInt(asteroid2.getSpeed()), rand.nextInt(asteroid2.getSpeed()) - 
					rand.nextInt(asteroid2.getSpeed()));
			getGraphicsManager().drawAsteroid(asteroid2, g2d, this);	
		}
		else {
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastAsteroid2Time) > NEW_ASTEROID_DELAY){
				// draw a new asteroid
				lastAsteroid2Time = currentTime;
				status.setNewAsteroid(false);
				asteroid2.setLocation((int) (this.getWidth() - asteroid2.getPixelsWide()),
						(rand.nextInt((int) (this.getHeight() - asteroid2.getPixelsTall() - 32))));
			}

			else{
				// draw explosion
				getGraphicsManager().drawAsteroidExplosion(asteroidExplosion, g2d, this);
			}
		}
	}
	

	@Override
	public Platform[] newPlatforms(int n){
		platforms = new Platform[n];
		int k = 1;
		for(int i=0; i<n; i++){
			this.platforms[i] = new Platform(0,0);
			
			if(i < 4 ) {
				platforms[i].setLocation(200, getHeight() / 4 + (i + 1) * 60);
			}
			else {
			
				platforms[i].setLocation(250, getHeight() / 6 + (k) * 60);
				k++;
			}
		}
		return platforms;
	}

}
