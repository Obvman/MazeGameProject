import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class OptionsScreen extends JPanel {
	private MainWindow mainWindow;

	// options
	Dimension resolution;
	int difficulty; // 1, 2, or 3
	ArrayList<Integer> validKeysArray;
	private int moveRightKey;
	private int moveLeftKey;
	private int moveUpKey;
	private int moveDownKey;
	private int shootKey;

	// tmp options
	Dimension tmpResolution;
	int tmpDifficulty;
	private int tmpRightKey;
	private int tmpLeftKey;
	private int tmpUpKey;
	private int tmpDownKey;
	private int tmpSpaceKey;


	public OptionsScreen (MainWindow mainWindow) {
		this.mainWindow = mainWindow;

		GridLayout layout = new GridLayout(0, 1);
		this.setLayout(layout);

		initResolutionPicker();
		initControlSelector();
		initDifficultyPicker();
		initConfirmation();

		// initalise array of valid keys for remapping
		// SORRY I DON'T KNOW A BETTER WAY TO DO THIS
		Integer[] validKeysTmp = {KeyEvent.VK_Q, KeyEvent.VK_W, KeyEvent.VK_E,
				KeyEvent.VK_R, KeyEvent.VK_T, KeyEvent.VK_Y, KeyEvent.VK_U,
				KeyEvent.VK_I, KeyEvent.VK_O, KeyEvent.VK_P, KeyEvent.VK_A,
				KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_F, KeyEvent.VK_G,
				KeyEvent.VK_H, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L,
				KeyEvent.VK_Z, KeyEvent.VK_Z, KeyEvent.VK_X, KeyEvent.VK_C,
				KeyEvent.VK_V, KeyEvent.VK_B, KeyEvent.VK_N, KeyEvent.VK_M,
				KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4,
				KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7, KeyEvent.VK_8,
				KeyEvent.VK_9, KeyEvent.VK_0, KeyEvent.VK_TAB, KeyEvent.VK_SHIFT,
				KeyEvent.VK_ALT, KeyEvent.VK_CONTROL, KeyEvent.VK_SPACE,
				KeyEvent.VK_COMMA, KeyEvent.VK_PERIOD, KeyEvent.VK_SLASH,
				KeyEvent.VK_MINUS, KeyEvent.VK_PLUS};
		// stick mappings into ArrayList for better adding/removing
		ArrayList<Integer> validKeyList = new ArrayList<Integer>(validKeysTmp.length);
		for (int i = 0; i < validKeysTmp.length; ++i) {
			validKeyList.add(validKeysTmp[i]);
		}
	}

	public Dimension getResolution() {
		return resolution;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setKey(int keyCode, int keyValue) {
		switch (keyCode) {
		case 1:
			moveRightKey = keyValue;
			break;
		case 2:
			moveLeftKey = keyValue;
			break;
		case 3:
			moveUpKey = keyValue;
			break;
		case 4:
			moveDownKey = keyValue;
			break;
		case 5:
			shootKey = keyValue;
			break;
		}

	}

	public int getKey(int keyCode) {
		switch (keyCode) {
		case 1:
			return moveRightKey;
		case 2:
			return moveLeftKey;
		case 3:
			return moveUpKey;
		case 4:
			return moveDownKey;
		case 5:
			return shootKey;
		default:
			return -1;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image background = (new ImageIcon("resources/gameScreen_bg.jpg")).getImage();
		g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
	}

	private void initResolutionPicker() {
		// resolution picker (dropdown list)
		JPanel resolutionPicker = new JPanel(new GridBagLayout());
		resolutionPicker.setOpaque(false);
		add(resolutionPicker);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 0, 10, 0);

		JLabel resolutionLabel = new JLabel("Resolution: ");
		resolutionLabel.setForeground(Color.WHITE);

		Vector<String> resolutions = new Vector<String>();

		// add standard resolutions (note: game requires MINIMUM ~700 height and ~1400 width)
		resolutions.add("1920x1080 	(16:9)");
		resolutions.add("1680x1050 	(16:10)");
		resolutions.add("1600x900  	(16:9)");
		resolutions.add("1440x900  	(16:10)");
		resolutions.add("1360x768 	(16:9)");

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

		if (resolutions.size() == 0) {
			resolutions.add(nativeWidth+"x"+nativeHeight + " (native)");
		}
		
		JComboBox<String> resolutionCB = new JComboBox<String>(resolutions);
		resolutionCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				String resolution = (String)cb.getSelectedItem();
				String[] dimensions = resolution.substring(0, resolution.indexOf(" ")).split("x");
				OptionsScreen.this.tmpResolution = new Dimension(Integer.parseInt(dimensions[0]), Integer.parseInt(dimensions[1]));
			}

		});

		// default
		resolutionCB.setSelectedIndex(0);
		String defaultResolution = resolutionCB.getItemAt(0);
		String[] defaultDimensions = defaultResolution.substring(0, defaultResolution.indexOf(" ")).split("x");
		resolution = new Dimension(Integer.parseInt(defaultDimensions[0]), Integer.parseInt(defaultDimensions[1]));
		tmpResolution = resolution;

		resolutionPicker.add(resolutionLabel, gbc);
		resolutionPicker.add(resolutionCB, gbc);
	}

	// list of buttons that bring up a dialog when clicked
	// dialog prompts user to press the key they want to rebind to
	// then disappears. Key is confirmed mapped when 'Confirm' button is clicked
	// TODO each button shows the current key that it's mapped to
	// valid keys do not include keys that are already mapped (prevents remapping to used keys)
	// if the already mapped key is pressed with dialog open, nothing should happen
	private void initControlSelector() {
		JPanel controlPicker = new JPanel(new GridBagLayout());
		controlPicker.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();

		JLabel controlLabel = new JLabel("Controls placeholder");
		controlLabel.setForeground(Color.WHITE);

		AbstractAction showDialog = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog keyRemapDialog = new JDialog();
				JPanel dialogMessage = new JPanel();
				dialogMessage.add(new JLabel("Press the key you wish to rebind to"));
				keyRemapDialog.add(dialogMessage);
				keyRemapDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				keyRemapDialog.setLocationRelativeTo(mainWindow);
				keyRemapDialog.pack();
				keyRemapDialog.setVisible(true);
			}
		};

		gbc.ipady = 5;
		gbc.gridy = 0;
		controlPicker.add(controlLabel);

		gbc.gridy = 1;
		JButton rightRemap = new JButton("Right");
		rightRemap.setForeground(Color.WHITE);
		rightRemap.addActionListener(showDialog);
		controlPicker.add(rightRemap, gbc);


		gbc.gridy = 2;
		JButton leftRemap = new JButton("Left");
		leftRemap.setForeground(Color.WHITE);
		leftRemap.addActionListener(showDialog);
		controlPicker.add(leftRemap, gbc);

		gbc.gridy = 3;
		JButton upRemap = new JButton("Up");
		upRemap.setForeground(Color.WHITE);
		upRemap.addActionListener(showDialog);
		controlPicker.add(upRemap, gbc);

		gbc.gridy = 4;
		JButton downRemap = new JButton("Down");
		downRemap.setForeground(Color.WHITE);
		downRemap.addActionListener(showDialog);
		controlPicker.add(downRemap, gbc);

		gbc.gridy = 5;
		JButton shootRemap = new JButton("Shoot");
		shootRemap.setForeground(Color.WHITE);
		shootRemap.addActionListener(showDialog);
		controlPicker.add(shootRemap, gbc);        

		this.add(controlPicker);
	}

	private void initDifficultyPicker() {
		// resolution picker (dropdown list)
		JPanel difficultyPicker = new JPanel();
		difficultyPicker.setOpaque(false);
		add(difficultyPicker);

		JLabel difficultyLabel = new JLabel("Difficulty: ");
		difficultyLabel.setForeground(Color.WHITE);

		ButtonGroup group = new ButtonGroup();

		JRadioButton easyButton = new JRadioButton("Easy");
		easyButton.setForeground(Color.WHITE);
		easyButton.setOpaque(false);
		easyButton.setMnemonic(KeyEvent.VK_B);
		easyButton.setActionCommand("Easy");
		easyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				OptionsScreen.this.tmpDifficulty = 1;
			}

		});

		JRadioButton mediumButton = new JRadioButton("Medium");
		mediumButton.setForeground(Color.WHITE);
		mediumButton.setOpaque(false);
		mediumButton.setMnemonic(KeyEvent.VK_B);
		mediumButton.setActionCommand("Medium");
		mediumButton.setSelected(true);
		mediumButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				OptionsScreen.this.tmpDifficulty = 2;
			}

		});
		// default
		difficulty = 2;
		tmpDifficulty = difficulty;

		JRadioButton hardButton = new JRadioButton("Hard");
		hardButton.setForeground(Color.WHITE);
		hardButton.setOpaque(false);
		hardButton.setMnemonic(KeyEvent.VK_B);
		hardButton.setActionCommand("Hard");
		hardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				OptionsScreen.this.tmpDifficulty = 3;

			}

		});

		group.add(easyButton);
		group.add(mediumButton);
		group.add(hardButton);
		difficultyPicker.add(difficultyLabel);
		difficultyPicker.add(easyButton);
		difficultyPicker.add(mediumButton);
		difficultyPicker.add(hardButton);
	}

	private void initConfirmation() {
		JPanel confirmation = new JPanel();
		confirmation.setOpaque(false);
		// confirm 
		JButton confirmButton = new JButton(new ImageIcon("resources/confirm.png"));
		confirmButton.setContentAreaFilled(false);
		confirmButton.setMargin(new Insets(0, 0, 0, 0));
		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// update the settings
				OptionsScreen.this.resolution = OptionsScreen.this.tmpResolution;
				OptionsScreen.this.mainWindow.setSize(OptionsScreen.this.resolution);
				OptionsScreen.this.difficulty = OptionsScreen.this.tmpDifficulty;
				OptionsScreen.this.mainWindow.switchToMenu();
			}

		});
		confirmation.add(confirmButton);

		// cancel
		JButton cancelButton = new JButton(new ImageIcon("resources/cancel.png"));
		cancelButton.setContentAreaFilled(false);
		cancelButton.setMargin(new Insets(0, 0, 0, 0));
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OptionsScreen.this.mainWindow.switchToMenu();
			}
		});
		confirmation.add(cancelButton);

		add(confirmation);
	}

}
