import java.awt.EventQueue;

/* Main class subject to change just
 *  here to make other stuff work */
public class MazeSystem {
	public static void main(String[] args) {
		System.out.println("Let's start pls");
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainWindow window = new MainWindow();
				window.setVisible(true);;
			}
		});
	}

}
