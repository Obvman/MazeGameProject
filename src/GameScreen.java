import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class GameScreen extends JPanel implements ActionListener {

	private MainWindow mainWindow;

	// constants for this class
	public static final int WATER = 1;
	public static final int FIRE = 2;
	public static final int AIR = 3;

	// screens
	private JPanel mazeScreens; // screen controller for maze panel
	private MazePanel mazePlaying;
	private boolean isPauseActive;
	private boolean isHelpActive;

	// maze elements
	private Maze maze;
	private int[] playerKeys;
	private int spellType;
	private int currLevel;
	private int difficulty;
	private int totalScore;
	private double duration;

	// status bar components to be updated dynamically
	private JLabel objective;
	private JLabel monstersSlain;
	private JLabel gemsCollected;
	private JLabel time;
	private JLabel level;

	private Timer updateTimer;
	
	/**
	 * Creates a GameScreen starting at level 1 and the given difficulty
	 * @param mainWindow The JFrame containing the GameScreen
	 * @param difficulty The difficulty level of the game
	 */
	public GameScreen(MainWindow mainWindow, int difficulty, int[] keys) {
		this.mainWindow = mainWindow;
		this.currLevel = 1;
		this.difficulty = difficulty;
		this.updateTimer = new Timer(10, this);
		this.playerKeys = keys;

		setLayout(new CardLayout());

		initSpellSelect();
	}

	/**
	 * Pauses the game and maze
	 */
	public void gamePause() {
		updateTimer.stop();
		if (mazePlaying != null) mazePlaying.setRunning(false);
	}

	/**
	 * Resumes the game and maze
	 */
	public void gameResume() {
		updateTimer.start();
		if (mazePlaying != null) mazePlaying.setRunning(true);
	}

	/**
	 * Displays the spell select screen
	 */
	public void switchToSpellSelect() {
		CardLayout cl = (CardLayout) this.getLayout();
		cl.show(this, "Spell");
	}

	/**
	 * Displays the maze game screen
	 */
	public void switchToMazeUI() {
		CardLayout cl = (CardLayout) this.getLayout();
		cl.show(this, "MazeUI");
	}

	/**
	 * Displays the win screen 
	 */
	public void switchToMazeWon() {
		initMazeWon();
		CardLayout cl = (CardLayout) this.getLayout();
		cl.show(this, "Won");
	}

	/**
	 * Displays the lose screen
	 */
	public void switchToMazeLost() {
		initMazeLost();
		CardLayout cl = (CardLayout) this.getLayout();
		cl.show(this, "Lost");
	}

	/**
	 * Displays the running maze in the maze panel
	 */
	public void switchToMazePlaying() {
		CardLayout cl = (CardLayout) mazeScreens.getLayout();
		cl.show(mazeScreens, "Playing");
	}

	/**
	 * Displays a pause screen over the maze panel
	 */
	public void switchToMazePaused() {
		CardLayout cl = (CardLayout) mazeScreens.getLayout();
		cl.show(mazeScreens, "Paused");
	}
	
	/**
	 * Displays the help screen over the maze panel
	 */
	public void switchToMazeHelp() {
		CardLayout cl = (CardLayout) mazeScreens.getLayout();
		cl.show(mazeScreens, "Help");
	}

	/**
	 * Refreshes the state of the game 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (maze.isGameWon()) {
			gamePause();
			switchToMazeWon();
		} else if (maze.isGameLost()) {
			gamePause();
			switchToMazeLost();
		} else {
			duration += (double)updateTimer.getDelay()/1000;

			// update status bar
			if (maze.getKey()) {
				objective.setText("Objective: Unlock the door and escape!");
			} else {
				objective.setText("Objective: Get to the key!");
			}
			monstersSlain.setText("" + maze.getNumMonstersKilled());
			gemsCollected.setText("" + maze.getNumGemsCollected());
			time.setText("" + (int)duration);
			level.setText("Level: " + currLevel);
		}
	}

	/**
	 * Paints a background image for the GameScreen
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image background = (new ImageIcon("resources/gameScreen_bg.jpg")).getImage();
		g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
	}

	/**
	 * Initialises elements of the spell select screen
	 */
	private void initSpellSelect() {
		JPanel spellSelect = new JPanel(new GridBagLayout());
		spellSelect.setOpaque(false);
		add(spellSelect, "Spell");

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		
		// choose your spell label
		gbc.gridx = 1;
		gbc.gridy = 0;
		JLabel chooseLabel = new JLabel("CHOOSE THE LOOK OF YOUR SPELL DURING COMBAT", SwingConstants.CENTER);
		chooseLabel.setForeground(Color.ORANGE);
		chooseLabel.setFont(new Font("Calibri", Font.BOLD, 16));
		spellSelect.add(chooseLabel, gbc);

		gbc.weighty = 0;
		gbc.gridy = 1;
		
		gbc.gridx = 0;
		JLabel waterTitle = new JLabel("Water", SwingConstants.CENTER);
		waterTitle.setForeground(Color.BLUE);
		spellSelect.add(waterTitle, gbc);
		
		gbc.gridx = 1;
		JLabel fireTitle = new JLabel("Fire", SwingConstants.CENTER);
		fireTitle.setForeground(Color.RED);
		spellSelect.add(fireTitle, gbc);
		
		gbc.gridx = 2;
		JLabel airLabel = new JLabel("Air", SwingConstants.CENTER);
		airLabel.setForeground(Color.WHITE);
		spellSelect.add(airLabel, gbc);
		
		gbc.weighty = 1;
		gbc.gridy = 2;
		int scaledSize = mainWindow.getWidth() / 4;

		// water spell
		gbc.gridx = 0;
		JPanel water = new JPanel();
		water.setOpaque(false);
		
		JButton waterButton = new JButton(getScaledImageIcon(new ImageIcon("resources/element-icon-water.png"), scaledSize, scaledSize));
		water.add(waterButton);
		waterButton.setContentAreaFilled(false);
		waterButton.setFocusPainted(false);
		waterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameScreen.this.spellType = 1;
				gameResume();
				initMazeUI();
				switchToMazeUI();
			}

		});
		spellSelect.add(water, gbc);

		// fire spell
		gbc.gridx = 1;
		JPanel fire = new JPanel();
		fire.setOpaque(false);
		JButton fireButton = new JButton(getScaledImageIcon(new ImageIcon("resources/element-icon-fire.png"), scaledSize, scaledSize));
		fire.add(fireButton);
		fireButton.setContentAreaFilled(false);
		fireButton.setFocusPainted(false);
		fireButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameScreen.this.spellType = 2;
				gameResume();
				initMazeUI();
				switchToMazeUI();
			}
		});
		spellSelect.add(fire, gbc);

		// air spell
		gbc.gridx = 2;
		JPanel air = new JPanel();
		air.setOpaque(false);
		JButton airButton = new JButton(getScaledImageIcon(new ImageIcon("resources/element-icon-air.png"), scaledSize, scaledSize));
		air.add(airButton);
		airButton.setContentAreaFilled(false);
		airButton.setFocusPainted(false);
		airButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameScreen.this.spellType = 3;
				gameResume();
				initMazeUI();
				switchToMazeUI();
			}

		});
		spellSelect.add(air, gbc);
	}

	/**
	 * Initialises elements of the maze UI (status bar, maze panel)
	 */
	private void initMazeUI() {
		JPanel mazeUI = new JPanel(new GridBagLayout());
		mazeUI.setOpaque(false);
		this.add(mazeUI, "MazeUI");

		// status bar
		GridBagConstraints gbcStatus = new GridBagConstraints();
		gbcStatus.fill = GridBagConstraints.BOTH;
		gbcStatus.weighty = 0.1;
		gbcStatus.weightx = 1;
		gbcStatus.gridheight = 1;
		
		JPanel statusBar = new JPanel(new BorderLayout());
		statusBar.setBorder(new EmptyBorder(0, 40, 0, 40)); // set left and right padding
		statusBar.setOpaque(false);
		
	
		// status bar components
		JPanel statusButtons = new JPanel(new GridBagLayout()); // vertically aligns when no gbc are defined
		statusButtons.setOpaque(false);
		statusBar.add(statusButtons, BorderLayout.WEST);
		
		JButton mainMenu = new JButton(new ImageIcon("resources/buttons/main_menu.png"));
		mainMenu.setContentAreaFilled(false);
		mainMenu.setMargin(new Insets(0, 0, 0 ,0));
		mainMenu.setFocusable(false);
		
		AbstractAction menuPressed = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gamePause();
				GameScreen.this.mainWindow.switchToMenu();
			}
		};
		mainMenu.addActionListener(menuPressed);
		// allow button to be activated by 'Esc' key as well as by clicking
		mainMenu.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
			.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0), "pressEsc");
		mainMenu.getActionMap().put("pressEsc", menuPressed);
		mainMenu.setFocusable(false);
		statusButtons.add(mainMenu);


		// pause button
		final JButton pause = new JButton(new ImageIcon("resources/buttons/pause.png"));
		pause.setContentAreaFilled(false);
		pause.setMargin(new Insets(0, 0, 0 ,0));
		pause.setFocusable(false);
		
		AbstractAction pausePressed = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (updateTimer.isRunning()) {
					if (!isHelpActive) {
						pause.setIcon(new ImageIcon("resources/buttons/unpause.png"));
						// allow user to press P to pause
						pause.getActionMap().clear();
						pause.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).clear();
						pause.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
							.put(KeyStroke.getKeyStroke(KeyEvent.VK_U,0), "pressPause");
						pause.getActionMap().put("pressPause", this);
						
						isPauseActive = true;
						gamePause();
						switchToMazePaused();
					}
				} else {
					if (isPauseActive) {
						pause.setIcon(new ImageIcon("resources/buttons/pause.png"));
						
						// allow user to press U to unpause
						pause.getActionMap().clear();
						pause.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).clear();
						pause.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
							.put(KeyStroke.getKeyStroke(KeyEvent.VK_P,0), "pressPause");
						pause.getActionMap().put("pressPause", this);
	
						isPauseActive = false;
						gameResume();
						switchToMazePlaying();
					}
				}
			}
		};
		pause.addActionListener(pausePressed);
		pause.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_P,0), "pressPause");
		
		pause.getActionMap().put("pressPause", pausePressed);
		
		statusButtons.add(pause);

		// help button
		JButton help = new JButton(new ImageIcon("resources/buttons/help.png"));
		help.setContentAreaFilled(false);
		help.setMargin(new Insets(0, 0, 0 ,0));
		help.setFocusable(false);
		
		AbstractAction helpPressed = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (updateTimer.isRunning()) {
					if (!isPauseActive) {
						isHelpActive = true;
						gamePause();
						switchToMazeHelp();
					}
				} else {
					if (isHelpActive) {
						isHelpActive = false;
						gameResume();
						switchToMazePlaying();
					}
				}
			}
		};
		help.addActionListener(helpPressed);
		// allow button to be activated by 'h' key as well as by clicking
		help.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_H,0), "pressH");
		help.getActionMap().put("pressH", helpPressed);
		statusButtons.add(help);
		
		// dynamically updated components
		JPanel statusFields = new JPanel(new GridBagLayout());
		statusFields.setOpaque(false);
		
		GridBagConstraints gbcFields = new GridBagConstraints();
		gbcFields.ipadx = 40;
		
		objective = new JLabel();
		objective.setForeground(Color.WHITE);
		statusFields.add(objective, gbcFields);

		monstersSlain = new JLabel(getScaledImageIcon(new ImageIcon("resources/monstersKilled.png"), Maze.MAZE_CELL_SIZE*2/3, Maze.MAZE_CELL_SIZE*2/3));
		monstersSlain.setForeground(Color.WHITE);
		statusFields.add(monstersSlain, gbcFields);

		gemsCollected = new JLabel(getScaledImageIcon(new ImageIcon("resources/gems/gems-6.png"), Maze.MAZE_CELL_SIZE*4/5, Maze.MAZE_CELL_SIZE*4/5));
		gemsCollected.setForeground(Color.WHITE);
		statusFields.add(gemsCollected, gbcFields);

		time = new JLabel(getScaledImageIcon(new ImageIcon("resources/clock.png"), Maze.MAZE_CELL_SIZE*4/5, Maze.MAZE_CELL_SIZE*4/5));
		time.setForeground(Color.WHITE);
		statusFields.add(time, gbcFields);

		level = new JLabel();
		level.setForeground(Color.WHITE);
		statusFields.add(level, gbcFields);
		
		statusBar.add(statusFields, BorderLayout.EAST);
		
		mazeUI.add(statusBar, gbcStatus);


		// maze panel
		GridBagConstraints gbcMaze = new GridBagConstraints();
		
		gbcMaze.fill = GridBagConstraints.NONE;
		gbcMaze.weightx = 0;
		gbcMaze.weighty = 0.9;
		gbcMaze.gridy = 1;
		gbcMaze.gridheight = 10;
		mazeScreens = new JPanel(new CardLayout());
		mazeScreens.setOpaque(false);
		mazeUI.add(mazeScreens, gbcMaze);

		// maze playing
		mazePlaying = new MazePanel(currLevel, difficulty, spellType, playerKeys);
		maze = mazePlaying.getMaze();
		mazeScreens.add(mazePlaying, "Playing");
		
		// pack the panel
		Dimension size = new Dimension(Maze.MAZE_CELL_SIZE * maze.getGrid()[0].length, Maze.MAZE_CELL_SIZE * maze.getGrid().length);
		mazeScreens.setPreferredSize(size);

		initHelp();
		
		JPanel mazePaused = new JPanel(new GridBagLayout());
		mazePaused.setOpaque(false);
		JLabel pausedLabel = new JLabel("Game Paused.", SwingUtilities.CENTER);
		pausedLabel.setForeground(Color.WHITE);
		mazePaused.add(pausedLabel);
		mazeScreens.add(mazePaused, "Paused");
	}
	
	/**
	 * Initialises elements of the help panel 
	 */
	private void initHelp() {
		JPanel mazeHelp = new JPanel(new GridBagLayout());
		mazeHelp.setBackground(Color.LIGHT_GRAY);
		mazeScreens.add(mazeHelp, "Help");
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.ipady = 30;
		gbc.gridy = 2;
		JLabel help = new JLabel("IN-GAME HELP SCREEN");
		mazeHelp.add(help, gbc);
		
		
	
		//OBJECTIVE
		gbc.ipady = 0;
		gbc.gridy = 3;
		JLabel o1 = new JLabel("OBJECTIVE");
		mazeHelp.add(o1, gbc);
		
		gbc.gridy = 4;
		JLabel o2 = new JLabel("The objective is to reach the end of the maze with the highest score.");
		mazeHelp.add(o2, gbc);
		
		gbc.gridy = 5;
		JLabel o3 = new JLabel("First retrieve the key from it's hiding place, then use it to unlock the steel door.");
		mazeHelp.add(o3, gbc);
		
		gbc.ipady = 10;
		gbc.gridy = 6;
		JLabel oi1 = new JLabel(new ImageIcon("resources/obj.png"));
		mazeHelp.add(oi1, gbc);
		
		//mazeHelp.add(Box.createRigidArea(new Dimension(0, 50)), gbc);
		
		//CONTROLS
		gbc.ipady = 0;
		gbc.gridy = 7;
		JLabel c1 = new JLabel("CONTROLS");
		mazeHelp.add(c1, gbc);
		
		gbc.gridy = 8;
		JLabel c2 = new JLabel("Use the arrow keys (default) to navigate the maze. Press space to shoot a spell.");
		mazeHelp.add(c2, gbc);
		
		gbc.gridy = 9;
		JLabel ci1 = new JLabel(new ImageIcon("resources/controls.png"));
		mazeHelp.add(ci1, gbc);
		
		//ENEMIES
		gbc.ipady = 0;
		gbc.gridy = 10;
		JLabel e1 = new JLabel("ENEMIES");
		mazeHelp.add(e1, gbc);
		
		gbc.gridy = 11;
		JLabel e2 = new JLabel("Spells are used to kill monsters and dragons, and destroy portals.");
		mazeHelp.add(e2, gbc);
		
		gbc.gridy = 12;
		JLabel e3 = new JLabel("Monsters are restriced to navigating the maze, whereas dragons can"
				+ "fly over the lava to find you.");
		mazeHelp.add(e3, gbc);
		
		gbc.ipady = 10;
		gbc.gridy = 13;
		JLabel ei1 = new JLabel(new ImageIcon("resources/enemies.png"));
		mazeHelp.add(ei1, gbc);
		
		//mazeHelp.add(Box.createRigidArea(new Dimension(0, 50)), gbc);
		
		//GEMS
		gbc.ipady = 0;
		gbc.ipady = 0;
		gbc.gridy = 16;
		JLabel g1 = new JLabel("Collect gems to increase your score.");
		mazeHelp.add(g1, gbc);
		
		gbc.gridy = 17;
		JLabel gi1 = new JLabel(new ImageIcon("resources/gemssmall.png"));
		mazeHelp.add(gi1, gbc);
		
		//mazeHelp.add(Box.createRigidArea(new Dimension(0, 50)), gbc);

		gbc.gridy = 20;
		JLabel ret = new JLabel("Press H to resume playing.");
		mazeHelp.add(ret, gbc);
		
	}
	
	/**
	 * Initialises elements of the win screen
	 */
	private void initMazeWon() {
		JPanel mazeWon = new JPanel(new GridBagLayout());
		mazeWon.setOpaque(false);
		add(mazeWon, "Won");

		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridy = 0;
		JLabel level = new JLabel("DEFEATED LEVEL " + currLevel, SwingConstants.CENTER);
		level.setFont(new Font("Herculanum", Font.PLAIN, 70));
		level.setForeground(new Color(255,215,0));
		mazeWon.add(level, gbc);

		gbc.gridy = 1;
		JLabel roundInfo = new JLabel("Round Results", SwingConstants.CENTER);
		roundInfo.setFont(new Font("Devanagari MT", Font.PLAIN, 30));
		roundInfo.setForeground(Color.WHITE);
		mazeWon.add(roundInfo, gbc);
        
		gbc.gridy = 2;
		JLabel timeTaken = new JLabel();
		if (duration >= 60) {
			timeTaken.setText("Time taken: " + (int)duration/60 + "m " + (int)duration%60 + "s");
		} else {
			timeTaken.setText("Time taken: " + (int)duration + "s");
		}
		timeTaken.setFont(new Font("Devanagari MT", Font.PLAIN, 15));
		timeTaken.setForeground(Color.WHITE);
		mazeWon.add(timeTaken, gbc);
		
		gbc.gridy = 3;
		JLabel numMonstersKilled = new JLabel("Monsters slain: " + maze.getNumMonstersKilled(), SwingConstants.CENTER);
		numMonstersKilled.setFont(new Font("Devanagari MT", Font.PLAIN, 15));
		numMonstersKilled.setForeground(Color.WHITE);
		mazeWon.add(numMonstersKilled, gbc);

		gbc.gridy = 4;
		JLabel numGemsCollected = new JLabel("Gems collected: " + maze.getNumGemsCollected(), SwingConstants.CENTER);
		numGemsCollected.setFont(new Font("Devanagari MT", Font.PLAIN, 15));
		numGemsCollected.setForeground(Color.WHITE);
		mazeWon.add(numGemsCollected, gbc);
		
		gbc.gridy = 5;
		int roundScore = (int) ((maze.getScore() + 200*(currLevel+difficulty)) * duration/60);
		totalScore += roundScore;
		JLabel roundScoreLabel = new JLabel("Round score: " + roundScore, SwingConstants.CENTER);
		roundScoreLabel.setFont(new Font("Devanagari MT", Font.PLAIN, 15));
		roundScoreLabel.setForeground(Color.WHITE);
		mazeWon.add(roundScoreLabel, gbc);
		
		
		gbc.gridy = 6;
		JLabel totalScore = new JLabel("Total score: " + this.totalScore, SwingConstants.CENTER);
		totalScore.setFont(new Font("Devanagari MT", Font.PLAIN, 15));
		totalScore.setForeground(Color.WHITE);
		mazeWon.add(totalScore, gbc);

		gbc.ipady = 0;
		gbc.gridy = 7;
		JPanel mazeWonButtons = new JPanel();
		mazeWonButtons.setOpaque(false);
		
		JButton nextLevelButton = new JButton(new ImageIcon("resources/buttons/next_level.png"));
		nextLevelButton.setContentAreaFilled(false);
		nextLevelButton.setMargin(new Insets(0, 0, 0, 0));
		nextLevelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currLevel++;
				duration = 0;
				gameResume();
				initMazeUI();
				switchToMazeUI();
			}
		});
		mazeWonButtons.add(nextLevelButton);

		JButton menuButton = new JButton(new ImageIcon("resources/buttons/main_menu.png"));
		menuButton.setContentAreaFilled(false);
		menuButton.setMargin(new Insets(0, 0, 0, 0));
		menuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.switchToMenu();
			}
		});
		mazeWonButtons.add(menuButton);
		mazeWon.add(mazeWonButtons, gbc);
	}

	/**
	 * Initialises elements of the lose screen
	 */
	private void initMazeLost() {
		JPanel mazeLost = new JPanel(new GridBagLayout());
		
		mazeLost.setOpaque(false);
		add(mazeLost, "Lost");
		
		GridBagConstraints gbc = new GridBagConstraints();

	    gbc.weighty = 0;
	    gbc.weightx = 1;
	    
		gbc.gridy = 0;
	    
		JLabel lostLevelLabel = new JLabel("YOU DIED AT LEVEL " + currLevel, SwingConstants.CENTER);
		lostLevelLabel.setForeground(new Color(255,215,0));
		lostLevelLabel.setFont(new Font("Herculanum", Font.PLAIN, 70));
		mazeLost.add(lostLevelLabel, gbc);
		
		gbc.gridy = 1;
		JLabel gameInfo= new JLabel("Game Results", SwingConstants.CENTER);
		gameInfo.setForeground(Color.WHITE);
		gameInfo.setFont(new Font("Devanagari MT", Font.PLAIN, 30));
		mazeLost.add(gameInfo, gbc);
		
		
		gbc.gridy = 2;
		JLabel faction = new JLabel("", SwingConstants.CENTER);
		if (spellType == 1) {
			faction.setText("Chosen spell: Water");
		} else if (spellType == 2) {
			faction.setText("Chosen spell: Fire");
		} else if (spellType == 3) {
			faction.setText("Chosen spell: Air");
		}
		faction.setFont(new Font("Devanagari MT", Font.PLAIN, 15));
		faction.setForeground(Color.WHITE);
		mazeLost.add(faction, gbc);
		
		gbc.gridy = 3;
		JLabel monstersSlain = new JLabel("Monsters slain: " + maze.getNumMonstersKilled(), SwingConstants.CENTER);
		monstersSlain.setFont(new Font("Devanagari MT", Font.PLAIN, 15));
		monstersSlain.setForeground(Color.WHITE);
		mazeLost.add(monstersSlain, gbc);
		
		gbc.gridy = 4;
		JLabel gemsCollected = new JLabel("Gems collected: " + maze.getNumGemsCollected(), SwingConstants.CENTER);
		gemsCollected.setFont(new Font("Devanagari MT", Font.PLAIN, 15));
		gemsCollected.setForeground(Color.WHITE);
		mazeLost.add(gemsCollected, gbc);
		
		gbc.gridy = 5;
		gbc.ipady = 5; // gaps between lines
		JLabel numGemsCollected = new JLabel("Final score: " + maze.getScore(), SwingConstants.CENTER);
		numGemsCollected.setFont(new Font("Devanagari MT", Font.PLAIN, 15));
		numGemsCollected.setForeground(Color.WHITE);
		mazeLost.add(numGemsCollected, gbc);
		
		gbc.gridy = 6;
		gbc.ipady = 0;
		JPanel mazeLostButtons = new JPanel();
		mazeLostButtons.setOpaque(false);
		
		JButton startButton = new JButton(new ImageIcon("resources/buttons/start_again.png"));
		startButton.setContentAreaFilled(false);
		startButton.setMargin(new Insets(0, 0, 0, 0));
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.switchToGame();
			}
		});
		mazeLostButtons.add(startButton);
		
		JButton menuButton = new JButton(new ImageIcon("resources/buttons/main_menu.png"));
		menuButton.setContentAreaFilled(false);
		menuButton.setMargin(new Insets(0, 0, 0, 0));
		menuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.switchToMenu();
			}
		});
		mazeLostButtons.add(menuButton);
		
		mazeLost.add(mazeLostButtons, gbc);
	}

	/**
	 * Returns a scaled instance of an ImageIcon
	 * @param img the ImageIcon to be scaled
	 * @param width the width of the new ImageIcon
	 * @param height the height of the new ImageIcon
	 * @return
	 */
	private ImageIcon getScaledImageIcon(ImageIcon img, int width, int height) {
		return new ImageIcon(img.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
	}
}
