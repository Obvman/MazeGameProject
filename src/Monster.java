import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Monster implements MovableSprite {
	private int x;
	private int y;
	private int dx;
	private int dy;
	private Image image_N;
	private Image image_W;
	private Image image_S;
	private Image image_E;
	
	public Monster() {
		dy = 1;
		
		int scaledSize = (int)(2 * Maze.MAZE_CELL_SIZE) / 3;
		image_N = new ImageIcon("resources/monster_up.png").getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
		image_W = new ImageIcon("resources/monster_left.png").getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
		image_S = new ImageIcon("resources/monster_down.png").getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
		image_E = new ImageIcon("resources/monster_right.png").getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
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
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, getImage().getWidth(null), getImage().getHeight(null));
	}

	@Override
    public void manualMove(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
    	x += getDX();
    	y += getDY();
    }
	
	// picks a random STRAIGHT line motion
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
	}
	
	public void setPosition (int x, int y) {
		this.x = x;
		this.y = y;
	}

	
}