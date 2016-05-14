import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class MenuScreen extends JPanel {
	
	private MainWindow mainWindow;
	
	public MenuScreen(MainWindow mainWindow) {
		int paddingTop = (int)(mainWindow.getHeight() *  0.2);
		int paddingSide = (int)(mainWindow.getWidth() * 0.2);
		setBorder(BorderFactory.createEmptyBorder(paddingTop, paddingSide, 0, paddingSide));
		this.mainWindow = mainWindow;
		initMenu();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image background = (new ImageIcon("images/background.jpeg")).getImage();
		int x = (this.getWidth() - background.getWidth(null)) / 2;
	    int y = (this.getHeight() - background.getHeight(null)) / 2;
	    g.drawImage(background, x, y, null);
	}
	
	private void initMenu() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
        BoxLayout layout = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);
        buttonPanel.setLayout(layout);
        
		// add the buttons
		// start button
		JButton startButton = new JButton("Start Game");
		startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		startButton.setToolTipText("Start a new game");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// start game
				mainWindow.switchToGame();
			}
		});
		
		// tutorial button
		JButton tutorialButton = new JButton("Tutorial");
		tutorialButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		tutorialButton.setToolTipText("Displays instructions on how to play");
		tutorialButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// display tutorial
				mainWindow.switchToTutorial();
			}
		});
		
		// exit button
		JButton exitButton = new JButton("Exit");
		exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		exitButton.setToolTipText("Exit to desktop");
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// exit to desktop
				System.exit(0);
			}
		});
		
		buttonPanel.add(startButton);
		buttonPanel.add(Box.createVerticalStrut(20));
		buttonPanel.add(tutorialButton);
		buttonPanel.add(Box.createVerticalStrut(20));
		buttonPanel.add(exitButton);
		add(buttonPanel);
	}
	
	
}
