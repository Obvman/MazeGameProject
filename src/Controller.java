import javax.swing.*;
import java.awt.*;

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
