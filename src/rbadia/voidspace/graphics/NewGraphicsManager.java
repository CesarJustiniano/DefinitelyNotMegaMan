package rbadia.voidspace.graphics;

import java.awt.Graphics2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import rbadia.voidspace.model.Boss;
import rbadia.voidspace.model.MegaMan;

public class NewGraphicsManager extends GraphicsManager{
	
	private BufferedImage bossImg;
	private BufferedImage bossExplosionImg;
	private BufferedImage megaManImg;
	private BufferedImage megaFallRImg;
	private BufferedImage megaFireRImg;
	private BufferedImage megaManImgL;
	private BufferedImage megaFallLImg;
	private BufferedImage megaFireLImg;
	private BufferedImage backgroundImg;

	
	public NewGraphicsManager(){
		super();
		
		try {
			this.bossImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/ship.png"));
			this.bossExplosionImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/shipExplosion.png"));
			this.megaManImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaMan3.png"));
			this.megaFallRImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaFallRight.png"));
			this.megaFireRImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaFireRight.png"));
			this.backgroundImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/085.png"));

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
	
	public void drawMegaManL(MegaMan megaMan, Graphics2D g2d, ImageObserver observer) {
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-megaManImg.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		megaManImgL = op.filter(megaManImg, null);
		
		g2d.drawImage(megaManImgL, megaMan.x, megaMan.y, observer);	
	}
	
	public void drawMegaManFallL(MegaMan megaMan, Graphics2D g2d, ImageObserver observer) {
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-megaFallRImg.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		megaFallLImg = op.filter(megaFallRImg, null);
		
		g2d.drawImage(megaFallLImg, megaMan.x, megaMan.y, observer);
	}
	
	public void drawMegaManFireL(MegaMan megaMan, Graphics2D g2d, ImageObserver observer) {
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-megaFireRImg.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		megaFireLImg = op.filter(megaFireRImg, null);
		
		g2d.drawImage(megaFireLImg, megaMan.x, megaMan.y, observer);
	}
	
	public void drawBackground(int xPos, int yPos, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(backgroundImg, xPos, yPos, observer);
	}

}
