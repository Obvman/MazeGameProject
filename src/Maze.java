import java.awt.Rectangle;
import java.util.Iterator;
import java.util.LinkedList;

public class Maze {

	/**
	 * Generates a new maze where the size is dependent on the level and
	 * the number of enemies and portals is dependent on both the size and difficulty.
	 * @param level The current level of the game (affects maze size)
	 * @param difficulty The selected difficulty of the game (affects 
	 * number of enemies, portals, and score)
	 * and the score
	 * @param spellType The selected spell appearance for the game (water, fire, air)
	 * @param keys The key bindings corresponding the Player's controls
	 */
	public Maze(int level, int difficulty, int spellType, int[] keys) {
		// set maze height and width according to level
		mazeHeight = 21 - 4 * (level < 3 ? 3 - level : 0);
		mazeWidth = 37 - 8 * (level < 3 ? 3 - level : 0);

		// generate maze
		mazeGenerator = new MazeGeneratorDFS();
		mazeGrid = mazeGenerator.generateMaze(mazeHeight, mazeWidth);

		// spawn player
		player = new Player(spellType, keys);

		// place key
		while (true) {
			int keyX = (int) (Math.random() * (mazeWidth - 1));
			int keyY = (int) (Math.random() * (mazeHeight - 1));
			if (keyX > 0.33 * mazeWidth && keyY > 0.33 * mazeHeight && mazeGrid[keyY][keyX] == PATH_TILE) {
				mazeGrid[keyY][keyX] = KEY_TILE;
				break;
			}
		}

		// place gems
		int numGemsPlaced = 0;
		while (numGemsPlaced < (mazeHeight * mazeWidth) / 50) {
			int gemX = (int) (Math.random() * (mazeWidth - 1));
			int gemY = (int) (Math.random() * (mazeHeight - 1));
			if (mazeGrid[gemY][gemX] == PATH_TILE) {
				mazeGrid[gemY][gemX] = GEM_TILE;
				numGemsPlaced++;
			}
		}

		// place portals
		monsters = new LinkedList<Monster>();
		portals = new LinkedList<Portal>();
		for (int i = 0; i < (mazeHeight * mazeWidth) * difficulty / 100; i++) {
			boolean placed = false;
			while (!placed) {
				int portalX = (int) (Math.random() * (mazeWidth - 1));
				int portalY = (int) (Math.random() * (mazeHeight - 1));

				double distance = Math.sqrt(portalX*portalX + portalY*portalY);
				if (distance > 0.33 * mazeWidth && mazeGrid[portalY][portalX] == PATH_TILE) {
					portals.add(new Portal(portalX * MAZE_CELL_SIZE, portalY * MAZE_CELL_SIZE, monsters));
					placed = true;
				}
			}
		}

		// set max monsters
		maxMonsters = (mazeHeight * mazeWidth) * difficulty / 50 ;

		// spawn monsters at the start of game
		activatePortals();

	}

	/**
	 * @return LinkedList of all the Portals in the game
	 */
	public LinkedList<Portal> getPortals() {
		return this.portals;
	}

	/**
	 * @return true if the Player has picked up the key otherwise false
	 */
	public boolean getKey(){
		return keyAcquired;
	}

	/**
	 * @return 2D int array representing the maze where the value of a cell indicates
	 * the type of tile it is (refer to this class' tile constants)
	 */
	public int[][] getGrid() {
		return mazeGrid;
	}

	/**
	 * @return the Player object in the game
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return LinkedList of Monsters in the game
	 */
	public LinkedList<Monster> getMonsters() {
		return monsters;
	}

	/**
	 * @return the number of Monsters the Player has killed
	 */
	public int getNumMonstersKilled() {
		return numMonstersKilled;
	}

	/**
	 * @return the number of gems the Player has collected
	 */
	public int getNumGemsCollected() {
		return numGemsCollected;
	}

	/**
	 * @return The current round score calculated by 100*(Monsters killed) + 50*(gems collected)
	 */
	public int getScore() {
		return 100 * getNumMonstersKilled() + 50 * getNumGemsCollected();
	}

	/**
	 * Indicates whether the round has been lost
	 * @return true if Player is dead, false if player is alive
	 */
	public boolean isGameLost() {
		return !player.isAlive();
	}

	/**
	 * Indicates whether the round has been successfully completed
	 * @return true if the Player has collected the key and is at the exit tile
	 */
	public boolean isGameWon() {
		return keyAcquired && player.getX()/MAZE_CELL_SIZE == mazeWidth - 1 && player.getY()/MAZE_CELL_SIZE == mazeHeight - 1;
	}

	/**
	 * Activates every Portal to spawn a new Monster in the game
	 */
	public void activatePortals() {
		for (Portal p : portals) {
			if (monsters.size() > maxMonsters) {
				break;
			}

			p.spawnMonster();
		}
	}

	/**
	 * Refreshes the state of the game including checking whether the player has been killed,
	 * whether spells have killed monsters, whether spells have destroyed portals,
	 * whether key has been picked up, whether a gem has been picked up, and
	 * updating player and monster positions
	 */
	public void updateGame() {
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
			boolean monsterKilled = false;
			Spell s = spellIter.next();
			for (Iterator<Monster> monsterIter = monsters.iterator(); monsterIter.hasNext(); ) {
				Monster m = monsterIter.next();
				if (s.getBounds().intersects(m.getBounds())) {
					numMonstersKilled++;
					spellIter.remove();
					monsterIter.remove();
					monsterKilled = true;
					break;
				}
			}

			// check whether spells have killed portals
			if (monsterKilled) break;
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

		// check if key picked up
		if (mazeGrid[playerCellY][playerCellX] == KEY_TILE) {
			keyAcquired = true;
			mazeGrid[playerCellY][playerCellX] = PATH_TILE;
		}

		// check if gem picked up
		if (mazeGrid[playerCellY][playerCellX] == GEM_TILE) {
			numGemsCollected++;
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

			if (!withinMaze(s.getX()/MAZE_CELL_SIZE, s.getY()/MAZE_CELL_SIZE)) {
				spellIter.remove();
			} else {
				s.updatePosition();
			}
		}

		// update monster positions
		for (Monster m : monsters) {
			int monsterCellX = m.getX() / MAZE_CELL_SIZE;
			int monsterCellY = m.getY() / MAZE_CELL_SIZE;

			int distanceToPlayer = (int) Math.sqrt(Math.pow(monsterCellX-playerCellX, 2) + Math.pow(monsterCellY-playerCellY, 2));
			if ((distanceToPlayer > mazeWidth/3) || m.canFly()) {
				// random movement

				if (isLegalMove(m, m.getDX(), m.getDY()) && !(m.getDX() == 0 && m.getDY() == 0)) {
					// continue in straight line
					m.manualMove(m.getDX(), m.getDY());
				} else {
					// choose a random direction
					while (true) {
						m.randomiseDirection();
						if (isLegalMove(m, m.getDX(), m.getDY())) {
							m.manualMove(m.getDX(), m.getDY());
							break;
						}
					}
				}
			} else {
				// chase player
				
				boolean[][] pathToPlayer = solveMaze(monsterCellX, monsterCellY, playerCellX, playerCellY);

				int nextCellX = monsterCellX;
				int nextCellY = monsterCellY;
				if (withinMaze(monsterCellX, monsterCellY + 1) && pathToPlayer[monsterCellY + 1][monsterCellX]) {
					nextCellY += 1;
				} else if (withinMaze(monsterCellX - 1, monsterCellY) && pathToPlayer[monsterCellY][monsterCellX - 1]) {
					nextCellX -= 1;
				} else if (withinMaze(monsterCellX, monsterCellY - 1) && pathToPlayer[monsterCellY - 1][monsterCellX]) {
					nextCellY -= 1;
				} else if (withinMaze(monsterCellX + 1, monsterCellY) && pathToPlayer[monsterCellY][monsterCellX + 1]){
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

	/**
	 * Checks whether a coordinate is a valid position in the maze grid
	 * @param x the x coordinate to check
	 * @param y the y coordinate to check
	 * @return true if the coordinate is within the maze grid
	 */
	private boolean withinMaze(int x, int y) {
		return x >= 0 && y >= 0 && x < mazeWidth && y < mazeHeight;
	}

	/**
	 * Checks whether a MovableSprite can perform a given x-axis and y-axis movement
	 * @param sprite The MovableSprite to check
	 * @param dx the x-axis movement value of the MovableSprite
	 * @param dy the y-axis movement value of the MovableSprite
	 * @return true if the movement results in a valid position in the maze grid
	 * (within the maze and on a path tile)
	 */
	private boolean isLegalMove(MovableSprite sprite, int dx, int dy) {
		Rectangle spriteRect = sprite.getBounds();
		spriteRect.translate(dx, dy);

		if (spriteRect.getX() < 0 || spriteRect.getX() >= mazeWidth * MAZE_CELL_SIZE - spriteRect.getWidth()
				|| spriteRect.getY() < 0 || spriteRect.getY() >= mazeHeight * MAZE_CELL_SIZE - spriteRect.getHeight() - 1) {
			return false;
		}

		if (sprite instanceof FlyingMonster) {
			return true;
		}

		for (int i = 0; i < mazeHeight; i++) {
			for (int j = 0; j < mazeWidth; j++) {
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

	/**
	 * Finds a path between two coordinates in the maze grid
	 * @param startX the x coordinate of the starting location
	 * @param startY the y coordinate of the starting location
	 * @param goalX the x coordinate of the goal location
	 * @param goalY the y coordinate of the goal location
	 * @return 2D boolean array where a true value indicates that the 
	 * cell is in the solution path from the start to the goal coordinate
	 */
	public boolean[][] solveMaze(int startX, int startY, int goalX, int goalY) {
		boolean[][] visited = new boolean[mazeHeight][mazeWidth];
		boolean[][] solution = new boolean[mazeHeight][mazeWidth];

		for (int row = 0; row < mazeHeight; row++) {
			for (int col = 0; col < mazeWidth; col++) {
				visited[row][col] = false;
				solution[row][col] = false;
			}
		}

		recursiveSolve(startX, startY, goalX, goalY, visited, solution);
		return solution;
	}

	/**
	 * Recursively finds a path between two coordinates in the maze grid 
	 * @param startX the x coordinate of the starting location
	 * @param startY the y coordinate of the starting location
	 * @param goalX the x coordinate of the goal location
	 * @param goalY the y coordinate of the goal location
	 * @param visited 2D boolean array where a true value indicates that the cell has already been visited
	 * @param solution 2D boolean array where a true value indicates that the 
	 * cell is in the solution path from the start to the goal coordinate
	 * @return true if the current cell is part of the solution path otherwise false
	 */
	private boolean recursiveSolve(int x, int y, int goalX, int goalY, boolean[][] visited, boolean[][] solution) {
		// check if goal coordinate reached
		if (x == goalX && y == goalY) {
			solution[y][x] = true;
			return true; 
		}

		// check if current coordinate is a wall or has already been visited
		if (mazeGrid[y][x] == WALL_TILE || visited[y][x]) {
			return false;  
		}

		// mark current coordinate as visited
		visited[y][x] = true;
		
		// recursively check left cell
		if (x != 0) // Checks if not on left edge
			if (recursiveSolve(x-1, y, goalX, goalY, visited, solution)) {
				solution[y][x] = true; // Sets that path value to true;
				return true	;
			}

		// recursively check right cell
		if (x != mazeWidth - 1) // Checks if not on right edge
			if (recursiveSolve(x+1, y, goalX, goalY, visited, solution)) {
				solution[y][x] = true;
				return true;
			}

		// recursively check above cell
		if (y != 0)  // Checks if not on top edge
			if (recursiveSolve(x, y-1, goalX, goalY, visited, solution)) { 
				solution[y][x] = true;
				return true;
			}

		// recursively check below cell
		if (y != mazeHeight - 1) 
			if (recursiveSolve(x, y+1, goalX, goalY, visited, solution)) { 
				solution[y][x] = true;
				return true;
			}

		// current coordinate does not lead to the goal coordinate
		return false;
	}

	// public constants for the maze
	public static final int MAZE_CELL_SIZE = 32;
	public static final int PATH_TILE = 0;
	public static final int WALL_TILE = 1;
	public static final int START_TILE = 2;
	public static final int END_TILE = 3;
	public static final int KEY_TILE = 4;
	public static final int GEM_TILE = 5;
	public static final int PORTAL_TILE = 6;

	// game configuration
	private int mazeHeight;
	private int mazeWidth;
	private int maxMonsters;

	// game elements
	private MazeGenerator mazeGenerator;
	private int[][] mazeGrid;
	private Player player;
	private LinkedList<Portal> portals;
	private LinkedList<Monster> monsters;

	// game statistics
	private int numMonstersKilled;
	private int numGemsCollected;
	private boolean keyAcquired;	
}