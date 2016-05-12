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
		Image background = (new ImageIcon("images/background.jpeg")).getImage();
        g.drawImage(background, 0, 0, null); 
	}
	
	private void initMenu() {
		// add the buttons
		// start button
		JButton startButton = new JButton("Start Game");
		startButton.setToolTipText("Start a new game");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// start game
				// TODO: start a NEW game
				mainWindow.switchToGame();
			}
		});
		
		// tutorial button
		JButton tutorialButton = new JButton("Tutorial");
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
		exitButton.setToolTipText("Exit to desktop");
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// exit to desktop
				System.exit(0);
			}
		});
		
		add(startButton);
		add(tutorialButton);
		add(exitButton);
	}
	
	
}
