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

		Image pathTile = maze.getPathTile();
		Image wallTile = maze.getWallTile();

		boolean[][] mazeGrid = maze.getGrid();
		for (int i = 0; i < mazeGrid.length; i++) {
			for (int j = 0; j < mazeGrid[i].length; j++) {
				if (mazeGrid[i][j]) {
					g.drawImage(pathTile, j * MAZE_CELL_SIZE, i * MAZE_CELL_SIZE, this);
				} else{
					g.drawImage(wallTile, j * MAZE_CELL_SIZE, i * MAZE_CELL_SIZE, this);
				}
			}
		}

		// paint the player
		Player player = maze.getPlayer();
		g.drawImage(player.getImage(), player.getX(), player.getY(), this);
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
