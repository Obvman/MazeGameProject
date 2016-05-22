import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.*;

@SuppressWarnings("serial")
public class GameScreen extends JPanel implements ActionListener {

	private MainWindow mainWindow;
	
	private JPanel spellSelect;
	
	// maze panel component
	private JPanel mazePanels; // maze panel controller
	private MazePanel mazePlaying;
	private JPanel mazeWon;
	private JPanel mazeLost;	
	private Maze maze;
	
	// components to be updated dynamically
	private JButton nextLevelButton; // part of maze panel
	private JLabel lostLevelLabel; // part of maze panel
	
	JPanel statusBar;
	private JLabel levelLabel; // part of status bar
	private JLabel scoreLabel; // part of status bar
	private JLabel timeLabel; // part of status bar
	private JLabel objectLabel; //part of status bar
	
	private Timer timer;
	private double duration;
	private int level;
	private int difficulty;
	private int totalScore;
	private int spellType;

	public GameScreen(MainWindow mainWindow, int difficulty) {
		this.mainWindow = mainWindow;
		
		setLayout(new GridBagLayout()); 

		initSpellSelect();
		
		// update frequency
		timer = new Timer(200, this);
		duration = 1;
		level = 1;
		this.difficulty = difficulty;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// refresh timer component
		if (duration >= 60) {
			timeLabel.setText("Time elapsed: " + (int)duration/60 + "m " + (int)(duration % 60) + "s");
		} else {
			timeLabel.setText("Time elapsed: " + (int)duration + "s");
		}
		
		// refresh score component
		scoreLabel.setText("Round Score: " + maze.getScore());
		if(maze.getKey()){
			objectLabel.setText("Target: Open the door");
		}else{
			objectLabel.setText("Target: Collect the key");
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
		timer.start();
		levelLabel.setText("Current level: " + level); 
		
		// initialise new game
		mazePlaying = new MazePanel(level, difficulty, spellType);
		maze = mazePlaying.getMaze();
		mazePlaying.setOpaque(false);
		
		Dimension size = new Dimension(Maze.MAZE_CELL_SIZE * maze.getGrid()[0].length, Maze.MAZE_CELL_SIZE * maze.getGrid().length);
		mazePanels.setPreferredSize(size);
		
		mazePanels.add(mazePlaying, "Playing");
		
		showStatusBar();
		CardLayout cl = (CardLayout) mazePanels.getLayout();
		cl.show(mazePanels, "Playing");
	}

	private void switchToMazeWon() {
		initMazeWon();
		CardLayout cl = (CardLayout) mazePanels.getLayout();
		cl.show(mazePanels, "Won");
	}
	
	private void switchToMazeLost() {
		initMazeLost();
		CardLayout cl = (CardLayout) mazePanels.getLayout();
		cl.show(mazePanels, "Lost");
	}
	
	private void initMazePanels() {
		mazePanels = new JPanel(new CardLayout());
		mazePanels.setOpaque(false);
		
		// maze panel layout configuration
		GridBagConstraints gbcMaze = new GridBagConstraints();
		gbcMaze.gridy = 1;
		gbcMaze.weightx = 1;
		
		add(mazePanels, gbcMaze);
	}
	
	private void initSpellSelect() {
		spellSelect = new JPanel(new GridBagLayout());
		spellSelect.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		add(spellSelect, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weighty = 0.5;
		JLabel chooseLabel = new JLabel("CHOOSE YOUR FACTION...");
		chooseLabel.setForeground(Color.ORANGE);
		chooseLabel.setFont(new Font("Calibri", Font.BOLD, 16));
		chooseLabel.setHorizontalAlignment(JLabel.CENTER);
		spellSelect.add(chooseLabel, gbc);
		
		gbc.weighty = 1;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.BOTH;
		
		int scaledSize = mainWindow.getWidth() / 3;
		
		JButton water = new JButton(getScaledImageIcon(new ImageIcon("resources/element-icon-water.png"), scaledSize, scaledSize));
		water.setContentAreaFilled(false);
		water.setFocusPainted(false);
		water.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameScreen.this.spellType = 1;
				initMazePanels();
				initStatusBar();
				initSideBar();
				spellSelect.setVisible(false);
				switchToMazePlaying();
			}
			
		});
		gbc.gridx = 0;
		spellSelect.add(water, gbc);
		
		JButton fire = new JButton(getScaledImageIcon(new ImageIcon("resources/element-icon-fire.png"), scaledSize, scaledSize));
		fire.setContentAreaFilled(false);
		fire.setFocusPainted(false);
		fire.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameScreen.this.spellType = 2;
				initMazePanels();
				initStatusBar();
				initSideBar();
				spellSelect.setVisible(false);
				switchToMazePlaying();
			}
		});
		gbc.gridx = 1;
		spellSelect.add(fire, gbc);
		
		JButton air = new JButton(getScaledImageIcon(new ImageIcon("resources/element-icon-air.png"), scaledSize, scaledSize));
		air.setContentAreaFilled(false);
		air.setFocusPainted(false);
		air.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameScreen.this.spellType = 3;
				initMazePanels();
				initStatusBar();
				initSideBar();
				spellSelect.setVisible(false);
				switchToMazePlaying();
			}
			
		});
		gbc.gridx = 2;
		spellSelect.add(air, gbc);
	}

	private void initMazeWon() {
		hideStatusBar();
		mazeWon = new JPanel(new GridBagLayout());
		mazeWon.setOpaque(false);
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridy = 0;
		JLabel level = new JLabel("You defeated level " + (this.level-1), SwingConstants.CENTER);
		level.setForeground(Color.WHITE);
		mazeWon.add(level, gbc);
		
		gbc.gridy = 1;
		JLabel roundInfo = new JLabel("Round Results", SwingConstants.CENTER);
		roundInfo.setForeground(Color.WHITE);
		Font font = roundInfo.getFont();
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		roundInfo.setFont(font.deriveFont(attributes));
		mazeWon.add(roundInfo, gbc);
		
		gbc.gridy = 2;
		JLabel numMonstersKilled = new JLabel("Monsters slain: " + maze.getNumMonstersKilled(), SwingConstants.CENTER);
		numMonstersKilled.setForeground(Color.WHITE);
		mazeWon.add(numMonstersKilled, gbc);
		
		gbc.gridy = 3;
		JLabel numGemsCollected = new JLabel("Gems collected: " + maze.getNumGemsCollected(), SwingConstants.CENTER);
		numGemsCollected.setForeground(Color.WHITE);
		mazeWon.add(numGemsCollected, gbc);

		gbc.gridy = 4;
		totalScore += maze.getScore();
		JLabel score = new JLabel("Total score: " + totalScore, SwingConstants.CENTER);
		score.setForeground(Color.WHITE);
		mazeWon.add(score, gbc);
		
		gbc.gridy = 5;
		nextLevelButton = new JButton(new ImageIcon("resources/next_level.png"));
		nextLevelButton.setContentAreaFilled(false);
		nextLevelButton.setMargin(new Insets(0, 0, 0, 0));
		nextLevelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchToMazePlaying();
			}
		});
		mazeWon.add(nextLevelButton, gbc);
		
		gbc.gridy = 6;
		JButton menuButton = new JButton(new ImageIcon("resources/main_menu.png"));
		menuButton.setContentAreaFilled(false);
		menuButton.setMargin(new Insets(0, 0, 0, 0));
		menuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.switchToMenu();
			}
		});
		mazeWon.add(menuButton, gbc);
		
		mazePanels.add(mazeWon, "Won");
	}
	
	private void initMazeLost() {
		hideStatusBar();
		mazeLost = new JPanel(new GridBagLayout());
		mazeLost.setOpaque(false);
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridy = 0;
		lostLevelLabel = new JLabel("You died at level " + level + "...", SwingConstants.CENTER);
		lostLevelLabel.setForeground(Color.WHITE);
		mazeLost.add(lostLevelLabel, gbc);
		
		gbc.gridy = 1;
		JLabel gameInfo= new JLabel("Game Results", SwingConstants.CENTER);
		gameInfo.setForeground(Color.WHITE);
		Font font = gameInfo.getFont();
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		gameInfo.setFont(font.deriveFont(attributes));
		mazeLost.add(gameInfo, gbc);
		
		gbc.gridy = 2;
		JLabel faction = new JLabel("", SwingConstants.CENTER);
		if (spellType == 1) {
			faction.setText("Faction: Water");
		} else if (spellType == 2) {
			faction.setText("Faction: Fire");
		} else if (spellType == 3) {
			faction.setText("Faction: Air");
		}
		faction.setForeground(Color.WHITE);
		mazeLost.add(faction, gbc);
		
		gbc.gridy = 3;
		JLabel monstersSlain = new JLabel("Monsters slain: " + maze.getNumMonstersKilled(), SwingConstants.CENTER);
		monstersSlain.setForeground(Color.WHITE);
		mazeLost.add(monstersSlain, gbc);
		
		gbc.gridy = 4;
		JLabel gemsCollected = new JLabel("Gems collected: " + maze.getNumGemsCollected(), SwingConstants.CENTER);
		gemsCollected.setForeground(Color.WHITE);
		mazeLost.add(gemsCollected, gbc);
		
		gbc.gridy = 5;
		JLabel numGemsCollected = new JLabel("Final score: " + maze.getScore(), SwingConstants.CENTER);
		numGemsCollected.setForeground(Color.WHITE);
		mazeLost.add(numGemsCollected, gbc);
		
		gbc.gridy = 6;
		JButton startButton = new JButton(new ImageIcon("resources/start_again.png"));
		startButton.setContentAreaFilled(false);
		startButton.setMargin(new Insets(0, 0, 0, 0));
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.switchToGame();
			}
		});
		mazeLost.add(startButton, gbc);
		
		gbc.gridy = 7;
		JButton menuButton = new JButton(new ImageIcon("resources/main_menu.png"));
		menuButton.setContentAreaFilled(false);
		menuButton.setMargin(new Insets(0, 0, 0, 0));
		menuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.switchToMenu();
			}
		});
		mazeLost.add(menuButton, gbc);
		
		mazePanels.add(mazeLost, "Lost");
	}

	private void initStatusBar() {
		statusBar = new JPanel(new CardLayout());
		statusBar.setOpaque(false);
		GridBagConstraints gbcStatus = new GridBagConstraints();
		gbcStatus.insets = new Insets(5,20,15,20);
		gbcStatus.fill = GridBagConstraints.BOTH;
		add(statusBar, gbcStatus);
		
		// shown panel
		JPanel statusBarShow = new JPanel();
		statusBarShow.setOpaque(false);
		
		statusBarShow.setLayout(new BoxLayout(statusBarShow, BoxLayout.X_AXIS));
		statusBarShow.setPreferredSize(new Dimension(0, 40)); // check
		
		// level
		levelLabel = new JLabel();
		levelLabel.setForeground(Color.WHITE);
		
		// score
		scoreLabel = new JLabel();
		scoreLabel.setForeground(Color.WHITE);
		
		// time
		timeLabel = new JLabel();
		timeLabel.setForeground(Color.WHITE);
		
		objectLabel = new JLabel();
		objectLabel.setForeground(Color.WHITE);

		statusBarShow.add(levelLabel);
		statusBarShow.add(Box.createGlue());
		statusBarShow.add(scoreLabel);
		statusBarShow.add(Box.createGlue());
		statusBarShow.add(timeLabel);
		statusBarShow.add(Box.createGlue());
		statusBarShow.add(objectLabel);
		
		
		statusBar.add(statusBarShow, "Show");
		
		// hidden panel
		JPanel statusBarHide = new JPanel();
		statusBarHide.setOpaque(false);
		statusBar.add(statusBarHide, "Hide");
		
		showStatusBar();
	}
	
	public void showStatusBar() {
		CardLayout cl = (CardLayout) statusBar.getLayout();
		cl.show(statusBar, "Show");
	}
	
	public void hideStatusBar() {
		CardLayout cl = (CardLayout) statusBar.getLayout();
		cl.show(statusBar, "Hide");
	}

	private void initSideBar() {
		JPanel sideBar = new JPanel();
		sideBar.setOpaque(false);
		GroupLayout groupLayout = new GroupLayout(sideBar);
		sideBar.setLayout(groupLayout);
		sideBar.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
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
		final JButton pauseButton = new JButton(new ImageIcon("resources/pause.png"));
		pauseButton.setContentAreaFilled(false);
		pauseButton.setMargin(new Insets(0, 0, 0, 0));
		pauseButton.setFocusable(false);
		pauseButton.setToolTipText("Pause/unpause the game and timer");
		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (timer.isRunning()) {
					pauseButton.setIcon(new ImageIcon("resources/unpause.png"));
					timer.stop();
					mazePlaying.setRunning(false);
				} else {
					pauseButton.setIcon(new ImageIcon("resources/pause.png"));
					timer.start();
					mazePlaying.setRunning(true);;
				}
			}
		});

		// menu button
		JButton menuButton = new JButton(new ImageIcon("resources/main_menu.png"));
		menuButton.setContentAreaFilled(false);
		menuButton.setMargin(new Insets(0, 0, 0, 0));
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
	
	private ImageIcon getScaledImageIcon(ImageIcon img, int width, int height) {
		Image image = img.getImage();
		Image newimg = image.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(newimg);
	}
}
