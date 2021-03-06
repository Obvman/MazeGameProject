import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class GameScreen extends JPanel implements ActionListener {

	/**
	 * Creates a GameScreen initialized to level 1 and a given difficulty
	 * @param mainWindow The JFrame containing the GameScreen
	 * @param difficulty The difficulty level of the game
	 * @param keys The key bindings corresponding the Player's controls
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
	 * Displays a pause screen in the maze panel
	 */
	public void switchToMazePaused() {
		CardLayout cl = (CardLayout) mazeScreens.getLayout();
		cl.show(mazeScreens, "Paused");
	}

	/**
	 * Displays the help screen in the maze panel
	 */
	public void switchToMazeHelp() {
		CardLayout cl = (CardLayout) mazeScreens.getLayout();
		cl.show(mazeScreens, "Help");
	}

	/**
	 * Updates the state of the game by checking whether the game
	 * has been won or lost to switch to the appropriate panel, 
	 * otherwise refreshes the status bar of the game
	 * (objective, monsters slain, gems collected, time elapsed, level)
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
	 * Initializes the spell select JPanel
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
	 * Initializes the maze UI JPanel containing the status bar JPanel and maze JPanel
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

		monstersSlain = new JLabel(getScaledImageIcon(new ImageIcon("resources/monstersKilled.png"), -1, Maze.MAZE_CELL_SIZE));
		monstersSlain.setForeground(Color.WHITE);
		statusFields.add(monstersSlain, gbcFields);

		gemsCollected = new JLabel(getScaledImageIcon(new ImageIcon("resources/gems/gems-6.png"), Maze.MAZE_CELL_SIZE, Maze.MAZE_CELL_SIZE));
		gemsCollected.setForeground(Color.WHITE);
		statusFields.add(gemsCollected, gbcFields);

		time = new JLabel(getScaledImageIcon(new ImageIcon("resources/clock.png"), Maze.MAZE_CELL_SIZE, Maze.MAZE_CELL_SIZE));
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
		JLabel pausedLabel1 = new JLabel("Game paused. ", SwingUtilities.CENTER);
		pausedLabel1.setForeground(Color.WHITE);
		mazePaused.add(pausedLabel1);
		JLabel pausedLabel2 = new JLabel("Press 'U' to unpause.", SwingUtilities.CENTER);
		pausedLabel2.setForeground(Color.ORANGE);
		mazePaused.add(pausedLabel2);
		mazeScreens.add(mazePaused, "Paused");
	}

	/**
	 * Initializes the in-game help JPanel which contains information on
	 * objective, controls, enemies, and score
	 */
	private void initHelp() {
		JPanel mazeHelp = new JPanel(new GridBagLayout());
		mazeHelp.setBackground(Color.LIGHT_GRAY);
		mazeHelp.setOpaque(false);
		mazeScreens.add(mazeHelp, "Help");

		GridBagConstraints gbc = new GridBagConstraints();


		//OBJECTIVE
		gbc.gridy = 0;
		JLabel o1 = new JLabel("OBJECTIVE");
		o1.setForeground(Color.WHITE);
		mazeHelp.add(o1, gbc);

		gbc.gridy = 1;
		JLabel o3 = new JLabel("Retrieve the key and unlock the steel door the next level");
		o3.setForeground(Color.WHITE);
		mazeHelp.add(o3, gbc);

		gbc.gridy = 2;
		JPanel key = new JPanel();
		key.setOpaque(false);
		key.add(new JLabel(new ImageIcon("resources/tiles/key_tile.gif")));
		key.add(new JLabel(new ImageIcon("resources/tiles/leon_closed_door.png")));
		mazeHelp.add(key, gbc);

		gbc.gridy = 3;
		mazeHelp.add(Box.createRigidArea(new Dimension(0, 10)), gbc);

		//CONTROLS
		gbc.ipady = 0;
		gbc.gridy = 4;
		JLabel c1 = new JLabel("CONTROLS (DEFAULT)");
		c1.setForeground(Color.WHITE);
		mazeHelp.add(c1, gbc);

		gbc.gridy = 5;
		JLabel c2 = new JLabel("Use the arrow keys to move and the space bar to shoot a spell.");
		c2.setForeground(Color.WHITE);
		mazeHelp.add(c2, gbc);

		gbc.gridy = 6;
		JPanel controls = new JPanel();
		controls.setOpaque(false);
		controls.add(new JLabel(new ImageIcon("resources/arrows.png")));
		controls.add(new JLabel(new ImageIcon("resources/space.png")));
		controls.add(new JLabel(new ImageIcon("resources/spells/water2.png")));
		controls.add(new JLabel(new ImageIcon("resources/spells/fire2.png")));
		controls.add(new JLabel(new ImageIcon("resources/spells/air2.png")));
		mazeHelp.add(controls, gbc);

		gbc.gridy = 7;
		mazeHelp.add(Box.createRigidArea(new Dimension(0, 10)), gbc);

		//ENEMIES
		gbc.ipady = 0;
		gbc.gridy = 8;
		JLabel e1 = new JLabel("ENEMIES");
		e1.setForeground(Color.WHITE);
		mazeHelp.add(e1, gbc);

		gbc.gridy = 9;
		JLabel e2 = new JLabel("Use spells to kill enemies and destroy portals to stop them from respawning");
		e2.setForeground(Color.WHITE);
		mazeHelp.add(e2, gbc);

		gbc.gridy = 10;
		JLabel e3 = new JLabel("Beware! Dragons can fly over lava.");
		e3.setForeground(Color.WHITE);
		mazeHelp.add(e3, gbc);

		gbc.gridy = 11;
		JPanel monsters = new JPanel();
		monsters.setOpaque(false);
		monsters.add(new JLabel(new ImageIcon("resources/monster_down.png")));
		monsters.add(new JLabel(new ImageIcon("resources/dragon/dragonE4.png")));
		monsters.add(new JLabel(new ImageIcon("resources/blue_portal_32.gif")));
		mazeHelp.add(monsters, gbc);

		gbc.gridy = 12;
		mazeHelp.add(Box.createRigidArea(new Dimension(0, 10)), gbc);

		gbc.gridy = 13;
		JLabel d1 = new JLabel("SCORE");
		d1.setForeground(Color.WHITE);
		mazeHelp.add(d1,  gbc);

		gbc.gridy = 14; 
		JLabel d2 = new JLabel("Improve your score by collecting gems scattered throughout the maze");
		d2.setForeground(Color.WHITE);
		mazeHelp.add(d2, gbc);

		gbc.gridy = 15;
		JLabel d3 = new JLabel(new ImageIcon("resources/gemssmall.png"));
		d3.setForeground(Color.WHITE);
		mazeHelp.add(d3, gbc);

		gbc.gridy  = 16;
		mazeHelp.add(Box.createRigidArea(new Dimension(0, 10)), gbc);

		gbc.gridy = 17;
		JLabel ret = new JLabel("Press 'H' to resume playing.");
		ret.setForeground(Color.ORANGE);
		mazeHelp.add(ret, gbc);

	}

	/**
	 * Initializes the win screen JPanel which contains
	 * results on the round performance
	 */
	private void initMazeWon() {
		JPanel mazeWon = new JPanel(new GridBagLayout());
		mazeWon.setOpaque(false);
		add(mazeWon, "Won");

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridy = 0;
		JPanel titlePanel = new JPanel();
		titlePanel.setOpaque(false);
		titlePanel.add(new JLabel(new ImageIcon("resources/winscreen.png"), SwingConstants.CENTER));
		JLabel titleTextLabel = new JLabel("" + currLevel, SwingConstants.CENTER);
		titleTextLabel.setFont(new Font("Devanagari MT", Font.PLAIN, 90));
		titleTextLabel.setForeground(new Color(153, 0, 0));
		titlePanel.add(titleTextLabel);
		mazeWon.add(titlePanel);

		int roundScore = (int) ((maze.getScore() + 200*(currLevel+difficulty)) * duration/60);
		totalScore += roundScore;

		gbc.gridy = 1;
		JLabel totalScoreLabel = new JLabel("Total score: ", SwingConstants.CENTER);
		totalScoreLabel.setFont(new Font("Devanagari MT", Font.BOLD, 15));
		totalScoreLabel.setForeground(Color.WHITE);
		mazeWon.add(totalScoreLabel, gbc);

		gbc.gridy = 2;
		JLabel totalScore = new JLabel(Integer.toString(this.totalScore), SwingConstants.CENTER);
		totalScore.setFont(new Font("Devanagari MT", Font.BOLD, 30));
		totalScore.setForeground(Color.YELLOW);
		mazeWon.add(totalScore, gbc);

		gbc.gridy = 3;
		JLabel roundScoreLabel = new JLabel("Round score: ", SwingConstants.CENTER);
		roundScoreLabel.setFont(new Font("Devanagari MT", Font.BOLD, 15));
		roundScoreLabel.setForeground(Color.WHITE);
		mazeWon.add(roundScoreLabel, gbc);

		gbc.gridy = 4;
		JLabel score = new JLabel(Integer.toString(roundScore), SwingConstants.CENTER);
		score.setFont(new Font("Devanagari MT", Font.BOLD, 20));
		score.setForeground(Color.YELLOW);
		mazeWon.add(score, gbc);

		gbc.gridy = 5;
		JPanel resultsPanel = new JPanel(new FlowLayout(SwingConstants.CENTER, 20, 20));
		resultsPanel.setOpaque(false);

		ImageIcon clockImage = getScaledImageIcon(new ImageIcon("resources/clock.png"), -1, Maze.MAZE_CELL_SIZE);
		String timeTakenString = "Time taken: " + (int)duration + "s";
		if (duration >= 60) {
			timeTakenString = "Time taken: " + (int)duration/60 + "m " + (int)duration%60 + "s";
		}
		JLabel timeTaken = new JLabel(timeTakenString, clockImage, SwingConstants.CENTER);
		timeTaken.setHorizontalTextPosition(JLabel.LEFT);
		timeTaken.setFont(new Font("Devanagari MT", Font.BOLD, 15));
		timeTaken.setForeground(Color.WHITE);
		resultsPanel.add(timeTaken);

		ImageIcon monstersImage = getScaledImageIcon(new ImageIcon("resources/monstersKilled.png"), -1, Maze.MAZE_CELL_SIZE);
		JLabel monstersSlain = new JLabel("Monsters slain: " + maze.getNumMonstersKilled(), 
				monstersImage, SwingConstants.CENTER);
		monstersSlain.setHorizontalTextPosition(JLabel.LEFT);
		monstersSlain.setFont(new Font("Devanagari MT", Font.BOLD, 15));
		monstersSlain.setForeground(Color.WHITE);
		resultsPanel.add(monstersSlain, gbc);


		ImageIcon gemsImage = getScaledImageIcon(
				new ImageIcon("resources/gems/gems-6.png"), -1, Maze.MAZE_CELL_SIZE);
		JLabel gemsCollected = new JLabel("Gems collected: " + maze.getNumGemsCollected(), 
				gemsImage, SwingConstants.LEFT);
		gemsCollected.setHorizontalTextPosition(JLabel.LEFT);
		gemsCollected.setFont(new Font("Devanagari MT", Font.BOLD, 15));
		gemsCollected.setForeground(Color.WHITE);
		resultsPanel.add(gemsCollected, gbc);

		mazeWon.add(resultsPanel, gbc);

		gbc.gridy = 6;
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
	 * Initializes lose screen JPanel which contains 
	 * overall results for the game instance
	 */
	private void initMazeLost() {
		JPanel mazeLost = new JPanel(new GridBagLayout());

		mazeLost.setOpaque(false);
		add(mazeLost, "Lost");

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.weighty = 0;
		gbc.weightx = 1;

		gbc.gridy = 0;
		gbc.gridx = 0;

		JPanel titlePanel = new JPanel();
		titlePanel.setOpaque(false);
		titlePanel.add(new JLabel(new ImageIcon("resources/losescreen.png")));
		JLabel titleTextLabel = new JLabel("" + currLevel, SwingConstants.CENTER);
		titleTextLabel.setFont(new Font("Devanagari MT", Font.PLAIN, 90));
		titleTextLabel.setForeground(new Color(153, 0, 0));
		titlePanel.add(titleTextLabel);
		mazeLost.add(titlePanel);

		gbc.gridy = 2;
		gbc.ipady = 5; // gaps between lines
		JLabel numGemsCollected = new JLabel("Final score: ", SwingConstants.CENTER);
		numGemsCollected.setFont(new Font("Devanagari MT", Font.BOLD, 15));
		numGemsCollected.setForeground(Color.WHITE);
		mazeLost.add(numGemsCollected, gbc);

		gbc.gridy = 3;
		totalScore += maze.getScore();
		JLabel score = new JLabel("" + totalScore, SwingConstants.CENTER);
		score.setFont(new Font("Devanagari MT", Font.BOLD, 40));
		score.setForeground(Color.YELLOW);
		mazeLost.add(score, gbc);

		gbc.gridy = 4;
		JPanel resultsPanel = new JPanel(new FlowLayout(SwingConstants.CENTER, 20, 20));
		resultsPanel.setOpaque(false);
		ImageIcon factionImage = getScaledImageIcon(new ImageIcon("resources/element-icon-water.png"), -1, Maze.MAZE_CELL_SIZE);
		String factionText = "Chosen spell: Water";
		if (spellType == GameScreen.FIRE) {
			factionText = "Chosen spell: Fire"; 
			factionImage = getScaledImageIcon(new ImageIcon("resources/element-icon-fire.png"), -1, Maze.MAZE_CELL_SIZE);
		} else if (spellType == GameScreen.AIR) {
			factionText = "Chosen spell: Air";
			factionImage = getScaledImageIcon(new ImageIcon("resources/element-icon-air.png"), -1, Maze.MAZE_CELL_SIZE);
		}
		JLabel faction = new JLabel(factionText, factionImage, SwingConstants.RIGHT);
		faction.setHorizontalTextPosition(JLabel.LEFT);
		faction.setFont(new Font("Devanagari MT", Font.BOLD, 15));
		faction.setForeground(Color.WHITE);
		resultsPanel.add(faction, gbc);

		ImageIcon monstersImage = getScaledImageIcon(new ImageIcon("resources/monstersKilled.png"), -1, Maze.MAZE_CELL_SIZE);
		JLabel monstersSlain = new JLabel("Monsters slain: " + maze.getNumMonstersKilled(), 
				monstersImage, SwingConstants.CENTER);
		monstersSlain.setHorizontalTextPosition(JLabel.LEFT);
		monstersSlain.setFont(new Font("Devanagari MT", Font.BOLD, 15));
		monstersSlain.setForeground(Color.WHITE);
		resultsPanel.add(monstersSlain, gbc);

		ImageIcon gemsImage = getScaledImageIcon(new ImageIcon("resources/gems/gems-6.png"), -1, Maze.MAZE_CELL_SIZE);
		JLabel gemsCollected = new JLabel("Gems collected: " + maze.getNumGemsCollected(), 
				gemsImage, SwingConstants.LEFT);
		gemsCollected.setHorizontalTextPosition(JLabel.LEFT);
		gemsCollected.setFont(new Font("Devanagari MT", Font.BOLD, 15));
		gemsCollected.setForeground(Color.WHITE);
		resultsPanel.add(gemsCollected, gbc);

		mazeLost.add(resultsPanel, gbc);

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
	 * @return the scaled ImageIcon
	 */
	private ImageIcon getScaledImageIcon(ImageIcon img, int width, int height) {
		return new ImageIcon(img.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
	}

	// public constants for the spells
	public static final int WATER = 1;
	public static final int FIRE = 2;
	public static final int AIR = 3;

	// container JFrame
	private MainWindow mainWindow;

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
}