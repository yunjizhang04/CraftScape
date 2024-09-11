import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class chickenWinnerPage extends JPanel implements ActionListener{

	JPanel panel, panel2, buttonPanel;
	JLabel title;
	JButton restart, exitEND;
	
	public chickenWinnerPage () {		
		panel = new JPanel();
		panel.setLayout(new GridLayout(4,1));
		panel.setBackground(Color.YELLOW);
		
		//center panel (where title and character is at
		panel2 = new JPanel();
		panel2.setBackground(Color.YELLOW);
		
		title = new JLabel ("YOU'VE ESCAPED!");
		title.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		title.setFont(new Font("Courier New", Font.BOLD, 50));
		
		//panel for the buttons
		buttonPanel = new JPanel ();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 10));
		buttonPanel.setBackground(Color.YELLOW);
		
		//buttons
		restart = new JButton ("RESTART");
		restart.setActionCommand("restart");
		restart.addActionListener(this);
		
		exitEND = new JButton ("EXIT");
		exitEND.setActionCommand("exitEND");
		exitEND.addActionListener(this);
		
		//adding onto button panel
		buttonPanel.add(restart);
		buttonPanel.add(exitEND);
		
		//adding onto panel2
		panel2.add(title);
		
		//adding onto main panel
		panel.add(panel2);
		panel.add(buttonPanel);
		//bob
		JLabel bob = new JLabel ();
		bob.setIcon(new ImageIcon ("bob.png"));
		panel2.add(bob);
		
		add(panel);
		setBackground(Color.YELLOW);
	}

	public static void main(String[] args) {
		//frame settings
		chickenWinnerPage main = new chickenWinnerPage ();
		JFrame frame = new JFrame ("Craftscape");

		frame.setSize(1000,600);
		frame.setLocation(150,50);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.add(main);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String a = e.getActionCommand();
		
		if (a.equals("restart")) {
//			panel.removeAll();
			panel2.setBackground(Color.PINK);
//			JPanel gamePanel = new JPanel ();
//			gamePanel.setBackground(Color.PINK);
			
		}
		else if (a.equals("exitEND")) {
			System.exit(0);
		}
	}

}
