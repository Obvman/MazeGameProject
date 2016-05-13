public interface MazeConstants {
	// maze configuration
	int MAZE_SIZE_1 = 15 /*25*/;
	int MAZE_SIZE_2 = 15 /*45*/;
	int MAZE_CELL_SIZE = 32;
	int DIFFICULTY = 3; // corresponds to number of monsters that spawn
	
	// types of tiles
	int PATH_TILE = 0;
	int WALL_TILE = 1;
	int START_TILE = 2;
	int END_TILE = 3;
	int KEY_TILE = 4;
}
