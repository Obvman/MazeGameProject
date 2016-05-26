import java.util.Random;

/**
 * A recursive maze generator
 */
public class MazeGeneratorRecursive implements MazeGenerator {

	public int[][] generateMaze(int mazeSize1, int mazeSize2) {
		int[][] maze = new int[mazeSize1][mazeSize2]; 
		maze = new int[mazeSize1][mazeSize2];

		for (int row = 0; row < mazeSize1; row++) {
			for (int col = 0; col < mazeSize2; col++) {
				maze[row][col] = Maze.PATH_TILE;
			}
		}
		maze = generateMazeRecursive(0, mazeSize1, 0, mazeSize2, maze);
		// set start, end, and key tile
		maze[0][0] = Maze.START_TILE;
		maze[mazeSize1 - 1][mazeSize2 - 1] = Maze.END_TILE;
		
		return maze;
	}
	
	private int[][] generateMazeRecursive(int xStart, int xEnd, int yStart, int yEnd, int[][] maze) {
		if (xEnd - xStart > 2){
			//This will insert 4 walls in the middle of the matrix
			if (yEnd - yStart>2) {
				for (int i = 0; i<yEnd-yStart; i++) {
					maze[xStart + (xEnd-xStart)/2][i+yStart] = Maze.WALL_TILE;
				}
			}
			for(int i = 0; i<xEnd-xStart; i++){
				maze[i+xStart][yStart + (yEnd-yStart)/2]= Maze.WALL_TILE;
			}
			/*This will dig 3 holes on the 3 of the 4 walls we generated.
			 * 0 -> the top wall; 1 -> the left wall; 2 -> the bottom wall;
			 * 3 -> the right wall*/
			 
			int ignore = new Random().nextInt(4);
			int newHole;
			for(int i = 0; i < 4 ; i++) { 
				if (ignore == i) {
					continue;
				}
				switch (i) {
					case 0:
						//this if statement makes sure we won't dig a hole in where a wall might generate
						//since all walls will be generated in an even index (0, 2, 4...)
						if (yEnd - yStart > 3) {
							newHole = new Random().nextInt((yEnd-yStart)/4)*2;
						} else {
							newHole = 0;
						}
						maze[xStart + (xEnd-xStart)/2][yStart + newHole] = Maze.PATH_TILE;
						break;
					case 1:
						if (xEnd - xStart > 3) {
							newHole = new Random().nextInt((xEnd-xStart)/4)*2;
						} else {
							newHole = 0;
						}
						maze[xStart + newHole][yStart + (yEnd-yStart)/2] = Maze.PATH_TILE;
						break;
					case 2:
						if (yEnd - yStart > 3) {
							newHole = new Random().nextInt((yEnd-yStart)/4)*2;
						} else {
							newHole = 0;
						}
						maze[xStart + (xEnd-xStart)/2][yStart + newHole+(yEnd-yStart)/2 +1] = Maze.PATH_TILE;
						break;
					case 3:
						if (xEnd - xStart > 3) {
							newHole = new Random().nextInt((xEnd-xStart)/4)*2;
						} else {
							newHole = 0;
						}
						maze[newHole + (xEnd-xStart)/2 +1 + xStart][yStart + (yEnd-yStart)/2] = Maze.PATH_TILE;
						break;
				}
			}
			if ((xEnd-xStart > 3) && (yEnd-yStart > 3)) {
				maze = generateMazeRecursive(xStart, xStart + (xEnd-xStart)/2, yStart, yStart + (yEnd-yStart)/2, maze);
				maze = generateMazeRecursive(xStart + (xEnd-xStart)/2+1, xEnd, yStart, yStart + (yEnd-yStart)/2, maze);
				maze = generateMazeRecursive(xStart + (xEnd-xStart)/2+1, xEnd, yStart + (yEnd-yStart)/2+1, yEnd, maze);
				maze = generateMazeRecursive(xStart, xStart + (xEnd-xStart)/2, yStart + (yEnd-yStart)/2+1, yEnd, maze);
			}
		}
		return maze;
	}
}
