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
	private JPanel spellSelect;

	private JPanel mazeUI;
	private JPanel mazeScreens; // screen controller for mazePlaying & mazePaused
	private MazePanel mazePlaying;
	private JPanel mazePaused;
	private JPanel mazeHelp; //check if we need

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
	
	// highscores
	JTextField usernameField;
	private String username;

	public GameScreen(MainWindow mainWindow, int difficulty) {
		this.mainWindow = mainWindow;
		this.currLevel = 1;
		this.difficulty = difficulty;
		this.timer = new Timer(10, this);
		this.username = "";

		setLayout(new CardLayout());

		initSpellSelect();
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
	
	public void switchToMazeHelp() {
		CardLayout cl = (CardLayout) mazeScreens.getLayout();
		cl.show(mazeScreens, "Help");
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


	private void initMazeUI() {
		mazeUI = new JPanel(new GridBagLayout());
		mazeUI.setOpaque(false);
		this.add(mazeUI, "MazeUI");

		// status bar
		GridBagConstraints gbcStatus = new GridBagConstraints();
		gbcStatus.fill = GridBagConstraints.BOTH;
		gbcStatus.weighty = 0.1;
		gbcStatus.weightx = 1;
		gbcStatus.gridheight = 1;
		
		JPanel statusBar = new JPanel(new BorderLayout());
//		statusBar.setBorder(BorderFactory.createLineBorder(Color.RED)); // debug border
		statusBar.setBorder(new EmptyBorder(0, 40, 0, 40)); // set left and right padding
		statusBar.setOpaque(false);
		
	
		// status bar components
		// TODO: remove place holder text
		JPanel statusButtons = new JPanel(new GridBagLayout()); // vertically aligns when no gbc are defined
		statusButtons.setOpaque(false);
		statusBar.add(statusButtons, BorderLayout.WEST);
		
		JButton mainMenu = new JButton(new ImageIcon("resources/main_menu.png"));
		mainMenu.setContentAreaFilled(false);
		mainMenu.setMargin(new Insets(0, 0, 0 ,0));
		
		AbstractAction menuPressed = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameScreen.this.mainWindow.switchToMenu();
			}
		};
		mainMenu.addActionListener(menuPressed);
		// allow button to be activated by 'Esc' key as well as by clicking
		mainMenu.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0), "pressEsc");
		mainMenu.getActionMap().put("pressEsc", menuPressed);
		statusButtons.add(mainMenu);


		// pause button
		JButton pause = new JButton(new ImageIcon("resources/pause.png"));
		pause.setContentAreaFilled(false);
		pause.setMargin(new Insets(0, 0, 0 ,0));
		
		AbstractAction pausePressed = new AbstractAction() {
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
		};
		pause.addActionListener(pausePressed);
		// allow button to be activated by 'p' key as well as by clicking
		pause.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_P,0), "pressP");
		pause.getActionMap().put("pressP", pausePressed);
		
		statusButtons.add(pause);

		// help button
		JButton help = new JButton(new ImageIcon("resources/help.png"));
		help.setContentAreaFilled(false);
		help.setMargin(new Insets(0, 0, 0 ,0));
		
		AbstractAction helpPressed = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (timer.isRunning()) {
					gamePause();
					switchToMazeHelp();
				} else {
					gameResume();
					switchToMazePlaying();
				}
			}
		};
		help.addActionListener(helpPressed);
		// allow button to be activated by 'h' key as well as by clicking
		pause.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_H,0), "pressH");
		pause.getActionMap().put("pressH", pausePressed);
		statusButtons.add(help);


		// dynamically updated components
		JPanel statusFields = new JPanel(new GridBagLayout());
		statusFields.setOpaque(false);
		
		GridBagConstraints gbcFields = new GridBagConstraints();
		gbcFields.ipadx = 40;
		
		objective = new JLabel();
		objective.setForeground(Color.WHITE);
		statusFields.add(objective, gbcFields);

		monstersSlain = new JLabel();
		monstersSlain.setForeground(Color.WHITE);
		statusFields.add(monstersSlain, gbcFields);

		gemsCollected = new JLabel();
		gemsCollected.setForeground(Color.WHITE);
		statusFields.add(gemsCollected, gbcFields);

		time = new JLabel();
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
		mazeUI.add(mazeScreens, gbcMaze);

		// maze playing
		mazePlaying = new MazePanel(currLevel, difficulty, spellType);
		maze = mazePlaying.getMaze();
		mazeScreens.add(mazePlaying, "Playing");
		
		// pack the panel
		Dimension size = new Dimension(Maze.MAZE_CELL_SIZE * maze.getGrid()[0].length, Maze.MAZE_CELL_SIZE * maze.getGrid().length);
		mazeScreens.setPreferredSize(size);

		initHelp();
		initPause();
	}
	
	private void initHelp() {
		mazeHelp = new JPanel(new GridBagLayout());
		mazeHelp.setBackground(Color.LIGHT_GRAY);
		add(mazeHelp, "Maze Help");
		mazeScreens.add(mazeHelp, "Help");
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.ipady = 30;
		gbc.gridy = 2;
		JLabel help = new JLabel("IN-GAME HELP SCREEN");
		mazeHelp.add(help, gbc);
		
		//mazeHelp.add(Box.createRigidArea(new Dimension(0, 50)), gbc);
		
		gbc.ipady = 0;
		
		//OBJECTIVE
		gbc.gridy = 3;
		JLabel o1 = new JLabel("OBJECTIVE");
		mazeHelp.add(o1, gbc);
		
		gbc.gridy = 4;
		JLabel o2 = new JLabel("The objective is to reach the end of the maze with the highest score.");
		mazeHelp.add(o2, gbc);
		
		gbc.gridy = 5;
		JLabel o3 = new JLabel("First retrieve the key from it's hiding place, then use it to unlock the steel door.");
		mazeHelp.add(o3, gbc);
		
		gbc.gridy = 6;
		JLabel oi1 = new JLabel(new ImageIcon("resources/obj.png"));
		mazeHelp.add(oi1, gbc);
		
		//CONTROLS
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
		
		gbc.gridy = 13;
		JLabel ei1 = new JLabel(new ImageIcon("resources/enemies.png"));
		mazeHelp.add(ei1, gbc);
		
		//GEMS
		gbc.ipady = 0;
		gbc.gridy = 16;
		JLabel g1 = new JLabel("Collect gems to increase your score.");
		mazeHelp.add(g1, gbc);
		
		gbc.gridy = 17;
		JLabel gi1 = new JLabel(new ImageIcon("resources/gems.png"));
		mazeHelp.add(gi1, gbc);

		gbc.gridy = 20;
		JLabel ret = new JLabel("Press P or H to resume playing.");
		mazeHelp.add(ret, gbc);
		
	}
	
	private void initPause() {
		mazePaused = new JPanel(new GridBagLayout());
		mazePaused.add(new JLabel("Game Paused. Press P or H to continue playing."));
		mazePaused.setBackground(Color.LIGHT_GRAY);
		mazeScreens.add(mazePaused, "Paused");
	}

	private void initMazeWon() {
		JPanel mazeWon = new JPanel(new GridBagLayout()){
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image background = (new ImageIcon("resources/BG.png")).getImage(); 
			    g.drawImage(background, 0, 0, null);
			}
		};;
		JPanel mazeWonButtons = new JPanel();
		mazeWon.setOpaque(false);
		mazeWonButtons.setOpaque(false);
		add(mazeWon, "Won");

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.ipady = 20;
		
		gbc.gridy = 0;
		gbc.weighty = 0.2;
	    mazeWon.add(new JLabel(" "), gbc);  // blank JLabel
	    
	    gbc.weighty = 0.2;
	    gbc.weightx = 0;
		gbc.gridy = 1;
		JLabel level = new JLabel("<html><center>LEVEL " + this.currLevel + "<br>VICTORY</center></html>");
		level.setFont(new Font("Herculanum", Font.PLAIN, 70));
		level.setForeground(new Color(255,215,0));
		mazeWon.add(level, gbc);

		gbc.gridy = 2;
		JLabel roundInfo = new JLabel("Round Results", SwingConstants.CENTER);
		roundInfo.setFont(new Font("Devanagari MT", Font.PLAIN, 30));
		roundInfo.setForeground(Color.WHITE);
		mazeWon.add(roundInfo, gbc);
        
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
		JLabel roundScore = new JLabel("Round score: " + maze.getScore(), SwingConstants.CENTER);
		roundScore.setFont(new Font("Devanagari MT", Font.PLAIN, 15));
		roundScore.setForeground(Color.WHITE);
		mazeWon.add(roundScore, gbc);
		
		gbc.gridy = 6;
		this.totalScore += maze.getScore();
		JLabel totalScore = new JLabel("Total score: " + this.totalScore, SwingConstants.CENTER);
		totalScore.setFont(new Font("Devanagari MT", Font.PLAIN, 15));
		totalScore.setForeground(Color.WHITE);
		mazeWon.add(totalScore, gbc);

		gbc.ipady = 0;
		gbc.gridy = 7;
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
		mazeWonButtons.add(nextLevelButton);
		//mazeWon.add(nextLevelButton, gbc);

		JButton menuButton = new JButton(new ImageIcon("resources/main_menu.png"));
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
	    gbc.weighty = 0.5;

	    mazeWon.add(new JLabel(" "), gbc);  // blank JLabel
	}

	private void initMazeLost() {
		HighscoresPanel mazeLost = new HighscoresPanel(new GridBagLayout()){
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image background = (new ImageIcon("resources/BG.png")).getImage(); 
			    g.drawImage(background, 0, 0, null);
			}
		};;
		JPanel mazeLostButton = new JPanel();
		
		mazeLost.setOpaque(false);
		mazeLostButton.setOpaque(false);
		add(mazeLost, "Lost");
		
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridy = 0;
	    gbc.weighty = 0;
	    gbc.weightx = 1;
	    
		gbc.gridy = 1;
	    
		JLabel lostLevelLabel = new JLabel("LEVEL " + currLevel + "... YOU DIED", SwingConstants.CENTER);
		lostLevelLabel.setForeground(Color.RED);
		lostLevelLabel.setFont(new Font("Devanagari MT", Font.PLAIN, 70));
		mazeLost.add(lostLevelLabel, gbc);
		
		if (username.equals("")) {
			gbc.gridy = 2;
			JLabel highscoresLabel = new JLabel("Enter a name to share your highscore");
			highscoresLabel.setForeground(Color.WHITE);
			mazeLost.add(highscoresLabel, gbc);
			
			gbc.gridy = 3;
			usernameField = new JTextField(20);
			mazeLost.add(usernameField, gbc);
			
			gbc.gridy = 4;
			JButton highscoreButton = new JButton("Share");
			highscoreButton.addActionListener(new ActionListener()
			{
			  public void actionPerformed(ActionEvent e)
			  {
			    username = usernameField.getText();
			    username = username.replaceAll("\\s+","");
			    switchToMazeLost();
			  }
			});
			mazeLost.add(highscoreButton, gbc);
		}

		
		gbc.gridy = 5;
		JLabel gameInfo= new JLabel("Game Results", SwingConstants.CENTER);
		gameInfo.setForeground(Color.WHITE);
		gameInfo.setFont(new Font("Devanagari MT", Font.PLAIN, 30));
		mazeLost.add(gameInfo, gbc);
		
		gbc.gridy = 6;
		JLabel faction = new JLabel("", SwingConstants.CENTER);
		if (spellType == 1) {
			faction.setText("Faction: Water");
		} else if (spellType == 2) {
			faction.setText("Faction: Fire");
		} else if (spellType == 3) {
			faction.setText("Faction: Air");
		}
		faction.setFont(new Font("Devanagari MT", Font.PLAIN, 15));
		faction.setForeground(Color.WHITE);
		mazeLost.add(faction, gbc);
		
		gbc.gridy = 7;
		JLabel monstersSlain = new JLabel("Monsters slain: " + maze.getNumMonstersKilled(), SwingConstants.CENTER);
		monstersSlain.setForeground(Color.WHITE);
		mazeLost.add(monstersSlain, gbc);
		
		gbc.gridy = 8;
		JLabel gemsCollected = new JLabel("Gems collected: " + maze.getNumGemsCollected(), SwingConstants.CENTER);
		gemsCollected.setForeground(Color.WHITE);
		mazeLost.add(gemsCollected, gbc);
		
		gbc.gridy = 9;
		gbc.ipady = 5; // gaps between lines
		JLabel numGemsCollected = new JLabel("Final score: " + maze.getScore(), SwingConstants.CENTER);
		numGemsCollected.setForeground(Color.WHITE);
		mazeLost.add(numGemsCollected, gbc);
		
		gbc.gridy = 10;
		String name = "";
		int score = 0;
		if (!username.equals("")) {
			name = username;
			score = maze.getScore();
		}
		for (JLabel label : mazeLost.getHighscores(score, name)) {
			mazeLost.add(label, gbc);
			gbc.gridy++;
		}
		
		gbc.ipady = 8;
		JButton startButton = new JButton(new ImageIcon("resources/start_again.png"));
		startButton.setContentAreaFilled(false);
		startButton.setMargin(new Insets(0, 0, 0, 0));
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.switchToGame();
			}
		});
		mazeLostButton.add(startButton);
		JButton menuButton = new JButton(new ImageIcon("resources/main_menu.png"));
		menuButton.setContentAreaFilled(false);
		menuButton.setMargin(new Insets(0, 0, 0, 0));
		menuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.switchToMenu();
			}
		});
		mazeLostButton.add(menuButton);
		mazeLost.add(mazeLostButton, gbc);
	}

	private Icon getScaledImageIcon(ImageIcon img, int width, int height) {
		return new ImageIcon(img.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
	}
}
