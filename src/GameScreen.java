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
		
		this.setLayout(new GridBagLayout());
		this.setSize(1200,600);
		
		// allow this panel to add inner panels using BorderLayout
//		setLayout(new BorderLayout());
//		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// maze panel
		mazePanel = new JPanel(new CardLayout());
		//add(mazePanel);
		initMazeScorePanel();
		initMazeLostPanel();
		
		// status bar panel and side menu panel
		initStatusBar();
		initSideMenu();
		
		// initialise layout
		GridBagConstraints gbcMaze = new GridBagConstraints();
		GridBagConstraints gbcStatus = new GridBagConstraints();
		GridBagConstraints gbcSide = new GridBagConstraints();
		
//		gbcStatus.insets = new Insets(5, 5, 5, 5);
		
		gbcMaze.fill = GridBagConstraints.BOTH;
		gbcSide.fill = GridBagConstraints.BOTH;
		gbcSide.fill = GridBagConstraints.BOTH;
		
		gbcStatus.gridy = 0;
		gbcStatus.gridx = 0;
		gbcStatus.gridheight = 1;
		gbcStatus.gridwidth = 10;
		gbcStatus.weightx = 0.5;
		gbcStatus.weighty = 0.01;
		
		gbcMaze.gridy = 1;
		gbcMaze.gridx = 0;
		gbcMaze.gridheight = 10;
		gbcMaze.gridwidth = 10;
		gbcMaze.weightx = 1.0;
		gbcMaze.weighty = 1.0;
		
		gbcSide.gridy = 1;
		gbcSide.gridx = 10;
		gbcSide.gridheight = 11;
		gbcSide.gridwidth = 2;
		gbcSide.weightx = 0.1;
		gbcSide.weighty = 0.45;
		
		this.add(mazePanel, gbcMaze);
		this.add(statusBar, gbcStatus);
		this.add(sideMenu, gbcSide);
		
		
		
		
		
//		gLayout.setAutoCreateGaps(true);
//		gLayout.setAutoCreateContainerGaps(true);
		
//		gLayout.setHorizontalGroup(
//				gLayout.createParallelGroup(Alignment.LEADING)
//					.addGroup(Alignment.TRAILING, gLayout.createSequentialGroup()
//						.addContainerGap()
//						.addGroup(gLayout.createParallelGroup(Alignment.LEADING)
//							.addComponent(mazePanel, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE)
//							.addComponent(statusBar, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE)
//						)
//						.addPreferredGap(ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
//						.addComponent(sideMenu, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
//						.addContainerGap()
//					)
//		);
//		gLayout.setVerticalGroup(
//				gLayout.createParallelGroup(Alignment.LEADING)
//					.addGroup(Alignment.TRAILING, gLayout.createSequentialGroup()
//						.addContainerGap()
//						.addGroup(gLayout.createParallelGroup(Alignment.TRAILING)
//							.addGroup(gLayout.createSequentialGroup()
//								.addComponent(statusBar, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
//								.addPreferredGap(ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
//								.addComponent(mazePanel, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
//							)
//							.addComponent(sideMenu, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
//						)
//						.addContainerGap()
//					)
//		);
		
//		gLayout.setHorizontalGroup(
//				gLayout.createSequentialGroup()
//					
//					.addGroup(gLayout.createParallelGroup()
//						.addComponent(statusBar)
//						.addComponent(mazePanel)
//					)
//					
//		);
//		
//		gLayout.setVerticalGroup(
//				gLayout.createSequentialGroup()
//					.addGroup(gLayout.createParallelGroup()
//						.addComponent(statusBar)
//					)
//					.addComponent(mazePanel)
//		);
		
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
				duration += (double)timer.getDelay()/1000;
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
		JLabel time = new JLabel("Time: " + (int)duration);
		statusBar.add(time, BorderLayout.CENTER);
		time.setHorizontalAlignment(JLabel.CENTER);

		// hints remaining
		JLabel hintsRemaining = new JLabel("Hints Remaining: ");
		statusBar.add(hintsRemaining, BorderLayout.EAST);

		//add(statusBar, BorderLayout.NORTH);
	}

	private void initSideMenu() {
		sideMenu = new JPanel();
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

		// TODO: clean this
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup().addGroup(groupLayout.createParallelGroup().addComponent(hintButton)
				.addComponent(pauseButton).addComponent(menuButton)));

		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(groupLayout.createSequentialGroup().addComponent(hintButton).addComponent(pauseButton)
						.addComponent(menuButton)));

		groupLayout.linkSize(hintButton, pauseButton, menuButton); // keep size of buttons consistent

		//add(sideMenu, BorderLayout.EAST);
	}
}
