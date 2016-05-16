import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Spell implements MovableSprite {

	private int initialX;
	private int initialY;
	private int x;
	private int y;
	private int dx;
	private int dy;
	private Image image;
	private int stage; 

	public Spell(int startX, int startY, int dx, int dy) {
		initialX = startX;
		initialY = startY;
		x = startX;
		y = startY;
		this.dx = dx;
		this.dy = dy;
		image = (new ImageIcon("resources/32_flame_1.png")).getImage();
		
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
		if (stage == 0) {
			image = (new ImageIcon("resources/32_flame_1.png")).getImage();
		} else if (stage == 1) {
			image = (new ImageIcon("resources/32_flame_2.png")).getImage();
		} else {
			image = (new ImageIcon("resources/32_flame_3.png")).getImage();
		}
		
		return image;
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
	}
	
	public void updatePosition() {
		x += dx;
		y += dy;
		
		stage = (int)Math.sqrt(Math.pow(x - initialX, 2) + Math.pow(y - initialY, 2))/image.getWidth(null);
	}
	
	public int getStage() {
		return stage;
	}
	
	public void updateStage() {
		stage++;
	}

	@Override
	public void manualMove(int dx, int dy) {
		x += dx;
		y += dy;
	}
}
