import java.util.Random;

public class MazeGenerator implements MazeConstants {
	private int[][] maze;

	/**
	 * This method takes an integer mazeSize as input and 
	 * returns a generated maze.
	 * The input must be a power of 2-1(2^n -1) to do recursive 
	 * generate.
	 * @param mazeSize
	 * @return
	 */
	public int[][] generateMaze(int mazeSize1, int mazeSize2){
		if (this.maze == null) {
			this.maze = new int[mazeSize1][mazeSize2];

			for (int row = 0; row < mazeSize1; row++) {
				for (int col = 0; col < mazeSize2; col++) {
					this.maze[row][col] = PATH_TILE;
				}
			}

			generateMazeRecursive(0, mazeSize1,0 , mazeSize2);
		}

		// set start and end tile
		maze[0][0] = START_TILE;
		maze[mazeSize1 - 1][mazeSize2 - 1] = END_TILE;

		return this.maze;
	}

	/**
	 * A recursive maze generator
	 * @param xStart
	 * @param xEnd
	 * @param yStart
	 * @param yEnd
	 * @param mazeSize
	 */
	private void generateMazeRecursive(int xStart, int xEnd, int yStart, int yEnd){
		if(xEnd - xStart > 2) {
			//This will insert 4 walls in the middle of the matrix
			if(yEnd - yStart>2){
				for(int i = 0; i<yEnd-yStart; i++){
					this.maze[xStart + (xEnd-xStart)/2][i+yStart]= WALL_TILE;
				}
			}
			for(int i = 0; i<xEnd-xStart; i++){
				this.maze[i+xStart][yStart + (yEnd-yStart)/2]= WALL_TILE;
			}
			/* This will dig 3 holes on the 3 of the 4 walls we generated.
			 * 0 -> the top wall; 1 -> the left wall; 2 -> the bottom wall;
			 * 3 -> the right wall
			 */
			int ignore = new Random().nextInt(4);
			int newHole;
			for(int i = 0; i<4 ; i++){
				if (ignore == i){
					continue;
				}
				switch (i){
					case 0:
						//this if statement makes sure we won't dig a hole in where a wall might generate
						//since all walls will be generated in an even index (0, 2, 4...)
						if(yEnd - yStart > 3){
							newHole = new Random().nextInt((yEnd-yStart)/4)*2;
						}else{
							newHole = 0;
						}
						this.maze[xStart + (xEnd-xStart)/2][yStart + newHole] = PATH_TILE;
						break;
					case 1:
						if(xEnd - xStart > 3){
							newHole = new Random().nextInt((xEnd-xStart)/4)*2;
						}else{
							newHole = 0;
						}
						this.maze[xStart + newHole][yStart + (yEnd-yStart)/2] = PATH_TILE;
						break;
					case 2:
						if(yEnd - yStart > 3){
							newHole = new Random().nextInt((yEnd-yStart)/4)*2;
						}else{
							newHole = 0;
						}
						this.maze[xStart + (xEnd-xStart)/2][yStart + newHole+(yEnd-yStart)/2 +1] = PATH_TILE;
						break;
					case 3:
						if(xEnd - xStart > 3){
							newHole = new Random().nextInt((xEnd-xStart)/4)*2;
						}else{
							newHole = 0;
						}
						this.maze[newHole + (xEnd-xStart)/2 +1 + xStart][yStart + (yEnd-yStart)/2] = PATH_TILE;
						break;
				}
			}
			if((xEnd-xStart > 3) && (yEnd-yStart > 3)){
				generateMazeRecursive(xStart, xStart + (xEnd-xStart)/2, yStart, yStart + (yEnd-yStart)/2);
				generateMazeRecursive(xStart + (xEnd-xStart)/2+1, xEnd, yStart, yStart + (yEnd-yStart)/2);
				generateMazeRecursive(xStart + (xEnd-xStart)/2+1, xEnd, yStart + (yEnd-yStart)/2+1, yEnd);
				generateMazeRecursive(xStart, xStart + (xEnd-xStart)/2, yStart + (yEnd-yStart)/2+1, yEnd);
			}
		}
	}
}
