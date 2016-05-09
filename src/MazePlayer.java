import java.awt.Image;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class MazePlayer {
	private int dx;
	private int dy;
	private int x;
	private int y;
	private Image image;
	
	public MazePlayer() {
		initPlayer();
	}

	private void initPlayer() {
		ImageIcon playerImage = new ImageIcon("dot.png");
		image = playerImage.getImage(); // set image
		x = 0;
		y = 0;
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
	
	/**
	 * Move player based on values of dx, dy
	 * Note: dx, dy are set by key presses (arrows)
	 */
	public void move() {
		
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
    	System.out.println("SADKSDKApsk");
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
}
