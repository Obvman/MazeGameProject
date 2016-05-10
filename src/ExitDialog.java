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
	private String mode; // either menu or desktop depending on exit procedure used
	
	public ExitDialog(MainWindow _parentWindow, String _mode) {
		this.parentWindow = _parentWindow;
		this.mode = _mode;
		init();
	}

	private void init() {
		if (mode.equals("menu"))
			this.setTitle("Exit to Menu");
		else if (mode.equals("desktop"));
			this.setTitle("Exit to desktop");
		
		this.setResizable(false);
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

		// create buttons and text to be displayed
		JButton okButton = new JButton("Ok");
		JButton cancelButton = new JButton("Cancel");
		JTextPane message = new JTextPane();

		if (mode.equals("menu")) {
			message.setText("Are you sure you want to return to main menu?");
		} else if (mode.equals("desktop")) {
			message.setText("Are you sure you want to exit to desktop?");
		}
		message.setEditable(false);
		message.setBackground(getContentPane().getBackground());

		// set listeners and responses
		// Ok takes you back to the main menu
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mode.equals("menu"))
					// go to menu screen
					parentWindow.goToMenu();
				else if (mode.equals("desktop"))
					System.exit(0);
				
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
		
		this.setBackground(Color.white);
		this.setLocationRelativeTo(parentWindow);
		this.setSize(320, 130);
	}

	// helper to allow this references from inside abstract classes
//	private ExitDialog getThis() {
//		return this;
//	}
}
