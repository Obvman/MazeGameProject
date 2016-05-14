import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.*;

public class Maze {
	// MAZE CONSTANTS:
	// maze configuration
	public static int MAZE_SIZE_1 = 15 /*25*/;
	public static int MAZE_SIZE_2 = 25 /*45*/;
	public static int MAZE_CELL_SIZE = 32;
	public static int DIFFICULTY = 3; // corresponds to number of monsters that spawn
	
	// types of tiles
	public static int PATH_TILE = 0;
	public static int WALL_TILE = 1;
	public static int START_TILE = 2;
	public static int END_TILE = 3;
	public static int KEY_TILE = 4;
	
	private MazeGenerator mazeGenerator;
	private int[][] mazeGrid;
	private Player player;
	private LinkedList<Monster> monsters;
	private Image pathTile;
	private Image wallTile;
	private Image startTile;
	private Image endTile;
	private Image keyTile;
	private boolean keyAcquired;
	private int keyX;
	private int keyY;
	private int counter;

	public Maze() {
		// maze
		mazeGenerator = new MazeGenerator();
		mazeGrid = mazeGenerator.generateMaze(MAZE_SIZE_1, MAZE_SIZE_2);

		// determine where key tile should go
		// TODO: improve this dodgy algorithm
		boolean keyTileFound = false;
		for (int row = MAZE_SIZE_1 - 1; row >= 0 && !keyTileFound; row--) {
			for (int col = MAZE_SIZE_2 - 1; col >= 0 && !keyTileFound; col--) {
				if (withinMaze(col, row) && mazeGrid[row][col] == PATH_TILE) {
					// check whether it has 3 surrounding wall tiles;
					int surroundingWalls = 0;
					if (withinMaze(row, col-1) && mazeGrid[row][col-1] == WALL_TILE) {
						surroundingWalls++;
					}
					if (withinMaze(row-1, col) && mazeGrid[row-1][col] == WALL_TILE) {
						surroundingWalls++;
					}
					if (withinMaze(row, col+1) && mazeGrid[row][col+1] == WALL_TILE) {
						surroundingWalls++;
					}
					if (withinMaze(row+1, col) && mazeGrid[row+1][col] == WALL_TILE) {
						surroundingWalls++;
					}

					if (surroundingWalls == 3) {
						mazeGrid[row][col] = KEY_TILE;
						keyX = col;
						keyY = row;
						keyTileFound = true;
					}
				}
			}
		}

		if (!keyTileFound) {
			keyX = -1;
			keyY = -1;
			keyAcquired = true;
		} 

		// keyAcquired = true; // temporary to test game

		// player
		player = new Player();

		// monsters
		// TODO: fix hardcode of 3 monsters
		monsters = new LinkedList<Monster>();
		Monster m1 = new Monster();
		m1.setPosition((MAZE_SIZE_2 - 1) * MAZE_CELL_SIZE, 
				(MAZE_SIZE_1 - 1) * MAZE_CELL_SIZE);
		Monster m2 = new Monster();
		m2.setPosition((MAZE_SIZE_2 - 1) * MAZE_CELL_SIZE, 0);
		Monster m3 = new Monster();
		m3.setPosition(0, (MAZE_SIZE_1 - 1) * MAZE_CELL_SIZE);
		monsters.add(m1);
		monsters.add(m2);
		monsters.add(m3);

		// tiles
		pathTile = (new ImageIcon("images/leon_path.png")).getImage();
		wallTile = (new ImageIcon("images/leon_wall_lava.png")).getImage();
		startTile = (new ImageIcon("images/leon_open_door.png")).getImage();
		endTile = (new ImageIcon("images/leon_closed_door.png")).getImage();
		keyTile = (new ImageIcon("images/key_for_32.png")).getImage();

		counter = 0;
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

	public Image getPathTile() {
		return pathTile;
	}

	public Image getWallTile() {
		return wallTile;
	}

	public Image getStartTile() {
		return startTile;
	}

	public Image getEndTile() {
		return endTile;
	}

	public Image getKeyTile() {
		return keyTile;
	}

	public boolean isGameLost() {
		return !player.isAlive();
	}

	public boolean isGameWon() {
		return keyAcquired && player.getX()/MAZE_CELL_SIZE == MAZE_SIZE_2 - 1 && player.getY()/MAZE_CELL_SIZE == MAZE_SIZE_1 - 1;
	}

	public void updateSprites(ActionEvent e) {
		// check if game finished
		if (isGameWon()) {
			return;
		}

		// update player

		// check if player has been killed from monsters
		Image playerImage = player.getImage();
		Rectangle playerRect = new Rectangle(player.getX(), player.getY(), playerImage.getWidth(null), playerImage.getHeight(null));
		for (Monster m: monsters) {
			Image monsterImage = m.getImage();
			Rectangle monsterRect = new Rectangle(m.getX(), m.getY(), monsterImage.getWidth(null), monsterImage.getHeight(null));
			
			if (playerRect.intersects(monsterRect)) {
				player.setAlive(false);
				return;
			}
		}
		
		// check if key picked up
		if (player.getX()/MAZE_CELL_SIZE == keyX && player.getY()/MAZE_CELL_SIZE == keyY) {
			keyAcquired = true;
			mazeGrid[keyY][keyX] = PATH_TILE;
		}

		// update movement
		if (isLegalMove(player, player.getDX(), player.getDY())) {
			player.move();
		} else if (player.getDX() != 0 && player.getDY() != 0) {
			// player is holding two arrow keys so check if just activating one of the two makes the move legal
			if (isLegalMove(player, 0, player.getDY())) {
				// move in Y-axis direction only
				player.manualMove(0, player.getDY());
			} else if (isLegalMove(player, player.getDX(), 0)) {
				// move in X-axis direction only
				player.manualMove(player.getDX(), 0);
			}
		}

		// update spell positions
		// TODO: remove hack to slow down spell
		counter++;
		for (Iterator<Spell> spellIter = player.getSpells().iterator(); spellIter.hasNext(); ) {
			Spell s = spellIter.next();
			// kind of a hack to prevent spell from going out of the maze
			int xDirection = 0;
			if (s.getDX() > 0) {
				xDirection = 1;
			} else if (s.getDX() < 0) {
				xDirection = -1;
			}
			
			int yDirection = 0;
			if (s.getDY() > 0) {
				yDirection = 1;
			} else if (s.getDY() < 0) {
				yDirection = -1;
			}
			
			if (!withinMaze((s.getX() + xDirection * s.getImage().getWidth(null))/MAZE_CELL_SIZE , 
					(s.getY() + yDirection * s.getImage().getHeight(null))/MAZE_CELL_SIZE)) {
				spellIter.remove();
			} else {
				s.updatePosition();
				if (counter % 15 == 0) {
					s.updateStage();
				}
			}
		}
		
		// check whether spells have killed monsters
		for (Iterator<Spell> spellIter = player.getSpells().iterator(); spellIter.hasNext(); ) {
			Spell s = spellIter.next();
			Image spellImage = s.getImage();
			Rectangle spellRect = new Rectangle(s.getX(), s.getY(), spellImage.getWidth(null), spellImage.getHeight(null));

			for (Iterator<Monster> monsterIter = monsters.iterator(); monsterIter.hasNext(); ) {
				Monster m = monsterIter.next();
				Image monsterImage = m.getImage();
				Rectangle monsterRect = new Rectangle(m.getX(), m.getY(), monsterImage.getWidth(null), monsterImage.getHeight(null));

				if (spellRect.intersects(monsterRect)) {
					spellIter.remove();
					monsterIter.remove();
					break;
				}
			}
		}

		// update monsters
		for (Monster m : monsters) {
			int cellX = m.getX() / MAZE_CELL_SIZE;
			int cellY = m.getY() / MAZE_CELL_SIZE;
			boolean[][] solution = solveMaze(cellY, cellX, player.getY() / MAZE_CELL_SIZE, 
					player.getX() / MAZE_CELL_SIZE);

			int nextX = 0, nextY = 0;
			for (int i = cellX - 1; i <= cellX + 1; i++) {
				for (int j = cellY - 1; j <= cellY + 1 ; j++) {
					if ((i == cellX || j == cellY) && !(i == cellX && j == cellY)
							&& i >= 0 && j >= 0
							&& i < MAZE_SIZE_2 && j < MAZE_SIZE_1
							&& solution[j][i]) {
						nextX = i;
						nextY = j;
					}
				}
			}

			if (nextX != cellX) {
				// check if left or right
				int dx = nextX - cellX;


				if (isLegalMove(m, dx, 0)) {
					m.manualMove(dx, 0);
				} else if (isLegalMove(m, 0, 1)){
					// stuck so move up
					m.manualMove(0, -1);
				}
			} else if (nextY != cellY) {
				int dy = nextY - cellY;

				if (isLegalMove(m, 0, dy)) {
					m.manualMove(0, dy);
				} else if (isLegalMove(m, -1, 0)) {
					// stuck so left
					m.manualMove(-1, 0);
				}
			}
		}
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

	private boolean withinMaze(int y, int x) {
		return x >= 0 && y >= 0 && x < MAZE_SIZE_2 && y < MAZE_SIZE_1;
	}

	private boolean isLegalMove(MovableSprite sprite, int dx, int dy) {
		for (int i = 0; i < mazeGrid.length; i++) {
			for (int j = 0; j < mazeGrid[i].length; j++) {
				if (mazeGrid[i][j] == WALL_TILE) {
					// wall 
					int xIndex = j * MAZE_CELL_SIZE;
					int yIndex = i * MAZE_CELL_SIZE;
					Rectangle wallRect = new Rectangle(xIndex, yIndex, MAZE_CELL_SIZE, MAZE_CELL_SIZE);

					// sprite
					int spriteWidth = sprite.getImage().getWidth(null);
					int spriteHeight = sprite.getImage().getHeight(null);
					Rectangle spriteRect = new Rectangle(sprite.getX(), sprite.getY(), spriteWidth, spriteHeight);
					spriteRect.translate(dx, dy);

					if (wallRect.intersects(spriteRect) 
							|| spriteRect.getX() < 0 
							|| spriteRect.getX() >= mazeGrid[0].length * MAZE_CELL_SIZE - spriteWidth
							|| spriteRect.getY() < 0 
							|| spriteRect.getY() >= mazeGrid.length * MAZE_CELL_SIZE - spriteHeight) {
						return false;
					}
				} 
			}
		}

		return true;
	}




}