import java.awt.Image;
import java.awt.Rectangle;
import java.util.LinkedList;
import javax.swing.ImageIcon;

/**
 * The Portal class is responsible for spawning monsters and flying monsters in the maze game 
 */
public class Portal {
	
	/**
	 * Creates a Portal given the coordinates of the Portal and the LinkedList of exiting Maze Monsters to add to
	 * @param x the x coordinate the Portal
	 * @param y the y coordinate of the Portal
	 * @param the LinkedList of existing Maze Monsters to add to
	 */
	public Portal(int x, int y, LinkedList<Monster> monsters) {
		this.x = x;
		this.y = y;
		this.image = new ImageIcon("resources/blue_portal_32.gif").getImage()
					 .getScaledInstance(Maze.MAZE_CELL_SIZE, Maze.MAZE_CELL_SIZE, Image.SCALE_DEFAULT);
		this.monsters = monsters;
	}
	
	/**
	 * @return the x coordinate of the Portal
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * @return the y coordinate of the Portal
	 */
	public int getY() {
		return this.y;
	}
	
	/**
	 * @return the Image representing a Portal
	 */
	public Image getImage() {
		return this.image;
	}
	
	/**
	 * Spawns a Monster or FlyingMonster (chosen randomly) at the same position as the portal
	 */
	public void spawnMonster() {
		Monster m = Math.random() > 0.5 ? new Monster() : new FlyingMonster();
		m.setPosition(this.x, this.y);
		monsters.add(m);
	}
	
	/**
	 * Sets the position of the Portal
	 * @param x the new x coordinate of the Portal
	 * @param y the new y coordinate of the Portal
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the Rectangle representing the bounds of the Portal
	 */
	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), Maze.MAZE_CELL_SIZE, Maze.MAZE_CELL_SIZE);
	}
	
	private int x;
	private int y;
	private Image image;
	private LinkedList<Monster> monsters;
}
