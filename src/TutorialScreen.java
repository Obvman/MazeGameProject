import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class TutorialScreen extends JPanel {

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
		gbc.gridy = 0;
		JLabel t1 = new JLabel(new ImageIcon("resources/tut.png"));
		tutorialScreen.add(t1, gbc);

		gbc.gridy = 1;
		tutorialScreen.add(Box.createRigidArea(new Dimension(0, 25)), gbc);
		
		//Backstory
		gbc.gridy = 2;
		JLabel bg1 = new JLabel("Exiled to a lava wasteland, you must navigate a maze"
				+ " filled with deadly monsters to escape. ", SwingConstants.CENTER);
		bg1.setForeground(Color.WHITE);
		tutorialScreen.add(bg1, gbc);

		gbc.gridy = 3;
		tutorialScreen.add(Box.createRigidArea(new Dimension(0, 25)), gbc);

		gbc.gridy = 4;
		JLabel bg2 = new JLabel("Each level will contain a golden key which you must first find and collect"
				+ " before being able to unlock the door to the next level", SwingConstants.CENTER);
		bg2.setForeground(Color.WHITE);
		tutorialScreen.add(bg2, gbc);

		gbc.gridy = 5;
		JPanel key = new JPanel();
		key.setOpaque(false);
		key.add(new JLabel(new ImageIcon("resources/tiles/key_tile.gif")));
		key.add(new JLabel(new ImageIcon("resources/tiles/leon_closed_door.png")));
		tutorialScreen.add(key, gbc);	

		gbc.gridy = 6;
		tutorialScreen.add(Box.createRigidArea(new Dimension(0, 25)), gbc);

		//Enemies
		gbc.gridy = 7;
		JLabel e1 = new JLabel("During your journey, you will encounter monsters and dragons that spawn from portals to try and kill you.", SwingConstants.CENTER);
		e1.setForeground(Color.WHITE);
		tutorialScreen.add(e1, gbc);
		
		gbc.gridy = 8;
		JLabel e2 = new JLabel("Beware! Dragons are able to fly over lava.", SwingConstants.CENTER);
		e2.setForeground(Color.WHITE);
		tutorialScreen.add(e2, gbc);

		gbc.gridy = 9;
		JPanel monsters = new JPanel();
		monsters.setOpaque(false);
		monsters.add(new JLabel(new ImageIcon("resources/monster_down.png")));
		monsters.add(new JLabel(new ImageIcon("resources/dragon/dragonE4.png")));
		monsters.add(new JLabel(new ImageIcon("resources/blue_portal_32.gif")));
		tutorialScreen.add(monsters, gbc);

		gbc.gridy = 10;
		tutorialScreen.add(Box.createRigidArea(new Dimension(0, 25)), gbc);

		gbc.gridy = 11;
		JLabel e3 = new JLabel("Use spells to defeat monsters and destroy portals to stop them from respawning", SwingConstants.CENTER);
		e3.setForeground(Color.WHITE);
		tutorialScreen.add(e3, gbc);

		gbc.gridy = 12;
		JPanel spells = new JPanel();
		spells.setOpaque(false);
		spells.add(new JLabel(new ImageIcon("resources/spells/water2.png")));
		spells.add(new JLabel(new ImageIcon("resources/spells/fire2.png")));
		spells.add(new JLabel(new ImageIcon("resources/spells/air2.png")));
		tutorialScreen.add(spells, gbc);

		gbc.gridy = 13;
		tutorialScreen.add(Box.createRigidArea(new Dimension(0, 25)), gbc);

		gbc.gridy = 14;
		JLabel g1 = new JLabel("Improve your score by collecting gems scattered throughout the maze", SwingConstants.CENTER);
		g1.setForeground(Color.WHITE);
		tutorialScreen.add(g1, gbc);

		gbc.gridy = 15;
		JPanel gems = new JPanel();
		gems.setOpaque(false);
		gems.add(new JLabel(new ImageIcon("resources/gemssmall.png")));
		tutorialScreen.add(gems, gbc);

		gbc.gridy = 16;
		tutorialScreen.add(Box.createRigidArea(new Dimension(0, 25)), gbc);

		gbc.gridy = 17;
		// Main menu button
		JButton menuButton = new JButton(new ImageIcon("resources/buttons/back.png"));
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

	private MainWindow mainWindow;
}
