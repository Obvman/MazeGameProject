import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class GameScreen extends JPanel implements ActionListener {

	private MainWindow mainWindow;
	
	// panels making up the mazePanel
	private Maze maze;
	private JPanel mazePanel;
	private MazePanel mazeGame;
	private JPanel mazeScorePanel;
	private JPanel mazeLostPanel;	
	
	// components to be updated dynamically
	JButton nextLevelButton; // maze panel
	JLabel lostLevelLabel; // maze panel
	JLabel level; // status bar
	JLabel time; // status bar
	
	private Timer timer;
	private double duration;
	private int currLevel;

	public GameScreen(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		
		setLayout(new GridBagLayout()); 

		initMazePanel();
		initStatusBar();
		initSideMenu();
		
		// initial stats
		currLevel = 1;
		
		// update frequency
		timer = new Timer(30, this);
		timer.start();
		
		switchToMazePlayingPanel();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// refresh timer component
		time.setText("Time: " + (int)duration);
			
		if(e.getSource() == timer) {
			if (maze.isGameLost()) {
				mazeGame.setRunning(false);
				switchToMazeLostPanel();
			} else if (maze.isGameWon()) {
				mazeGame.setRunning(false);
				timer.stop();
				currLevel++;
				switchToMazeFinishedPanel();
			} else {
				duration += (double)timer.getDelay()/1000;
			}
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image background = (new ImageIcon("resources/gameScreen_bg.jpg")).getImage(); // TODO: move into field so we dont reload
	    g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
	}
	
	private void switchToMazePlayingPanel() {
		level.setText("Level: " + currLevel); // update level JLabel
		
		// initialise new game
		mazeGame = new MazePanel();
		mazeGame.setOpaque(false);
		mazePanel.add(mazeGame, "Playing");
		maze = mazeGame.getMaze();
		
		CardLayout cl = (CardLayout) mazePanel.getLayout();
		cl.show(mazePanel, "Playing");
	}

	private void switchToMazeFinishedPanel() {
		nextLevelButton.setText("Continue to level " + currLevel);
		CardLayout cl = (CardLayout) mazePanel.getLayout();
		cl.show(mazePanel, "Finished");
	}
	
	private void switchToMazeLostPanel() {
		lostLevelLabel.setText("You died at level " + currLevel);
		CardLayout cl = (CardLayout) mazePanel.getLayout();
		cl.show(mazePanel, "Lost");
	}
	
	private void initMazePanel() {
		mazePanel = new JPanel(new CardLayout());
		mazePanel.setOpaque(false);
		Dimension size = new Dimension(Maze.MAZE_CELL_SIZE * Maze.MAZE_SIZE_2, Maze.MAZE_CELL_SIZE * Maze.MAZE_SIZE_1);
		mazePanel.setPreferredSize(size);
		initMazeScorePanel();
		initMazeLostPanel();
		
		// maze panel layout configuration
		GridBagConstraints gbcMaze = new GridBagConstraints();
		gbcMaze.insets = new Insets((int)(mainWindow.getHeight() * 0.1),0,0,0);
		gbcMaze.gridy = 1;
		gbcMaze.weightx = 1.0;
		gbcMaze.weighty = 0;
		
		add(mazePanel, gbcMaze);
	}

	private void initMazeScorePanel() {
		mazeScorePanel = new JPanel();
		mazeScorePanel.setOpaque(false);
		
		nextLevelButton = new JButton("Continue to level " + currLevel);
		nextLevelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchToMazePlayingPanel();
				timer.start();
			}
		});
		
		
		JButton menuButton = new JButton("Back to Main Menu");
		menuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.switchToMenu();
			}
		});
		
		mazeScorePanel.add(nextLevelButton);
		mazeScorePanel.add(menuButton);
		mazePanel.add(mazeScorePanel, "Finished");
	}
	
	private void initMazeLostPanel() {
		mazeLostPanel = new JPanel();
		mazeLostPanel.setOpaque(false);
		
		lostLevelLabel = new JLabel("You died at level " + currLevel);
		lostLevelLabel.setForeground(Color.WHITE);
		
		JButton startButton = new JButton("Start Again");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.switchToGame();
			}
		});
		
		
		JButton menuButton = new JButton("Back to Main Menu");
		menuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.switchToMenu();
			}
		});
		
		mazeLostPanel.add(lostLevelLabel);
		mazeLostPanel.add(startButton);
		mazeLostPanel.add(menuButton);
		mazePanel.add(mazeLostPanel, "Lost");
	}

	private void initStatusBar() {
		JPanel statusBar = new JPanel();
		statusBar.setOpaque(false);
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
		level = new JLabel("Level: " + currLevel);
		level.setForeground(Color.WHITE);

		// time
		time = new JLabel("Time: " + (int)duration);
		time.setForeground(Color.WHITE);
		time.setHorizontalAlignment(JLabel.CENTER);

		statusBar.add(level, BorderLayout.WEST);
		statusBar.add(time, BorderLayout.CENTER);
		add(statusBar, gbcStatus);
	}

	private void initSideMenu() {
		JPanel sideMenu = new JPanel();
		sideMenu.setOpaque(false);
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
		
		add(sideMenu, gbcSide);

		// to check
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);

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
						.addComponent(pauseButton)
						.addComponent(menuButton)
				)
		);

		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(pauseButton)
						.addComponent(menuButton)
				)
		);

		groupLayout.linkSize(pauseButton, menuButton); // keep size of buttons consistent
	}
}
