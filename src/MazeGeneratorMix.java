import java.util.Random;

public class MazeGeneratorMix implements MazeGenerator {
	private int[][] myMaze; 
	
	@Override
	public int[][] generateMaze(int height, int width) {
		this.myMaze = new int[height][width];
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				this.myMaze[row][col] = Maze.PATH_TILE;
			}
		}
		//insert 2 walls right in the middle of the maze
		int midX = height/2;
		for(int i = 0; i<width; i++){
			myMaze[midX][i] = Maze.WALL_TILE;
		}
		int midY = width/2;
		for(int i = 0; i<height; i++){
			myMaze[i][midY] = Maze.WALL_TILE;
		}
		//dig holes
		int ignore = new Random().nextInt(4);
		int newHole;
		for(int i = 0; i < 4 ; i++) { 
			if (ignore == i) {
				continue;
			}
			switch (i) {
				case 0:
					newHole = new Random().nextInt(midX/2);
					this.myMaze[2*newHole][midY] = Maze.PATH_TILE;
					break;
				case 1:
					newHole = new Random().nextInt(midX/2);
					this.myMaze[2*newHole+midX+1][midY] = Maze.PATH_TILE;
					break;
				case 2:
					newHole = new Random().nextInt(midY/2);
					this.myMaze[midX][2*newHole] = Maze.PATH_TILE;
					break;
				case 3:
					newHole = new Random().nextInt(midY/2);
					this.myMaze[midX][2*newHole+midY+1] = Maze.PATH_TILE;
					break;
			}
		}
		
		mazeTranslate(0, midX-1, 0, midY-1);
		mazeTranslate(midX+1, height-1, 0, midY-1);
		mazeTranslate(midX+1, height-1, midY+1, width-1);
		mazeTranslate(0, midX-1, midY+1, width-1);
		
		this.myMaze[0][0] = Maze.START_TILE;
		this.myMaze[height - 1][width - 1] = Maze.END_TILE;
		
		return myMaze;
	}
	
	private void mazeTranslate(int xstart, int xend, int ystart, int yend){
		int height = xend-xstart+1;
		int width = yend-ystart+1;
		int[][] cell = new MazeGeneratorDFS().generateMaze(height,width);
		for (int i = 0; i<height; i++){
			for(int j = 0; j<width; j++){
				this.myMaze[i+xstart][j+ystart] = cell[i][j];
			}
		}
	}

}
