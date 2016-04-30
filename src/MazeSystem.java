import java.awt.EventQueue;

/* Main class subject to change just
 *  here to make other stuff work */
public class MazeSystem {
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				System.out.println("Opening Main Window");
				MainWindow window = new MainWindow();
				window.setVisible(true);;
			}
		});
	}

}
