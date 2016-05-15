import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Monster implements MovableSprite {
	private int x;
	private int y;
	private int dx;
	private int dy;
	private Image image;
	
	public Monster() {
		ImageIcon monsterImage = new ImageIcon("resources/monster_down.png");
		image = monsterImage.getImage();
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
    	ImageIcon monsterImage = null;
    	
    	if (dx > 0) {
    		monsterImage = new ImageIcon("resources/monster_right.png");
    		image = monsterImage.getImage();
    	} else if (dx < 0) {
    		monsterImage = new ImageIcon("resources/monster_left.png");
    		image = monsterImage.getImage();
    	} else if (dy > 0) {
    		monsterImage = new ImageIcon("resources/monster_down.png");
    		image = monsterImage.getImage();
    	} else if (dy < 0) {
    		monsterImage = new ImageIcon("resources/monster_up.png");
    		image = monsterImage.getImage();
    	}
    	
        return image;
    }
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
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