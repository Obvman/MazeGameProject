import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Player implements MovableSprite, ActionListener {
	private double x;
	private double y;
	private int dx;
	private int dy;
	private LinkedList<Spell> spells;
	private boolean alive;

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
	private int scaledHeight = 3/4 * Maze.MAZE_CELL_SIZE;

	private Timer timer;
	private int spriteCounter = 0;

	public Player()  {
		spells = new LinkedList<Spell>();
		alive = true;

		// sprites
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
				image_N[i] = getScaledImage(ImageIO.read(new File("resources/player/playerN" + (i+1) + ".png")), scaledHeight, scaledHeight);
				image_NE[i] = getScaledImage(ImageIO.read(new File("resources/player/playerNE" + (i+1) + ".png")), scaledHeight, scaledHeight);
				image_E[i] = getScaledImage(ImageIO.read(new File("resources/player/playerE" + (i+1) + ".png")), scaledHeight, scaledHeight);
				image_SE[i] = getScaledImage(ImageIO.read(new File("resources/player/playerSE" + (i+1) + ".png")), scaledHeight, scaledHeight);
				image_S[i] = getScaledImage(ImageIO.read(new File("resources/player/playerS" + (i+1) + ".png")), scaledHeight, scaledHeight);
				image_SW[i] = getScaledImage(ImageIO.read(new File("resources/player/playerSW" + (i+1) + ".png")), scaledHeight, scaledHeight);
				image_W[i] = getScaledImage(ImageIO.read(new File("resources/player/playerW" + (i+1) + ".png")), scaledHeight, scaledHeight);
				image_NW[i] = getScaledImage(ImageIO.read(new File("resources/player/playerNW" + (i+1) + ".png")), scaledHeight, scaledHeight);
			}
			lastImage = image_S;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		timer = new Timer(200, this); 
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		spriteCounter = (spriteCounter + 1) % 6;
	}

	@Override
	public int getX() {
		return (int)x;
	}

	@Override
	public int getY() {
		return (int)y;
	}

	@Override
	public int getDX() {
		return dx;
	}

	@Override
	public int getDY() {
		return dy;
	}

	@Override
	public boolean canFly() {
		return false;
	}

	@Override
	public BufferedImage getImage() {
		BufferedImage[] image = null;

		if (dx == 0 && dy == 0) {
			timer.stop();
			return lastImage[spriteCounter];
		}

		if (!timer.isRunning()) {
			spriteCounter = (spriteCounter + 1) % 6;
			timer.start();
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

	@Override
	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), scaledHeight, scaledHeight);
	}

	public LinkedList<Spell> getSpells() {
		for (Iterator<Spell> iterator = spells.iterator(); iterator.hasNext(); ) {
			if (iterator.next().getStage() > 2) {
				iterator.remove();
			}
		}
		return spells;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	@Override
	public void manualMove(int dx, int dy) {
		x += dx * (double)Maze.MAZE_CELL_SIZE/32;
		y += dy * (double)Maze.MAZE_CELL_SIZE/32;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_LEFT) {
			dx = -1;
		}

		if (key == KeyEvent.VK_RIGHT) {
			dx = 1;
		}

		if (key == KeyEvent.VK_UP) {
			dy = -1;
		}

		if (key == KeyEvent.VK_DOWN) {
			dy = 1;
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_LEFT) {
			if (dx != 1) dx = 0;
		}

		if (key == KeyEvent.VK_RIGHT) {
			if (dx != -1) dx = 0;
		}

		if (key == KeyEvent.VK_UP) {
			if (dy != 1) dy = 0;
		}

		if (key == KeyEvent.VK_DOWN) {
			if (dy != -1) dy = 0;
		}

		if (key == KeyEvent.VK_SPACE) {
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
						getY() + lastDY * imageHeight, 2*lastDX, 2*lastDY));
			}
		}
	}

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
}
