import java.awt.Image;
import java.io.File;
import java.util.LinkedList;
import javax.swing.ImageIcon;


public class Portal {
	private int x;
	private int y;
	private Image image;
	private LinkedList<Monster> monsters;
	private int totalMonsters;
	private int monstersSpawned;
	
	public Portal(int totalMonsters) {
		this.image = new ImageIcon("resources/monster_up.png").getImage();
		this.monsters = new LinkedList<Monster>();
		this.totalMonsters = totalMonsters;
		this.monstersSpawned = 0;
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
}
