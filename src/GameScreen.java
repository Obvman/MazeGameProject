import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

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
	
	// side menu bar
	JPanel sideMenu;
	
	private float duration;
	private int currLevel;
	private Timer timer;

	public GameScreen(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		
		// use grid bag layout for consistency when resizing
		this.setLayout(new GridBagLayout());

		// maze panel, status bar panel and side menu panel
		initMazePanel();
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
		// refresh status bar
		revalidate();
		initStatusBar();
		
		if(e.getSource() == timer) {
			if (maze.isGameLost()) {
				timer.stop();
				initMazeLostPanel();
				switchToMazeLostPanel();
			} else if (maze.isGameWon()) {
				timer.stop();
				currLevel++;
				initMazeScorePanel();
				switchToMazeFinishedPanel();
			} else {
				duration += (double)timer.getDelay()/1000;
			}
		}
	}
	
	private void switchToMazePlayingPanel() {
		mazeGame = new MazePanel();
		mazePanel.add(mazeGame, "Playing");
		maze = mazeGame.getMaze();
		
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
	
	private void initMazePanel() {
		mazePanel = new JPanel(new CardLayout());
		initMazeScorePanel();
		initMazeLostPanel();
		
		// initialise maze panel layout
		GridBagConstraints gbcMaze = new GridBagConstraints();
		gbcMaze.fill = GridBagConstraints.BOTH;
		gbcMaze.gridy = 1;
		gbcMaze.gridx = 0;
		gbcMaze.gridheight = 10;
		gbcMaze.gridwidth = 10;
		gbcMaze.weightx = 1.0;
		gbcMaze.weighty = 1.0;
		
		this.add(mazePanel, gbcMaze);	
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
		
		GridBagConstraints gbcStatus = new GridBagConstraints();
		gbcStatus.insets = new Insets(5,20,15,20);
		gbcStatus.fill = GridBagConstraints.BOTH;
		gbcStatus.gridy = 0;
		gbcStatus.gridx = 0;
		gbcStatus.gridheight = 1;
		gbcStatus.gridwidth = 10;
		gbcStatus.weightx = 0.5;
		gbcStatus.weighty = 0.01;

		// score
		JLabel level = new JLabel("Level: " + currLevel);
		statusBar.add(level, BorderLayout.WEST);

		// time
		JLabel time = new JLabel("Time: " + (int)duration);
		statusBar.add(time, BorderLayout.CENTER);
		time.setHorizontalAlignment(JLabel.CENTER);

		// hints remaining
		JLabel hintsRemaining = new JLabel("Hints Remaining: ");
		statusBar.add(hintsRemaining, BorderLayout.EAST);
		
		this.add(statusBar, gbcStatus);
	}

	private void initSideMenu() {
		sideMenu = new JPanel();
		GroupLayout groupLayout = new GroupLayout(sideMenu);
		sideMenu.setLayout(groupLayout);
		
		// set parameters for sizing and positioning in main window
		GridBagConstraints gbcSide = new GridBagConstraints();
		gbcSide.fill = GridBagConstraints.BOTH;
		gbcSide.gridy = 1;
		gbcSide.gridx = 11;
		gbcSide.gridheight = 11;
		gbcSide.gridwidth = 2;
		gbcSide.weightx = 0.1;
		gbcSide.weighty = 0.45;
		this.add(sideMenu, gbcSide);

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
		// final modifier to allow modification of the button (see action listener)
		final JButton pauseButton = new JButton("Pause");
		pauseButton.setFocusable(false);
		pauseButton.setToolTipText("Pause/unpause the game and timer");
		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (timer.isRunning()) {
					pauseButton.setText("Unpause");
					timer.stop();
					mazeGame.setRunning(false);
				} else {
					pauseButton.setText("Pause");
					timer.start();
					mazeGame.setRunning(true);;
				}
			}
		});

		// menu button
		JButton menuButton = new JButton("Back to Main Menu");
		menuButton.setFocusable(false);
		menuButton.setToolTipText("Return back to the main menu");
		menuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mazeGame.setRunning(false);
				mainWindow.switchToMenu();
			}
		});

		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup()
						.addComponent(hintButton)
						.addComponent(pauseButton)
						.addComponent(menuButton)
				)
		);

		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(hintButton)
						.addComponent(pauseButton)
						.addComponent(menuButton)
				)
		);

		groupLayout.linkSize(hintButton, pauseButton, menuButton); // keep size of buttons consistent

	}
}
