import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class mainProgramMenu extends JPanel implements ActionListener {
	String name = "";
	static JPanel panelMAIN;
	JPanel hsPanelMAIN, panel2MAIN, buttonPanelM;
	JLabel titleM, highscore, character;
	JButton start, exit;
	
	List<Highscore> score = new ArrayList<>();
	
	public mainProgramMenu () {
		panelMAIN = new JPanel();
		panelMAIN.setLayout(new BorderLayout());
		panelMAIN.setBackground(Color.decode("#FF0FFF"));
		
		//higscore panelMAIN
		hsPanelMAIN = new JPanel();
		hsPanelMAIN.setLayout(new GridLayout(1,3));
		hsPanelMAIN.setBackground(Color.decode("#FF0FFF"));

		score.add(new Highscore(name, 0));
//		Collections.sort(score);
		highscore = new JLabel ("High Score: " + score.get(0));
		
		//center panelMAIN
		panel2MAIN = new JPanel();
		panel2MAIN.setLayout(new FlowLayout());
		panel2MAIN.setBackground(Color.YELLOW);
		
		titleM = new JLabel ("CraftScape");
		titleM.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		titleM.setAlignmentX(Component.CENTER_ALIGNMENT);
		titleM.setFont(new Font("Courier New", Font.BOLD, 62));
		
		character = new JLabel (new ImageIcon ((new ImageIcon("bob.png").getImage().getScaledInstance(400,400, Image.SCALE_DEFAULT))));
		character.setAlignmentX(Component.CENTER_ALIGNMENT);
		character.setAlignmentY(Component.TOP_ALIGNMENT);
		
		//panel for the buttons
		buttonPanelM = new JPanel ();
		buttonPanelM.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 10));
		buttonPanelM.setBackground(Color.YELLOW);
		
		//buttons
		start = new JButton ("START");
		start.setActionCommand("start");
		start.addActionListener(this);
		
		exit = new JButton ("EXIT");
		exit.setActionCommand("exit");
		exit.addActionListener(this);
		
		//adding onto high score panel
		hsPanelMAIN.add(new JLabel ());
		hsPanelMAIN.add(new JLabel ());
		hsPanelMAIN.add(highscore);
		
		//adding onto button panel
		buttonPanelM.add(start);
		buttonPanelM.add(exit);
		
		//adding onto panel2
		panel2MAIN.add(titleM);
		panel2MAIN.add(character);
		
		//adding onto main panel
		panelMAIN.add(hsPanelMAIN, BorderLayout.NORTH);
		panelMAIN.add(panel2MAIN, BorderLayout.CENTER);
		panelMAIN.add(buttonPanelM, BorderLayout.SOUTH);

		setBackground(Color.YELLOW);
	}

	public static void main(String[] args) {
		//frame settings
		mainProgramMenu main = new mainProgramMenu ();
		JFrame frame = new JFrame ("Craftscape");

		frame.setSize(1000,600);
		frame.setLocation(150,50);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.add(panelMAIN);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String a = e.getActionCommand();
		
		if (a.equals("start")) {
			name = JOptionPane.showInputDialog("Enter your name:");
		}
		else if (a.equals("exit")) {
			System.exit(0);
		}
	}

}
