import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class GameScreen extends JPanel implements ActionListener {

	private MainWindow mainWindow;
	private JPanel mazePanel;
	
	// panels making up the mazePanel
	private Maze maze;
	private MazePanel mazeGame;
	private JPanel mazeScorePanel;
	private JPanel mazeLostPanel;
	
	// status bar components
	JPanel statusBar;
	
	private long startTime;
	private long duration;
	private int currLevel;
	private Timer timer;

	public GameScreen(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		
		// allow this panel to add inner panels using BorderLayout
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// maze panel
		mazePanel = new JPanel(new CardLayout());
		add(mazePanel);
		initMazeScorePanel();
		initMazeLostPanel();
		
		// status bar panel and side menu panel
		initStatusBar();
		initSideMenu();
		
		// initialise stats
		currLevel = 1;

		timer = new Timer(30, this);
		timer.start();
		
		switchToMazePlayingPanel();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		updateComponents();
		
		if(e.getSource() == timer) {
			if (maze.isGameLost()) {
				timer.stop();
				switchToMazeLostPanel();
			} else if (maze.isGameWon()) {
				timer.stop();
				currLevel++;
				switchToMazeFinishedPanel();
			} else {
				duration = System.nanoTime() - startTime;
			}
		}
	}
	
	private void updateComponents() {
		// update status bar
		initStatusBar();
		initMazeScorePanel();
		initMazeLostPanel();
		
		statusBar.repaint();
	}
	
	private void switchToMazePlayingPanel() {
		mazeGame = new MazePanel();
		mazePanel.add(mazeGame, "Playing");
		maze = mazeGame.getMaze();
		
		startTime = System.nanoTime();
		
		CardLayout cl = (CardLayout) mazePanel.getLayout();
		cl.show(mazePanel, "Playing");
		
		revalidate();
		repaint();
	}

	private void switchToMazeFinishedPanel() {
		CardLayout cl = (CardLayout) mazePanel.getLayout();
		cl.show(mazePanel, "Finished");
	}
	
	private void switchToMazeLostPanel() {
		CardLayout cl = (CardLayout) mazePanel.getLayout();
		cl.show(mazePanel, "Lost");
	}

	private void initMazeScorePanel() {
		mazeScorePanel = new JPanel();
		JButton nextLevelButton = new JButton("Continue to level " + (currLevel+1));
		nextLevelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchToMazePlayingPanel();
				timer.start();
			}
		});
		mazeScorePanel.add(nextLevelButton);
		
		JButton menuButton = new JButton("Back to Main Menu");
		menuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.switchToMenu();
			}
		});
		mazeScorePanel.add(menuButton);
		
		mazePanel.add(mazeScorePanel, "Finished");
	}
	
	private void initMazeLostPanel() {
		mazeLostPanel = new JPanel();
		JLabel levelLabel = new JLabel("You died at level " + currLevel);
		mazeLostPanel.add(levelLabel);
		
		JButton startButton = new JButton("Start Again");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.switchToGame();
			}
		});
		mazeLostPanel.add(startButton);
		
		JButton menuButton = new JButton("Back to Main Menu");
		menuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.switchToMenu();
			}
		});
		mazeLostPanel.add(menuButton);
		
		mazePanel.add(mazeLostPanel, "Lost");
	}

	private void initStatusBar() {
		statusBar = new JPanel();
		statusBar.setLayout(new BorderLayout());
		statusBar.setPreferredSize(new Dimension(0, 40)); // check

		// score
		JLabel level = new JLabel("Level: " + currLevel);
		statusBar.add(level, BorderLayout.WEST);

		// time
		JLabel time = new JLabel("Time: " + duration/1000000000);
		statusBar.add(time, BorderLayout.CENTER);
		time.setHorizontalAlignment(JLabel.CENTER);

		// hints remaining
		JLabel hintsRemaining = new JLabel("Hints Remaining: ");
		statusBar.add(hintsRemaining, BorderLayout.EAST);

		add(statusBar, BorderLayout.NORTH);
	}

	private void initSideMenu() {
		JPanel sideMenu = new JPanel();
		GroupLayout groupLayout = new GroupLayout(sideMenu);
		sideMenu.setLayout(groupLayout);

		// to check
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);

		// hint button
		JButton hintButton = new JButton("Hint");
		hintButton.setFocusable(false);
		hintButton.setToolTipText("Reveals a hint");
		hintButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Hahaha we don't give you hints");
			}
		});

		// pause button
		JButton pauseButton = new JButton("Pause");
		pauseButton.setFocusable(false);
		pauseButton.setToolTipText("Pause the game and timer");
		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("The game is actually paused right now, trust me");
			}
		});

		// menu button
		JButton menuButton = new JButton("Back to Main Menu");
		menuButton.setFocusable(false);
		menuButton.setToolTipText("Return back to the main menu");
		menuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.switchToMenu();
			}
		});

		// TODO: clean this
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup().addGroup(groupLayout.createParallelGroup().addComponent(hintButton)
				.addComponent(pauseButton).addComponent(menuButton)));

		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(groupLayout.createSequentialGroup().addComponent(hintButton).addComponent(pauseButton)
						.addComponent(menuButton)));

		groupLayout.linkSize(hintButton, pauseButton, menuButton); // keep size of buttons consistent

		add(sideMenu, BorderLayout.EAST);
	}
}
