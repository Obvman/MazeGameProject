import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainWindow extends JFrame {
	private JPanel statusBar;
	private JPanel sideMenu;
	private MazePanel mazePanel;
	private JPanel containerPanel;
	private int frameWidth;
	private int frameHeight;

	public MainWindow(int width, int height) {

		init(width, height);
	}

	private void init(int width, int height) {
		// encompassing panel for some extra flexiblity
		containerPanel = new JPanel();
		containerPanel.setLayout(new BorderLayout());
		containerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// frame to display the maze state
		mazePanel = new MazePanel();
		initMazePanel();

		// statusBar to appear above the maze
		statusBar = new JPanel();
		initStatusBar();

		// sideMenu to appear right of the maze
		sideMenu = new JPanel();
		initSideMenu();

		containerPanel.add(mazePanel, BorderLayout.CENTER);
		containerPanel.add(statusBar, BorderLayout.NORTH);
		containerPanel.add(sideMenu, BorderLayout.EAST);

		// Initialize main frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setTitle("Main Window");
		this.setLocationRelativeTo(null);
		this.add(containerPanel);
	}

	// put this into MazePanel's init() when done
	private void initMazePanel() {
		Double dHeight = this.frameWidth * 0.8;
		Double dWidth = this.frameHeight * 0.8;
		mazePanel.setSize(dWidth.intValue(), dHeight.intValue());
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

		// set out the layout
		g1.setHorizontalGroup(g1.createSequentialGroup().addGroup(
				g1.createParallelGroup()
				.addComponent(hintButton)
				.addComponent(pauseButton)
				.addComponent(exitButton))
		);
		
		g1.setVerticalGroup(g1.createParallelGroup(GroupLayout.Alignment.CENTER).addGroup(
				g1.createSequentialGroup()
				.addComponent(hintButton)
				.addComponent(pauseButton)
				.addComponent(exitButton))
		);
		
		g1.linkSize(hintButton, pauseButton, exitButton);
		pack();
		
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
				System.out.println("Hahaha we don't give you hints");
			}
		});
		
		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("The game is actually paused right now, trust me");
			}
		});
		
		// Should we go straight to the main menu or give an are you sure prompt?
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//goToMenu() or:
				ExitDialog ed = new ExitDialog();
				ed.setVisible(true);
			}
		});
		
	}
	
	private void goToMenu() {
		
	}

	public void changeSize(int width, int height) {
		this.setSize(width, height);
	}
}
