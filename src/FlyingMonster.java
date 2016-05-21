import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FlyingMonster extends Monster implements MovableSprite, ActionListener {
	private double x;
	private double y;
	private int dx;
	private int dy;
	
	private Image[] image_N;
	private Image[] image_W;
	private Image[] image_S;
	private Image[] image_E;
	private int scaledHeight;
	
	private Timer timer;
	private Timer timer2;
	private int spriteCounter = 0;
	
	public FlyingMonster() {
		scaledHeight = Maze.MAZE_CELL_SIZE;
		
		image_N = new Image [4];
		image_W = new Image [4];
		image_S = new Image [4];
		image_E = new Image [4];
		for (int i = 0; i < 4; i++) {
			image_N[i] = new ImageIcon("resources/dragon/dragonN" + (i+1) + ".png").getImage().getScaledInstance(-1, scaledHeight, Image.SCALE_SMOOTH);
			image_W[i] = new ImageIcon("resources/dragon/dragonW" + (i+1) + ".png").getImage().getScaledInstance(-1, scaledHeight, Image.SCALE_SMOOTH);
			image_S[i] = new ImageIcon("resources/dragon/dragonS" + (i+1) + ".png").getImage().getScaledInstance(-1, scaledHeight, Image.SCALE_SMOOTH);
			image_E[i] = new ImageIcon("resources/dragon/dragonE" + (i+1) + ".png").getImage().getScaledInstance(-1, scaledHeight, Image.SCALE_SMOOTH);
		}
		
		timer = new Timer(300, this); 
		timer.start();
		timer2 = new Timer(2000, this);
		timer2.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		spriteCounter = (spriteCounter + 1) % 4;
		
		if (e.getSource() == timer2) {
			randomiseDirection();
		}
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
		return true;
	}
	
	@Override
    public Image getImage() {
		Image[] image = image_W;
		
    	if (dx == 0 && dy < 0) {
    		image = image_N;
    	} else if (dx < 0 && dy == 0) {
    		image = image_W;
    	} else if (dx == 0 && dy > 0) {
    		image = image_S;
    	} else if (dx > 0 && dy == 0) {
    		image = image_E;
    	}
    	
    	return image[spriteCounter];
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), scaledHeight, scaledHeight);
	}

	@Override
    public void manualMove(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
    	x += dx * (double)Maze.MAZE_CELL_SIZE/32;
    	y += dy * (double)Maze.MAZE_CELL_SIZE/32;
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