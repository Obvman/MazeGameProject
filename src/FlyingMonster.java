import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class FlyingMonster extends Monster implements ActionListener {

	public FlyingMonster() {
		randomiseDirection();
		
		scaledHeight = Maze.MAZE_CELL_SIZE;

		image_N = new BufferedImage[4];
		image_W = new BufferedImage[4];
		image_S = new BufferedImage[4];
		image_E = new BufferedImage[4];

		try {
			for (int i = 0; i < 4; i++) {
				image_N[i] = getScaledBufferedImage(ImageIO.read(new File("resources/dragon/dragonN" + (i+1) + ".png")), scaledHeight, scaledHeight);
				image_W[i] = getScaledBufferedImage(ImageIO.read(new File("resources/dragon/dragonW" + (i+1) + ".png")), scaledHeight, scaledHeight);
				image_S[i] = getScaledBufferedImage(ImageIO.read(new File("resources/dragon/dragonS" + (i+1) + ".png")), scaledHeight, scaledHeight);
				image_E[i] = getScaledBufferedImage(ImageIO.read(new File("resources/dragon/dragonE" + (i+1) + ".png")), scaledHeight, scaledHeight);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		animationTimer = new Timer(300, this); 
		animationTimer.start();
		changeDirectionTimer = new Timer(2000, this);
		changeDirectionTimer.start();
	}

	@Override
	public Image getImage() {
		Image[] image = image_W;

		if (getDX() == 0 && getDY() < 0) {
			image = image_N;
		} else if (getDX() < 0 && getDY() == 0) {
			image = image_W;
		} else if (getDX() == 0 && getDY() > 0) {
			image = image_S;
		} else if (getDX() > 0 && getDY() == 0) {
			image = image_E;
		}

		return image[spriteCounter];
	}

	@Override
	public void randomiseDirection() {
		Double rand = Math.random();
		int dx = 0;
		int dy = 0;
		if (rand > 0.75) {
			dx = 1;
		} else if (rand > 0.5) {
			dx = -1;
		} else if (rand > 0.25) {
			dy = 1;
		} else {
			dy = -1;
		}
		
		setDX((int) (dx * (double)Maze.MAZE_CELL_SIZE/16));
		setDY((int) (dy * (double)Maze.MAZE_CELL_SIZE/16));
	}
	
	@Override
	public boolean canFly() {
		return true;
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), scaledHeight, scaledHeight);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		spriteCounter = (spriteCounter + 1) % 4;

		if (e.getSource() == changeDirectionTimer) {
			randomiseDirection();
		}
	}

	private BufferedImage getScaledBufferedImage(BufferedImage src, int w, int h){
		int finalw = w;
		int finalh = h;
		double factor = 1.0d;
		if(src.getWidth() > src.getHeight()){
			factor = ((double)src.getHeight()/(double)src.getWidth());
			finalh = (int)(finalw * factor);                
		}else{
			factor = ((double)src.getWidth()/(double)src.getHeight());
			finalw = (int)(finalh * factor);
		}   

		BufferedImage resizedImg = new BufferedImage(finalw, finalh, BufferedImage.TRANSLUCENT);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(src, 0, 0, finalw, finalh, null);
		g2.dispose();
		return resizedImg;
	}
	
	private BufferedImage[] image_N;
	private BufferedImage[] image_W;
	private BufferedImage[] image_S;
	private BufferedImage[] image_E;
	private int scaledHeight;

	private Timer animationTimer;
	private Timer changeDirectionTimer;
	private int spriteCounter;

}