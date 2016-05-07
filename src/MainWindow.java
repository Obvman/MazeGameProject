import java.awt.*;
import javax.swing.*;

public class MainWindow extends JFrame {
	private JPanel statusBar;
	private JList sideMenu;
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
		containerPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
	
		
		// frame to display the maze state
		mazePanel = new MazePanel();
		initMazePanel();
		
		// statusBar to appear above the maze
		statusBar = new JPanel();
		initStatusBar();
		
		// sideMenu to appear right of the maze
		sideMenu = new JList();
		initSideMenu();
		
		containerPanel.add(mazePanel, BorderLayout.CENTER);
		containerPanel.add(statusBar, BorderLayout.NORTH);
		containerPanel.add(sideMenu,  BorderLayout.EAST);
		
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
		
	}
	
	public void changeSize(int width, int height) {
		this.setSize(width, height);
	}
}
