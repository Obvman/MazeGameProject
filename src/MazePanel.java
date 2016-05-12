import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class MazePanel extends JPanel implements ActionListener, MazeConstants {
	private Maze maze;
	private Timer timer;
	private final int REFRESH_TIME = 10;

	public MazePanel() {
		initMazePanel();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		requestFocus(); // TODO: fix this hack (disable focus for all other elements)

		maze.updateSprites(e);

		repaint();
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);

		int[][] mazeGrid = maze.getGrid();
		for (int i = 0; i < MAZE_SIZE_1; i++) {
			for (int j = 0; j < MAZE_SIZE_2; j++) {
				Image tile = null;
				switch (mazeGrid[i][j]) {
				case PATH_TILE: tile = maze.getPathTile(); break;
				case WALL_TILE: tile = maze.getWallTile(); break;
				case START_TILE: tile = maze.getStartTile(); break;
				case END_TILE: tile = maze.getEndTile(); break;
				};

				g.drawImage(tile, j * MAZE_CELL_SIZE, i * MAZE_CELL_SIZE, this);
				
				if (mazeGrid[i][j] == WALL_TILE) {
					// check whether there are adjacent wall tiles for improved graphics
					if (withinMaze(i, j-1) && mazeGrid[i][j-1] == WALL_TILE) {
						tile = (new ImageIcon("images/leon_wall_left_cover_lava.png")).getImage();
						g.drawImage(tile, j * MAZE_CELL_SIZE, i * MAZE_CELL_SIZE, this);
					}
					if (withinMaze(i-1, j) && mazeGrid[i-1][j] == WALL_TILE) {
						tile = (new ImageIcon("images/leon_wall_top_cover_lava.png")).getImage();
						g.drawImage(tile, j * MAZE_CELL_SIZE, i * MAZE_CELL_SIZE, this);
					}
					if (withinMaze(i, j+1) && mazeGrid[i][j+1] == WALL_TILE) {
						tile = (new ImageIcon("images/leon_wall_right_cover_lava.png")).getImage();
						g.drawImage(tile, j * MAZE_CELL_SIZE, i * MAZE_CELL_SIZE, this);
					}
					if (withinMaze(i+1, j) && mazeGrid[i+1][j] == WALL_TILE) {
						tile = (new ImageIcon("images/leon_wall_bottom_cover_lava.png")).getImage();
						g.drawImage(tile, j * MAZE_CELL_SIZE, i * MAZE_CELL_SIZE, this);
					}
				}
			}
		}

		// paint the player
		Player player = maze.getPlayer();
		g.drawImage(player.getImage(), player.getX(), player.getY(), this);
		
		// paint the monsters
		for (Monster m : maze.getMonsters()) {
			g.drawImage(m.getImage(), m.getX(), m.getY(), this);
		}
	}

	private boolean withinMaze(int x, int y) {
		return x >= 0 && y >= 0 && x < MAZE_SIZE_1 && y < MAZE_SIZE_2;
	}

	private void initMazePanel() {
		maze = new Maze();

		addKeyListener(new TAdapter());

		timer = new Timer(REFRESH_TIME, this);
		timer.start();
	}

	private class TAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			maze.getPlayer().keyPressed(e);
		}  

		@Override
		public void keyReleased(KeyEvent e) {
			maze.getPlayer().keyReleased(e);
		}


	}
}
