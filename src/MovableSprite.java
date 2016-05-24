import java.awt.Image;
import java.awt.Rectangle;

public interface MovableSprite {
	int getX();
	int getY();
	int getDX();
	int getDY();
	Image getImage();
	Rectangle getBounds();
	void manualMove(int dx, int dy);
}
