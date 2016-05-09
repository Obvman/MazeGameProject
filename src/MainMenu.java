import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MainMenu extends JPanel {
	private JPanel parentWindow;
	
	public MainMenu(JPanel _parentWindow) {
		this.parentWindow = _parentWindow;
		initialiseMenu();
	}
	
	public void initialiseMenu() {
		// add the start screen
		JPanel startScreen = new JPanel();
		
		JButton startButton = new JButton("Start Game");
		JButton exitButton = new JButton("Exit to Desktop");
		
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
