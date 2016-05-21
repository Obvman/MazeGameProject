import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	
	private JPanel screens; // contains all the screens of the game
	private MenuScreen menu;
	private OptionsScreen options; // used to control game settings
	
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
		
		switchToMenu(); // starting screen
	}
	
	public void switchToGame() {
		// create a game with the options chosen in the OptionsScreen (or default if none chosen)
		screens.add(new GameScreen(this, options.getDifficulty()), "Game");
		CardLayout cl = (CardLayout) screens.getLayout();
		cl.show(screens, "Game");
	}
	
	public void switchToMenu() {
		CardLayout cl = (CardLayout) screens.getLayout();
		cl.show(screens, "Menu");
	}
	
	public void switchToTutorial() {
		screens.add(new TutorialScreen(this), "Tutorial");
		CardLayout cl = (CardLayout) screens.getLayout();
		cl.show(screens, "Tutorial");
	}
	
	public void switchToOptions() {
		CardLayout cl = (CardLayout) screens.getLayout();
		cl.show(screens, "Options");
	}
	
	
}
