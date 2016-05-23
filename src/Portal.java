import java.awt.Image;
import java.awt.Rectangle;
import java.util.LinkedList;
import javax.swing.ImageIcon;


public class Portal {
	private int x;
	private int y;
	private Image image;
	private LinkedList<Monster> monsters;
	private int totalMonsters;
	private int monstersSpawned;
	private int numTimesShot;
	private LinkedList<Spell> spells;
	
	public Portal(int totalMonsters) {
		this.image = new ImageIcon("resources/portal.png").getImage()
					.getScaledInstance(Maze.MAZE_CELL_SIZE, Maze.MAZE_CELL_SIZE, Image.SCALE_SMOOTH);
		this.monsters = new LinkedList<Monster>();
		this.totalMonsters = totalMonsters;
		this.monstersSpawned = 0;
		this.numTimesShot = 0;
		this.spells = new LinkedList<Spell>();
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
	
	public boolean canSpawnMonster() {
		return (monstersSpawned < totalMonsters);
	}
	
	public boolean canKill(Spell s) {
		return (numTimesShot > 2) && (!spells.contains(s));
	}
	
	public void weaken(Spell s) {
		this.numTimesShot++;
		spells.add(s);
	}
	
	public Monster spawnMonster() {
		Monster m = new Monster();
		m.setPosition(this.x, this.y);
		monsters.add(m);
		monstersSpawned++;
		return m;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), Maze.MAZE_CELL_SIZE, Maze.MAZE_CELL_SIZE);
	}
}
