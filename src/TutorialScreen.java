import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class TutorialScreen extends JPanel {

	public TutorialScreen(MainWindow mainWindow) {
		this.mainWindow = mainWindow;

		setLayout(new GridBagLayout());

		initTutorial();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image background = (new ImageIcon("resources/gameScreen_bg.jpg")).getImage();
		g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
	}

	private void initTutorial() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(3, 3, 3, 3);
		gbc.weighty = 1;

		// Tutorial image
		gbc.gridy = 0;
		JLabel t1 = new JLabel(new ImageIcon("resources/tut.png"));
		add(t1, gbc);

		// Back story
		gbc.gridy = 1;
		JPanel story = new JPanel(new GridLayout(0, 1));
		story.setOpaque(false);
		JLabel storyLabel = new JLabel("Exiled to a lava wasteland, you must navigate a maze filled with deadly monsters to escape. ", SwingConstants.CENTER);
		storyLabel.setForeground(Color.WHITE);
		story.add(storyLabel);
		add(story, gbc);

		// Objective
		gbc.gridy = 2;
		JPanel objective = new JPanel(new GridBagLayout());
		objective.setOpaque(false);

		GridBagConstraints gbcObjective = new GridBagConstraints();
		
		JLabel objectiveLabel = new JLabel("Each level will contain a golden key which you must first find and collect "
				+ "before being able to unlock the door to the next level", SwingConstants.CENTER);
		objectiveLabel.setForeground(Color.WHITE);
		gbcObjective.gridy = 0;
		objective.add(objectiveLabel, gbcObjective);

		JPanel objectiveImages = new JPanel();
		objectiveImages.setOpaque(false);
		objectiveImages.add(new JLabel(new ImageIcon("resources/tiles/key_tile.gif")));
		objectiveImages.add(new JLabel(new ImageIcon("resources/tiles/leon_closed_door.png")));
		gbcObjective.gridy = 1;
		objective.add(objectiveImages, gbcObjective);

		add(objective, gbc);

		// Enemies
		gbc.gridy = 3;
		JPanel enemies = new JPanel(new GridBagLayout());
		enemies.setOpaque(false);
		
		GridBagConstraints gbcEnemies = new GridBagConstraints();

		JLabel enemiesLabel1 = new JLabel("During your journey, you will encounter monsters and dragons that "
				+ "spawn from portals to try and kill you.", SwingConstants.CENTER);
		enemiesLabel1.setForeground(Color.WHITE);
		gbcEnemies.gridy = 0;
		enemies.add(enemiesLabel1, gbcEnemies);

		JLabel enemiesLabel2 = new JLabel("Beware! Dragons are able to fly over lava.", SwingConstants.CENTER);
		enemiesLabel2.setForeground(Color.WHITE);
		gbcEnemies.gridy = 1;
		enemies.add(enemiesLabel2, gbcEnemies);

		JPanel enemiesButtons = new JPanel();
		enemiesButtons.setOpaque(false);
		enemiesButtons.add(new JLabel(new ImageIcon("resources/monster_down.png")));
		enemiesButtons.add(new JLabel(new ImageIcon("resources/dragon/dragonE4.png")));
		enemiesButtons.add(new JLabel(new ImageIcon("resources/blue_portal_32.gif")));
		gbcEnemies.gridy = 2;
		enemies.add(enemiesButtons, gbcEnemies);

		add(enemies, gbc);

		// Spells
		gbc.gridy = 4;
		JPanel spells = new JPanel(new GridBagLayout());
		spells.setOpaque(false);
		
		GridBagConstraints gbcSpells = new GridBagConstraints();

		JLabel spellsLabel = new JLabel("Use spells to defeat monsters and destroy portals to stop them from respawning", SwingConstants.CENTER);
		spellsLabel.setForeground(Color.WHITE);
		gbcSpells.gridy = 0;
		spells.add(spellsLabel, gbcSpells);

		JPanel spellsImages = new JPanel();
		spellsImages.setOpaque(false);
		spellsImages.add(new JLabel(new ImageIcon("resources/spells/water2.png")));
		spellsImages.add(new JLabel(new ImageIcon("resources/spells/fire2.png")));
		spellsImages.add(new JLabel(new ImageIcon("resources/spells/air2.png")));
		gbcSpells.gridy = 1;
		spells.add(spellsImages, gbcSpells);

		add(spells, gbc);

		// Gems
		gbc.gridy = 5;
		JPanel gems = new JPanel(new GridBagLayout());
		gems.setOpaque(false);

		GridBagConstraints gbcGems = new GridBagConstraints();
		
		JLabel gemsLabel = new JLabel("Improve your score by collecting gems scattered throughout the maze", SwingConstants.CENTER);
		gemsLabel.setForeground(Color.WHITE);
		gbcGems.gridy = 0;
		gems.add(gemsLabel, gbcGems);

		JPanel gemsImages = new JPanel();
		gemsImages.setOpaque(false);
		gemsImages.add(new JLabel(new ImageIcon("resources/gemssmall.png")));
		gbcGems.gridy = 1;
		gems.add(gemsImages, gbcGems);

		add(gems, gbc);

		// Main menu button
		gbc.gridy = 6;
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
		add(menuButton, gbc);
	}

	private MainWindow mainWindow;
}
