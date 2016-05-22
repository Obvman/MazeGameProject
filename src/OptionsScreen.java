import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class OptionsScreen extends JPanel {
	private MainWindow mainWindow;
	
	// options
	Dimension resolution;
	int difficulty; // 1, 2, or 3
	
	// tmp options
	Dimension tmpResolution;
	int tmpDifficulty;
	
	
	public OptionsScreen (MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		
		GridLayout layout = new GridLayout(0, 1);
		setLayout(layout);
		
		// default options
		resolution = new Dimension(1600, 900);
		difficulty = 2;
		tmpResolution = resolution;
		tmpDifficulty = difficulty;
		
		initResolutionPicker();
		initDifficultyPicker();
		initConfirmation();
		repaint();
	}

	public Dimension getResolution() {
		return resolution;
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image background = (new ImageIcon("resources/gameScreen_bg.jpg")).getImage(); // TODO: move into field so we dont reload
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
		
		String[] resolutions = { "1920x1080", "1600x900", "1366x768" };
		JComboBox<String> resolutionCB = new JComboBox<String>(resolutions);
		resolutionCB.setSelectedIndex(1); // 1600x900
		resolutionCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				String resolution = (String)cb.getSelectedItem();
				String[] dimensions = resolution.split("x");
				OptionsScreen.this.tmpResolution = new Dimension(Integer.parseInt(dimensions[0]), Integer.parseInt(dimensions[1]));
			}
			
		});
		
		resolutionPicker.add(resolutionLabel, gbc);
		resolutionPicker.add(resolutionCB, gbc);
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
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
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
