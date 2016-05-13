public interface MazeConstants {
	// maze configuration
	public static int MAZE_SIZE_1 = 15 /*25*/;
	public static int MAZE_SIZE_2 = 25 /*45*/;
	public static int MAZE_CELL_SIZE = 32;
	public static int DIFFICULTY = 3; // corresponds to number of monsters that spawn
	
	// types of tiles
	public static int PATH_TILE = 0;
	public static int WALL_TILE = 1;
	public static int START_TILE = 2;
	public static int END_TILE = 3;
	public static int KEY_TILE = 4;
}
