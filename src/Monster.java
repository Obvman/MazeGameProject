import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Monster implements MovableSprite {
	private double x;
	private double y;
	private int dx;
	private int dy;
	private BufferedImage image_N;
	private BufferedImage image_W;
	private BufferedImage image_S;
	private BufferedImage image_E;
	private int scaledHeight;
	
	public Monster() {
		randomiseDirection();
		
		scaledHeight = Maze.MAZE_CELL_SIZE * 3/4;
		
		try {
			image_E = getScaledImage(ImageIO.read(new File("resources/monster_right.png")), scaledHeight, scaledHeight);
			image_N = getScaledImage(ImageIO.read(new File("resources/monster_up.png")), scaledHeight, scaledHeight);
			image_W = getScaledImage(ImageIO.read(new File("resources/monster_left.png")), scaledHeight, scaledHeight);
			image_S = getScaledImage(ImageIO.read(new File("resources/monster_down.png")), scaledHeight, scaledHeight);
		} catch (IOException e) {
			e.printStackTrace();
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
    	if (dx == 0 && dy < 0) {
    		return image_N;
    	} else if (dx < 0 && dy == 0) {
    		return image_W;
    	} else if (dx == 0 && dy > 0) {
    		return image_S;
    	} else if (dx > 0 && dy == 0) {
    		return image_E;
    	}
    	
    	return null;
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), scaledHeight, scaledHeight);
	}

	@Override
    public void manualMove(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
    	x += dx * (double)Maze.MAZE_CELL_SIZE/32;
    	y += dy * (double)Maze.MAZE_CELL_SIZE/32;
    }
	
	// picks a random STRAIGHT line motion
	public void randomiseDirection() {
		Double rand = Math.random();
		dx = 0;
		dy = 0;
		if (rand > 0.75) {
			dx = 1;
		} else if (rand > 0.5) {
			dx = -1;
		} else if (rand > 0.25) {
			dy = 1;
		} else {
			dy = -1;
		}
	}
	
	public void setPosition (int x, int y) {
		this.x = x;
		this.y = y;
	}

	 private BufferedImage getScaledImage(BufferedImage src, int w, int h){
		int finalw = w;
		int finalh = h;
		double factor = 1.0d;
		if(src.getWidth() > src.getHeight()){
			factor = ((double)src.getHeight()/(double)src.getWidth());
	        finalh = (int)(finalw * factor);                
	    }else{
	        factor = ((double)src.getWidth()/(double)src.getHeight());
	        finalw = (int)(finalh * factor);
	    }   

		BufferedImage resizedImg = new BufferedImage(finalw, finalh, BufferedImage.TRANSLUCENT);
	    Graphics2D g2 = resizedImg.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(src, 0, 0, finalw, finalh, null);
	    g2.dispose();
	    return resizedImg;
	 }
	
}