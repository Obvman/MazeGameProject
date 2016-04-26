import java.awt.*;
import java.awt.event.*;
import java.awt.Container;

import javax.swing.*;

public class MainWindow extends JFrame {
	/**
	 * Constructor
	 * @params 
	 * x: int
	 * 
	 */
	public MainWindow () {
		Container cp = this.getContentPane();
		cp.setLayout(new FlowLayout());
		cp.add(new JLabel("HI there"));
		thing = new JTextField("0", 10);
		thing.setEditable(false);
		cp.add(thing);
		
		thingo = new JButton("START");
		cp.add(thingo);
		
		thingo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				i++;
				thing.setText(i + " ");
			}
		});
	}

	// Hayden wants our fields at the bottom for some reason
	private JTextField thing;
	private JButton thingo;
	private int i = 0;
}
