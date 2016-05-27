import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Monster implements MovableSprite {

	/**
	 * Constructor
	 * Randomises the initial direction, sets the height of the sprite
	 * image, sets the sprite images.
	 */
	public Monster() {
		//randomises the initial dir of the monster
		randomiseDirection();

		//changes height to 75% of the cell size
		scaledHeight = Maze.MAZE_CELL_SIZE * 3/4;

		//Set the sprite images
		try {
			image_E = getScaledImage(ImageIO.read(new File("resources/monster_right.png")), scaledHeight, scaledHeight);
			image_N = getScaledImage(ImageIO.read(new File("resources/monster_up.png")), scaledHeight, scaledHeight);
			image_W = getScaledImage(ImageIO.read(new File("resources/monster_left.png")), scaledHeight, scaledHeight);
			image_S = getScaledImage(ImageIO.read(new File("resources/monster_down.png")), scaledHeight, scaledHeight);
		} catch (IOException e) {
			//do nothing
		}
	}

	/**
	 * Sets the position (x and y coords) of the monster
	 */
	@Override
	public void setPosition (int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the x coordinate of the monster
	 * @return The x coordinate
	 */
	@Override
	public int getX() {
		return (int)x;
	}

	/**
	 * Returns the y coordinate of the monster
	 * @return The y coordinate
	 */
	@Override
	public int getY() {
		return (int)y;
	}

	/**
	 * Returns the dx value (x dir) of the monster
	 * @return The dx value
	 */
	@Override
	public int getDX() {
		return dx;
	}

	/**
	 * Returns the dy value (y dir) of the monster
	 * @return The dy value
	 */
	@Override
	public int getDY() {
		return dy;
	}

	/**
	 * Sets the dx value of the monster
	 * @param dx The dx (x direction) value
	 */
	public void setDX(int dx) {
		this.dx = dx;
	}

	/**
	 * Sets the dy value of the monster
	 * @param dy The dy (y direction) value
	 */
	public void setDY(int dy) {
		this.dy = dy;
	}

	/**
	 * Returns the sprite image of the monster corresponding
	 * to its current x and y direction
	 * @return The sprite image
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
	 * Gets the hitbox of a sprite, used to tell when two sprites intersect
	 * @return Rectangle representing the thibox
	 */
	@Override
	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), scaledHeight, scaledHeight);
	}

	/**
	 * Manually moves the monster in the given direction
	 * Updates its location
	 * @param dx The x value to be moved
	 * @param dy The y value to be moved)
	 */
	@Override
	public void manualMove(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
		x += dx;
		y += dy;
	}

	/**
	 * Picks a random straight line direction for the monster
	 * to move in. Updates the dx and dy accordingly.
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
	 * Returns a value indicating whether this movableSprite can
	 * fly.
	 * @return False This is not a flying monster.
	 */
	public boolean canFly() {
		return false;
	}

	/**
	 * Returns a scaled instance of an ImageIcon
	 * @param img the ImageIcon to be scaled
	 * @param width the width of the new ImageIcon
	 * @param height the height of the new ImageIcon
	 * @return The resized instance
	 */
	private BufferedImage getScaledImage(BufferedImage src, int w, int h){
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

	private double x;
	private double y;
	private int dx;
	private int dy;

	private BufferedImage image_N;
	private BufferedImage image_W;
	private BufferedImage image_S;
	private BufferedImage image_E;
	private int scaledHeight;
}