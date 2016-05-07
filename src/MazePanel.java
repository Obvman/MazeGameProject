import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class MazePanel extends JPanel {
	private boolean[][] maze;
	public MazePanel() {
		init();
		this.maze = null;
	}
	
	public void addMaze(boolean[][] maze) {
		this.maze = maze;
	}
	
	private void init() {
		
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		int xIndex = 0;
		int yIndex = 0;
		int size = 5;
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
