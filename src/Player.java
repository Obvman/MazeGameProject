import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.Timer;

public class Player implements MovableSprite, ActionListener {
	/**
	 * Creates a Player with a given spell type and a controls scheme to control the Player
	 * @param spellType the spell type of the player 
	 * @param keys The key bindings corresponding the Player's controls
	 */
	public Player(int spellType, int[] keys)  {
		spells = new LinkedList<Spell>();
		this.spellType = spellType;
		alive = true;

		// set key mappings
		moveRightKey = keys[0];
		moveLeftKey = keys[1];
		moveUpKey = keys[2];
		moveDownKey = keys[3];
		shootKey = keys[4];

		// load images
		scaledHeight = (3 * Maze.MAZE_CELL_SIZE) / 4;
		image_N = new BufferedImage[6];
		image_NE = new BufferedImage[6];
		image_E = new BufferedImage[6];
		image_SE = new BufferedImage[6];
		image_S = new BufferedImage[6];
		image_SW = new BufferedImage[6];
		image_W = new BufferedImage[6];
		image_NW = new BufferedImage[6];
		try {
			for (int i = 0; i < 6; i++) {
				image_N[i] = getScaledBufferedImage(ImageIO.read(new File("resources/player/playerN" + (i+1) + ".png")), scaledHeight, scaledHeight);
				image_NE[i] = getScaledBufferedImage(ImageIO.read(new File("resources/player/playerNE" + (i+1) + ".png")), scaledHeight, scaledHeight);
				image_E[i] = getScaledBufferedImage(ImageIO.read(new File("resources/player/playerE" + (i+1) + ".png")), scaledHeight, scaledHeight);
				image_SE[i] = getScaledBufferedImage(ImageIO.read(new File("resources/player/playerSE" + (i+1) + ".png")), scaledHeight, scaledHeight);
				image_S[i] = getScaledBufferedImage(ImageIO.read(new File("resources/player/playerS" + (i+1) + ".png")), scaledHeight, scaledHeight);
				image_SW[i] = getScaledBufferedImage(ImageIO.read(new File("resources/player/playerSW" + (i+1) + ".png")), scaledHeight, scaledHeight);
				image_W[i] = getScaledBufferedImage(ImageIO.read(new File("resources/player/playerW" + (i+1) + ".png")), scaledHeight, scaledHeight);
				image_NW[i] = getScaledBufferedImage(ImageIO.read(new File("resources/player/playerNW" + (i+1) + ".png")), scaledHeight, scaledHeight);
			}
			lastImage = image_S;
		} catch (IOException e) {
			// do nothing
		}
		
		// start animationTimer
		animationTimer = new Timer(200, this); 
		animationTimer.start();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPosition(int x, int y) {
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
		return (int) (dx * (double)Maze.MAZE_CELL_SIZE/16);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDY() {
		return (int) (dy * (double)Maze.MAZE_CELL_SIZE/16);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BufferedImage getImage() {
		BufferedImage[] image = null;

		if (dx == 0 && dy == 0) {
			animationTimer.stop();
			return lastImage[spriteCounter];
		}

		if (!animationTimer.isRunning()) {
			spriteCounter = (spriteCounter + 1) % 6;
			animationTimer.start();
		}

		if (dx == 0 && dy < 0) {
			image =  image_N;
		} else if (dx > 0 && dy < 0) {
			image =  image_NE;
		} else if (dx > 0 && dy == 0) {
			image =  image_E;
		} else if (dx > 0 && dy > 0) {
			image =  image_SE;
		} else if (dx == 0 && dy > 0) {
			image =  image_S;
		} else if (dx < 0 && dy > 0) {
			image =  image_SW;
		} else if (dx < 0 && dy == 0) {
			image =  image_W;
		} else if (dx < 0 && dy < 0) {
			image =  image_NW;
		}

		lastImage = image;
		return image[spriteCounter];
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
		x += dx;
		y += dy;
	}
	
	/**
	 * Periodically updates the Player image to create animation
	 * @param e The ActionEvent triggered by the animation timer
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		spriteCounter = (spriteCounter + 1) % 6;
	}
	
	/**
	 * @return LinkedList of Spells casted by the player and are currently in the Maze
	 */
	public LinkedList<Spell> getSpells() {
		for (Iterator<Spell> iterator = spells.iterator(); iterator.hasNext(); ) {
			if (iterator.next().getStage() > 2) {
				iterator.remove();
			}
		}
		return spells;
	}		

	/**
	 * Indicates whether the Player has been killed or not
	 * @return true if the Player is alive else false
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * Sets whether the Player is alive or not
	 * @param alive the boolean indicating whether the Player is alive or not (true = alive, false = not alive)
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	

	/**
	 * Sets the movement direction of the Player when a movement key has been pressed (start movement)
	 * @param e the KeyEvent corresponding to the movement key which was pressed
	 */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == moveLeftKey) {
			dx = -1;
		}

		if (key == moveRightKey) {
			dx = 1;
		}

		if (key == moveUpKey) {
			dy = -1;
		}

		if (key == moveDownKey) {
			dy = 1;
		}
	}

	/**
	 * Sets the movement direction of the Player when a movement key has been released (halt movement)
	 * Casts spells if the shoot key was released.
	 * @param e the KeyEvent corresponding to a movement key or shoot key that was released
	 */
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == moveLeftKey) {
			if (dx != 1) dx = 0;
		}

		if (key == moveRightKey) {
			if (dx != -1) dx = 0;
		}

		if (key == moveUpKey) {
			if (dy != 1) dy = 0;
		}

		if (key == moveDownKey) {
			if (dy != -1) dy = 0;
		}

		if (key == shootKey) {
			if (spells.size() < 2) {
				// work out direction currently being faced
				int lastDX = 0;
				int lastDY = 1; // initially facing down
				if (lastImage == image_N) {
					lastDX = 0; lastDY  =-1;
				} else if (lastImage == image_NE) {
					lastDX = 1; lastDY = -1;
				} else if (lastImage == image_E) {
					lastDX = 1; lastDY = 0;
				} else if (lastImage == image_SE) {
					lastDX = 1; lastDY = 1;
				} else if (lastImage == image_S) {
					lastDX = 0; lastDY = 1;
				} else if (lastImage == image_SW) {
					lastDX = -1; lastDY = 1;
				} else if (lastImage == image_W) {
					lastDX = -1; lastDY = 0;
				} else if (lastImage == image_NW) {
					lastDX = -1; lastDY = -1;
				}

				int imageWidth = getImage().getWidth(null);
				int imageHeight = getImage().getHeight(null);

				spells.add(new Spell(getX() + lastDX * imageWidth, 
						getY() + lastDY * imageHeight, 2*lastDX, 2*lastDY, spellType));
			}
		}
	}
	
	/**
	 * Stops the Player's movement
	 */
	public void releaseKeys() {
		dx = 0;
		dy = 0;
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
	private LinkedList<Spell> spells;
	private int spellType;
	private boolean alive;
	private int moveRightKey;
	private int moveLeftKey;
	private int moveUpKey;
	private int moveDownKey;
	private int shootKey;

	// character sprites
	private BufferedImage[] image_N;
	private BufferedImage[] image_NE;
	private BufferedImage[] image_E;
	private BufferedImage[] image_SE;
	private BufferedImage[] image_S;
	private BufferedImage[] image_SW;
	private BufferedImage[] image_W;
	private BufferedImage[] image_NW;
	private BufferedImage[] lastImage;
	private int scaledHeight;

	private Timer animationTimer;
	private int spriteCounter;
}
