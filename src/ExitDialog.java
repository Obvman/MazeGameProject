/**
 * Class for prompt when exiting game screen
 * TODO may want to stick this inside GamePanel cos it doesn't work with any other class
 * 
 * COMP2911 Project - 16s1
 * @author Anna Azzam
 * @author Charlotte Han
 * @author Connor Coyne
 * @author Craig Feeney
 * @author Leon Nguyen
 * 
 */

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextPane;

public class ExitDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private MainWindow parentWindow;
	
	public ExitDialog(MainWindow _parentWindow) {
		this.parentWindow = _parentWindow;
		init();
	}

	private void init() {
		this.setTitle("Exit to Menu");
		this.setResizable(false);
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

		// create buttons and text to be displayed
		JButton okButton = new JButton("Ok");
		JButton cancelButton = new JButton("Cancel");
		JTextPane message = new JTextPane();

		message.setText("Are you sure you want to exit?");
		message.setEditable(false);
		message.setBackground(getContentPane().getBackground());

		// set listeners and responses
		// Ok takes you back to the main menu
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				// go to menu screen
				parentWindow.goToMenu();
				dispose(); // get rid of dialogue box
			}
		});

		// cancel closes the dialog and does nothing else
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		this.add(message);
		this.add(okButton);
		this.add(cancelButton);

		pack();
		this.setSize(250, 130);
		this.setBackground(Color.white);
		this.setLocationRelativeTo(parentWindow);
	}

	// helper to allow this references from inside abstract classes
//	private ExitDialog getThis() {
//		return this;
//	}
}
