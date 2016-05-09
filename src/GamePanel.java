
/**
 * Game Panel class that displays after starting a game
 * Contains:
 *   - Status bar (top)
 *     * Hints
 *     * Time display
 *     * Score display
 *   - maze panel (centre left)
 *     * displays image of the maze
 *   - side menu (right)
 *     * Hint button
 *     * Pause button
 *     * exit button (brings up ExitDialog)
 * 
 * COMP2911 Project - 16s1
 * @author Anna Azzam
 * @author Charlotte Han
 * @author Connor Coyne
 * @author Craig Feeney
 * @author Leon Nguyen
 * 
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private MainWindow parentWindow;
	private JPanel statusBar;
	private JPanel sideMenu;
	private MazePanel mazePanel; // just the maze itself
	private ExitDialog menuExitDialog; // returning to menu dialog
	private ExitDialog desktopExitDialog; // returning to menu dialog

	public GamePanel(MainWindow _parentWindow) {
		this.parentWindow = _parentWindow;
		init();
	}

	private void init() {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// frame to display the maze state
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
		// Double dHeight = this.frameWidth * 0.8;
		// Double dWidth = this.frameHeight * 0.8;
		// mazePanel.setSize(dWidth.intValue(), dHeight.intValue());
		// just so we can differentiate it until the real maze is made
		
		// mazePanel.setBackground(Color.blue);
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
		JButton menuExitButton = new JButton("Exit to Menu");
		JButton desktopExitButton = new JButton("Exit to desktop");

		// tool tips
		hintButton.setToolTipText("Get a hint");
		pauseButton.setToolTipText("Pause the game");
		menuExitButton.setToolTipText("Return to the main menu");
		desktopExitButton.setToolTipText("Quit game and return to desktop");

		// set out the layout
		g1.setHorizontalGroup(g1.createSequentialGroup().addGroup(g1.createParallelGroup().addComponent(hintButton)
				.addComponent(pauseButton).addComponent(menuExitButton).addComponent(desktopExitButton)));

		g1.setVerticalGroup(g1.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(g1.createSequentialGroup().addComponent(hintButton).addComponent(pauseButton)
						.addComponent(menuExitButton).addComponent(desktopExitButton)));

		g1.linkSize(hintButton, pauseButton, menuExitButton);
		// pack();

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

		menuExitDialog = new ExitDialog(parentWindow, "menu");
		desktopExitDialog = new ExitDialog(parentWindow, "desktop");

		menuExitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				menuExitDialog.setLocationRelativeTo(parentWindow);
				menuExitDialog.setVisible(true);
			}
		});

		desktopExitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				desktopExitDialog.setLocationRelativeTo(parentWindow);
				desktopExitDialog.setVisible(true);
			}
		});

	}
}
