import javax.swing.SwingUtilities;

public class MazeGame {

	/**
	 * Main entry point for the application
	 * @param args Command-line arguments
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				MainWindow game = new MainWindow();
				game.setVisible(true);
			}
		});
	}
}