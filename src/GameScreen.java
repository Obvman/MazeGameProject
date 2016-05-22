import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.*;

@SuppressWarnings("serial")
public class GameScreen extends JPanel implements ActionListener {

	private MainWindow mainWindow;

	// constants for this class
	public static final int WATER = 1;
	public static final int FIRE = 2;
	public static final int AIR = 3;

	// screens
	private JPanel spellSelect;
	private JPanel mazeWon;
	private JPanel mazeLost;
	private JPanel help;

	private JPanel mazeUI;
	private JPanel mazeScreens; // screen controller for mazePlaying & mazePaused
	private MazePanel mazePlaying;
	private JPanel mazePaused;

	// model
	private Maze maze;
	private int totalScore;
	private int currLevel;
	private int difficulty;
	private int spellType;
	private double duration;

	// status bar components to be updated dynamically
	JLabel objective;
	JLabel monstersSlain;
	JLabel gemsCollected;
	JLabel time;
	JLabel level;

	// GameScreen controller
	private Timer timer;

	public GameScreen(MainWindow mainWindow, int difficulty) {
		this.mainWindow = mainWindow;
		this.currLevel = 1;
		this.difficulty = difficulty;
		this.timer = new Timer(10, this);

		setLayout(new CardLayout());

		initSpellSelect();
		initHelp();
	}

	public void gamePause() {
		timer.stop();
		if (mazePlaying != null) mazePlaying.setRunning(false);
	}

	public void gameResume() {
		timer.start();
		if (mazePlaying != null) mazePlaying.setRunning(true);
	}

	public void switchToSpellSelect() {
		CardLayout cl = (CardLayout) this.getLayout();
		cl.show(this, "Spell");
	}

	public void switchToHelp() {
		CardLayout cl = (CardLayout) this.getLayout();
		cl.show(this, "Help");
	}

	public void switchToMazeUI() {
		CardLayout cl = (CardLayout) this.getLayout();
		cl.show(this, "MazeUI");
	}

	// controlled by mazeUI
	public void switchToMazeWon() {
		initMazeWon();
		CardLayout cl = (CardLayout) this.getLayout();
		cl.show(this, "Won");
	}

	// controlled by mazeUI
	public void switchToMazeLost() {
		initMazeLost();
		CardLayout cl = (CardLayout) this.getLayout();
		cl.show(this, "Lost");
	}

	// controlled by mazeScreens
	public void switchToMazePlaying() {
		CardLayout cl = (CardLayout) mazeScreens.getLayout();
		cl.show(mazeScreens, "Playing");
	}

	// controlled by mazeScreens
	public void switchToMazePaused() {
		CardLayout cl = (CardLayout) mazeScreens.getLayout();
		cl.show(mazeScreens, "Paused");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (maze.isGameWon()) {
			gamePause();
			switchToMazeWon();
		} else if (maze.isGameLost()) {
			gamePause();
			switchToMazeLost();
		} else {
			duration += (double)timer.getDelay()/1000;

			// update status bar
			if (maze.isKeyAcquired()) {
				objective.setText("Objective: Unlock the door and escape!");
			} else {
				objective.setText("Objective: Get to the key!");
			}
			monstersSlain.setText("Monsters slain: " + maze.getNumMonstersKilled());
			gemsCollected.setText("Gems collected: " + maze.getNumGemsCollected());
			time.setText("Time elapsed: " + (int)duration);
			level.setText("Level: " + currLevel);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image background = (new ImageIcon("resources/gameScreen_bg.jpg")).getImage();
		g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
	}

	private void initSpellSelect() {
		spellSelect = new JPanel(new GridBagLayout());
		spellSelect.setOpaque(false);
		add(spellSelect, "Spell");

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;

		// choose your spell label
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weighty = 0.5;
		JLabel chooseLabel = new JLabel("CHOOSE YOUR SPELL...");
		chooseLabel.setForeground(Color.ORANGE);
		chooseLabel.setFont(new Font("Calibri", Font.BOLD, 16));
		chooseLabel.setHorizontalAlignment(JLabel.CENTER);
		spellSelect.add(chooseLabel, gbc);

		gbc.gridy = 1;
		gbc.weighty = 1;
		int scaledSize = mainWindow.getWidth() / 3;

		// water spell
		gbc.gridx = 0;
		JButton water = new JButton(getScaledImageIcon(new ImageIcon("resources/element-icon-water.png"), scaledSize, scaledSize));
		water.setContentAreaFilled(false);
		water.setFocusPainted(false);
		water.addActionListener(new ActionListener() {
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
		JButton fire = new JButton(getScaledImageIcon(new ImageIcon("resources/element-icon-fire.png"), scaledSize, scaledSize));
		fire.setContentAreaFilled(false);
		fire.setFocusPainted(false);
		fire.addActionListener(new ActionListener() {
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
		JButton air = new JButton(getScaledImageIcon(new ImageIcon("resources/element-icon-air.png"), scaledSize, scaledSize));
		air.setContentAreaFilled(false);
		air.setFocusPainted(false);
		air.addActionListener(new ActionListener() {
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

	private void initHelp() {

	}

	private void initMazeUI() {
		mazeUI = new JPanel(new GridBagLayout());
		mazeUI.setOpaque(false);
		add(mazeUI, "MazeUI");

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;

		// status bar
		gbc.weighty = 0.1;
		JPanel statusBar = new JPanel();
		statusBar.setBorder(BorderFactory.createLineBorder(Color.RED));
		statusBar.setOpaque(false);
		mazeUI.add(statusBar, gbc);

		// status bar
		// TODO: remove place holder text
		// main menu button
		JButton mainMenu = new JButton(new ImageIcon("resources/main_menu.png"));
		mainMenu.setContentAreaFilled(false);
		mainMenu.setMargin(new Insets(0, 0, 0 ,0));
		mainMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameScreen.this.mainWindow.switchToMenu();
			}
		});
		statusBar.add(mainMenu);


		// pause button
		JButton pause = new JButton(new ImageIcon("resources/pause.png"));
		pause.setContentAreaFilled(false);
		pause.setMargin(new Insets(0, 0, 0 ,0));
		pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (timer.isRunning()) {
					gamePause();
					switchToMazePaused();
				} else {
					gameResume();
					switchToMazePlaying();
				}
			}
		});
		statusBar.add(pause);

		// help button
		JButton help = new JButton(new ImageIcon("resources/help.png"));
		help.setContentAreaFilled(false);
		help.setMargin(new Insets(0, 0, 0 ,0));
		help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("This should be similar to the tutorial screen");
			}
		});
		statusBar.add(help);


		// dynamically updated components
		objective = new JLabel();
		objective.setForeground(Color.WHITE);
		statusBar.add(objective);

		monstersSlain = new JLabel();
		monstersSlain.setForeground(Color.WHITE);
		statusBar.add(monstersSlain);

		gemsCollected = new JLabel();
		gemsCollected.setForeground(Color.WHITE);
		statusBar.add(gemsCollected);

		time = new JLabel();
		time.setForeground(Color.WHITE);
		statusBar.add(time);

		level = new JLabel();
		level.setForeground(Color.WHITE);
		statusBar.add(level);


		// maze panel
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		gbc.weighty = 0.9;
		gbc.gridy = 1;
		mazeScreens = new JPanel(new CardLayout());
		mazeUI.add(mazeScreens, gbc);

		// maze playing
		mazePlaying = new MazePanel(currLevel, difficulty, spellType);
		maze = mazePlaying.getMaze();
		
		// pack the panel
		Dimension size = new Dimension(Maze.MAZE_CELL_SIZE * maze.getGrid()[0].length, Maze.MAZE_CELL_SIZE * maze.getGrid().length);
		mazeScreens.setPreferredSize(size);
		
		mazeScreens.add(mazePlaying, "Playing");
		mazePlaying.setBorder(BorderFactory.createLineBorder(Color.BLUE));

		// maze paused
		mazePaused = new JPanel();
		mazePaused.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
		mazeScreens.add(mazePaused, "Paused");

	}

	private void initMazeWon() {
		JPanel mazeWon = new JPanel(new GridBagLayout());
		mazeWon.setOpaque(false);
		add(mazeWon, "Won");

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridy = 0;
		JLabel level = new JLabel("You defeated level " + this.currLevel, SwingConstants.CENTER);
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
		JLabel roundScore = new JLabel("Round score: " + maze.getScore(), SwingConstants.CENTER);
		roundScore.setForeground(Color.WHITE);
		mazeWon.add(roundScore, gbc);
		
		gbc.gridy = 5;
		this.totalScore += maze.getScore();
		JLabel totalScore = new JLabel("Total score: " + this.totalScore, SwingConstants.CENTER);
		totalScore.setForeground(Color.WHITE);
		mazeWon.add(totalScore, gbc);

		gbc.gridy = 6;
		JButton nextLevelButton = new JButton(new ImageIcon("resources/next_level.png"));
		nextLevelButton.setContentAreaFilled(false);
		nextLevelButton.setMargin(new Insets(0, 0, 0, 0));
		nextLevelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currLevel++;
				gameResume();
				initMazeUI();
				switchToMazeUI();
			}
		});
		mazeWon.add(nextLevelButton, gbc);

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
		mazeWon.add(menuButton, gbc);
	}

	private void initMazeLost() {
		JPanel mazeLost = new JPanel(new GridBagLayout());
		mazeLost.setOpaque(false);
		add(mazeLost, "Lost");
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridy = 0;
		JLabel lostLevelLabel = new JLabel("You died at level " + currLevel + "...", SwingConstants.CENTER);
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
	}

	private Icon getScaledImageIcon(ImageIcon img, int width, int height) {
		return new ImageIcon(img.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
	}
}
