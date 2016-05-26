import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;

public class Spell implements MovableSprite {

	private int initialX;
	private int initialY;
	double x;
	double y;
	private int dx;
	private int dy;
	private Image image1;
	private Image image2;
	private Image image3;
	private int stage; 
	private boolean muted;

	public Spell(int startX, int startY, int dx, int dy, int spellType, boolean muted) {
		initialX = startX;
		initialY = startY;

		x = startX;
		y = startY;
		this.dx = dx;
		this.dy = dy;
		
		this.muted = muted;

		int scaledSize = (3 * Maze.MAZE_CELL_SIZE) / 4;

		if (spellType == GameScreen.FIRE) {
			image1 = (new ImageIcon("resources/spells/fire1.png")).getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
			image2 = (new ImageIcon("resources/spells/fire2.png")).getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
			image3 = (new ImageIcon("resources/spells/fire3.png")).getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
			if (!muted) playSound("resources/sound/fire.wav");
		} else if (spellType == GameScreen.WATER) {
			image1 = (new ImageIcon("resources/spells/water1.png")).getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
			image2 = (new ImageIcon("resources/spells/water2.png")).getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
			image3 = (new ImageIcon("resources/spells/water3.png")).getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
			if (!muted) playSound("resources/sound/water.wav");
		} else if (spellType == GameScreen.AIR) {
			image1 = (new ImageIcon("resources/spells/air1.png")).getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
			image2 = (new ImageIcon("resources/spells/air2.png")).getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
			image3 = (new ImageIcon("resources/spells/air3.png")).getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
			if (!muted) playSound("resources/sound/wind.wav");
		}

	}

	@Override
	public int getX() {
		return (int)x;
	}

	@Override
	public int getY() {
		return (int)y;
	}

	@Override
	public int getDX() {
		return dx;
	}

	@Override
	public int getDY() {
		return dy;
	}

	@Override
	public Image getImage() {
		if (stage == 0) {
			return image1;
		} else if (stage == 1) {
			return image2;
		} else {
			return image3;
		}
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), getImage().getWidth(null), getImage().getHeight(null));
	}

	public void updatePosition() {
		x += dx * (double)Maze.MAZE_CELL_SIZE/32;
		y += dy * (double)Maze.MAZE_CELL_SIZE/32;

		stage = (int)Math.sqrt(Math.pow(x - initialX, 2) + Math.pow(y - initialY, 2))/getImage().getWidth(null);
	}

	public int getStage() {
		return stage;
	}

	public void updateStage() {
		stage++;
	}

	@Override
	public void manualMove(int dx, int dy) {
		x += dx;
		y += dy;
	}

	private void playSound(String soundName) {
		try 
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
