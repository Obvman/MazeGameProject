import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class MazePanel extends JPanel implements ActionListener, MazeConstants {
	private Maze maze;
	private Timer timer; 
	private long startTime;
	private long duration;
	private int counter;
	
	// temp
	public Maze getMaze() {
		return maze;
	}
	
	public MazePanel() {
		initMazePanel();
		startTime = System.nanoTime();
	}

	public boolean isGameWon() {
		return maze.isGameWon();
	}
	
	public boolean isGameLost() {
		return maze.isGameLost();
	}
	
	public long getDuration() {
		return duration;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		requestFocus(); // TODO: fix this hack (disable focus for all other elements)

		maze.updateSprites(e);
		
		
		if (maze.isGameWon() || maze.isGameLost()) {
			return;
		} else {
			// update duration
			duration = System.nanoTime() - startTime;
		}
		
		

		repaint();
	}

	@Override
	protected void paintComponent(Graphics g){
//		super.paintComponent(g);

		int[][] mazeGrid = maze.getGrid();
		for (int i = 0; i < MAZE_SIZE_1; i++) {
			for (int j = 0; j < MAZE_SIZE_2; j++) {
				Image tile = null;
				switch (mazeGrid[i][j]) {
				case PATH_TILE: tile = maze.getPathTile(); break;
				case WALL_TILE: tile = maze.getWallTile(); break;
				case START_TILE: tile = maze.getStartTile(); break;
				case END_TILE: tile = maze.getEndTile(); break;
				default: tile = maze.getPathTile(); 
				};

				g.drawImage(tile, j * MAZE_CELL_SIZE, i * MAZE_CELL_SIZE, this);
				if (mazeGrid[i][j] == KEY_TILE) {
					g.drawImage(maze.getKeyTile(), j * MAZE_CELL_SIZE, i * MAZE_CELL_SIZE, this);
				}
				
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
		
		// paint the player's spells
		for (Spell s : player.getSpells()) {
			// TODO: remove hack to slow down spell
			counter++;
			if (counter % 15 == 0)
				s.updateStage();
			
			g.drawImage(s.getImage(), s.getX(), s.getY(), this);
			g.drawImage(s.getImage(), s.getX(), s.getY(), this); // TODO: remove 
			
			
			
		}
		 
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

		timer = new Timer(10, this);
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
