import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
	private MainWindow parentWindow;
	private JPanel statusBar;
	private JPanel sideMenu;
	private MazePanel mazePanel; // just the maze itself
	private ExitDialog exitDialog; // returning to menu dialog
	
	public GamePanel(MainWindow _parentWindow) {
		this.parentWindow = _parentWindow;
		init();
	}
	
	private void init() {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// frame to display the maze state
		// TODO move this Maze to a legit class maybe
		Maze maze = new Maze();
		mazePanel = new MazePanel();
		mazePanel.addMaze(maze.getMaze(31));
		initMazePanel();

		// statusBar to appear above the maze
		statusBar = new JPanel();
		initStatusBar();

		// sideMenu to appear right of the maze
		sideMenu = new JPanel();
		initSideMenu();

		this.add(mazePanel, BorderLayout.CENTER);
		this.add(statusBar, BorderLayout.NORTH);
		this.add(sideMenu, BorderLayout.EAST);

		
	}
	
	// put this into MazePanel's init() when done
	private void initMazePanel() {
//		Double dHeight = this.frameWidth * 0.8;
//		Double dWidth = this.frameHeight * 0.8;
//		mazePanel.setSize(dWidth.intValue(), dHeight.intValue());
		// just so we can differentiate it until the real maze is made
		mazePanel.setBackground(Color.blue);
	}

	private void initStatusBar() {

		statusBar.setLayout(new BorderLayout());
		statusBar.setPreferredSize(new Dimension(-1, 40));

		JLabel hints = new JLabel("Hints Remaining: ");
		JLabel time = new JLabel("Time: ");
		JLabel score = new JLabel("Score:");

		hints.setHorizontalAlignment(JLabel.LEFT);
		time.setHorizontalAlignment(JLabel.CENTER);
		score.setHorizontalAlignment(JLabel.RIGHT);

		statusBar.add(hints, BorderLayout.WEST);
		statusBar.add(time);
		statusBar.add(score, BorderLayout.EAST);
	}

	private void initSideMenu() {
		GroupLayout g1 = new GroupLayout(sideMenu);
		sideMenu.setLayout(g1);

		g1.setAutoCreateContainerGaps(true);
		g1.setAutoCreateGaps(true);

		// Create buttons for menu
		JButton hintButton = new JButton("Hint");
		JButton pauseButton = new JButton("Pause");
		JButton exitButton = new JButton("Exit to Menu");
		
		// tooltips
		hintButton.setToolTipText("Get a hint");
		pauseButton.setToolTipText("Pause the game");
		exitButton.setToolTipText("Return to the main menu");

		// set out the layout
		g1.setHorizontalGroup(g1.createSequentialGroup().addGroup(
				g1.createParallelGroup().addComponent(hintButton).addComponent(pauseButton).addComponent(exitButton)));

		g1.setVerticalGroup(g1.createParallelGroup(GroupLayout.Alignment.CENTER).addGroup(g1.createSequentialGroup()
				.addComponent(hintButton).addComponent(pauseButton).addComponent(exitButton)));

		g1.linkSize(hintButton, pauseButton, exitButton);
		//pack();

		// add listeners for each button
		hintButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Hahaha we don't give you hints");
			}
		});

		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("The game is actually paused right now, trust me");
			}
		});

		exitDialog = new ExitDialog(parentWindow);
		// Should we go straight to the main menu or give an are you sure
		// prompt?
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// goToMenu() or:
				exitDialog.setLocationRelativeTo(parentWindow);
				exitDialog.setVisible(true);
			}
		});

	}
}
