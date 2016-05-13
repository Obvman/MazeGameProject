import java.awt.Image;

import javax.swing.ImageIcon;

public class Spell implements MovableSprite {

	private int x;
	private int y;
	private int dx;
	private int dy;
	private int stage; 

	public Spell(int startX, int startY, int dx, int dy) {
		x = startX;
		y = startY;
		this.dx = dx;
		this.dy = dy;
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
			return (new ImageIcon("images/32_flame_1.png")).getImage();
		} else if (stage == 1) {
			return (new ImageIcon("images/32_flame_2.png")).getImage();
		} else {
			return (new ImageIcon("images/32_flame_3.png")).getImage();
		}
	}
	
	public void updatePosition() {
		x += dx;
		y += dy;
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
