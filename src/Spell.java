import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/**
 * Class for Spells, which are created by the player upon spellkey key press.
 * Spell can be either FIRE, WATER or AIR and is chosen by the user when gameplay starts.
 */
public class Spell implements MovableSprite {
	// Spell position variables
	private int initialX;
	private int initialY;
	private double x;
	private double y;
	private int dx;
	private int dy;
	
	private Image image[]; // Holds 3 different images for when spell is displayed
	private int scaledSize;

	/**
	 * Constructor for spell - makes a new spell object given start coordinates,
	 * 		spell type, and change in X and Y.
	 * @param startX Starting X coordinate
	 * @param startY Starting Y coordinate
	 * @param dx Change in X
	 * @param dy Change in Y
	 * @param spellType Type of spell i.e. (FIRE|WATER|AIR)
	 */
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

	/**
	 * Given X and Y coordinates, set the current location of the spell
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	@Override
	public void setPosition (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return Current X coordinate of spell
	 */
	@Override
	public int getX() {
		return (int)x;
	}

	/**
	 * @return Current Y coordinate of spell
	 */
	@Override
	public int getY() {
		return (int)y;
	}

	/**
	 * @return DX of spell
	 */
	@Override
	public int getDX() {
		return dx;
	}

	/**
	 * @return DY of spell
	 */
	@Override
	public int getDY() {
		return dy;
	}

	/**
	 * Gets the appropriate image given the stage the spell is at - spell is displayed
	 *    as moving away from the player sprite with 3 different images at 1 second intervals.
	 * @return image
	 */
	@Override
	public Image getImage() {
		return image[getStage() > 2 ? 2 : getStage()];
	}

	/**
	 * Gets rectangle for bounds of the Spell current location.
	 * @return rectangle
	 */
	@Override
	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), scaledSize, scaledSize);
	}

	/**
	 * Moves the location given X and Y values for the degree movement of the spell.
	 * @param dx DX coordinate
	 */
	@Override
	public void manualMove(int dx, int dy) {
		x += dx;
		y += dy;
	}
	
	/**
	 * Changes the position of the spell by 1/16 times the maze cell size each iteration.
	 */
	public void updatePosition() {
		x += dx * (double)Maze.MAZE_CELL_SIZE/16;
		y += dy * (double)Maze.MAZE_CELL_SIZE/16;
	}
	
	/**
	 * Gets the current stage of the spell (1, 2 or 3).
	 * @return stage
	 */
	public int getStage() {
		return (int)Math.sqrt(Math.pow(x - initialX, 2) + Math.pow(y - initialY, 2))/scaledSize;
	}
}
