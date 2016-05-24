import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class Maze {
	// MAZE CONSTANTS
	// maze configuration
	
	public static final int MAZE_CELL_SIZE = 32;

	// TODO: enum?
	// types of tiles
	public static final int PATH_TILE = 0;
	public static final int WALL_TILE = 1;
	public static final int START_TILE = 2;
	public static final int END_TILE = 3;
	public static final int KEY_TILE = 4;
	public static final int GEM_TILE = 5;
	public static final int PORTAL_TILE = 6;

	// GAME CONFIGURATION
	// maze and characters
	
	private int MAZE_SIZE_1;
    private int MAZE_SIZE_2;
    
	private MazeGenerationStrategy mazeGenerator;
	private int[][] mazeGrid;
	private Player player;
	private LinkedList<Portal> portals;
	private LinkedList<Monster> monsters;
	private int maxMonsters;
	private boolean keyAcquired;
	
	// game stats
	private int numMonstersKilled;
	private int numGemsCollected;

	public Maze(int level, int difficulty, int spellType) {
		// the determine the height and width based on the level
		// TODO: clean this 
		// TODO: figure out maximum size given the window size
		if (level < 3) {
			MAZE_SIZE_1 = 21 - 4 * (3 - level);
			MAZE_SIZE_2 = 37 - 8 * (3 - level);
		} else {
			MAZE_SIZE_1 = 21;
			MAZE_SIZE_2 = 37;
		}
		
		// maze
		mazeGenerator = new MazeGenerateDfs();
		mazeGrid = mazeGenerator.generateMaze(MAZE_SIZE_1, MAZE_SIZE_2);

		player = new Player(spellType);
		
		// place key
		while (true) {
			int keyX = (int) (Math.random() * (MAZE_SIZE_2 - 1));
			int keyY = (int) (Math.random() * (MAZE_SIZE_1 - 1));
			if (keyX > 0.33 * MAZE_SIZE_2 && keyY > 0.33 * MAZE_SIZE_1 && mazeGrid[keyY][keyX] == PATH_TILE) {
				mazeGrid[keyY][keyX] = KEY_TILE;
				break;
			}
		}
		
		// place gems
		int numGemsPlaced = 0;
		while (numGemsPlaced < (MAZE_SIZE_1 * MAZE_SIZE_2)/50) {
			int gemX = (int) (Math.random() * (MAZE_SIZE_2 - 1));
			int gemY = (int) (Math.random() * (MAZE_SIZE_1 - 1));
			if (mazeGrid[gemY][gemX] == PATH_TILE) {
				mazeGrid[gemY][gemX] = GEM_TILE;
				numGemsPlaced++;
			}
		}
		
		// place portals
		monsters = new LinkedList<Monster>();
		portals = new LinkedList<Portal>();
		for (int i = 0; i < 2 * (level + difficulty); i++) {
			boolean placed = false;
			while (!placed) {
				int portalX = (int) (Math.random() * (MAZE_SIZE_2 - 1));
				int portalY = (int) (Math.random() * (MAZE_SIZE_1 - 1));
				
				double distance = Math.sqrt(portalX*portalX + portalY*portalY);
				if (distance > 0.33 * MAZE_SIZE_2 && mazeGrid[portalY][portalX] == PATH_TILE) {
					portals.add(new Portal(portalX * MAZE_CELL_SIZE, portalY * MAZE_CELL_SIZE, monsters));
					placed = true;
				}
			}
		}
		
		// place starting random monsters
		for (int i = 0; i < 2 * (level + difficulty); i++) {
			boolean placed = false;
			while (!placed) {
				int monsterX = (int) (Math.random() * (MAZE_SIZE_2 - 1));
				int monsterY = (int) (Math.random() * (MAZE_SIZE_1 - 1));
				
				double distance = Math.sqrt(monsterX*monsterX + monsterY*monsterY);
				if (distance > 0.33 * MAZE_SIZE_2 && mazeGrid[monsterY][monsterX] == PATH_TILE) {
					Monster m = Math.random() > 0.5 ? new Monster() : new FlyingMonster();
					m.setPosition(monsterX * MAZE_CELL_SIZE, monsterY * MAZE_CELL_SIZE);
					monsters.add(m);
					placed = true;
				}
			}
		}
		
		maxMonsters = 6 * (level + difficulty);
	}
	
	public void addMonster(Monster m) {
		this.monsters.add(m);
	}
	
	public LinkedList<Portal> getPortals() {
		return this.portals;
	}
	
	public boolean getKey(){
		return keyAcquired;
	}

	public int[][] getGrid() {
		return mazeGrid;
	}

	public Player getPlayer() {
		return player;
	}

	public LinkedList<Monster> getMonsters() {
		return monsters;
	}
	
	public boolean isKeyAcquired(){
		return keyAcquired;
	}
	
	public int getNumMonstersKilled() {
		return numMonstersKilled;
	}
	
	public int getNumGemsCollected() {
		return numGemsCollected;
	}
	
	public int getScore() {
		return 100 * getNumMonstersKilled() + 50 * getNumGemsCollected();
	}

	public boolean isGameLost() {
		return !player.isAlive();
	}

	public boolean isGameWon() {
		return keyAcquired && player.getX()/MAZE_CELL_SIZE == MAZE_SIZE_2 - 1 && player.getY()/MAZE_CELL_SIZE == MAZE_SIZE_1 - 1;
	}
	
	public void activatePortals() {
		for (Portal p : portals) {
			if (monsters.size() > maxMonsters) {
				break;
			}
			p.spawnMonster();
		}
	}

	public void updateSprites(ActionEvent e) {
		int playerCellX = player.getX() / MAZE_CELL_SIZE;
		int playerCellY = player.getY() / MAZE_CELL_SIZE;

		// check if player has been killed from monsters
		for (Monster m: monsters) {
			if (m.getBounds().intersects(player.getBounds())) {
				player.setAlive(false);
				return;
			}
		}

		for (Iterator<Spell> spellIter = player.getSpells().iterator(); spellIter.hasNext(); ) {
			// check whether spells have killed monsters
			boolean canKillPortal = true;
			Spell s = spellIter.next();
			for (Iterator<Monster> monsterIter = monsters.iterator(); monsterIter.hasNext(); ) {
				Monster m = monsterIter.next();
				if (s.getBounds().intersects(m.getBounds())) {
					numMonstersKilled++;
					spellIter.remove();
					monsterIter.remove();
					canKillPortal = false;
					break;
				}
			}
			
			// check whether spells have killed portals
			if (!canKillPortal) break;
			for (Iterator<Portal> portalIter = portals.iterator(); portalIter.hasNext(); ) {
				Portal p = portalIter.next();
				if (s.getBounds().intersects(p.getBounds())) {
					spellIter.remove();
					portalIter.remove();
					portals.remove(p);
					break;
				}
			}
		}
		

		// TODO: make it smoother pickup
		
		// check if key picked up
		if (mazeGrid[playerCellY][playerCellX] == KEY_TILE) {
			keyAcquired = true;
			mazeGrid[playerCellY][playerCellX] = PATH_TILE;
		}
		
		// check if gem picked up
		if (mazeGrid[playerCellY][playerCellX] == GEM_TILE) {
			numGemsCollected++;
			playSound("resources/sound/gem.wav");
			mazeGrid[playerCellY][playerCellX] = PATH_TILE;
		}

		// update player position
		if (isLegalMove(player, player.getDX(), player.getDY())) {
			player.manualMove(player.getDX(), player.getDY());
		} else if (player.getDX() != 0 && player.getDY() != 0) {
			// player is holding two arrow keys so check if just activating one of the two makes the move legal
			if (isLegalMove(player, 0, player.getDY())) {
				player.manualMove(0, player.getDY()); // move in Y-axis direction only
			} else if (isLegalMove(player, player.getDX(), 0)) {
				player.manualMove(player.getDX(), 0); // move in X-axis direction only
			}
		}
		// update spell positions
		for (Iterator<Spell> spellIter = player.getSpells().iterator(); spellIter.hasNext(); ) {
			Spell s = spellIter.next();

			if (!withinMaze(s.getY()/MAZE_CELL_SIZE, s.getX()/MAZE_CELL_SIZE)) {
				spellIter.remove();
			} else {
				s.updatePosition();
			}
		}

		// update monster positions
		for (Monster m : monsters) {
			int monsterCellX = m.getX() / MAZE_CELL_SIZE;
			int monsterCellY = m.getY() / MAZE_CELL_SIZE;

			if ((Math.abs(monsterCellX-playerCellX) > 0.25 * MAZE_SIZE_2 
					&& Math.abs(monsterCellY-playerCellY) > 0.25 * MAZE_SIZE_1) || m instanceof FlyingMonster) {
				// random movement
				// TODO: improve algorithm

				if (isLegalMove(m, m.getDX(), m.getDY()) && !(m.getDX() == 0 && m.getDY() == 0)) {
					// continue in straight line
					m.manualMove(m.getDX(), m.getDY());
				} else {
					while (true) {
						m.randomiseDirection();
						if (isLegalMove(m, m.getDX(), m.getDY())) {
							m.manualMove(m.getDX(), m.getDY());
							break;
						}
					}
				}
			} else if (!(m instanceof FlyingMonster)) {
				// TODO: fix coordinates order is reversed
				boolean[][] pathToPlayer = solveMaze(monsterCellY, monsterCellX, playerCellY, playerCellX);

				int nextCellX = monsterCellX;
				int nextCellY = monsterCellY;
				if (withinMaze(monsterCellY + 1, monsterCellX) && pathToPlayer[monsterCellY + 1][monsterCellX]) {
					nextCellY += 1;
				} else if (withinMaze(monsterCellY, monsterCellX - 1) && pathToPlayer[monsterCellY][monsterCellX - 1]) {
					nextCellX -= 1;
				} else if (withinMaze(monsterCellY - 1, monsterCellX) && pathToPlayer[monsterCellY - 1][monsterCellX]) {
					nextCellY -= 1;
				} else {
					nextCellX += 1;
				}

				if (nextCellX != monsterCellX) {
					// move horizontally
					int dx = nextCellX > monsterCellX ? 1 : -1;

					if (isLegalMove(m, dx, 0)) {
						m.manualMove(dx, 0);
					} else if (isLegalMove(m, 0, -1)){
						// stuck so move up
						m.manualMove(0, -1);
					}
				} else if (nextCellY != monsterCellY) {
					// move vertically
					int dy = nextCellY  > monsterCellY ? 1 : -1;

					if (isLegalMove(m, 0, dy)) {
						m.manualMove(0, dy);
					} else if (isLegalMove(m, -1, 0)) {
						// stuck so left
						m.manualMove(-1, 0);
					}
				} else {
					m.manualMove(-1, -1);
				}
			}
		}
	}

	private boolean withinMaze(int y, int x) {
		return x >= 0 && y >= 0 && x < MAZE_SIZE_2 && y < MAZE_SIZE_1;
	}

	private boolean isLegalMove(MovableSprite sprite, int dx, int dy) {
		Rectangle spriteRect = sprite.getBounds();
		spriteRect.translate(dx, dy);
		
		if (spriteRect.getX() < 0 || spriteRect.getX() >= MAZE_SIZE_2 * MAZE_CELL_SIZE - spriteRect.getWidth()
			|| spriteRect.getY() < 0 || spriteRect.getY() >= MAZE_SIZE_1 * MAZE_CELL_SIZE - spriteRect.getHeight()) {
			return false;
		}
		
		if (sprite instanceof FlyingMonster) {
			return true;
		}
		
		for (int i = 0; i < MAZE_SIZE_1; i++) {
			for (int j = 0; j < MAZE_SIZE_2; j++) {
				if (mazeGrid[i][j] == WALL_TILE) {
					// wall 
					Rectangle wallRect = new Rectangle(j * MAZE_CELL_SIZE, i * MAZE_CELL_SIZE, MAZE_CELL_SIZE, MAZE_CELL_SIZE);

					if (wallRect.intersects(spriteRect)) {
						return false;
					}
				} 
			}
		}

		return true;
	}

	public boolean[][] solveMaze(int startX, int startY, int goalX, int goalY) {
		boolean[][] visited = new boolean[MAZE_SIZE_1][MAZE_SIZE_2];
		boolean[][] solution = new boolean[MAZE_SIZE_1][MAZE_SIZE_2];

		for (int row = 0; row < MAZE_SIZE_1; row++) {
			for (int col = 0; col < MAZE_SIZE_2; col++) {
				visited[row][col] = false;
				solution[row][col] = false;
			}
		}

		recursiveSolve(startX, startY, goalX, goalY, visited, solution);
		return solution;
	}

	private boolean recursiveSolve(int x, int y, int goalX, int goalY, boolean[][] visited, boolean[][] solution) {
		if (x == goalX && y == goalY) {
			solution[x][y] = true;
			return true; // If you reached the end
		}

		if (mazeGrid[x][y] == WALL_TILE || visited[x][y]) return false;  
		// If you are on a wall or already were here

		visited[x][y] = true;
		if (x != 0) // Checks if not on left edge
			if (recursiveSolve(x-1, y, goalX, goalY, visited, solution)) { // Recalls method one to the left
				solution[x][y] = true; // Sets that path value to true;
				return true	;
			}

		if (x != MAZE_SIZE_1 - 1) // Checks if not on right edge
			if (recursiveSolve(x+1, y, goalX, goalY, visited, solution)) { // Recalls method one to the right
				solution[x][y] = true;
				return true;
			}

		if (y != 0)  // Checks if not on top edge
			if (recursiveSolve(x, y-1, goalX, goalY, visited, solution)) { // Recalls method one up
				solution[x][y] = true;
				return true;
			}

		if (y != MAZE_SIZE_2 - 1) // Checks if not on bottom edge
			if (recursiveSolve(x, y+1, goalX, goalY, visited, solution)) { // Recalls method one down
				solution[x][y] = true;
				return true;
			}

		return false;
	}
	
	private void playSound(String soundName) {
		System.out.println("Playing for the boys");
	       try 
	       {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	       }
	       catch(Exception ex){
	    	   System.out.println("Exception for the boys");
	       }
	}
}