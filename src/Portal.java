import java.awt.Image;
import java.awt.Rectangle;
import java.util.LinkedList;
import javax.swing.ImageIcon;


public class Portal {
	private int x;
	private int y;
	private Image image;
	private LinkedList<Monster> monsters;
	
	public Portal(int x, int y, LinkedList<Monster> monsters) {
		this.x = x;
		this.y = y;
		this.image = new ImageIcon("resources/blue_portal_32.gif").getImage()
					 .getScaledInstance(Maze.MAZE_CELL_SIZE, Maze.MAZE_CELL_SIZE, Image.SCALE_DEFAULT);
		this.monsters = monsters;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public Image getImage() {
		return this.image;
	}
	
	public void spawnMonster() {
		Monster m = Math.random() > 0.5 ? new Monster() : new FlyingMonster();
		m.setPosition(this.x, this.y);
		monsters.add(m);
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), Maze.MAZE_CELL_SIZE, Maze.MAZE_CELL_SIZE);
	}
}
