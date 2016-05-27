import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Monster implements MovableSprite {

	/**
	 * Creates a Monster with a randomized initial direction
	 */
	public Monster() {
		randomiseDirection(); // random initial direction

		// load images
		scaledHeight = Maze.MAZE_CELL_SIZE * 3/4;
		try {
			image_E = getScaledBufferedImage(ImageIO.read(new File("resources/monster_right.png")), scaledHeight, scaledHeight);
			image_N = getScaledBufferedImage(ImageIO.read(new File("resources/monster_up.png")), scaledHeight, scaledHeight);
			image_W = getScaledBufferedImage(ImageIO.read(new File("resources/monster_left.png")), scaledHeight, scaledHeight);
			image_S = getScaledBufferedImage(ImageIO.read(new File("resources/monster_down.png")), scaledHeight, scaledHeight);
		} catch (IOException e) {
			//do nothing
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPosition (int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getX() {
		return (int)x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getY() {
		return (int)y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDX() {
		return dx;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDY() {
		return dy;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getImage() {
		if (dx == 0 && dy < 0) {
			return image_N;
		} else if (dx < 0 && dy == 0) {
			return image_W;
		} else if (dx == 0 && dy > 0) {
			return image_S;
		} else if (dx > 0 && dy == 0) {
			return image_E;
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), scaledHeight, scaledHeight);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void manualMove(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
		x += dx;
		y += dy;
	}
	
	/**
	 * Sets the x-axis movement value
	 * @param dx The x-axis movement value
	 */
	public void setDX(int dx) {
		this.dx = dx;
	}

	/**
	 * Sets the y-axis movement value
	 * @param dy the y-axis movement
	 */
	public void setDY(int dy) {
		this.dy = dy;
	}

	/**
	 * Sets the movement direction to be north, south, east, or west (randomly chosen)
	 */
	public void randomiseDirection() {
		Double rand = Math.random();
		dx = 0;
		dy = 0;
		if (rand > 0.75) {
			dx = 1;
		} else if (rand > 0.5) {
			dx = -1;
		} else if (rand > 0.25) {
			dy = 1;
		} else {
			dy = -1;
		}

		dx *= (double)Maze.MAZE_CELL_SIZE/32;
		dy *= (double)Maze.MAZE_CELL_SIZE/32;
	}

	/**
	 * Indicates whether the Monster is capable of ignoring maze walls
	 * @return false
	 */
	public boolean canFly() {
		return false;
	}

	/**
	 * Returns a scaled instance of a BufferedImage
	 * @param img the BufferedImage to be scaled
	 * @param width the width of the scaled BufferedImage
	 * @param height the height of the scaled BufferedImage
	 * @return the scaled BufferedImage
	 */
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

	// movement
	private double x;
	private double y;
	private int dx;
	private int dy;

	// images
	private BufferedImage image_N;
	private BufferedImage image_W;
	private BufferedImage image_S;
	private BufferedImage image_E;
	private int scaledHeight;
}