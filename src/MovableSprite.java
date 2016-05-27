import java.awt.Image;
import java.awt.Rectangle;

/**
 * Interface for all movable sprites in the maze game.
 */
public interface MovableSprite {
	/**
	 * Sets the position of the MovableSprite
	 * @param x the new x coordinate of the MovableSprite 
	 * @param y the new y coordinate of the MovableSprite
	 */
	void setPosition(int x, int y);
	
	/**
	 * @return the x coordinate of the MovableSprite
	 */
	int getX();
	
	/**
	 * @return the y coordinate of the MovableSprite
	 */
	int getY();
	
	/**
	 * @return the x-axis movement value
	 */
	int getDX();
	
	/**
	 * @return the y-axis movement value
	 */
	int getDY();
	
	/**
	 * @return the Image representing the MovableSprite
	 */
	Image getImage();
	
	/**
	 * @return the Rectangle representing the bounds of the MovableSprite
	 */
	Rectangle getBounds();
	
	/**
	 * Updates the position of the MovableSprite based on a given x-axis and y-axis movement value
	 * @param dx the x-axis movement value
	 * @param dy the y-axis movement value
	 */
	void manualMove(int dx, int dy);
}
