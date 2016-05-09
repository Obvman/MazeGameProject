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

import javax.swing.JPanel;

public class MazePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private boolean[][] maze;
	
	public MazePanel() {
		this.maze = null;
	}
	
	public void addMaze(boolean[][] maze) {
		this.maze = maze;
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		int xIndex = 0;
		int yIndex = 0;
		int size = 16;
		for(int i=0; i<maze[0].length; i++){
			for(int j=0; j<maze[1].length; j++){
				if(maze[i][j] == true){
					g.setColor(Color.white);
				}else{
					g.setColor(Color.black);
				}
				g.fillRect(xIndex, yIndex, size, size);
				xIndex = xIndex + size;
			}
			xIndex = 0;
			yIndex = yIndex + size;
		}
	}
	
}
