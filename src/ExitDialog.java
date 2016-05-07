import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextPane;

public class ExitDialog extends JDialog {
	public ExitDialog() {
		init();
	}

	private void init() {
		setTitle("Exit to Menu");
		setResizable(false);

		// create buttons and text to be displayed
		JButton okButton = new JButton("Ok");
		JButton cancelButton = new JButton("Cancel");
		JTextPane message = new JTextPane();
		
		message.setText("Are you sure you want to exit?");
		message.setEditable(false);
		

		// set listeners and responses
		// ok takes you back to the main menu
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//goToMenu();
			}
		});
		
		// cancel closes the dialog and does nothing else
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		this.add(okButton);
		this.add(cancelButton);
		this.add(message);

		pack();
		this.setSize(300, 150);
		this.setLocationRelativeTo(null);
	}
}
