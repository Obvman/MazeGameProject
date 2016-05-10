import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class MazePanel extends JPanel implements ActionListener {
	private boolean[][] maze;
	private final int mazeSize = 31;
	private Player player;
	private Timer timer;
	
	// constants
	private final int MAZE_CELL_SIZE = 32;
	private final int REFRESH_TIME = 10;
	
	public MazePanel() {
		initMazePanel();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO: clean everything
		
		requestFocus(); // TODO: fix this hack
		
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
		
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		
		// paint the maze
		Image stoneTile = (new ImageIcon("src/images/32_stone.png")).getImage();
		Image fireTile = (new ImageIcon("src/images/32_mountain.png")).getImage();
		
		int size = MAZE_CELL_SIZE;
		for (int i=0; i<maze.length; i++) {
			for (int j=0; j<maze[0].length; j++) {
				if (maze[i][j] == true) {
					g.drawImage(stoneTile, j * size, i * size, this);
				} else{
					g.drawImage(fireTile, j * size, i * size, this);
				}
			}
		}
		
		// paint the player
		g.drawImage(player.getImage(), player.getX(), player.getY(), this);
	}
	
	private void initMazePanel() {
		MazeGenerator mazeGenerator = new MazeGenerator();
		maze = mazeGenerator.generateMaze(mazeSize);
		
		player = new Player();
		addKeyListener(new TAdapter());
		
		timer = new Timer(REFRESH_TIME, this);
		timer.start();
	}
	
	/**
	 * Checks whether a player will intersect with a maze wall based on where they want to move
	 * @param playerDX the wanted x-axis movement
	 * @param playerDY the wanted y-axis movement
	 * @return
	 */
	private boolean isLegalMove(int playerDX, int playerDY) {
		for (int i=0; i<maze.length; i++) {
			for (int j=0; j<maze[0].length; j++) {
				if (maze[i][j] == false) {
					// wall 
					int xIndex = j * MAZE_CELL_SIZE;
					int yIndex = i * MAZE_CELL_SIZE;
					Rectangle wallRect = new Rectangle(xIndex, yIndex, MAZE_CELL_SIZE, MAZE_CELL_SIZE);
					
					// player
					int playerWidth = player.getImage().getWidth(this);
					int playerHeight = player.getImage().getHeight(this);
					Rectangle playerRect = new Rectangle(player.getX(), player.getY(), playerWidth, playerHeight);
					playerRect.translate(playerDX, playerDY);
					
					// check if wall and player intersection or if player outside of maze
					if (wallRect.intersects(playerRect) 
							|| playerRect.getX() < 0 
							|| playerRect.getX() >= maze[0].length * MAZE_CELL_SIZE
							|| playerRect.getY() < 0 
							|| playerRect.getY() >= maze.length * MAZE_CELL_SIZE) {
						
						return false;
					}
				} 
			}
		}
		
		return true;
	}
	
	private class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
        }
    }
}
