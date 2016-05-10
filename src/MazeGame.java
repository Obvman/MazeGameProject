import javax.swing.*;

public class MazeGame {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				MainWindow mw = new MainWindow(900, 1000);
				mw.setVisible(true);
			}
		});

	}

}
