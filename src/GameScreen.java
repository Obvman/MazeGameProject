import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class GameScreen extends JPanel {
	
	private MainWindow mainWindow;
	
	public GameScreen(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		initGame();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image background = (new ImageIcon("images/game_bg.jpg")).getImage();
        g.drawImage(background, 0, 0, null); 
	}
	
	private void initGame() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // check

		// add UI components
		add(new MazePanel());
		initStatusBar();
		initSideMenu();
	}
	
	private void initStatusBar() {
		JPanel statusBar = new JPanel();
		statusBar.setLayout(new BorderLayout());
		statusBar.setPreferredSize(new Dimension(0, 40)); // check

		// score
		JLabel score = new JLabel("Score: ");
		statusBar.add(score, BorderLayout.WEST);
		
		// time
		JLabel time = new JLabel("Time: ");
		statusBar.add(time, BorderLayout.CENTER);
		time.setHorizontalAlignment(JLabel.CENTER);
		
		// hints remaining
		JLabel hints = new JLabel("Hints Remaining: ");
		statusBar.add(hints, BorderLayout.EAST);
		
		add(statusBar, BorderLayout.NORTH);
	}
	
	private void initSideMenu() {
		JPanel sideMenu = new JPanel();
		GroupLayout groupLayout = new GroupLayout(sideMenu);
		sideMenu.setLayout(groupLayout);
		
		// to check
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		
		// hint button
		JButton hintButton = new JButton("Hint");
		hintButton.setToolTipText("Reveals a hint");
		hintButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Hahaha we don't give you hints");
			}
		});
		
		// pause button
		JButton pauseButton = new JButton("Pause");
		pauseButton.setToolTipText("Pause the game and timer");
		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("The game is actually paused right now, trust me");
			}
		});
		
		// menu button
		JButton menuButton = new JButton("Back to Main Menu");
		menuButton.setToolTipText("Return back to the main menu");
		menuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.switchToMenu();
			}
		});
		
		// TODO: clean this
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup().addGroup(groupLayout.createParallelGroup().addComponent(hintButton)
				.addComponent(pauseButton).addComponent(menuButton)));

		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(groupLayout.createSequentialGroup().addComponent(hintButton).addComponent(pauseButton)
						.addComponent(menuButton)));
		
		groupLayout.linkSize(hintButton, pauseButton, menuButton); // keep size of buttons consistent

		add(sideMenu, BorderLayout.EAST);
	}
}
