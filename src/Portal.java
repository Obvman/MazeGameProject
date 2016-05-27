import java.awt.Image;
import java.awt.Rectangle;
import java.util.LinkedList;
import javax.swing.ImageIcon;

/**
 * Class for Game portals, which are scattered across the maze screen and
 * 	   spawn monsters and flying monsters at certain intervals. Portals can
 *     be killed to stop them from releasing monsters.
 */
public class Portal {
	private int x;
	private int y;
	private Image image;
	private LinkedList<Monster> monsters;
	
	/**
	 * Constructor for Portal - makes a new Portal with coordinates x and y and list monsters.
	 * @param x X coordinate for portal
	 * @param y Y coordinate for portal
	 * @param monsters
	 */
	public Portal(int x, int y, LinkedList<Monster> monsters) {
		this.x = x;
		this.y = y;
		this.image = new ImageIcon("resources/blue_portal_32.gif").getImage()
					 .getScaledInstance(Maze.MAZE_CELL_SIZE, Maze.MAZE_CELL_SIZE, Image.SCALE_DEFAULT);
		this.monsters = monsters;
	}
	
	/**
	 * Get X coordinate for Portal.
	 * @return X coordinate
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * Get Y coordinate for Portal.
	 * @return Y coordinate
	 */
	public int getY() {
		return this.y;
	}
	
	/**
	 * Gets image sprite for the Portal.
	 * @return image
	 */
	public Image getImage() {
		return this.image;
	}
	
	/**
	 * Spawns a new monster from the portal. Randomised 50/50 chance of the spawned
	 *     monster to be a normal monster, or a flying monster.
	 */
	public void spawnMonster() {
		Monster m = Math.random() > 0.5 ? new Monster() : new FlyingMonster();
		m.setPosition(this.x, this.y);
		monsters.add(m);
	}
	
	/**
	 * Sets the position of the portal given x and y coordinates.
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets rectangle for bounds of the image location.
	 * @return rectangle
	 */
	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), Maze.MAZE_CELL_SIZE, Maze.MAZE_CELL_SIZE);
	}
}
