import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Spell implements MovableSprite {

	public Spell(int startX, int startY, int dx, int dy, int spellType) {
		initialX = startX;
		initialY = startY;

		x = startX;
		y = startY;
		this.dx = dx;
		this.dy = dy;
		
		scaledSize = (3 * Maze.MAZE_CELL_SIZE) / 4;

		image = new Image[3];
		if (spellType == GameScreen.FIRE) {
			image[0] = (new ImageIcon("resources/spells/fire1.png")).getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
			image[1] = (new ImageIcon("resources/spells/fire2.png")).getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
			image[2] = (new ImageIcon("resources/spells/fire3.png")).getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
		} else if (spellType == GameScreen.WATER) {
			image[0] = (new ImageIcon("resources/spells/water1.png")).getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
			image[1] = (new ImageIcon("resources/spells/water2.png")).getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
			image[2] = (new ImageIcon("resources/spells/water3.png")).getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
		} else if (spellType == GameScreen.AIR) {
			image[0] = (new ImageIcon("resources/spells/air1.png")).getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
			image[1] = (new ImageIcon("resources/spells/air2.png")).getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
			image[2] = (new ImageIcon("resources/spells/air3.png")).getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
		}

	}

	@Override
	public void setPosition (int x, int y) {
		this.x = x;
		this.y = y;
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
	public Image getImage() {
		return image[getStage() > 2 ? 2 : getStage()];
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), scaledSize, scaledSize);
	}

	@Override
	public void manualMove(int dx, int dy) {
		x += dx;
		y += dy;
	}
	
	public void updatePosition() {
		x += dx * (double)Maze.MAZE_CELL_SIZE/16;
		y += dy * (double)Maze.MAZE_CELL_SIZE/16;
	}
	
	public int getStage() {
		return (int)Math.sqrt(Math.pow(x - initialX, 2) + Math.pow(y - initialY, 2))/scaledSize;
	}
	
	private int initialX;
	private int initialY;
	private double x;
	private double y;
	private int dx;
	private int dy;
	private Image image[];
	private int scaledSize;
}
