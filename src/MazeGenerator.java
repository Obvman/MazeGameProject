
public class MazeGenerator implements MazeConstants {
	private int[][] maze;

	// TODO: fix this description -- not in the right place
	/**
	 * This method takes an integer mazeSize as input and 
	 * returns a generated maze.
	 * The input must be a power of 2-1(2^n -1) to do recursive 
	 * The input must be 2*n - 1 for the new generate method
	 * generate.
	 * @param mazeSize
	 * @return
	 */
	public int[][] generateMaze(int mazeSize1, int mazeSize2) {
		if (this.maze == null) {
			this.maze = new int[mazeSize1][mazeSize2];

			for (int row = 0; row < mazeSize1; row++) {
				for (int col = 0; col < mazeSize2; col++) {
					this.maze[row][col] = PATH_TILE;
				}
			}
			MazeGenerationStrategy s = new MazeGenerateDfs();
			this.maze = s.generateMaze(mazeSize1, mazeSize2, this.maze);
		}

		// set start, end, and key tile
		maze[0][0] = START_TILE;
		maze[mazeSize1 - 1][mazeSize2 - 1] = END_TILE;
		
		return this.maze;
	}
}