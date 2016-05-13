import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class GameScreen extends JPanel implements ActionListener {

	private MainWindow mainWindow;
	private JPanel mazePanel;
	
	// panels making up the mazePanel
	private MazePanel mazeGame;
	private JPanel mazeScorePanel;
	
	// status bar components
	JLabel level;
	JLabel time;
	JLabel hintsRemaining;
	
	private int currLevel;
	private Timer timer;

	public GameScreen(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		initGame();

		timer = new Timer(10, this);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		updateComponents();
		
		if(e.getSource() == timer) {
			updateComponents();
			
			if (mazeGame.isGameFinished()) {
				timer.stop();
				currLevel++;
				initMazeScorePanel();
				switchToMazeFinishedPanel();
			}
		}
		
		repaint();
	}

	private void updateComponents() {
		// update level
		level.setText("Level: " + currLevel);
		time.setText("Time: " + mazeGame.getDuration()/1000000000 + "s");
	}
	
	private void switchToMazePlayingPanel() {
		mazeGame = new MazePanel();
		mazePanel.add(mazeGame, "Playing");
		CardLayout cl = (CardLayout) mazePanel.getLayout();
		cl.show(mazePanel, "Playing");
	}

	private void switchToMazeFinishedPanel() {
		CardLayout cl = (CardLayout) mazePanel.getLayout();
		cl.show(mazePanel, "Finished");
	}

	private void initGame() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // check

		// add UI components
		initMazePanel();
		initStatusBar();
		initSideMenu();
		
		currLevel = 1;
	}

	private void initMazePanel() {
		mazePanel = new JPanel(new CardLayout());
		add(mazePanel);
		
		initMazeGame();
		initMazeScorePanel();
		
		switchToMazePlayingPanel();
	}
	
	private void initMazeGame() {
		mazeGame = new MazePanel();
		mazePanel.add(mazeGame, "Playing");
	}
	
	private void initMazeScorePanel() {
		mazeScorePanel = new JPanel();
		JButton nextLevelButton = new JButton("Continue to level " + currLevel);
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

	private void initStatusBar() {
		JPanel statusBar = new JPanel();
		statusBar.setLayout(new BorderLayout());
		statusBar.setPreferredSize(new Dimension(0, 40)); // check

		// score
		level = new JLabel("Level: " + level);
		statusBar.add(level, BorderLayout.WEST);

		// time
		time = new JLabel("Time: ");
		statusBar.add(time, BorderLayout.CENTER);
		time.setHorizontalAlignment(JLabel.CENTER);

		// hints remaining
		hintsRemaining = new JLabel("Hints Remaining: ");
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
		hintButton.setToolTipText("Reveals a hint");
		hintButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Hahaha we don't give you hints");
			}
		});

		// pause button
		JButton pauseButton = new JButton("Pause");
		pauseButton.setToolTipText("Pause the game and timer");
		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("The game is actually paused right now, trust me");
			}
		});

		// menu button
		JButton menuButton = new JButton("Back to Main Menu");
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
