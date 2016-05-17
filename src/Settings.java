import java.awt.event.KeyEvent;

/*
 * Storage class for user-modifiable settings
 * TODO Perhaps store to config file?
 */
public class Settings {
	private int windowSizeX;
	private int windowSizeY;
	
	private int moveLeftKey;
	private int moveRightKey;
	private int moveUpKey;
	private int moveDownKey;
	
	private boolean fullscreen;
	
	public Settings() {
		this.moveLeftKey = KeyEvent.VK_LEFT;
		this.moveRightKey = KeyEvent.VK_RIGHT;
		this.moveUpKey = KeyEvent.VK_UP;
		this.moveDownKey = KeyEvent.VK_DOWN;
		
		this.fullscreen = false;
		
		this.windowSizeX = 1200;
		this.windowSizeY = 600;
	}
	
	public void setLeftKey(int key) {
		this.moveLeftKey = key;
	}
	
	public void setRightKey(int key) {
		this.moveRightKey = key;
	}
	
	public void setUpKey(int key) {
		this.moveUpKey = key;
	}
	
	public void setDownKey(int key) {
		this.moveDownKey = key;
	}
	
	public void setFullscreen(boolean b) {
		this.fullscreen = b;
	}
	
	public void setWindowSizeX(int width) {
		this.windowSizeX = width;
	}
	
	public void setWindowSizeY(int height) {
		this.windowSizeY = height;
	}
	
	/* GETTERS */
	
	public int getLeftKey() {
		return moveLeftKey;
	}
	
	public int getRightKey() {
		return moveRightKey;
	}
	
	public int getUpKey() {
		return moveUpKey;
	}
	
	public int getDownKey() {
		return moveDownKey;
	}
	
	public boolean getFullscreen() {
		return fullscreen;
	}
	
	public int getWindowSizeX() {
		return windowSizeX;
	}
	
	public int getWindowSizeY() {
		return windowSizeY;
	}
}
