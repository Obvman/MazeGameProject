import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.*;

public class Player implements MovableSprite {
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
		ImageIcon playerImage = new ImageIcon("resources/player_down.png");
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
    		playerImage = new ImageIcon("resources/player_right.png");
    		image = playerImage.getImage();
    	} else if (dx < 0) {
    		playerImage = new ImageIcon("resources/player_left.png");
    		image = playerImage.getImage();
    	} else if (dy > 0) {
    		playerImage = new ImageIcon("resources/player_down.png");
    		image = playerImage.getImage();
    	} else if (dy < 0) {
    		playerImage = new ImageIcon("resources/player_up.png");
    		image = playerImage.getImage();
    	} 
    	
        return image;
    }
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
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
		x += dx;
		y += dy;
	}
	
	public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
        	lastDX = -1;
            dx = -1;
        }

        if (key == KeyEvent.VK_RIGHT) {
        	lastDX = 1;
            dx = 1;
        }

        if (key == KeyEvent.VK_UP) {
        	lastDY = -1;
        	dy = -1;
        }

        if (key == KeyEvent.VK_DOWN) {
        	lastDY = 1;
        	dy = 1;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
        	if (dy != 0) {
        		lastDX = 0;
        	} else {
        		lastDY = 0;
        	}
        	
        	if (dx != 1) dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
        	if (dy != 0) {
        		lastDX = 0;
        	} else {
        		lastDY = 0;
        	}
        	
        	if (dx != -1) dx = 0;
        }

        if (key == KeyEvent.VK_UP) {
        	if (dx != 0) { 
        		lastDY = 0;
        	} else {
        		lastDX = 0;
        	}
        	
            if (dy != 1) dy = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
        	if (dx != 0) {
        		lastDY = 0;
        	} else {
        		lastDX = 0;
        	}
        	
            if (dy != -1) dy = 0;
        }
        
        if (key == KeyEvent.VK_SPACE) {
        	if (spells.size() < 2) {
        		int imageWidth = image.getWidth(null);
            	int imageHeight = image.getHeight(null);
        		spells.add(new Spell(x + lastDX * imageWidth  - imageWidth/2, 
        							 y + lastDY * imageWidth - imageHeight/2, 2*lastDX, 2*lastDY));
        	}
        }
    }

	
}
