import java.awt.Image;
import java.awt.Rectangle;

public interface MovableSprite {
	int getX();
	int getY();
	int getDX();
	int getDY();
	boolean canFly();
	Image getImage();
	Rectangle getBounds();
	void manualMove(int dx, int dy);
}
