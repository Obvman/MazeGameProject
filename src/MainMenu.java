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
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// switch screens
				CardLayout c1 = (CardLayout) (parentWindow.getLayout());
				c1.show(parentWindow, "Game Screen");
			}
		});
		
		startScreen.add(startButton);
		this.add(startScreen);
//		this.screens.add(startScreen, "Start Screen");
		
		// add the game screen
//		GameScreen gameScreen = new GameScreen();
//		gameScreen.add(new JLabel("Hello this is the game..."));
//		this.screens.add(gameScreen, "Game Screen");
		
		// make the first screen the start screen
//		CardLayout cl = (CardLayout) this.screens.getLayout();
//		cl.show(this.screens, "Start Screen");
	}
}
