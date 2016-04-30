import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.*;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class MainWindow extends JFrame {
	/**
	 * Constructor. Creates window in it's initial state (at the start menu)
	 * 
	 */
	public MainWindow () {
		initUI();
	}
	
	/**
	 * 
	 */
	private void initUI() {
		// create new button objects
		JButton quitButton = new JButton("Quit");
		JButton startButton = new JButton("Start");
		
		// set text for hovering over button
		quitButton.setToolTipText("Exit Program");
		startButton.setToolTipText("Start Game");
		
		// set shortcut keys
		startButton.setMnemonic(KeyEvent.VK_ENTER);
		
		// creates listener that waits for button to be clicked 
		// exits program on event activation
		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("Exiting program by quit button");
				System.exit(0);
			}
		});
		
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("Start Button pressed");
			}
		});
		
		createLayout(quitButton, startButton);
		createMenuBar();
		
		setTitle("Maze Game");
		setSize(300, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	
	private void createLayout(JComponent... arg) {
		Container pane = getContentPane();
		GroupLayout gl = new GroupLayout(pane);
		pane.setLayout(gl);
		
		gl.setAutoCreateContainerGaps(true);
		
		SequentialGroup glHoriz = gl.createSequentialGroup();
		glHoriz.addComponent(arg[0]).addGap(200);
		glHoriz.addComponent(arg[1]).addGap(200);
		gl.setHorizontalGroup(glHoriz);
		
		ParallelGroup glVert = gl.createParallelGroup();
		glVert.addComponent(arg[0]).addGap(120);
		glVert.addComponent(arg[1]).addGap(120);
		gl.setVerticalGroup(glVert);
		
		pack();
	}
	
	private void createMenuBar() {
		JMenuBar menubar = new JMenuBar();
		
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		
		JMenuItem eMenuItem = new JMenuItem("Exit");
		eMenuItem.setMnemonic(KeyEvent.VK_E);
		eMenuItem.setToolTipText("Exit program");
		eMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("Exiting program by menu");
				System.exit(0);
			}
		});
		
		setJMenuBar(menubar);
	}
	
}
