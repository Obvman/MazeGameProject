import java.awt.Image;

public interface MovableSprite {

	public int getX();
	public int getY();
	public int getDX();
	public int getDY();
	public Image getImage();
	public void manualMove(int dx, int dy);
}
