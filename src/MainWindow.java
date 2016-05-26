import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	
	private JPanel screens; // contains all the screens of the game
	private MenuScreen menu;
	private OptionsScreen options; // used to control game settings
	private TutorialScreen tutorial;
	private GameScreen game;
	
	/**
	 * Constructor.
	 * Creates JPanel that contains all components of the program.
	 * Initializes a cardlayout which holds the options screen, menu screen
	 * and tutorial screen.
	 * Displays menu screen once constructed
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
	 * Create a new GameScreen and switches to it.
	 */
	public void switchToGame() {
		// create a game with the options chosen in the OptionsScreen (or default if none chosen)
		screens.add(new GameScreen(this, options.getDifficulty(), options.getKeyArray()), "Game" );
		CardLayout cl = (CardLayout) screens.getLayout();
		cl.show(screens, "Game");
	}
	
	/**
	 * displays to main menu screen
	 */
	public void switchToMenu() {
		CardLayout cl = (CardLayout) screens.getLayout();
		cl.show(screens, "Menu");
	}
	
	/**
	 * displays tutorial screen
	 */
	public void switchToTutorial() {
		CardLayout cl = (CardLayout) screens.getLayout();
		cl.show(screens, "Tutorial");
	}
	
	/**
	 * displays options screen
	 */
	public void switchToOptions() {
		CardLayout cl = (CardLayout) screens.getLayout();
		cl.show(screens, "Options");
	}
	
	
}
