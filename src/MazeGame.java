import javax.swing.*;

public class MazeGame {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				MainWindow mw = new MainWindow(800, 600);
				mw.setVisible(true);
			}
		});

	}

}
