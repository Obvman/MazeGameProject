import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainWindow extends JFrame {
	private JPanel screens; // governing panel containing menus and game panel
	
	private JPanel statusBar;
	private JPanel sideMenu;
	private MazePanel mazePanel; // just the maze itself
	private JPanel gamePanel; // the maze panel, side menu and status bar
	private ExitDialog exitDialog; // returning to menu dialog
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
		
		
		initGamePanel();
		initMainMenu();
		// open up at the main menu
		screens.add(mainMenuScreen, "Main Menu");
		screens.add(gamePanel, "Game Screen");
		
		// make the first screen the start screen
		goToMenu();
		this.add(screens);
	}

	private void initMainMenu() {
		mainMenuScreen = new MainMenu(screens);
	}

	private void initGamePanel() {
		// encompassing panel for some extra flexiblity
		gamePanel = new JPanel();
		gamePanel.setLayout(new BorderLayout());
		gamePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// frame to display the maze state
		// TODO move this Maze to a legit class maybe
		Maze maze = new Maze();
		mazePanel = new MazePanel();
		mazePanel.addMaze(maze.getMaze(31));
		initMazePanel();

		// statusBar to appear above the maze
		statusBar = new JPanel();
		initStatusBar();

		// sideMenu to appear right of the maze
		sideMenu = new JPanel();
		initSideMenu();

		gamePanel.add(mazePanel, BorderLayout.CENTER);
		gamePanel.add(statusBar, BorderLayout.NORTH);
		gamePanel.add(sideMenu, BorderLayout.EAST);

		// Initialize main frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(frameWidth, frameHeight);
		this.setResizable(false);
		this.setTitle("Maze Game: MainWindow");
		this.setLocationRelativeTo(null);
	}

	// put this into MazePanel's init() when done
	private void initMazePanel() {
		Double dHeight = this.frameWidth * 0.8;
		Double dWidth = this.frameHeight * 0.8;
		mazePanel.setSize(dWidth.intValue(), dHeight.intValue());
		// just so we can differentiate it until the real maze is made
		mazePanel.setBackground(Color.blue);
	}

	private void initStatusBar() {

		statusBar.setLayout(new BorderLayout());
		statusBar.setPreferredSize(new Dimension(-1, 40));

		JLabel hints = new JLabel("Hints Remaining: ");
		JLabel time = new JLabel("Time: ");
		JLabel score = new JLabel("Score:");

		hints.setHorizontalAlignment(JLabel.LEFT);
		time.setHorizontalAlignment(JLabel.CENTER);
		score.setHorizontalAlignment(JLabel.RIGHT);

		statusBar.add(hints, BorderLayout.WEST);
		statusBar.add(time);
		statusBar.add(score, BorderLayout.EAST);
	}

	private void initSideMenu() {
		GroupLayout g1 = new GroupLayout(sideMenu);
		sideMenu.setLayout(g1);

		g1.setAutoCreateContainerGaps(true);
		g1.setAutoCreateGaps(true);

		// Create buttons for menu
		JButton hintButton = new JButton("Hint");
		JButton pauseButton = new JButton("Pause");
		JButton exitButton = new JButton("Exit to Menu");

		// set out the layout
		g1.setHorizontalGroup(g1.createSequentialGroup().addGroup(
				g1.createParallelGroup().addComponent(hintButton).addComponent(pauseButton).addComponent(exitButton)));

		g1.setVerticalGroup(g1.createParallelGroup(GroupLayout.Alignment.CENTER).addGroup(g1.createSequentialGroup()
				.addComponent(hintButton).addComponent(pauseButton).addComponent(exitButton)));

		g1.linkSize(hintButton, pauseButton, exitButton);
		pack();

		// add listeners for each button
		hintButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Hahaha we don't give you hints");
			}
		});

		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("The game is actually paused right now, trust me");
			}
		});

		exitDialog = new ExitDialog(this);
		// Should we go straight to the main menu or give an are you sure
		// prompt?
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// goToMenu() or:
				exitDialog.setLocationRelativeTo(getThis());
				exitDialog.setVisible(true);
			}
		});

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

	public void changeSize(int width, int height) {
		this.setSize(width, height);
	}

	// helper to allow this references from inside abstract classes
	private MainWindow getThis() {
		return this;
	}
}
