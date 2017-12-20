package rbadia.voidspace.main;

import java.awt.Font;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class NewLevelLogic extends LevelLogic{

	private long lastBulletTime;
	private long lastExchangeTime;
	private long lastBigBulletTime;
	private int stack= 0;

	protected Font originalFont;
	protected Font bigFont;
	protected Font biggestFont;
	
	private boolean isMute = false;
	private boolean isFacingRight = true;
	

	public NewLevelLogic(){
		super();
	}
	
	public boolean isMegaManFacingRight() {
		return isFacingRight;
	}
	
	@Override
	public void handleKeysDuringPlay(InputHandler ih, LevelState levelState) {

		GameStatus status = getLevelState().getGameStatus();

		// fire bullet if space is pressed
		if(ih.isSpacePressed()){
			// fire only up to 5 bullets per second
			stack=0;
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastBulletTime) > 1000/5){
				lastBulletTime = currentTime;
				getLevelState().fireBullet();
			}
		}

		if(ih.isEPressed()){
			if(status.getAsteroidsDestroyed()>= 1500){
				long currentTime = System.currentTimeMillis();
				if((currentTime - lastExchangeTime > 1000)){
					lastExchangeTime = currentTime;
					status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() - 1500);
					status.setLivesLeft(status.getLivesLeft() + 1);
				}
			}
		}

		if(ih.isQPressed()){
			if(stack==0 && status.getAsteroidsDestroyed()>= 0){
				stack++;
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed()-0);
			}
			else if(stack>= 1){
				long currentTime = System.currentTimeMillis();
				if((currentTime - lastBigBulletTime) > 1000){
					lastBigBulletTime = currentTime;
					getLevelState().fireBigBullet();
				}

			}
		}

		if(ih.isShiftPressed()){
			getLevelState().speedUpMegaMan();
		}

		if(ih.isUpPressed()){
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastBigBulletTime) > 570){
				lastBigBulletTime = currentTime;
				for(int i=0; i<6; i++){
					getLevelState().moveMegaManUp();
				}
			}
		}
		
		//mute button
		if(ih.isMPressed()){
			if(isMute){
				MegaManMain.audioFile = new File("audio/mainGame.wav");
				try {
					MegaManMain.audioStream = AudioSystem.getAudioInputStream(MegaManMain.audioFile);
					MegaManMain.audioClip.open(MegaManMain.audioStream);
					MegaManMain.audioClip.start();
					MegaManMain.audioClip.loop(Clip.LOOP_CONTINUOUSLY);
				} catch (UnsupportedAudioFileException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (LineUnavailableException e1) {
					e1.printStackTrace();
				}
				isMute = false;
			}
			else{
				MegaManMain.audioClip.close();
				isMute = true;
			}
		}

		if(ih.isDownPressed()){
			getLevelState().moveMegaManDown();
		}

		if(ih.isLeftPressed()){
			getLevelState().moveMegaManLeft();
			isFacingRight = false;
		}

		if(ih.isRightPressed()){
			getLevelState().moveMegaManRight();
			isFacingRight = true;
		}
	}
}
