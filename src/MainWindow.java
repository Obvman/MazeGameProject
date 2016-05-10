import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	
	private JPanel screens; // contains all the screens of the game
	
	public MainWindow(int width, int height) {
		initScreens(width, height);
	}
	
	public void switchToMenu() {
		CardLayout cl = (CardLayout) screens.getLayout();
		cl.show(screens, "Menu");
	}
	
	public void switchToTutorial() {
		CardLayout cl = (CardLayout) screens.getLayout();
		cl.show(screens, "Tutorial");
	}
	
	public void switchToGame() {
		CardLayout cl = (CardLayout) screens.getLayout();
		cl.show(screens, "Game");
	}
	
	private void initScreens(int width, int height) {
		screens = new JPanel(new CardLayout());
		add(screens);
		
		// configure the main window
		setTitle("Maze Game");
		setSize(width, height);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		// add the screens
		screens.add(new MenuScreen(this), "Menu");
		screens.add(new TutorialScreen(this), "Tutorial");
		screens.add(new GameScreen(this), "Game");
		
		switchToMenu(); // starting screen
	}
}
