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
	private final int MAZE_CELL_SIZE = 16;
	private final int REFRESH_TIME = 10;
	
	public MazePanel() {
		initMazePanel();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO: clean everything
		
		requestFocus(); // TODO: Fix this hack
		boolean intersecting = false;
		for(int i=0; i<maze.length; i++){
			for(int j=0; j<maze[0].length; j++){
				if (maze[i][j] == false) {
					// wall
					int xIndex = j * MAZE_CELL_SIZE; // TODO: remove hardcode
					int yIndex = i * MAZE_CELL_SIZE; // TODO: remove hardcode
					
					Rectangle wallRect = new Rectangle(xIndex, yIndex, MAZE_CELL_SIZE, MAZE_CELL_SIZE); // TODO: remove hardcode
					int playerWidth = player.getImage().getWidth(this);
					int playerHeight = player.getImage().getHeight(this);
					Rectangle playerRect = new Rectangle(player.getX(), player.getY(), playerWidth, playerHeight); // TODO: remove hardcode
					playerRect.translate(player.getDX(), player.getDY());
					
					if (wallRect.intersects(playerRect) 
							|| playerRect.getX() < 0 
							|| playerRect.getX() >= maze[0].length * MAZE_CELL_SIZE
							|| playerRect.getY() < 0 
							|| playerRect.getY() >= maze.length * MAZE_CELL_SIZE) {
						
						intersecting = true;
					}
				} 
			}
		}
		
		if (!intersecting) {
			player.move();
		} 
		
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		
		// paint the maze
		int size = MAZE_CELL_SIZE; // TODO: remove hardcode
		for (int i=0; i<maze.length; i++) {
			for (int j=0; j<maze[0].length; j++) {
				if (maze[i][j] == true) {
					g.setColor(Color.white);
				} else{
					g.setColor(Color.black);
				}
				
				g.fillRect(j * size, i * size, size, size);
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
