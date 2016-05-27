import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * The MazePanel is responsible for rendering graphics using the Maze as a model
 */
@SuppressWarnings("serial")
public class MazePanel extends JPanel implements ActionListener {

	/**
	 * Creates a new MazePanel with a given level, difficulty, spell type, and key binding
	 * @param level the level of the Maze
	 * @param difficulty the difficulty of the Maze
	 * @param spellType the spellType of the Player
	 * @param keys The key bindings corresponding the Player's controls
	 */
	public MazePanel(int level, int difficulty, int spellType, int[] keys) {
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		addKeyListener(new TAdapter());

		maze = new Maze(level, difficulty, spellType, keys);
		tileGenerator = new TileGenerator();
		gameSpeedTimer = new Timer(20, this);
		gameSpeedTimer.start();
		portalTimer = new Timer(8000, this);
		portalTimer.start();
	}

	/**
	 * @return the Maze associated with the MazePanel
	 */
	public Maze getMaze() {
		return maze;
	}

	/**
	 * Sets whether the game is running (updating) or not
	 * @param isRunning true if the game is running else false
	 */
	public void setRunning(boolean isRunning) {
		if (isRunning && !gameSpeedTimer.isRunning()) {
			gameSpeedTimer.start();
			portalTimer.start();
		} else if (!isRunning && gameSpeedTimer.isRunning()){
			maze.getPlayer().releaseKeys();
			gameSpeedTimer.stop();
			portalTimer.stop();
		}
	}

	/**
	 * Updates the Maze every timer tick
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		requestFocusInWindow();
		maze.updateGame();
		if (e.getSource() == portalTimer) {
			maze.activatePortals();
		}
		repaint();
	}

	/**
	 * Paints the elements of the Maze including path tiles, wall tiles, 
	 * key tiles, gems, Portals, the Player, Spells, and Monsters 
	 */
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);

		int[][] mazeGrid = maze.getGrid();
		for (int i = 0; i < mazeGrid.length; i++) {
			for (int j = 0; j < mazeGrid[i].length; j++) {

				int x = j * Maze.MAZE_CELL_SIZE;
				int y = i * Maze.MAZE_CELL_SIZE;

				Image tile = null;

				if (mazeGrid[i][j] == Maze.PATH_TILE) tile = tileGenerator.getPathTile();
				else if (mazeGrid[i][j] == Maze.WALL_TILE) tile = tileGenerator.getWallTile();
				else if (mazeGrid[i][j] == Maze.START_TILE) tile = tileGenerator.getStartTile();
				else if (mazeGrid[i][j] == Maze.END_TILE) tile = tileGenerator.getEndTile();
				else if (mazeGrid[i][j] == Maze.KEY_TILE) tile = tileGenerator.getKeyTile(); 
				else tile = tileGenerator.getPathTile();

				g.drawImage(tile, x, y, this);
				
				// overlay gem on top of the tiles
				if (mazeGrid[i][j] == Maze.GEM_TILE) {
					Image gemImage = tileGenerator.getGemImage(x, y);
					g.drawImage(gemImage, x + (Maze.MAZE_CELL_SIZE/2 - gemImage.getWidth(this)/2), 
									      y + (Maze.MAZE_CELL_SIZE/2), this);
				}

				// connect wall tile graphics for enhanced UX
				if (mazeGrid[i][j] == Maze.WALL_TILE) {
					if (withinMaze(j, i-1) && mazeGrid[i-1][j] == Maze.WALL_TILE) {
						g.drawImage(tileGenerator.getWallTileN(), x, y, this);
					}

					if (withinMaze(j-1, i) && mazeGrid[i][j-1] == Maze.WALL_TILE) {
						g.drawImage(tileGenerator.getWallTileW(), x, y, this);
					}

					if (withinMaze(j, i+1) && mazeGrid[i+1][j] == Maze.WALL_TILE) {
						g.drawImage(tileGenerator.getWallTileS(), x, y, this);
					}

					if (withinMaze(j+1, i) && mazeGrid[i][j+1] == Maze.WALL_TILE) {
						g.drawImage(tileGenerator.getWallTileE(), x, y, this);
					}
				}
			}
		}

		// paint the portals
		for (Portal p : maze.getPortals()) {
			g.drawImage(p.getImage(), p.getX(), p.getY(), this);
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

	/**
	 * Helper function to check whether a coordinate is within the Maze grid
	 * @param x the x coordinate to check
	 * @param y the y coordinate to check
	 * @return true if the coordinate is within the Maze grid else false
	 */
	private boolean withinMaze(int x, int y) {
		return x >= 0 && y >= 0 && x < maze.getGrid()[0].length && y < maze.getGrid().length;
	}

	/**
	 *	Private class to allow KeyEvents to update the player in the Maze
	 */
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
	
	private Maze maze;
	private TileGenerator tileGenerator;
	private Timer gameSpeedTimer;
	private Timer portalTimer;
}
