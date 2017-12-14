package rbadia.voidspace.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import rbadia.voidspace.model.Boss;

public class NewGraphicsManager extends GraphicsManager{
	
	private BufferedImage bossImg;
	private BufferedImage bossExplosionImg;
	
	public NewGraphicsManager(){
		super();
		
		try {
			this.bossImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/ship.png"));
			this.bossExplosionImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/shipExplosion.png"));

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "The graphic files are either corrupt or missing.",
					"VoidSpace - Fatal Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public void drawBoss(Boss boss, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(bossImg, boss.x, boss.y, observer);
	}

	public void drawBossExplosion(Rectangle bossExplosion, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(bossExplosionImg, bossExplosion.x, bossExplosion.y, observer);
	}

}
