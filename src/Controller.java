/**
 * Main GUI class that handles all panels/screens
 * 
 * COMP2911 Project - 16s1
 * @author Anna Azzam
 * @author Charlotte Han
 * @author Connor Coyne
 * @author Craig Feeney
 * @author Leon Nguyen
 * 
 */

import javax.swing.SwingUtilities;

public class Controller {
	// Uses a border layout to implement a status bar and side menu
	public static void main(String[] args) {

		// Start the main window
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainWindow mainWindow = new MainWindow(800, 600);
				mainWindow.setVisible(true);
			}
		});
	}
}
