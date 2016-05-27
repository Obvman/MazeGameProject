import java.awt.CardLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	
	/**
	 * Creates a JFrame that contains all the components of the game including
	 * MenuScreen, GameScreen, TutorialScreen and OptionsScreen. 
	 * Initialized to display the MenuScreen first.
	 */
	public MainWindow() {
		screens = new JPanel(new CardLayout());
		add(screens);
		
		// option screen
		options = new OptionsScreen(this);
		screens.add(options, "Options");
		
		// configure the main window
		setTitle("Exiled Maze");
		setSize(options.getResolution());
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// when resizing the frame, reposition to the centre of the screen
		// and update the padding for the components
		addComponentListener(new ComponentAdapter() {
		    @Override
		    public void componentResized( ComponentEvent e ) {
		    	menu.setBorder(BorderFactory.createEmptyBorder((int)(getHeight() * 0.5), (int)(getWidth() * 0.5), 0, (int)(getWidth() * 0.5)));
		    	setLocationRelativeTo(null);
		    }
		} );
		
		// menu screen
		menu = new MenuScreen(this);
		screens.add(menu, "Menu");
		
		// tutorial screen
		tutorial = new TutorialScreen(this);
		screens.add(tutorial, "Tutorial");
		
		switchToMenu(); // starting screen
	}
	
	/**
	 * Create a new GameScreen (instance of a game) and displays it.
	 */
	public void switchToGame() {
		// create a game with the options chosen in the OptionsScreen (or default if none chosen)
		screens.add(new GameScreen(this, options.getDifficulty(), options.getKeyArray()), "Game");
		CardLayout cl = (CardLayout) screens.getLayout();
		cl.show(screens, "Game");
	}
	
	/**
	 * Displays the MenuScreen
	 */
	public void switchToMenu() {
		CardLayout cl = (CardLayout) screens.getLayout();
		cl.show(screens, "Menu");
	}
	
	/**
	 * Displays the TutorialScreen
	 */
	public void switchToTutorial() {
		CardLayout cl = (CardLayout) screens.getLayout();
		cl.show(screens, "Tutorial");
	}
	
	/**
	 * Displays the OptionsScreen
	 */
	public void switchToOptions() {
		CardLayout cl = (CardLayout) screens.getLayout();
		cl.show(screens, "Options");
	}
	
	private JPanel screens; // contains all the screens of the game
	private MenuScreen menu;
	private OptionsScreen options; 
	private TutorialScreen tutorial;
}
