/**
 * Maze Panel 
 * Contains graphical representation of the maze
 * 
 * COMP2911 Project - 16s1
 * @author Anna Azzam
 * @author Charlotte Han
 * @author Connor Coyne
 * @author Craig Feeney
 * @author Leon Nguyen
 * 
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

public class MazePanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private boolean[][] maze;
	private MazePlayer player;
	private Timer timer; // tick rate... 
	private final int DELAY = 10; // dunno what this does lol
	
	public MazePanel() {
		initMazePanel();
	}
	
	private void initMazePanel() {
		maze = null;
		addKeyListener(new TAdapter());
		setFocusable(true); // duno what this does
		player = new MazePlayer();
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public void addMaze(boolean[][] maze) {
		this.maze = maze;
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g); // dunno what this does
		
		// paint the maze
		int size = 16; // HARDCODED
		for(int i=0; i<maze.length; i++){
			for(int j=0; j<maze[0].length; j++){
				if(maze[i][j] == true){
					g.setColor(Color.white);
				}else{
					g.setColor(Color.black);
				}
				
				g.fillRect(j * size, i * size, size, size);
			}
		}
		
		
		// paint the player
		g.drawImage(player.getImage(), player.getX(), player.getY(), this);
		Toolkit.getDefaultToolkit().sync(); // dunno what this does
	}


	/**
	 * Trigger every DELAY ms (refer to timer above)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		requestFocus(); // NOICE! fixes the glitchy shiz switching screens
		// TODO: OPTIMISATION
		boolean intersecting = false;
		for(int i=0; i<maze.length; i++){
			for(int j=0; j<maze[0].length; j++){
				if (maze[i][j] == false) {
					// wall
					int xIndex = j * 16; // THIS 16 iS HARDCODED... AND SO AS OTHERS
					int yIndex = i * 16; // HARDCODED TOO I THINK
					
					Rectangle wallRect = new Rectangle(xIndex, yIndex, 16, 16); // HARDCODE
					Rectangle playerRect = new Rectangle(player.getX(), player.getY(), 8, 8); // HARDCODE
					playerRect.translate(player.getDX(), player.getDY());
					
					if (wallRect.intersects(playerRect)) {
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
	
	/**
	 * Smooooth movement ~
	 */
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
