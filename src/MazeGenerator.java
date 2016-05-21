
public class MazeGenerator {
	private int[][] maze;

	public int[][] generateMaze(int mazeSize1, int mazeSize2) {
		if (this.maze == null) {
			MazeGenerationStrategy s = new MazeGenerateDfs(); // TODO: fix this. it is not correct strategy pattern
			this.maze = s.generateMaze(mazeSize1, mazeSize2);
		}
		
		return this.maze;
	}
}