import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class OptionsScreen extends JPanel {

	/**
	 * Creates a OptionsScreen containing a resolution picker, controls remapper, difficulty picker and return button.
	 * @param mainWindow The JFrame containing the OptionsScreen
	 */
	public OptionsScreen (MainWindow mainWindow) {
		this.mainWindow = mainWindow;

		this.setLayout(new GridBagLayout());
		
		// set default keys
		moveRightKey = KeyEvent.VK_RIGHT;
		moveLeftKey = KeyEvent.VK_LEFT;
		moveUpKey = KeyEvent.VK_UP;
		moveDownKey = KeyEvent.VK_DOWN;
		shootKey = KeyEvent.VK_SPACE;

		initResolutionPicker();
		initControlSelector();
		initDifficultyPicker();
		initBackButton();
		
		// initialize array of valid keys for remapping
		Integer[] validKeysTmp = {KeyEvent.VK_Q, KeyEvent.VK_W, KeyEvent.VK_E,
				KeyEvent.VK_R, KeyEvent.VK_T, KeyEvent.VK_Y,
				KeyEvent.VK_I, KeyEvent.VK_O, KeyEvent.VK_A,
				KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_F, KeyEvent.VK_G,
				KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L,
				KeyEvent.VK_Z, KeyEvent.VK_Z, KeyEvent.VK_X, KeyEvent.VK_C,
				KeyEvent.VK_V, KeyEvent.VK_B, KeyEvent.VK_N, KeyEvent.VK_M,
				KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4,
				KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7, KeyEvent.VK_8,
				KeyEvent.VK_9, KeyEvent.VK_0, KeyEvent.VK_TAB, KeyEvent.VK_SHIFT,
				KeyEvent.VK_ALT, KeyEvent.VK_CONTROL, KeyEvent.VK_COMMA, 
				KeyEvent.VK_PERIOD, KeyEvent.VK_SLASH,
				KeyEvent.VK_MINUS, KeyEvent.VK_PLUS};
		
		// store valid keys as a field
		this.validKeyList = new ArrayList<Integer>(validKeysTmp.length);
		for (int i = 0; i < validKeysTmp.length; ++i) {
			validKeyList.add(validKeysTmp[i]);
		}
	}

	/**
	 * @return the current Dimension representing the resolution of the window
	 */
	public Dimension getResolution() {
		return resolution;
	}

	/**
	 * 
	 * @return the current difficulty level of the game
	 */
	public int getDifficulty() {
		return difficulty;
	}
	
	/**
	 * 
	 * @return int array of keycodes for currently selected control scheme
	 */
	public int[] getKeyArray() {
		int[] keys = {moveRightKey, moveLeftKey, moveUpKey, moveDownKey, shootKey};
		return keys;
	}

	/**
	 * Paints a background image for the OptionsScreen
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image background = (new ImageIcon("resources/gameScreen_bg.jpg")).getImage();
		g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
	}

	/**
	 * Initializes a resolution drop-down menu. The menu contains standard resolutions
	 * supported by the user's native screen resolution.
	 */
	private void initResolutionPicker() {
		JPanel resolutionPicker = new JPanel(new GridBagLayout());
		resolutionPicker.setOpaque(false);
		GridBagConstraints gbcPanel = new GridBagConstraints();
		gbcPanel.gridy = 0;
		gbcPanel.weighty = 1;
		add(resolutionPicker, gbcPanel);

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridy = 0;
		JLabel resolutionLabel = new JLabel("Resolution: ", SwingConstants.CENTER);
		resolutionLabel.setForeground(Color.WHITE);
		resolutionLabel.setPreferredSize(new Dimension(180,30));
		
		resolutionPicker.add(resolutionLabel, gbc);

		gbc.gridy = 1;
		Vector<String> resolutions = new Vector<String>();

		// add standard resolutions
		resolutions.add("1920x1080 	(16:9)");
		resolutions.add("1680x1050 	(16:10)");
		resolutions.add("1600x900  	(16:9)");
		resolutions.add("1440x900  	(16:10)");
		resolutions.add("1366x768 	(16:9)");

		Dimension nativeScreen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int nativeWidth = (int)(nativeScreen.getWidth() * 0.95);
		int nativeHeight = (int)(nativeScreen.getHeight() * 0.95);

		// remove resolutions that are unsupported by screen size
		for (Iterator<String> iter = resolutions.iterator(); iter.hasNext();) {
			String sizeDescription = iter.next();
			String[] dimensions = sizeDescription.substring(0, sizeDescription.indexOf(" ")).split("x");
			if (Integer.parseInt(dimensions[0]) > nativeWidth || Integer.parseInt(dimensions[1]) > nativeHeight) {
				iter.remove();
			}
		}
		
		// fail-safe resolution
		if (resolutions.size() == 0) {
			resolutions.add(nativeWidth+"x"+nativeHeight + " (Native)");
		}

		JComboBox<String> resolutionCB = new JComboBox<String>(resolutions);
		resolutionCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
				String resolution = (String)cb.getSelectedItem();
				String[] dimensions = resolution.substring(0, resolution.indexOf(" ")).split("x");
				OptionsScreen.this.resolution = new Dimension(Integer.parseInt(dimensions[0]), Integer.parseInt(dimensions[1]));
				OptionsScreen.this.mainWindow.setSize(OptionsScreen.this.resolution);
			}

		});

		// default
		resolutionCB.setSelectedIndex(0);
		String defaultResolution = resolutionCB.getItemAt(0);
		String[] defaultDimensions = defaultResolution.substring(0, defaultResolution.indexOf(" ")).split("x");
		resolution = new Dimension(Integer.parseInt(defaultDimensions[0]), Integer.parseInt(defaultDimensions[1]));
		
		resolutionPicker.add(resolutionCB, gbc);
	}

	/**
	 * Initialises a controls remapper where users can remap standard keys used in the game
	 * (move up, move left, move down, move right, shoot spell). 
	 */
	private void initControlSelector() {
		JPanel controlPicker = new JPanel(new GridBagLayout());
		controlPicker.setOpaque(false);
		GridBagConstraints gbcPanel = new GridBagConstraints();
		gbcPanel.gridy = 1;
		gbcPanel.weighty = 1;
		add(controlPicker, gbcPanel);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 0 , 0, 0);

		JLabel controlLabel = new JLabel("Rebind Keys:", SwingConstants.CENTER);
		controlLabel.setForeground(Color.WHITE);
		controlLabel.setPreferredSize(new Dimension(180,20));
		
		AbstractAction showDialog = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JDialog keyRemapDialog = new JDialog();
				
				final JLabel dialogMessage = new JLabel("<html><body align=\"center\">"
						+ "Press the key you wish to rebind to<br>"
						+ "or press Escape to cancel</body><html>");
				dialogMessage.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
				
				keyRemapDialog.setPreferredSize(new Dimension(300, 100));
				keyRemapDialog.add(dialogMessage);
				keyRemapDialog.setModal(true); // lock focus to the dialog
				keyRemapDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				keyRemapDialog.setLocationRelativeTo(mainWindow);
				keyRemapDialog.setResizable(false);
				
				final JButton sourceButton = (JButton)e.getSource();
				keyRemapDialog.addKeyListener(new KeyListener() {
					@Override
					public void keyPressed(KeyEvent e) {
						return;
					}
					
					@Override
					public void keyReleased(KeyEvent e) {
						// Escape key closes dialog and cancels key rebinding process
						if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
							keyRemapDialog.setVisible(false);
							keyRemapDialog.dispose();
						
						// If key is valid, start rebind process
						} else if (validKeyList.contains(e.getKeyCode())) {
							
							// prevent user from changing to already bound key
							validKeyList.remove(Integer.valueOf(e.getKeyCode()));
							
							// change button text to reflect new key
							String buttonToChange = sourceButton.getText().split("\\(")[0];
							sourceButton.setText(buttonToChange + "(" 
										+ KeyEvent.getKeyText(e.getKeyCode()) + ")");
							// update controls according to input key and update list of 
							// valid keys accordingly
							switch (buttonToChange) {
							case "Move Right ":
								validKeyList.add(moveRightKey);
								moveRightKey = e.getKeyCode();
								validKeyList.remove(Integer.valueOf(moveRightKey));
								break;
							case "Move Left ":
								validKeyList.add(moveLeftKey);
								moveLeftKey = e.getKeyCode();
								validKeyList.remove(Integer.valueOf(moveLeftKey));
								break;
							case "Move Up ":
								validKeyList.add(moveUpKey);
								moveUpKey = e.getKeyCode();
								validKeyList.remove(Integer.valueOf(moveUpKey));
								break;
							case "Move Down ":
								validKeyList.add(moveDownKey);
								moveDownKey = e.getKeyCode();
								validKeyList.remove(Integer.valueOf(moveDownKey));
								break;
							case "Shoot ":
								validKeyList.add(shootKey);
								shootKey = e.getKeyCode();
								validKeyList.remove(Integer.valueOf(shootKey));
								break;
							}
							// close message after a valid key is entered
							keyRemapDialog.setVisible(false);
							keyRemapDialog.dispose();
							
						// Invalid key should display message to user and keep dialog 
						// open for further input
						} else {
							dialogMessage.setText("<html><body align=\"center\">"
									+ "That key is invalid or already in use.<br>"
									+ "Try another key or press Escape to cancel"
									+ "</body><html>");
						}
					}
					@Override
					public void keyTyped(KeyEvent e) {
						return;
					}
				});
				
				keyRemapDialog.pack();
				keyRemapDialog.setVisible(true);
			}
		};

		gbc.ipady = 5;
		gbc.gridy = 0;
		controlPicker.add(controlLabel, gbc);
		
		gbc.gridy = 1;
		JButton upRemap = new JButton("Move Up ("+ KeyEvent.getKeyText(moveUpKey)+")", new ImageIcon("resources/buttons/plain_small.png"));
		upRemap.setMargin(new Insets(0, 0, 0, 0));
		upRemap.setContentAreaFilled(false);
		upRemap.setHorizontalTextPosition(SwingConstants.CENTER);
		upRemap.setForeground(Color.WHITE);
		upRemap.addActionListener(showDialog);
		controlPicker.add(upRemap, gbc);
		
		gbc.gridy = 2;
		JButton leftRemap = new JButton("Move Left ("+ KeyEvent.getKeyText(moveLeftKey)+")", new ImageIcon("resources/buttons/plain_small.png"));
		leftRemap.setMargin(new Insets(0, 0, 0, 0));
		leftRemap.setContentAreaFilled(false);
		leftRemap.setHorizontalTextPosition(SwingConstants.CENTER);
		leftRemap.setForeground(Color.WHITE);
		leftRemap.addActionListener(showDialog);
		controlPicker.add(leftRemap, gbc);

		gbc.gridy = 3;
		JButton downRemap = new JButton("Move Down ("+ KeyEvent.getKeyText(moveDownKey)+")", new ImageIcon("resources/buttons/plain_small.png"));
		downRemap.setMargin(new Insets(0, 0, 0, 0));
		downRemap.setContentAreaFilled(false);
		downRemap.setHorizontalTextPosition(SwingConstants.CENTER);
		downRemap.setForeground(Color.WHITE);
		downRemap.addActionListener(showDialog);
		controlPicker.add(downRemap, gbc);
		
		gbc.gridy = 4;
		JButton rightRemap = new JButton("Move Right ("+ KeyEvent.getKeyText(moveRightKey)+")", new ImageIcon("resources/buttons/plain_small.png"));
		rightRemap.setMargin(new Insets(0, 0, 0, 0));
		rightRemap.setContentAreaFilled(false);
		rightRemap.setHorizontalTextPosition(SwingConstants.CENTER);
		rightRemap.setForeground(Color.WHITE);
		rightRemap.addActionListener(showDialog);
		controlPicker.add(rightRemap, gbc);

		gbc.gridy = 5;
		JButton shootRemap = new JButton("Shoot ("+ KeyEvent.getKeyText(shootKey)+")", new ImageIcon("resources/buttons/plain_small.png"));
		shootRemap.setMargin(new Insets(0, 0, 0, 0));
		shootRemap.setContentAreaFilled(false);
		shootRemap.setHorizontalTextPosition(SwingConstants.CENTER);
		shootRemap.setForeground(Color.WHITE);
		shootRemap.addActionListener(showDialog);
		controlPicker.add(shootRemap, gbc);        
	}

	/**
	 * Initializes a button group containing 3 radio buttons to choose the difficulty of the maze game
	 * (easy, medium, hard)
	 */
	private void initDifficultyPicker() {
		JPanel difficultyPicker = new JPanel(new GridBagLayout());
		difficultyPicker.setOpaque(false);
		GridBagConstraints gbcPanel = new GridBagConstraints();
		gbcPanel.gridy = 2;
		gbcPanel.weighty = 1;
		add(difficultyPicker, gbcPanel);
		
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 0, 0, 0);
		
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		JLabel difficultyLabel = new JLabel("Difficulty: ", SwingConstants.CENTER);
		difficultyLabel.setForeground(Color.WHITE);
		
		difficultyPicker.add(difficultyLabel, gbc);

		gbc.gridy = 1;
		gbc.gridwidth = 1;
		ButtonGroup group = new ButtonGroup();

		JRadioButton easyButton = new JRadioButton("Easy");
		easyButton.setForeground(new Color(127, 255, 0));
		easyButton.setOpaque(false);
		easyButton.setMnemonic(KeyEvent.VK_B);
		easyButton.setActionCommand("Easy");
		easyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				OptionsScreen.this.difficulty = 1;
			}

		});

		JRadioButton mediumButton = new JRadioButton("Medium");
		mediumButton.setForeground(new Color(255, 215, 0));
		mediumButton.setOpaque(false);
		mediumButton.setMnemonic(KeyEvent.VK_B);
		mediumButton.setActionCommand("Medium");
		mediumButton.setSelected(true);
		mediumButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				OptionsScreen.this.difficulty = 2;
			}

		});
		// default
		difficulty = 2;

		JRadioButton hardButton = new JRadioButton("Hard");
		hardButton.setForeground(new Color(216, 31, 42));
		hardButton.setOpaque(false);
		hardButton.setMnemonic(KeyEvent.VK_B);
		hardButton.setActionCommand("Hard");
		hardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				OptionsScreen.this.difficulty = 3;
			}

		});

		group.add(easyButton);
		group.add(mediumButton);
		group.add(hardButton);
		difficultyPicker.add(easyButton, gbc);
		difficultyPicker.add(mediumButton, gbc);
		difficultyPicker.add(hardButton, gbc);
	}

	/**
	 * Initializes a button to save the current options and return to the main menu
	 */
	private void initBackButton() {
		JPanel backToMenu = new JPanel();
		backToMenu.setOpaque(false);
		GridBagConstraints gbcPanel = new GridBagConstraints();
		gbcPanel.gridy = 3;
		gbcPanel.weighty = 1;
		add(backToMenu, gbcPanel);
		
		// cancel
		JButton backButton = new JButton(new ImageIcon("resources/buttons/back.png"));
		backButton.setContentAreaFilled(false);
		backButton.setMargin(new Insets(0, 0, 0, 0));
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OptionsScreen.this.mainWindow.switchToMenu();
			}
		});
		backToMenu.add(backButton);
	}

	private MainWindow mainWindow;

	private Dimension resolution;
	
	// key bindings
	private ArrayList<Integer> validKeyList; // contains selected valid keys and currently bound keys
	private int moveRightKey;
	private int moveLeftKey;
	private int moveUpKey;
	private int moveDownKey;
	private int shootKey;
	
	private int difficulty;
}
