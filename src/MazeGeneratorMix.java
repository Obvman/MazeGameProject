import java.util.Random;

public class MazeGeneratorMix implements MazeGenerator {
	private int[][] myMaze; 
	
	@Override
	public int[][] generateMaze(int mazeSize1, int mazeSize2) {
		this.myMaze = new int[mazeSize1][mazeSize2];
		for (int row = 0; row < mazeSize1; row++) {
			for (int col = 0; col < mazeSize2; col++) {
				this.myMaze[row][col] = Maze.PATH_TILE;
			}
		}
		//insert 2 walls right in the middle of the maze
		int midX = mazeSize1/2;
		for(int i = 0; i<mazeSize2; i++){
			myMaze[midX][i] = Maze.WALL_TILE;
		}
		int midY = mazeSize2/2;
		for(int i = 0; i<mazeSize1; i++){
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
		mazeTranslate(midX+1, mazeSize1-1, 0, midY-1);
		mazeTranslate(midX+1, mazeSize1-1, midY+1, mazeSize2-1);
		mazeTranslate(0, midX-1, midY+1, mazeSize2-1);
		
		this.myMaze[0][0] = Maze.START_TILE;
		this.myMaze[mazeSize1 - 1][mazeSize2 - 1] = Maze.END_TILE;
		
		return myMaze;
	}
	
	private void mazeTranslate(int xstart, int xend, int ystart, int yend){
		int mazeSize1 = xend-xstart+1;
		int mazeSize2 = yend-ystart+1;
		int[][] cell = new MazeGeneratorDFS().generateMaze(mazeSize1,mazeSize2);
		for (int i = 0; i<mazeSize1; i++){
			for(int j = 0; j<mazeSize2; j++){
				this.myMaze[i+xstart][j+ystart] = cell[i][j];
			}
		}
	}

}
