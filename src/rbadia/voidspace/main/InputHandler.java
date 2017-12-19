package rbadia.voidspace.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


/**
 * Handles user input events.
 */
public class InputHandler implements KeyListener{
	private boolean leftIsPressed;
	private boolean rightIsPressed;
	private boolean downIsPressed;
	private boolean upIsPressed;
	private boolean spaceIsPressed = false;
	private boolean shiftIsPressed;
	private boolean eIsPressed;
	private boolean qIsPressed;
	private boolean mIsPressed;
	private boolean nIsPressed;
	private boolean sIsPressed;
	private boolean iIsPressed;
	
	private boolean isFacingRight = true;
	private boolean isMute = false;

	private LevelState levelState;
	//private GameScreen gScreen;
	private LevelLogic LevelLogic;

	//getters
	public LevelState getLevelState() { return levelState; }
	public LevelLogic getLevelLogic() {return LevelLogic;}
	
	//setters
	public void setLevelLogic(LevelLogic levelLogic) {LevelLogic = levelLogic;}
	public void setLevelState(LevelState levelState) { this.levelState = levelState; }

	/**
	 * Create a new input handler
	 * @param gameLogic the game logic handler
	 */
	public InputHandler(){
		reset();
	}

	public void reset() {
		leftIsPressed = false;
		rightIsPressed = false;
		downIsPressed = false;
		upIsPressed = false;
		spaceIsPressed = false;
		shiftIsPressed = false;
		eIsPressed = false;
		qIsPressed = false;
		mIsPressed = false;
		nIsPressed = false;
		sIsPressed = false;
		iIsPressed = false;
		isFacingRight = true;
		isMute = isMute();
	}

	public boolean isLeftPressed() {
		return leftIsPressed;
	}

	public boolean isRightPressed() {
		return rightIsPressed;
	}

	public boolean isDownPressed() {
		return downIsPressed;
	}

	public boolean isUpPressed() {
		return upIsPressed;
	}

	public boolean isSpacePressed() {
		return spaceIsPressed;
	}

	public boolean isShiftPressed() {
		return shiftIsPressed;
	}

	public boolean isEPressed() {
		return eIsPressed;
	}

	public boolean isQPressed() {
		return qIsPressed;
	}

	public boolean isMPressed() {
		return mIsPressed;
	}
	
	public boolean isNPressed(){
		return nIsPressed;
	}

	public boolean isSPressed() {
		return sIsPressed;
	}

	public boolean isIPressed() {
		return iIsPressed;
	}
	
	public boolean isMute() {
		return isMute;
	}
	public boolean isMegaManFacingRight(){
		return isFacingRight;
	}

	/**
	 * Handle a key input event.
	 */
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_UP:
			this.upIsPressed = true;
			break;
		case KeyEvent.VK_DOWN:
			this.downIsPressed = true;
			break;
		case KeyEvent.VK_LEFT:
			this.leftIsPressed = true;
			this.isFacingRight = false;		//MegaMan is looking at the left
			break;
		case KeyEvent.VK_RIGHT:
			this.rightIsPressed = true;
			this.isFacingRight = true;		//MegaMan is looking at the right
			break;
		case KeyEvent.VK_SPACE:
			this.spaceIsPressed = true;
			break;
		case KeyEvent.VK_SHIFT:
			this.shiftIsPressed = true;
			break;
		case KeyEvent.VK_ESCAPE:
			System.exit(1);
			break;
		case KeyEvent.VK_E:
			this.eIsPressed = true;
			break;
		case KeyEvent.VK_Q:
			this.qIsPressed= true;
			break;
		case KeyEvent.VK_M:
			this.mIsPressed= true;
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
			break;
		case KeyEvent.VK_N:
			this.nIsPressed = true;
			break;
		case KeyEvent.VK_S:
			this.sIsPressed = true;
			break;
		case KeyEvent.VK_I:
			this.iIsPressed = true;
			break;
		}
		e.consume();
	}

	/**
	 * Handle a key release event.
	 */
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_UP:
			this.upIsPressed = false;
			break;
		case KeyEvent.VK_DOWN:
			this.downIsPressed = false;
			break;
		case KeyEvent.VK_LEFT:
			this.leftIsPressed = false;
			break;
		case KeyEvent.VK_RIGHT:
			this.rightIsPressed = false;
			break;
		case KeyEvent.VK_SPACE:
			this.spaceIsPressed = false;
			break;
		case KeyEvent.VK_SHIFT:
			this.shiftIsPressed = false;
			this.getLevelState().slowDownMegaMan();
			break;
		case KeyEvent.VK_E:
			this.eIsPressed = false;
			break;
		case KeyEvent.VK_Q:
			this.qIsPressed = false;
			break;
		case KeyEvent.VK_M:
			this.mIsPressed = false;
			break;
		case KeyEvent.VK_N:
			this.nIsPressed = false;
			break;
		case KeyEvent.VK_S:
			this.sIsPressed = false;
			break;
		case KeyEvent.VK_I:
			this.iIsPressed = false;
			break;
		}
		e.consume();
	}

	public void keyTyped(KeyEvent e) {
		// not used
	}

	public boolean getSpace(){
		return spaceIsPressed;
	}

}
