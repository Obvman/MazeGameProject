
public interface MazeGenerator {
	
	/**
	 * Generates 2D int array representing a maze where the value of the cell
	 * indicates the type of tile it is (refer to the Maze class constants)
	 * @param height the height of the maze in number of cells
	 * @param width the width of the maze in number of cells
	 * @return 2D int array representing the maze where the value of the cell
	 * indicates the type of tile it is (refer to Maze.java tile constants)
	 */
	public int[][] generateMaze(int height, int width);
}
