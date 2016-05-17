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
	private LinkedList<Spell> spells;
	private boolean alive;
	
	// character sprites
	private Image lastImage;
	private Image image_N;
	private Image image_NE;
	private Image image_E;
	private Image image_SE;
	private Image image_S;
	private Image image_SW;
	private Image image_W;
	private Image image_NW;
	
	public Player() {
		spells = new LinkedList<Spell>();
		alive = true;
		
		// sprites
		int scaledSize = (int)(2 * Maze.MAZE_CELL_SIZE) / 3;
		image_N = new ImageIcon("resources/player_N.png").getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
		image_NE = new ImageIcon("resources/player_NE.png").getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
		image_E = new ImageIcon("resources/player_E.png").getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
		image_SE = new ImageIcon("resources/player_SE.png").getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
		image_S = new ImageIcon("resources/player_S.png").getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
		image_SW = new ImageIcon("resources/player_SW.png").getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
		image_W = new ImageIcon("resources/player_W.png").getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
		image_NW = new ImageIcon("resources/player_NW.png").getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
		lastImage = image_S; // initially face down
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
		Image image = null;
		
		if (dx == 0 && dy == 0) {
			return lastImage;
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
        return image;
    }
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, getImage().getWidth(null), getImage().getHeight(null));
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
        		spells.add(new Spell(x + lastDX * imageWidth  - imageWidth/2, 
        							 y + lastDY * imageWidth - imageHeight/2, 2*lastDX, 2*lastDY));
        	}
        }
    }

	
}
