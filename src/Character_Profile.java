import java.awt.*;
import java.util.*;
import javax.swing.*;
public class Character_Profile {

	private TreeMap <String, Integer> inventory = new TreeMap <String, Integer>();
	private Character player;
	
	private JFrame frame;
	private JPanel panel, characterPanel, inventoryPanel;
	private JLabel bob;
	
	//Constructor: Character_Profile
	//return: n/a
	//description: initialize objects
	//parameters: n/a
	public Character_Profile (Character p) {
		player = p;
		putTree();
		
		//jFrame stuff
		frame = new JFrame ("Character Inventory");
		frame.setPreferredSize(new Dimension (280, 655));
		frame.setLocation(0, 10);
		
		panel = new JPanel();
		panel.setLayout(new GridLayout (2, 1));
		panel.setBackground(Color.decode("#FCFAE8"));
		
		characterPanel = new JPanel();
		bob = new JLabel ();
		bob.setIcon(new ImageIcon ("bob.png"));
		
		inventoryPanel = new JPanel();
		inventoryPanel.setLayout(new GridLayout(3,4));

		for (Map.Entry<String, Integer> entry: inventory.entrySet()) {
			JPanel icon = new JPanel();
			JLabel temp = new JLabel ();
			temp.setIcon(new ImageIcon((new ImageIcon(entry.getKey()+".png")).getImage().getScaledInstance(65, 65, Image.SCALE_DEFAULT)));
			icon.add(temp);
			icon.add(new JLabel(entry.getValue()+""));
			inventoryPanel.add(icon);
		}
		
		characterPanel.add(bob);
		panel.add(characterPanel);
		panel.add(inventoryPanel);
		
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
	
	//method: putTree
	//parameters: n/a
	//Description: initializes the tree map
	//return type: void
	public void putTree () {
		inventory.put("Logs", player.getLogs());
		inventory.put("Bowl", player.getBowl());
		inventory.put("Apples", player.getApples());
		inventory.put("Stone", player.getStone());
		inventory.put("Iron", player.getIron());
		inventory.put("Gold", player.getGold());
		inventory.put("Wooden Pickaxe", player.getWPick());
		inventory.put("Stone Pickaxe", player.getSPick());
		inventory.put("Iron Pickaxe", player.getIPick());
	}
	
}
