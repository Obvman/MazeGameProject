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
		ImageIcon monsterImage = new ImageIcon("images/monster_down.png");
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
    		monsterImage = new ImageIcon("images/monster_right.png");
    		image = monsterImage.getImage();
    	} else if (dx < 0) {
    		monsterImage = new ImageIcon("images/monster_left.png");
    		image = monsterImage.getImage();
    	} else if (dy > 0) {
    		monsterImage = new ImageIcon("images/monster_down.png");
    		image = monsterImage.getImage();
    	} else if (dy < 0) {
    		monsterImage = new ImageIcon("images/monster_up.png");
    		image = monsterImage.getImage();
    	}
    	
        return image;
    }

	@Override
    public void manualMove(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
    	x += dx;
    	y += dy;
    }
	
	public void setPosition (int x, int y) {
		this.x = x;
		this.y = y;
	}
}