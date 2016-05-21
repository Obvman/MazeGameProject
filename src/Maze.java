import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.*;

public class Maze {
	// MAZE CONSTANTS
	// maze configuration
	public static final int MAZE_SIZE_1 = 15 /*25*/;
	public static final int MAZE_SIZE_2 = 35 /*45*/;
	public static final int MAZE_CELL_SIZE = 32;

	// types of tiles
	public static final int PATH_TILE = 0;
	public static final int WALL_TILE = 1;
	public static final int START_TILE = 2;
	public static final int END_TILE = 3;
	public static final int KEY_TILE = 4;

	// GAME CONFIGURATION
	// maze and characters
	private MazeGenerationStrategy mazeGenerator;
	private int[][] mazeGrid;
	private Player player;
	private LinkedList<Monster> monsters;
	private boolean keyAcquired;

	public Maze() {
		// maze
		mazeGenerator = new MazeGenerateDfs();
		mazeGrid = mazeGenerator.generateMaze(MAZE_SIZE_1, MAZE_SIZE_2); // TODO: place this somewhere else

		// determine where key tile should go
		// TODO: make sure it is not within a radius of start and end tile
		while (true) {
			int keyX = (int) (Math.random() * (MAZE_SIZE_2 - 1));
			int keyY = (int) (Math.random() * (MAZE_SIZE_1 - 1));
			if (mazeGrid[keyY][keyX] == PATH_TILE) {
				mazeGrid[keyY][keyX] = KEY_TILE;
				break;
			}
		}

		// player
		player = new Player();

		monsters = new LinkedList<Monster>();
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

	public void spawnMonsters(int numMonsters) {
		for (int i = 0; i < numMonsters*5; i++) {
			boolean placed = false;
			while (!placed) {
				int monsterX = (int) (Math.random() * (MAZE_SIZE_2 - 1));
				int monsterY = (int) (Math.random() * (MAZE_SIZE_1 - 1));

				if (monsterX > 5 && monsterY > 5 && mazeGrid[monsterY][monsterX] == PATH_TILE) {
					
					Monster m = Math.random() > 0.5 ? new FlyingMonster() : new Monster();
					m.setPosition(monsterX * MAZE_CELL_SIZE, monsterY * MAZE_CELL_SIZE);
					monsters.add(m);
					placed = true;
				}
			}
		}
	}

	public boolean isGameLost() {
		return !player.isAlive();
	}

	public boolean isGameWon() {
		return keyAcquired && player.getX()/MAZE_CELL_SIZE == MAZE_SIZE_2 - 1 && player.getY()/MAZE_CELL_SIZE == MAZE_SIZE_1 - 1;
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

		// check whether spells have killed monsters
		for (Iterator<Spell> spellIter = player.getSpells().iterator(); spellIter.hasNext(); ) {
			Spell s = spellIter.next();
			for (Iterator<Monster> monsterIter = monsters.iterator(); monsterIter.hasNext(); ) {
				Monster m = monsterIter.next();
				if (s.getBounds().intersects(m.getBounds())) {
					spellIter.remove();
					monsterIter.remove();
					break;
				}
			}
		}

		// check if key picked up
		if (mazeGrid[playerCellY][playerCellX] == KEY_TILE) {
			keyAcquired = true;
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

			double distance = Math.sqrt(Math.pow(monsterCellX-playerCellX,2) 
					+ Math.pow(monsterCellY-playerCellY, 2));

			if (distance > Math.sqrt(Math.pow(MAZE_SIZE_1, 2) + Math.pow(MAZE_SIZE_2, 2))/2 || m.canFly()) {
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
			} else if (!m.canFly()) {
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
		
		if (sprite.canFly()) {
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
}