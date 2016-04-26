import javax.swing.SwingUtilities;

/* Main class subject to change just
 *  here to make other stuff work */
public class MazeSystem {
	public static void main(String[] args) {
		System.out.println("Let's start pls");
		/*SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow();
				
			}
		});*/
		MainWindow w = new MainWindow();
		w.setVisible(true);

	}

}
