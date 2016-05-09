/**
 * Main class that runs and creates window
 * 
 * COMP2911 Project - 16s1
 * @author Anna Azzam
 * @author Charlotte Han
 * @author Connor Coyne
 * @author Craig Feeney
 * @author Leon Nguyen
 * 
 */

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Controller {
	// Uses a border layout to implement a status bar and side menu
	public static void main(String[] args) {

		// Start the main window
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainWindow mainWindow = new MainWindow(700, 600);
				mainWindow.setVisible(true);
			}
		});
	}
}
