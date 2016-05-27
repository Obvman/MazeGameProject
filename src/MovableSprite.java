import java.awt.Image;
import java.awt.Rectangle;

/**
 * Interface for all Movable sprites in the game.
 * 		(used by players, monsters etc.)
 */
public interface MovableSprite {
	void setPosition(int x, int y);
	
	/**
	 * Gets X coordinate of position of the sprite.
	 * @return X coordinate value
	 */
	int getX();
	
	/**
	 * Gets Y coordinate of position of the sprite.
	 * @return Y coordinate value
	 */
	int getY();
	
	/**
	 * Gets X coordinate of derivative of position of the sprite.
	 * @return DX value
	 */
	int getDX();
	
	/**
	 * Gets X coordinate of derivative of position of the sprite.
	 * @return DX value
	 */
	int getDY();
	
	/**
	 * Gets the image associated with the sprite.
	 * @return Image
	 */
	Image getImage();
	
	/**
	 * Gets rectangle for bounds of the image location.
	 * @return rectangle
	 */
	Rectangle getBounds();
	
	/**
	 * Moves the location given X and Y values for the degree movement of the sprite.
	 * @param dx Change in X
	 * @param dy Change in Y
	 */
	void manualMove(int dx, int dy);
}
