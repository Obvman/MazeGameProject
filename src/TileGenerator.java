import java.awt.*;
import javax.swing.*;

public class TileGenerator {
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
	
	// for gems
	private Image[] gemImages;

	public TileGenerator() {
		int cellSize = Maze.MAZE_CELL_SIZE;
		
		pathTile = (new ImageIcon("resources/leon_path.png")).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
		wallTile = (new ImageIcon("resources/leon_wall_lava.png")).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
		startTile = (new ImageIcon("resources/leon_open_door.png")).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
		endTile = (new ImageIcon("resources/leon_closed_door.png")).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
		keyTile = (new ImageIcon("resources/key_tile.gif")).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_DEFAULT);
		
		wallTileN = (new ImageIcon("resources/leon_wall_top_cover_lava.png")).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
		wallTileW = (new ImageIcon("resources/leon_wall_left_cover_lava.png")).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
		wallTileS = (new ImageIcon("resources/leon_wall_bottom_cover_lava.png")).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
		wallTileE = (new ImageIcon("resources/leon_wall_right_cover_lava.png")).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
		
		gemImages = new Image[40];
		for (int i = 0; i < 40; i++) {
			gemImages[i] = (new ImageIcon("resources/gems/gems-" + i + ".png")).getImage().getScaledInstance(cellSize*1/2, cellSize*1/2, Image.SCALE_SMOOTH);
		}
	}

	public Image getPathTile() {
		return pathTile;
	}

	public Image getWallTile() {
		return wallTile;
	}

	public Image getStartTile() {
		return startTile;
	}

	public Image getEndTile() {
		return endTile;
	}

	public Image getKeyTile() {
		return keyTile;
	}
	
	public Image getWallTileN() {
		return wallTileN;
	}
	
	public Image getWallTileW() {
		return wallTileW;
	}
	
	public Image getWallTileS() {
		return wallTileS;
	}
	
	public Image getWallTileE() {
		return wallTileE;
	}
	
	/**
	 * Returns a random gem tile image based on the coordinates of the tile
	 * @param x
	 * @param y
	 * @return
	 */
	public Image getGemImage(int x, int y) {
		// return a random gem
		return gemImages[(x*31+y*67) % gemImages.length];
	}
}
