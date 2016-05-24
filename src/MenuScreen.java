import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class MenuScreen extends JPanel {
	
	private MainWindow mainWindow;
	
	public MenuScreen(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		initMenu();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image background = (new ImageIcon("resources/gameScreen_bg_new.jpg")).getImage(); // TODO: move into field so we dont reload
	    g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
	}
	
	private void initMenu() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
        BoxLayout layout = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);
        buttonPanel.setLayout(layout);
        
		// add the buttons
		// start button
		JButton startButton = new JButton(new ImageIcon("resources/start_game.png"));
		startButton.setContentAreaFilled(false);
		startButton.setMargin(new Insets(0, 0, 0, 0));
		startButton.setFocusPainted(false);
		startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		startButton.setToolTipText("Start a new game");
		AbstractAction startPressed = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// start game
				mainWindow.switchToGame();
			}
		};
		startButton.addActionListener(startPressed);
		// allow button to be activated by 'Enter' key as well as by clicking
		startButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "pressEnter");
		startButton.getActionMap().put("pressEnter", startPressed);
		
		// tutorial button
		JButton tutorialButton = new JButton(new ImageIcon("resources/tutorial.png"));
		tutorialButton.setContentAreaFilled(false);
		tutorialButton.setMargin(new Insets(0, 0, 0, 0));
		tutorialButton.setFocusPainted(false);
		tutorialButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		tutorialButton.setToolTipText("Displays instructions on how to play");
		tutorialButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// display tutorial
				mainWindow.switchToTutorial();
			}
		});
		
		// options button
		JButton optionsButton = new JButton(new ImageIcon("resources/options.png"));
		optionsButton.setContentAreaFilled(false);
		optionsButton.setMargin(new Insets(0, 0, 0, 0));
		optionsButton.setFocusPainted(false);
		optionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		optionsButton.setToolTipText("Configure game settings");
		optionsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// display tutorial
				mainWindow.switchToOptions();
			}
		});
		
		// exit button
		JButton exitButton = new JButton(new ImageIcon("resources/quit.png"));
		exitButton.setOpaque(false);
		exitButton.setContentAreaFilled(false);
		exitButton.setMargin(new Insets(0, 0, 0, 0));
		exitButton.setFocusPainted(false);
		exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		exitButton.setToolTipText("Exit to desktop");
		AbstractAction exitPressed = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// exit to desktop
				System.exit(0);
			}
		};
		exitButton.addActionListener(exitPressed);
		// allow button to be activated by 'Escape' key as well as by clicking
		exitButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0), "pressEsc");
		exitButton.getActionMap().put("pressEsc", exitPressed);
		
		buttonPanel.add(Box.createVerticalStrut(60));
		buttonPanel.add(startButton);
		buttonPanel.add(Box.createVerticalStrut(10));
		buttonPanel.add(tutorialButton);
		buttonPanel.add(Box.createVerticalStrut(10));
		buttonPanel.add(optionsButton);
		buttonPanel.add(Box.createVerticalStrut(10));
		buttonPanel.add(exitButton);
		add(buttonPanel);
	}
	
	
}
