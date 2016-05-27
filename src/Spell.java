import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Spell implements MovableSprite {
	/**
	 * Creates a Spell given its initial coordinates, movement direction, and type
	 * @param startX the starting x coordinate of the Spell
	 * @param startY the starting y coordinate of the Spell
	 * @param dx the x-axis movement value of the Spell
	 * @param dy the y-axis movement value of the Spell
	 * @param spellType the type of the Spell
	 */
	public Spell(int startX, int startY, int dx, int dy, int spellType) {
		// movement
		initialX = startX;
		initialY = startY;
		x = startX;
		y = startY;
		this.dx = dx;
		this.dy = dy;
		
		// load images
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
	
	/**
	 * Updates the position of the Spell based on its current movement direction
	 */
	public void updatePosition() {
		x += dx * (double)Maze.MAZE_CELL_SIZE/16;
		y += dy * (double)Maze.MAZE_CELL_SIZE/16;
	}
	
	/**
	 * @return the current stage of the spell which indicates how far it is from its starting position
	 */
	public int getStage() {
		return (int)Math.sqrt(Math.pow(x - initialX, 2) + Math.pow(y - initialY, 2))/scaledSize;
	}
	
	// movement
	private int initialX;
	private int initialY;
	private double x;
	private double y;
	private int dx;
	private int dy;
	
	// images
	private Image image[];
	private int scaledSize;
}
