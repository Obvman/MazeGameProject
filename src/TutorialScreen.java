import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class TutorialScreen extends JPanel {

	private MainWindow mainWindow;
	
	public TutorialScreen(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		initTutorial();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image background = (new ImageIcon("resources/gameScreen_bg.jpg")).getImage();
	    g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
	}
	
	private void initTutorial() {
		JPanel tutorialScreen = new JPanel(new GridBagLayout());
		tutorialScreen.setOpaque(false);
		add(tutorialScreen, "Tutorial Screen");
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(3, 3, 3, 3);

		//Tutorial icon
		JLabel t1 = new JLabel(new ImageIcon("resources/tut.png"));
		tutorialScreen.add(t1, gbc);
		tutorialScreen.add(Box.createVerticalStrut(100));
		
		gbc.gridy = 2;
		tutorialScreen.add(Box.createRigidArea(new Dimension(0, 50)), gbc);
		
		//Backstory
		gbc.gridy = 3;
		JLabel bg1 = new JLabel("Exiled to a lava wasteland, you must navigate a treacherous"
				+ " maze filled with deadly monsters and precious loot to return back to society. ", SwingConstants.CENTER);
		bg1.setForeground(Color.WHITE);
		tutorialScreen.add(bg1, gbc);
		
		gbc.gridy = 4;
		JLabel bg2 = new JLabel("Traverse the maze until you find the golden key which is then used to "
				+ "unlock the steel door, allowing you to progress one level closer to freedom.", SwingConstants.CENTER);
		bg2.setForeground(Color.WHITE);
		tutorialScreen.add(bg2, gbc);
		
		gbc.gridy = 5;
		JLabel bgi1 = new JLabel(new ImageIcon("resources/key_for_32.png"));
		tutorialScreen.add(bgi1, gbc);
		
		JLabel bgi2 = new JLabel(new ImageIcon("resources/leon_closed_door"));
		tutorialScreen.add(bgi2, gbc);	
		
		//Controls
		gbc.gridy = 6;
		JLabel c1 = new JLabel("To control your player, use the arrow keys to traverse up, down, left or right"
				+ " throughout the maze.", SwingConstants.CENTER);
		c1.setForeground(Color.WHITE);
		tutorialScreen.add(c1, gbc);
		
		//add the controls image here
		gbc.gridy = 7;
		JLabel ci1 = new JLabel(new ImageIcon("resources/arrows.png"));
		tutorialScreen.add(ci1, gbc);
		
		//Enemies
		gbc.gridy = 8;
		JLabel e1 = new JLabel("Littered throughout the maze are monsters and dragons, which are trying to"
				+ " stop you from leaving alive. The monsters spawn from portals and patrol the maze path "
				+ "looking for you.", SwingConstants.CENTER);
		e1.setForeground(Color.WHITE);
		tutorialScreen.add(e1, gbc);
		
		//Monster image
		gbc.gridy = 9;
		JLabel ei1 = new JLabel(new ImageIcon("resources/monster_down.png"));
		tutorialScreen.add(ei1, gbc);
		
		gbc.gridy = 10;
		JLabel e2 = new JLabel("Dragons are able to fly over the lava to find you.", SwingConstants.CENTER);
		e2.setForeground(Color.WHITE);
		tutorialScreen.add(e2, gbc);
		
		//dragon image
		gbc.gridy = 11;
		JLabel ei2 = new JLabel(new ImageIcon("resources/dragon/dragonE4.png"));
		tutorialScreen.add(ei2, gbc);
		
		gbc.gridy = 12;
		JLabel e3 = new JLabel("However, you are not defenceless - press the space bar to unleash a powerful"
				+ " spell which will kill monsters, dragons and portals, clearing your path to freedom.", SwingConstants.CENTER);
		e3.setForeground(Color.WHITE);
		tutorialScreen.add(e3, gbc);
		
		//spell images??
		//space bar image
		gbc.gridy = 13;
		JLabel ei3 = new JLabel(new ImageIcon("resources/space.png"));
		tutorialScreen.add(ei3, gbc);
		
		//Gems
		
		gbc.gridy = 15;
		JLabel g1 = new JLabel("You will also encounter a multitude of "
				+ "gems which can be used to increase your score.", SwingConstants.CENTER);
		g1.setForeground(Color.WHITE);
		tutorialScreen.add(g1, gbc);
		
		gbc.gridy = 16;
		JLabel gi1 = new JLabel(new ImageIcon("resources/gems.png"));
		tutorialScreen.add(gi1, gbc);
		
		//Blank space
		gbc.gridy = 17;
		tutorialScreen.add(Box.createRigidArea(new Dimension(0, 50)), gbc);
		
		gbc.gridy = 18;
		// Main menu button
		JButton menuButton = new JButton(new ImageIcon("resources/main_menu.png"));
		menuButton.setContentAreaFilled(false);
		menuButton.setMargin(new Insets(0, 0, 0, 0));
		menuButton.setFocusPainted(false);
		menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		menuButton.setToolTipText("Return to main menu");
		menuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// display tutorial
				mainWindow.switchToMenu();
			}
		});
				tutorialScreen.add(menuButton, gbc);
		

	}
	
	
}
