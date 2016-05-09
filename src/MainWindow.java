/**
 * Main GUI class that handles all panels/screens.
 * Contains:
 * 	-Start Screen
 *  -Game Panel and its children
 *    * status bar
 *    * maze panel
 *    * side menu
 * Main window's size is currently determined by the Controller class
 * 
 * COMP2911 Project - 16s1
 * @author Anna Azzam
 * @author Charlotte Han
 * @author Connor Coyne
 * @author Craig Feeney
 * @author Leon Nguyen
 * 
 */

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainWindow extends JFrame {
	// dunno what this is for but it gets rid of the yellow warning in eclipse
	private static final long serialVersionUID = 1L;

	private JPanel screens; // governing panel containing menus and game panel

	private JPanel gamePanel; // the maze panel, side menu and status bar

	private MainMenu mainMenuScreen;
	private int frameWidth;
	private int frameHeight;

	public MainWindow(int width, int height) {
		this.frameWidth = width;
		this.frameHeight = height;
		initUI();
	}

	private void initUI() {
		// create card layout for switching between screens
		screens = new JPanel(new CardLayout());

		gamePanel = new GamePanel(this);
		mainMenuScreen = new MainMenu(screens);
		// open up at the main menu
		screens.add(mainMenuScreen, "Main Menu");
		screens.add(gamePanel, "Game Screen");

		// make the first screen the start screen
		goToMenu();
		this.add(screens);

		// Initialize main frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(frameWidth, frameHeight);
		this.setResizable(false);
		this.setTitle("Maze Game: MainWindow");
		this.setLocationRelativeTo(null);
	}

	/**
	 * @precondition
	 *               <li>The maze screen is currently showing</li>
	 */
	public void goToMenu() {
		CardLayout cl = (CardLayout) this.screens.getLayout();
		cl.show(this.screens, "Main Menu");
	}

	/**
	 * @precondition
	 *               <li>The menu screen is currently showing</li>
	 */
	public void goToGame() {
		CardLayout cl = (CardLayout) this.screens.getLayout();
		cl.show(this.screens, "Game Screen");
	}

	// helper to allow this references from inside abstract classes
//	private MainWindow getThis() {
//		return this;
//	}
}
