/**
 * Main Menu class that displays on program startup
 * Contains:
 *   - Start button that takes you to the GamePanel screen
 *   - Exit button that shuts down program
 * 
 * COMP2911 Project - 16s1
 * @author Anna Azzam
 * @author Charlotte Han
 * @author Connor Coyne
 * @author Craig Feeney
 * @author Leon Nguyen
 * 
 */

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MainMenu extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel parentWindow;
	
	public MainMenu(JPanel parentWindow) {
		this.parentWindow = parentWindow;
		initialiseMenu();
	}
	
	public void initialiseMenu() {
		// add the start screen
		JPanel startScreen = new JPanel();
		
		JButton startButton = new JButton("Start Game");
		JButton exitButton = new JButton("Exit to Desktop");
		
		startButton.setToolTipText("Start a new maze");
		exitButton.setToolTipText("Close program and return to desktop");
		
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// switch screens
				CardLayout c1 = (CardLayout) (parentWindow.getLayout());
				c1.show(parentWindow, "Game Screen");
			}
		});
		
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// exit program
				System.exit(0);
			}
		});
		
		startScreen.add(startButton);
		startScreen.add(exitButton);
		this.add(startScreen);
	}
}
