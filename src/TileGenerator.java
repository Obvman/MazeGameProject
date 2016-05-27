import java.awt.Image;

import javax.swing.ImageIcon;

public class TileGenerator {

	/**
	 * Creates a TileGenerator object and initializes the Image for all tiles and gems
	 */
	public TileGenerator() {
		int cellSize = Maze.MAZE_CELL_SIZE;
		
		pathTile = (new ImageIcon("resources/tiles/leon_path.png")).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
		wallTile = (new ImageIcon("resources/tiles/leon_wall_lava.png")).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
		startTile = (new ImageIcon("resources/tiles/leon_open_door.png")).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
		endTile = (new ImageIcon("resources/tiles/leon_closed_door.png")).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
		keyTile = (new ImageIcon("resources/tiles/key_tile.gif")).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_DEFAULT);
		
		wallTileN = (new ImageIcon("resources/tiles/leon_wall_top_cover_lava.png")).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
		wallTileW = (new ImageIcon("resources/tiles/leon_wall_left_cover_lava.png")).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
		wallTileS = (new ImageIcon("resources/tiles/leon_wall_bottom_cover_lava.png")).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
		wallTileE = (new ImageIcon("resources/tiles/leon_wall_right_cover_lava.png")).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
		
		gemImages = new Image[40];
		for (int i = 0; i < 40; i++) {
			gemImages[i] = (new ImageIcon("resources/gems/gems-" + i + ".png")).getImage().getScaledInstance(cellSize*1/2, cellSize*1/2, Image.SCALE_SMOOTH);
		}
	}

	/**
	 * @return the Image for a path tile
	 */
	public Image getPathTile() {
		return pathTile;
	}

	/**
	 * @return the Image for a wall tile
	 */
	public Image getWallTile() {
		return wallTile;
	}

	/**
	 * @return the Image for the start tile
	 */
	public Image getStartTile() {
		return startTile;
	}

	/**
	 * @return the Image for the end tile
	 */
	public Image getEndTile() {
		return endTile;
	}

	/**
	 * @return the Image for the key tile
	 */
	public Image getKeyTile() {
		return keyTile;
	}
	
	/**
	 * @return the Image for the north part of a wall tile
	 */
	public Image getWallTileN() {
		return wallTileN;
	}
	
	/**
	 * @return the Image for the west part of a wall tile
	 */
	public Image getWallTileW() {
		return wallTileW;
	}
	
	/**
	 * @return the Image for the south part of a wall tile
	 */
	public Image getWallTileS() {
		return wallTileS;
	}
	
	/**
	 * @return the Image for the east part of a wall tile
	 */
	public Image getWallTileE() {
		return wallTileE;
	}
	
	/**
	 * Generates an Image for a gem based on its coordinates
	 * @param x the x coordinate of the gem
	 * @param y the y coordinate of the gem
	 * @return the Image for the gem based on its coordinates
	 */
	public Image getGemImage(int x, int y) {
		return gemImages[(x*31+y*67) % gemImages.length];
	}
	
	// tiles
	private Image pathTile;
	private Image wallTile;
	private Image startTile;
	private Image endTile;
	private Image keyTile;
	
	// for connecting wall tiles for enhanced graphics
	private Image wallTileN;
	private Image wallTileW;
	private Image wallTileS;
	private Image wallTileE;
	
	private Image[] gemImages;
}
