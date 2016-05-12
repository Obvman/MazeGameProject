import javax.swing.*;

public class MazeGame {

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
