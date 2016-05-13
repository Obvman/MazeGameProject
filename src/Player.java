import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.*;

public class Player implements MovableSprite, MazeConstants {
	private int x;
	private int y;
	private int dx;
	private int dy;
	private int lastDX;
	private int lastDY;
	private Image image;
	private LinkedList<Spell> spells;
	private boolean alive;
	
	public Player() {
		ImageIcon playerImage = new ImageIcon("images/player_down.png");
		image = playerImage.getImage();
		spells = new LinkedList<Spell>();
		alive = true;
		lastDY = 1;
	}
	
	@Override
	public int getX() {
        return x;
    }

	@Override
    public int getY() {
        return y;
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
    public Image getImage() {
    	ImageIcon playerImage = null;
    	
    	if (dx > 0) {
    		playerImage = new ImageIcon("images/player_right.png");
    		image = playerImage.getImage();
    	} else if (dx < 0) {
    		playerImage = new ImageIcon("images/player_left.png");
    		image = playerImage.getImage();
    	} else if (dy > 0) {
    		playerImage = new ImageIcon("images/player_down.png");
    		image = playerImage.getImage();
    	} else if (dy < 0) {
    		playerImage = new ImageIcon("images/player_up.png");
    		image = playerImage.getImage();
    	} 
    	
        return image;
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
    	x += dx;
    	y += dy;
    }
	
	public void move() {
		if (!(dx == 0 && dy == 0)) {
			lastDX = dx;
			lastDY = dy;
		}
		x += dx;
		y += dy;
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
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_UP) {
            dy = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
        
        if (key == KeyEvent.VK_SPACE) {
        	int imageWidth = image.getWidth(null);
        	int imageHeight = image.getHeight(null);
    		spells.add(new Spell(x + lastDX*MAZE_CELL_SIZE - imageWidth/2, 
    							 y + lastDY*MAZE_CELL_SIZE - imageHeight, 2*lastDX, 2*lastDY));
        }
    }
}
