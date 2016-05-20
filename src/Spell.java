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
	private Image image1;
	private Image image2;
	private Image image3;
	private int stage; 

	public Spell(int startX, int startY, int dx, int dy) {
		initialX = startX;
		initialY = startY;
		
		x = startX;
		y = startY;
		this.dx = dx;
		this.dy = dy;
		
		image1 = (new ImageIcon("resources/32_flame_1.png")).getImage();
		image2 = (new ImageIcon("resources/32_flame_2.png")).getImage();
		image3 = (new ImageIcon("resources/32_flame_3.png")).getImage();
		
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
			return image1;
		} else if (stage == 1) {
			return image2;
		} else {
			return image3;
		}
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, getImage().getWidth(null), getImage().getHeight(null));
	}
	
	public void updatePosition() {
		x += dx;
		y += dy;
		
		stage = (int)Math.sqrt(Math.pow(x - initialX, 2) + Math.pow(y - initialY, 2))/getImage().getWidth(null);
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
