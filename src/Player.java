import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Player {
	private int dx;
	private int dy;
	private int x;
	private int y;
	private Image image;
	
	public Player() {
		initPlayer();
	}
	
	public int getDX() {
		return dx;
	}
	
	public int getDY() {
		return dy;
	}
	
	public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
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
    
    /**
	 * Move player based on values of dx, dy
	 * Note: dx, dy are set by key presses (arrows)
	 */
	public void move() {
		x += dx;
		y += dy;
	}
	
    private void initPlayer() {
		ImageIcon playerImage = new ImageIcon("src/images/dot.png");
		image = playerImage.getImage();
		x = 0;
		y = 0;
	}
}
