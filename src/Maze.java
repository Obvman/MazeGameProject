import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.LinkedList;

import javax.swing.*;

public class Maze implements MazeConstants{
	private MazeGenerator mazeGenerator;
	private int[][] mazeGrid;
	private Player player;
	private LinkedList<Monster> monsters;
	private Image pathTile;
	private Image wallTile;
	private Image startTile;
	private Image endTile;
	private int counter;

	public Maze() {
		// maze
		mazeGenerator = new MazeGenerator();
		mazeGrid = mazeGenerator.generateMaze(MAZE_SIZE);

		// player
		player = new Player();

		// monsters
		monsters = new LinkedList<Monster>();
		Monster m1 = new Monster();
		m1.setPosition((MAZE_SIZE - 1) * MAZE_CELL_SIZE, (MAZE_SIZE - 1) * MAZE_CELL_SIZE);
		Monster m2 = new Monster();
		m2.setPosition((MAZE_SIZE - 1) * MAZE_CELL_SIZE, 0);
		Monster m3 = new Monster();
		m3.setPosition(0, (MAZE_SIZE - 1) * MAZE_CELL_SIZE);
		monsters.add(m1);
		monsters.add(m2);
		monsters.add(m3);
		
		// tiles
		pathTile = (new ImageIcon("images/leon_path.png")).getImage();
		wallTile = (new ImageIcon("images/leon_wall_lava.png")).getImage();
		startTile = (new ImageIcon("images/leon_open_door.png")).getImage();
		endTile = (new ImageIcon("images/leon_closed_door.png")).getImage();
		
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

	public void updateSprites(ActionEvent e) {
		// update player
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

		// TODO: remove this hack to half speed of monsters
		counter++;
		if (counter % 2 == 0) {
			return;
		}

		// update monsters
		for (Monster m : monsters) {
			int cellX = m.getX()/MAZE_CELL_SIZE;
			int cellY = m.getY()/MAZE_CELL_SIZE;
			boolean[][] solution = solveMaze(cellY, cellX, player.getY()/MAZE_CELL_SIZE, player.getX()/MAZE_CELL_SIZE);

			int nextX = 0, nextY = 0;
			for (int i = cellX - 1; i <= cellX + 1; i++) {
				for (int j = cellY - 1; j <= cellY + 1 ; j++) {
					if ((i == cellX || j == cellY) && !(i == cellX && j == cellY)
							&& i >= 0 && j >= 0
							&& i < MAZE_SIZE && j < MAZE_SIZE
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
		boolean[][] visited = new boolean[MAZE_SIZE][MAZE_SIZE];
		boolean[][] solution = new boolean[MAZE_SIZE][MAZE_SIZE];

		for (int row = 0; row < MAZE_SIZE; row++) {
			for (int col = 0; col < MAZE_SIZE; col++) {
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

		if (x != MAZE_SIZE - 1) // Checks if not on right edge
			if (recursiveSolve(x+1, y, goalX, goalY, visited, solution)) { // Recalls method one to the right
				solution[x][y] = true;
				return true;
			}

		if (y != 0)  // Checks if not on top edge
			if (recursiveSolve(x, y-1, goalX, goalY, visited, solution)) { // Recalls method one up
				solution[x][y] = true;
				return true;
			}

		if (y != MAZE_SIZE - 1) // Checks if not on bottom edge
			if (recursiveSolve(x, y+1, goalX, goalY, visited, solution)) { // Recalls method one down
				solution[x][y] = true;
				return true;
			}

		return false;
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