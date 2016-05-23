import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class HighscoresPanel extends JPanel {
	
	public HighscoresPanel(LayoutManager layout) {
		this.setLayout(layout);
	}

	public LinkedList<JLabel> getHighscores(int newScore, String newName) {
		LinkedList<JLabel> highscoresLabels = new LinkedList<JLabel>();
		Scanner sc = null;
		try {
			LinkedList<HighscoresEntry> highscores = new LinkedList<HighscoresEntry>();
			
			sc = new Scanner(new FileReader("resources/highscores.txt"));
			while (sc.hasNextLine()) {
				highscores.add(new HighscoresEntry(sc.nextLine()));
			}
			
			// Add new highscore and sort
			if (highscores.get(9).getScore() < newScore) {
				highscores.remove(9);
				highscores.add(new HighscoresEntry(newName + " " + newScore));
				Comparator<HighscoresEntry> comp = new ScoreComparator();
				Collections.sort(highscores, comp);
			}
			
			
			// Print highscores to panel and 
			// output new highscores to file.
			PrintWriter writer = null;
			try {
				writer = new PrintWriter("resources/highscores.txt", "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO does nothing here
			}
			
			int rank = 0;
			for (HighscoresEntry entry : highscores) {
				JLabel scoreLabel = new JLabel(Integer.toString(rank+1) + ") " + entry.getName() + " " + entry.getScore());
				writer.println(entry.getName() + " " + entry.getScore());
				scoreLabel.setForeground(Color.WHITE);
				highscoresLabels.add(scoreLabel);
				rank++;
			}
			
			writer.close();
			
		} catch (FileNotFoundException e) {}
		finally {
			if (sc != null) sc.close();
		}
		
		return highscoresLabels;
	}
	
	private class ScoreComparator implements Comparator<HighscoresEntry> {
		public int compare(HighscoresEntry first, HighscoresEntry second) {
			if (first.getScore() > second.getScore()) return -1;
			if (first.getScore() < second.getScore()) return 1;
			return 0;
		}
	}
	
	/**
	 * Class used to store an entry from the highscores file
	 */
	private class HighscoresEntry {
		private String name;
		private int score;
		
		public HighscoresEntry(String str) {
			String[] tokens = str.split(" ");
			this.name = tokens[0];
			this.score = Integer.valueOf(tokens[1]);
		}

		public String getName() {
			return this.name;
		}
		
		public int getScore() {
			return this.score;
		}
		
	}

}
