import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class GameScreen extends JPanel implements ActionListener {

	private MainWindow mainWindow;
	
	// maze panel component
	private JPanel mazePanels; // maze panel controller
	private MazePanel mazePlaying;
	private JPanel mazeWon;
	private JPanel mazeLost;	
	private Maze maze;
	
	// components to be updated dynamically
	private JButton nextLevelButton; // part of maze panel
	private JLabel lostLevelLabel; // part of maze panel
	private JLabel levelLabel; // part of status bar
	private JLabel timeLabel; // part of status bar
	
	private Timer timer;
	private double duration;
	private int level;
	private int difficulty;

	public GameScreen(MainWindow mainWindow, int difficulty) {
		this.mainWindow = mainWindow;
		
		setLayout(new GridBagLayout()); 

		initMazePanels();
		initStatusBar();
		initSideBar();
		
		// update frequency
		timer = new Timer(200, this);
		timer.start();
		duration = 1;
		level = 1;
		this.difficulty = difficulty;
		
		switchToMazePlaying();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// refresh timer component
		if (duration >= 60) {
			timeLabel.setText("Time elapsed: " + (int)duration/60 + "m " + (int)(duration % 60) + "s");
		} else {
			timeLabel.setText("Time elapsed: " + (int)duration + "s");
		}
		
		if(e.getSource() == timer) {
			if (maze.isGameLost()) {
				mazePlaying.setRunning(false);
				switchToMazeLost();
			} else if (maze.isGameWon()) {
				mazePlaying.setRunning(false);
				timer.stop();
				level++;
				switchToMazeWon();
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
	
	private void switchToMazePlaying() {
		levelLabel.setText("Current level: " + level); // update levelLabel JLabel
		
		// initialise new game
		mazePlaying = new MazePanel(level, difficulty);
		maze = mazePlaying.getMaze();
		mazePlaying.setOpaque(false);
		
		Dimension size = new Dimension(Maze.MAZE_CELL_SIZE * maze.getGrid()[0].length, Maze.MAZE_CELL_SIZE * maze.getGrid().length);
		mazePanels.setPreferredSize(size);
		
		mazePanels.add(mazePlaying, "Playing");
		
		CardLayout cl = (CardLayout) mazePanels.getLayout();
		cl.show(mazePanels, "Playing");
	}

	private void switchToMazeWon() {
		nextLevelButton.setText("Continue to level " + level);
		CardLayout cl = (CardLayout) mazePanels.getLayout();
		cl.show(mazePanels, "Won");
	}
	
	private void switchToMazeLost() {
		lostLevelLabel.setText("You died at level " + level);
		CardLayout cl = (CardLayout) mazePanels.getLayout();
		cl.show(mazePanels, "Lost");
	}
	
	private void initMazePanels() {
		mazePanels = new JPanel(new CardLayout());
		mazePanels.setOpaque(false);
		initMazeWon();
		initMazeLost();
		
		// maze panel layout configuration
		GridBagConstraints gbcMaze = new GridBagConstraints();
		gbcMaze.insets = new Insets((int)(mainWindow.getHeight() * 0.1),0,0,0);
		gbcMaze.gridy = 1;
		gbcMaze.weightx = 1.0;
		gbcMaze.weighty = 0;
		
		add(mazePanels, gbcMaze);
	}

	private void initMazeWon() {
		mazeWon = new JPanel();
		mazeWon.setOpaque(false);
		
		nextLevelButton = new JButton("Continue to level " + level);
		nextLevelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchToMazePlaying();
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
		
		mazeWon.add(nextLevelButton);
		mazeWon.add(menuButton);
		mazePanels.add(mazeWon, "Won");
	}
	
	private void initMazeLost() {
		mazeLost = new JPanel();
		mazeLost.setOpaque(false);
		
		lostLevelLabel = new JLabel("You died at level " + level);
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
		
		mazeLost.add(lostLevelLabel);
		mazeLost.add(startButton);
		mazeLost.add(menuButton);
		mazePanels.add(mazeLost, "Lost");
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
		levelLabel = new JLabel();
		levelLabel.setForeground(Color.WHITE);

		// timeLabel
		timeLabel = new JLabel();
		timeLabel.setForeground(Color.WHITE);
		timeLabel.setHorizontalAlignment(JLabel.CENTER);

		statusBar.add(levelLabel, BorderLayout.WEST);
		statusBar.add(timeLabel, BorderLayout.CENTER);
		add(statusBar, gbcStatus);
	}

	private void initSideBar() {
		JPanel sideBar = new JPanel();
		sideBar.setOpaque(false);
		GroupLayout groupLayout = new GroupLayout(sideBar);
		sideBar.setLayout(groupLayout);
		
		// set parameters for sizing and positioning in main window
		GridBagConstraints gbcSide = new GridBagConstraints();
		gbcSide.fill = GridBagConstraints.BOTH;
		gbcSide.gridy = 1;
		gbcSide.gridx = 11;
		gbcSide.gridheight = 11;
		gbcSide.gridwidth = 2;
		gbcSide.weightx = 0.1;
		gbcSide.weighty = 0.45;
		
		add(sideBar, gbcSide);

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
					mazePlaying.setRunning(false);
				} else {
					pauseButton.setText("Pause");
					timer.start();
					mazePlaying.setRunning(true);;
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
				mazePlaying.setRunning(false);
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
	
	@Override
	public void setEnabled(boolean enabled) {
	    super.setEnabled(enabled);
	    for (Component component : getComponents()) {
	        component.setEnabled(enabled);
	    }
	}
}
