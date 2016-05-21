import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class MazePanel extends JPanel implements ActionListener {
	private Maze maze;
	private TileGenerator tileGenerator;
	private Timer timer; 

	public MazePanel(int level, int difficulty, int spellType) {
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		addKeyListener(new TAdapter());
		
		maze = new Maze(level, difficulty, spellType);
		tileGenerator = new TileGenerator();
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
		for (int i = 0; i < mazeGrid.length; i++) {
			for (int j = 0; j < mazeGrid[i].length; j++) {
				
				int x = j * Maze.MAZE_CELL_SIZE;
				int y = i * Maze.MAZE_CELL_SIZE;
				
				Image tile = null;

				if (mazeGrid[i][j] == Maze.PATH_TILE) tile = tileGenerator.getPathTile();
				else if (mazeGrid[i][j] == Maze.WALL_TILE) tile = tileGenerator.getWallTile();
				else if (mazeGrid[i][j] == Maze.START_TILE) tile = tileGenerator.getStartTile();
				else if (mazeGrid[i][j] == Maze.END_TILE) tile = tileGenerator.getEndTile();
				else tile = tileGenerator.getPathTile();

				// key
				g.drawImage(tile, x, y, this);
				if (mazeGrid[i][j] == Maze.KEY_TILE) {
					g.drawImage(tileGenerator.getKeyImage(), x, y, this);
				} else if (mazeGrid[i][j] == Maze.GEM_TILE) {
					g.drawImage(tileGenerator.getGemImage(x, y), x, y, this);
				}

				// create the illusion of connected wall tiles
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
		return x >= 0 && y >= 0 && x < maze.getGrid()[0].length && y < maze.getGrid().length;
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
