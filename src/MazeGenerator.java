import java.util.Random;

public class MazeGenerator {
	private boolean[][] maze;

	/**
	 * This method takes an integer mazeSize as input and 
	 * returns a generated maze.
	 * The input must be a power of 2-1(2^n -1) to do recursive 
	 * generate.
	 * @param mazeSize
	 * @return
	 */
	public boolean[][] generateMaze(int mazeSize){
		if (this.maze == null) {
			this.maze = new boolean[mazeSize][mazeSize];

			for (int row = 0; row < mazeSize; row++) {
				for (int col = 0; col < mazeSize; col++) {
					this.maze[row][col] = true;
				}
			}

			generateMazeRecursive(0, mazeSize,0 , mazeSize, mazeSize);
		}

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
	private void generateMazeRecursive(int xStart, int xEnd, int yStart, int yEnd, int mazeSize){
		if(xEnd - xStart > 2) {
			//This will insert 4 walls in the middle of the matrix
			if(yEnd - yStart>2){
				for(int i = 0; i<yEnd-yStart; i++){
					this.maze[yStart + (yEnd-yStart)/2][i+xStart]=false;
				}
			}
			for(int i = 0; i<xEnd-xStart; i++){
				this.maze[i+yStart][xStart + (xEnd-xStart)/2]=false;
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
						if(xEnd - xStart > 3){
							newHole = new Random().nextInt((xEnd-xStart)/4)*2;
						}else{
							newHole = 0;
						}
						this.maze[yStart + newHole][xStart + (xEnd-xStart)/2] = true;
						break;
					case 1:
						if(yEnd - yStart > 3){
							newHole = new Random().nextInt((yEnd-yStart)/4)*2;
						}else{
							newHole = 0;
						}
						this.maze[yStart + (yEnd-yStart)/2][xStart + newHole] = true;
						break;
					case 2:
						if(xEnd - xStart > 3){
							newHole = new Random().nextInt((xEnd-xStart)/4)*2;
						}else{
							newHole = 0;
						}
						this.maze[yStart + newHole+(xEnd-xStart)/2 +1][xStart + (xEnd-xStart)/2] = true;
						break;
					case 3:
						if(yEnd - yStart > 3){
							newHole = new Random().nextInt((yEnd-yStart)/4)*2;
						}else{
							newHole = 0;
						}
						this.maze[yStart + (yEnd-yStart)/2][newHole + (yEnd-yStart)/2 +1 + xStart] = true;
						break;
				}
			}
			if((xEnd-xStart > 3) && (yEnd-yStart > 3)){
				generateMazeRecursive(xStart, xStart + (xEnd-xStart)/2, yStart, yStart + (yEnd-yStart)/2, mazeSize);
				generateMazeRecursive(xStart + (xEnd-xStart)/2+1, xEnd, yStart, yStart + (yEnd-yStart)/2, mazeSize);
				generateMazeRecursive(xStart + (xEnd-xStart)/2+1, xEnd, yStart + (yEnd-yStart)/2+1, yEnd, mazeSize);
				generateMazeRecursive(xStart, xStart + (xEnd-xStart)/2, yStart + (yEnd-yStart)/2+1, yEnd, mazeSize);
			}
		}
	}
}
