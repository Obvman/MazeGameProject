import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	
	private JPanel screens; // contains all the screens of the game
	
	public MainWindow() {
		screens = new JPanel(new CardLayout());
		add(screens);
		
		// configure the main window
		setTitle("Maze Game");
		setSize(1200, 600);
		setLocationRelativeTo(null);
//		setResizable(false);
//		setExtendedState(JFrame.MAXIMIZED_BOTH); // full screen
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// add the screens
		screens.add(new MenuScreen(this), "Menu");
		screens.add(new TutorialScreen(this), "Tutorial");
		
		switchToMenu(); // starting screen
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
		// first start a new game
		screens.add(new GameScreen(this), "Game");
		CardLayout cl = (CardLayout) screens.getLayout();
		cl.show(screens, "Game");
	}
}
