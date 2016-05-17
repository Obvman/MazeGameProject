import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class MazePanel extends JPanel implements ActionListener {
	private Maze maze;
	private Timer timer; 

	// images of tiles
	// TODO: create a tile generator class
	private Image pathTile;
	private Image wallTile;
	private Image startTile;
	private Image endTile;
	private Image keyTile;

	public MazePanel() {
		maze = new Maze();

		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		// tiles
		pathTile = (new ImageIcon("resources/leon_path.png")).getImage();
		wallTile = (new ImageIcon("resources/leon_wall_lava.png")).getImage();
		startTile = (new ImageIcon("resources/leon_open_door.png")).getImage();
		endTile = (new ImageIcon("resources/leon_closed_door.png")).getImage();
		keyTile = (new ImageIcon("resources/key_for_32.png")).getImage();

		addKeyListener(new TAdapter());

		timer = new Timer(10, this); // corresponds to game speed
		timer.start();
	}

	public Maze getMaze() {
		return maze;
	}

	public void setRunning(boolean isRunning) {
		if (isRunning && !timer.isRunning()) {
			timer.start();
		} else if (!isRunning && timer.isRunning()){
			timer.stop();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		requestFocusInWindow();
		maze.updateSprites(e);
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);

		int[][] mazeGrid = maze.getGrid();
		for (int i = 0; i < Maze.MAZE_SIZE_1; i++) {
			for (int j = 0; j < Maze.MAZE_SIZE_2; j++) {
				Image tile = null;

				if (mazeGrid[i][j] == Maze.PATH_TILE) tile = pathTile;
				else if (mazeGrid[i][j] == Maze.WALL_TILE) tile = wallTile;
				else if (mazeGrid[i][j] == Maze.START_TILE) tile = startTile;
				else if (mazeGrid[i][j] == Maze.END_TILE) tile = endTile;
				else tile = pathTile;

				// key
				g.drawImage(tile, j * Maze.MAZE_CELL_SIZE, i * Maze.MAZE_CELL_SIZE, this);
				if (mazeGrid[i][j] == Maze.KEY_TILE) {
					g.drawImage(keyTile, j * Maze.MAZE_CELL_SIZE, i * Maze.MAZE_CELL_SIZE, this);
				}

				if (mazeGrid[i][j] == Maze.WALL_TILE) {
					// check whether there are adjacent wall tiles for improved graphics
					if (withinMaze(i, j-1) && mazeGrid[i][j-1] == Maze.WALL_TILE) {
						tile = (new ImageIcon("resources/leon_wall_left_cover_lava.png")).getImage();
						g.drawImage(tile, j * Maze.MAZE_CELL_SIZE, i * Maze.MAZE_CELL_SIZE, this);
					}
					if (withinMaze(i-1, j) && mazeGrid[i-1][j] == Maze.WALL_TILE) {
						tile = (new ImageIcon("resources/leon_wall_top_cover_lava.png")).getImage();
						g.drawImage(tile, j * Maze.MAZE_CELL_SIZE, i * Maze.MAZE_CELL_SIZE, this);
					}
					if (withinMaze(i, j+1) && mazeGrid[i][j+1] == Maze.WALL_TILE) {
						tile = (new ImageIcon("resources/leon_wall_right_cover_lava.png")).getImage();
						g.drawImage(tile, j * Maze.MAZE_CELL_SIZE, i * Maze.MAZE_CELL_SIZE, this);
					}
					if (withinMaze(i+1, j) && mazeGrid[i+1][j] == Maze.WALL_TILE) {
						tile = (new ImageIcon("resources/leon_wall_bottom_cover_lava.png")).getImage();
						g.drawImage(tile, j * Maze.MAZE_CELL_SIZE, i * Maze.MAZE_CELL_SIZE, this);
					}
				}
			}
		}

		// paint the player
		Player player = maze.getPlayer();
		g.drawImage(player.getImage(), player.getX(), player.getY(), this);

		// paint the player's spells
		for (Spell s : player.getSpells()) {
			g.drawImage(s.getImage(), s.getX(), s.getY(), this);
		}

		// paint the monsters
		for (Monster m : maze.getMonsters()) {
			g.drawImage(m.getImage(), m.getX(), m.getY(), this);
		}
	}

	private boolean withinMaze(int x, int y) {
		return x >= 0 && y >= 0 && x < Maze.MAZE_SIZE_1 && y < Maze.MAZE_SIZE_2;
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
