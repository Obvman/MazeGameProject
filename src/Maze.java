import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

public class Maze implements MazeConstants{
	private MazeGenerator mazeGenerator;
	private boolean[][] mazeGrid;
	private Player player;
	private Image pathTile;
	private Image wallTile;
	
	public Maze() {
		// maze
		mazeGenerator = new MazeGenerator();
		mazeGrid = mazeGenerator.generateMaze(MAZE_SIZE);
		
		// player
		player = new Player();
		
		// tiles
		pathTile = (new ImageIcon("images/32_stone.png")).getImage();
		wallTile = (new ImageIcon("images/32_mountain.png")).getImage();
	}

	public boolean[][] getGrid() {
		return mazeGrid;
	}

	public Player getPlayer() {
		return player;
	}
	
	public Image getPathTile() {
		return pathTile;
	}
	
	public Image getWallTile() {
		return wallTile;
	}
	
	public void updateSprites(ActionEvent e) {
		if (isLegalMove(player.getDX(), player.getDY())) {
			player.move();
	    } else if (player.getDX() != 0 && player.getDY() != 0) {
	    	// player is holding two arrow keys so check if just activating one of the two makes the move legal
	    	if (isLegalMove(0, player.getDY())) {
	    		// move in Y-axis direction only
			    player.manualMove(0, player.getDY());
	    	} else if (isLegalMove(player.getDX(), 0)) {
			    // move in X-axis direction only
			    player.manualMove(player.getDX(), 0);
	    	}
	    }
	}

	private boolean isLegalMove(int dx, int dy) {
		for (int i = 0; i < mazeGrid.length; i++) {
			for (int j = 0; j < mazeGrid[i].length; j++) {
				if (mazeGrid[i][j] == false) {
					// wall 
					int xIndex = j * MAZE_CELL_SIZE;
					int yIndex = i * MAZE_CELL_SIZE;
					Rectangle wallRect = new Rectangle(xIndex, yIndex, MAZE_CELL_SIZE, MAZE_CELL_SIZE);

					// player
					int playerWidth = player.getImage().getWidth(null);
					int playerHeight = player.getImage().getHeight(null);
					Rectangle playerRect = new Rectangle(player.getX(), player.getY(), playerWidth, playerHeight);
					playerRect.translate(dx, dy);

					// check if wall and player intersection or if player outside of maze
					if (wallRect.intersects(playerRect) 
							|| playerRect.getX() < 0 
							|| playerRect.getX() >= mazeGrid[0].length * MAZE_CELL_SIZE - playerWidth
							|| playerRect.getY() < 0 
							|| playerRect.getY() >= mazeGrid.length * MAZE_CELL_SIZE - playerHeight) {
						return false;
					}
				} 
			}
		}
		
		return true;
	}
}