import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

public class OptionsScreen extends JPanel {
	private MainWindow mainWindow;
	
	public OptionsScreen (MainWindow _mainWindow) {
		this.mainWindow = _mainWindow;
		init();
	}
	
	private void init() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		// resolution picker (dropdown list)
		
		// fullscreen option (checkbox)
		
		// controls list (up, down, left, right, shoot, pause, )
	}
}
