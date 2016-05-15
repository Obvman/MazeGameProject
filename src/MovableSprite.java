import java.awt.Image;
import java.awt.Rectangle;

public interface MovableSprite {

	public int getX();
	public int getY();
	public int getDX();
	public int getDY();
	public Image getImage();
	public Rectangle getBounds();
	public void manualMove(int dx, int dy);
}
