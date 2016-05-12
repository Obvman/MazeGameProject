import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Player {
	private int x;
	private int y;
	private int dx;
	private int dy;
	private Image image;
	
	public Player() {
		initPlayer();
	}
	
	public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDX() {
		return dx;
	}
	
	public int getDY() {
		return dy;
	}
    
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
	
	public void move() {
		x += dx;
		y += dy;
	}
    
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
    }
    
    private void initPlayer() {
		ImageIcon playerImage = new ImageIcon("images/player_down.png");
		image = playerImage.getImage();
		x = 0;
		y = 0;
	}

	
}
