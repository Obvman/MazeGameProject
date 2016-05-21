import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class WaterSpell implements Spell {

	private int initialX;
	private int initialY;
	double x;
	double y;
	private int dx;
	private int dy;
	private Image image1;
	private Image image2;
	private Image image3;
	private int stage; 

	public WaterSpell(int startX, int startY, int dx, int dy) {
		initialX = startX;
		initialY = startY;
		
		x = startX;
		y = startY;
		this.dx = dx;
		this.dy = dy;
		
		int scaledSize = (3 * Maze.MAZE_CELL_SIZE) / 4;
		image1 = (new ImageIcon("resources/water1.png")).getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
		image2 = (new ImageIcon("resources/water2.png")).getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
		image3 = (new ImageIcon("resources/water3.png")).getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
		
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
		return new Rectangle(getX(), getY(), getImage().getWidth(null), getImage().getHeight(null));
	}
	
	public void updatePosition() {
		x += dx * (double)Maze.MAZE_CELL_SIZE/32;
		y += dy * (double)Maze.MAZE_CELL_SIZE/32;
		
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
