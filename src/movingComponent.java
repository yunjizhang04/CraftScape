import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

@SuppressWarnings("serial")
public class movingComponent extends JPanel implements Runnable, KeyListener, MouseListener, ActionListener {
	//GLOBAL VARIABLES
	TreeSet<Integer> highScore = new TreeSet <Integer>();
	
	//JFrame Stuff
	static JPanel entirePanel, dropboxPanel;
	JComboBox <String> craftItems;
	JButton profile, openReadMe;

	//player stuff
	boolean game = true;
	Character player = new Character ();
	int cold = 500;

	boolean check = false;
	boolean fill = true;
	Deque <String> messages = new ArrayDeque <>();

	//fireplace house stuff
	boolean fire = false;
	boolean house = false;

	//rectangles
	Rectangle rect;
	Rectangle border;
	Tree [] trees1 = new Tree [5];
	Rectangle entrance;
	Rectangle lake;
	Rectangle houseB;
	Rectangle fireplaceB;
	Rectangle cave;
	Rectangle sign;

	//tree variables
	int treeH = 90;
	int treeW = 56;

	//day change related stuff
	int timer = 1;
	int day = 0;
	Image [] background = {Toolkit.getDefaultToolkit().getImage("grass.jpg"),Toolkit.getDefaultToolkit().getImage("grass2.jpg")};

	//moving and screen stuff
	boolean up, down, left, right;
	double speed = 5;
	int screenWidth = 1000;
	int screenHeight = 620;
	Thread thread;
	int FPS = 60;

	//bob image stuff
	Image [] bob = {Toolkit.getDefaultToolkit().getImage("bob.png"), Toolkit.getDefaultToolkit().getImage("bobleft.png"), Toolkit.getDefaultToolkit().getImage("bobswim.png"), Toolkit.getDefaultToolkit().getImage("bobswim2.png")};
	Image direction = bob[0];

	//mouse tracking stuff
	int mouseX;
	int mouseY;

	//initializer
	public movingComponent() {

		//----------GAME PANELS--------------------//
		entirePanel = new JPanel ();
		entirePanel.setLayout (new BorderLayout ());

		//set panel size and stuff
		entirePanel.setPreferredSize(new Dimension(screenWidth, screenHeight));

		//dropbox menu
		dropboxPanel = new JPanel ();
		dropboxPanel.setLayout(new FlowLayout());
		dropboxPanel.setBackground(Color.ORANGE);
		entirePanel.add(dropboxPanel, BorderLayout.NORTH);

		//dropbox thing
		String [] itemList = {"Bowl", "Wooden Pickaxe", "Stone Pickaxe", "Iron Pickaxe", "Fireplace", "Special Key :O"};
		craftItems = new JComboBox (itemList);
		craftItems.setSelectedIndex (0);
		craftItems.setActionCommand("craft");
		craftItems.addActionListener (this);

		profile = new JButton("View Profile");
		profile.setActionCommand("profile");
		profile.addActionListener(this);
		
		openReadMe = new JButton("Instructions");
		openReadMe.setActionCommand("textfile");
		openReadMe.addActionListener(this);
		
		dropboxPanel.add(craftItems);
		dropboxPanel.add(profile);
		dropboxPanel.add(openReadMe);

		entirePanel.add(this, BorderLayout.CENTER);

		//setVisible(true);
		entirePanel.addMouseListener(this);
		entirePanel.addKeyListener (this);
		entirePanel.setFocusable(true);
		thread = new Thread(this);
		thread.start();
	}

	public void initialize() {
		//setups before the game starts running
		rect = new Rectangle(447, 230, 53, 70);
		border = new Rectangle (447-40, 230-35, 53+60, 70+65);
		messages.addLast("Hello World");

		trees1[0] = new Tree(20, 590-treeH, treeW, treeH);
		trees1[1] = new Tree(300, 40, treeW, treeH);
		trees1[2] = new Tree(850, 100, treeW, treeH);
		trees1[3] = new Tree(900-treeW, 570-treeH, treeW, treeH);
		trees1[4] = new Tree(250, 350, treeW, treeH);

		entrance = new Rectangle (0,0,200, 80);
		lake = new Rectangle (350, 500, 400, 100);
		houseB = new Rectangle (500-80, 300-80, 80, 80);
		fireplaceB = new Rectangle (520, 300-60, 50, 50);
		cave = new Rectangle (1000-40, 300, 40, 100);
		sign = new Rectangle (1000-60, 420, 40, 40);
	}

	//method that draws the stuff
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		//background
		g2.fillRect(0, 0, screenWidth, screenHeight);
		g2.drawImage(background[day], 0, 0, screenWidth, screenHeight, this);

		//trees
		for(int i = 0; i < trees1.length; i++) {
			g2.drawImage(Toolkit.getDefaultToolkit().getImage("tree.png"), (int) trees1[i].getTreeRect().getX()-65, (int) trees1[i].getTreeRect().getY()-35, treeW+120, treeH+75, this);
		}

		//entrance
		g2.drawImage(Toolkit.getDefaultToolkit().getImage("entrance.png"), entrance.x, entrance.y, 200, 90, this);

		//cave
		g2.setColor(Color.GRAY);
		g2.fill(cave);
		
		//sign
		g2.drawImage(Toolkit.getDefaultToolkit().getImage("sign.png"), sign.x - 6, sign.y-5, 50, 50, this);

		//lake
		g2.setColor(Color.BLUE);
		g2.fill(lake);

		//house
		g2.drawImage(Toolkit.getDefaultToolkit().getImage("square.jpg"), houseB.x, houseB.y, 80,80,this);
		if (house) {
			g2.drawImage(Toolkit.getDefaultToolkit().getImage("house.png"), houseB.x - 23, houseB.y-20, 120, 120, this);
		}

		//fireplace
		g2.drawImage(Toolkit.getDefaultToolkit().getImage("gravel.png"), fireplaceB.x, fireplaceB.y, 50, 50, this);
		if (fire) {
			if (player.getFireplace().getLogs() < 1) {
				g2.drawImage(Toolkit.getDefaultToolkit().getImage("fireplace.png"), fireplaceB.x - 20, fireplaceB.y-30, 90, 90, this);
			}
			else if (player.getFireplace().getLogs() > 0) {
				g2.drawImage(Toolkit.getDefaultToolkit().getImage("fireplaceLIT.png"), fireplaceB.x - 22, fireplaceB.y-25, 90, 90, this);
			}
		}

		//character text
		if (true) {
			g2.setColor(Color.WHITE);
			g.drawString(messages.getLast(), rect.x+75, rect.y);
		}
		
		if(border.intersects(sign)) {
			g2.setColor(Color.BLACK);
			g.drawString("CLICK ME", sign.x, sign.y+50);
		}

		//character
		if (fill) {
			g2.drawImage(direction, rect.x-48, rect.y-50, 147, 155, this);
		}
	}
	//OTHER METHODS

	//moves the character
	void move() {
		if(left) {
			rect.x += -speed;
			border.x = rect.x-30;
			fill = true;
		}
		else if(right) {
			rect.x += speed;
			border.x = rect.x-30;
			fill = true;
		}

		if(up) {
			rect.y += -speed;
			border.y = rect.y-32;
			fill = true;
		}
		else if(down) {
			rect.y += speed;
			border.y = rect.y-32;
			fill = true;
		}
	}

	//keeps character in bounds
	void keepInBound() {
		if(rect.x < 0) {
			rect.x = 0;
		}
		else if(rect.x > screenWidth - rect.width) {
			rect.x = screenWidth - rect.width;
		}

		if(rect.y < 0) {
			rect.y = 0;
		}
		else if(rect.y > screenHeight - rect.height) {
			rect.y = screenHeight - rect.height;
		}
	}

	//checks for collision
	void checkCollision(Rectangle wall) {
		//check if rect touches wall
		if(rect.intersects(wall)) {

			//stop the rect from moving
			double left1 = rect.getX();
			double right1 = rect.getX() + rect.getWidth();
			double top1 = rect.getY();
			double bottom1 = rect.getY() + rect.getHeight();
			double left2 = wall.getX();
			double right2 = wall.getX() + wall.getWidth();
			double top2 = wall.getY();
			double bottom2 = wall.getY() + wall.getHeight();

			if(right1 > left2 && 
					left1 < left2 && 
					right1 - left2 < bottom1 - top2 && 
					right1 - left2 < bottom2 - top1)
			{
				//rect collides from left side of the wall
				rect.x = wall.x - rect.width;
			}
			else if(left1 < right2 &&
					right1 > right2 && 
					right2 - left1 < bottom1 - top2 && 
					right2 - left1 < bottom2 - top1)
			{
				//rect collides from right side of the wall
				rect.x = wall.x + wall.width;
			}
			else if(bottom1 > top2 && top1 < top2)
			{
				//rect collides from top side of the wall
				rect.y = wall.y - rect.height;
			}
			else if(top1 < bottom2 && bottom1 > bottom2)
			{
				//rect collides from bottom side of the wall
				rect.y = wall.y + wall.height;
			}
		}
	}

	//running component // actual game part
	@Override
	public void run() {
		initialize();
		//main game loop
		while(game && player.health > 0) { //player.health > 0
			update();
			timer++;
			repaint();
			try {
				Thread.sleep(1000/FPS);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		highScore.add(timer);
	}
	public void update() {
		move();
		keepInBound();
		//check for collisions
		for(int i = 0; i < trees1.length; i++) {
			checkCollision(trees1[i].getTreeRect());
			//update logs and apples
			if (trees1[i].getLogs() < 1) {
				trees1[i].replenishTree();
			}
			if (trees1[i].getApples() < 1) {
				trees1[i].replenishApple();
			}
		}
		if (player.getFireplace().getLogs() > 0) {
			player.getFireplace().subtractLogs();
			System.out.println(player.getHealth());
		}
		checkCollision (cave);
		checkCollision(entrance);
		if (house) {
			checkCollision (houseB);
		}
		if (fire) {
			checkCollision (fireplaceB);
		}

		//change in day
		if (timer%3000 == 0) {
			if (day == 0) {
				day+=1;
			}
			else {
				day=0;
			}
		}

		//cold bar update
		if (day == 1 && player.hasFireplace() == false && player.getFireplace().getLogs() < 1) {
			cold--;
			if (cold % 10 == 0) {
				player.setHealth(player.getHealth()-1);
				if (player.getHealth() < 50) {
					messages.addLast("I am cold and dying ;-;");
					speed = 2;
				}
			}
		}
		else if ((day == 1 && player.hasFireplace() && player.getFireplace().getLogs() > 1) || day == 0) {
			cold = 370;
			player.setHealth(100);
			speed = 5;
			messages.addLast("");
		}

		if (rect.intersects(lake) && right) {
			direction = bob[2];
		}
		else if (rect.intersects(lake) && left) {
			direction = bob[3];
		}
	}

	//keyboard stuff
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_A) {
			left = true;
			right = false;
			direction = bob [1];
		}else if(key == KeyEvent.VK_D) {
			right = true;
			left = false;
			direction = bob[0];
		}else if(key == KeyEvent.VK_W) {
			up = true;
			down = false;
		}else if(key == KeyEvent.VK_S) {
			down = true;
			up = false;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_A) {
			left = false;
		}else if(key == KeyEvent.VK_D) {
			right = false;
		}else if(key == KeyEvent.VK_W) {
			up = false;
		}else if(key == KeyEvent.VK_S) {
			down = false;
		}
	}

	public void mouseClicked (MouseEvent e){
		if (game == true) {
			//if player click on tree
			for (int i = 0; i < trees1.length; i++) {
				if (trees1[i].getTreeRect().contains(e.getX(), e.getY()-36) && border.intersects(trees1[i].getTreeRect())) {
					if (trees1[i].getLogs() > 0) {
						player.setLogs(player.getLogs()+1);
						messages.addLast("Log + 1");
						trees1[i].setLogs(trees1[i].getLogs()-1);
						if (trees1[i].getLogs() < 1) {
							messages.addLast("NO MORE LOGS");
						}
					}
					if (trees1[i].getApples() > 0 && trees1[i].giveApple()) {
						player.setApples(player.getApples()+1);
						messages.addLast("Apples + 1");
						trees1[i].setApples(trees1[i].getApples()-1);
						if (trees1[i].getApples() < 1) {
							messages.addLast("NO MORE APPLES");
						}
					}
					break;
				}
			}

			//if player click on sign
			if(border.intersects(sign) && sign.contains(e.getX(), e.getY()-36)) {
				JOptionPane.showMessageDialog(null, "Hello, that gray thing above me is the cave :)", "My name is Joe.", JOptionPane.ERROR_MESSAGE);
			}

			//if player click on cave
			if (cave.contains(e.getX(), e.getY()-36) && border.intersects(cave)) {
				int r;
				if (player.getWPick() < 1 && player.getSPick() < 1 && player.getIPick() < 1) {
					JOptionPane.showMessageDialog(null, "Cannot access cave yet.", "No Entry", JOptionPane.ERROR_MESSAGE);
				}
				//if player has wooden pick
				else if (player.getWPick() > 0 && player.getSPick() < 1 && player.getIPick() < 1) {
					fill = false;
					repaint();
					r = player.caveReturn("stone");
					if (r == 0) {
						player.setStone(player.getStone()+1);
						messages.addLast("Stone + 1");
					}
				}
				//if player only have stone pick
				else if (player.getSPick() > 0 && player.getIPick() < 1) {
					fill = false;
					repaint();
					r = player.caveReturn("iron");
					if (r == 0) {
						player.setStone(player.getStone()+1);
						messages.addLast("Stone + 1");
					}
					else if (r == 1) {
						player.setIron(player.getIron()+1);
						messages.addLast("Iron + 1");
					}
				}
				//if player have iron pick
				else if (player.getIPick() > 0) {
					fill = false;
					repaint();
					r = player.caveReturn("gold");

					if (r == 0) {
						player.setStone(player.getStone()+1);
						messages.addLast("Stone + 1");
					}
					else if (r == 1) {
						player.setIron(player.getIron()+1);
						messages.addLast("Iron + 1");
					}
					else if (r == 3) {
						player.setGold (player.getGold()+1);
						messages.addLast("Gold + 1");
					}
				}
			}

			//if player click on fireplace spot.
			if (fireplaceB.contains(e.getX(), e.getY()-36) && border.intersects(fireplaceB)) {
				if (player.hasFireplace()&&fire==true) {
					int i = JOptionPane.showConfirmDialog(null, "Do you want to add more logs to your fire?", "Feed the fire", JOptionPane.YES_NO_OPTION);
					if (i == 0) {
						boolean tf = true;
						int j = 0;
						while (tf) {
							try {
								j = Integer.parseInt (JOptionPane.showInputDialog("How many logs would you like to put in?"));
								if (j < 0) {
									throw new NumberFormatException();
								}
								tf = false;
							}
							catch (NumberFormatException a) {
								tf = true;
							}
						}
						if (player.getLogs() >= j) {
							player.getFireplace().setLogs(j);
						}
						else if (player.getLogs() < j) {
							JOptionPane.showMessageDialog(null, "Not enough logs", "scammer", JOptionPane.ERROR_MESSAGE);
						}

					}
				}
				else if (player.hasFireplace()) {
					int i = JOptionPane.showConfirmDialog(null, "Do you want to place your fireplace here?", "cozy", JOptionPane.YES_NO_OPTION);
					if (i == 0) {
						fire = true;
					}
				}
				else {
					JOptionPane.showMessageDialog(null,"You have no fireplace yet.", "Yikes", JOptionPane.ERROR_MESSAGE);
				}
			}

			//if player click on house spot
			if (houseB.contains(e.getX(),e.getY()-36) && border.intersects(houseB)) {
				if (player.getHouse() == false) {
					int i = JOptionPane.showConfirmDialog(null, "Would you like to build a house here? Items needed: 10 logs, 3 iron, 1 waterbowl, 1 apple.", "Home sweet home", JOptionPane.YES_NO_OPTION);
					if (i == 0) {
						if (player.getLogs() >= 10 && player.getIron() >= 3 && player.getWaterBowl() >= 1 && player.getApples() >= 1) {
							player.setLogs(player.getLogs()-10);
							player.setIron(player.getIron() -3);
							player.setApples(player.getApples()-1);
							player.setWaterBowl(player.getWaterBowl() - 1);
							player.setBowl(player.getBowl()+1);
							
							player.setHouse(true);
							house = true;
							messages.addLast("House + 1");
						}
						else {
							int need = 10 - player.getLogs();
							int need2 = 3 - player.getIron();
							int need3 = 1 - player.getWaterBowl();
							int need4 = 1 - player.getApples();
							if (need < 0) {
								need=0;
							}
							if(need2 < 0) {
								need2 = 0;
							}
							if(need3 < 0) {
								need3 =0;
							}
							if (need4 < 0) {
								need4 = 0;
							}
							
							JOptionPane.showMessageDialog(null, "Insufficient amount of materials. You need " + need + " more log(s), " +need2+" more iron, "+need3+" more waterbowl(s), and " + need4+" more apple(s).", "Homeless", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				else if (player.getHouse()){
					System.out.println("hi");
					int i = JOptionPane.showConfirmDialog(null, "Would you like to go to sleep?", "Goodnight", JOptionPane.YES_NO_OPTION);
					if (i == 0) {
						day = 0;
					}
				}
			}

			//if player click on lake
			if (lake.contains(e.getX(), e.getY()-36) && border.intersects(lake)) {
				if (player.getBowl() > 0) {
					player.setWaterBowl(player.getWaterBowl() +1);
					messages.addLast("Water Bowl + 1");
					player.setBowl(player.getBowl()-1);
				}
			}

			//if player click on entrance
			if (entrance.contains(e.getX(), e.getY()-36) && border.intersects(entrance)) {
				if (player.getKey()) {
					System.out.println("You have completed the game!");
					game = false;
				}
				else {
					JOptionPane.showMessageDialog(null, "You don't have access to this yet.", "Be gone", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	//action listener
	@Override
	public void actionPerformed(ActionEvent a) {
		String e = a.getActionCommand();
		entirePanel.requestFocus();
		
		//view profile
		if (e.equals("profile")) {
			new Character_Profile (player);
		}
		//view read me
		else if (e.equals("textfile")) {
			try {
				Desktop.getDesktop().open(new File ("readme.txt"));
			}
			catch (IOException b) {
				
			}
		}
		//crafting option
		else if (e.equals("craft")) {
			JComboBox comboBox = (JComboBox)a.getSource ();
			String item = (String)comboBox.getSelectedItem();

			//crafting bowls
			if (item.equals("Bowl")) {
				int i = JOptionPane.showConfirmDialog(null, "Do you want to craft a bowl? Items needed: 3 logs", "Need water ;-;", JOptionPane.YES_NO_OPTION);
				if (i == 0) {
					if (player.getLogs() >= 3) {
						messages.addLast("Bowl + 1");
						player.setLogs(player.getLogs()-3);
						player.setBowl(player.getBowl()+1);
					}
					else {
						int need = 3 - player.getLogs();
						JOptionPane.showMessageDialog(null, "Insufficient amount of materials. You need " + need + " more log(s)", "Stay Thirsty", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			//crafting wooden pickaxe
			else if (item.equals("Wooden Pickaxe")) {
				int i = JOptionPane.showConfirmDialog(null, "Do you want to craft a Wooden Pickaxe? Items needed: 5 logs", "Baby's first steps", JOptionPane.YES_NO_OPTION);
				if (i == 0) {
					if (player.getLogs() >= 5) {
						messages.addLast("Wooden PickAxe + 1");
						player.setLogs(player.getLogs()-5);
						player.setWPick(player.getWPick()+1);
					}
					else {
						int need = 5 - player.getLogs();
						JOptionPane.showMessageDialog(null, "Insufficient amount of materials. You need " + need+ " more log(s)", "noob moment", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			//crafting stone pickaxe
			else if (item.equals("Stone Pickaxe")) {
				int i = JOptionPane.showConfirmDialog(null, "Do you want to craft a Stone Pickaxe? Items needed: 2 logs, 3 stone", "Iron man", JOptionPane.YES_NO_OPTION);
				if (i == 0) {
					if (player.getLogs() >= 2 && player.getStone() >= 3) {
						messages.addLast("Stone Pickaxe + 1");
						player.setLogs(player.getLogs()-2);
						player.setStone(player.getStone()-3);
						player.setSPick(player.getSPick()+1);
					}
					else {
						int need1 = 2 - player.getLogs();
						int need2 = 3 - player.getStone();
						if (need1 < 0) {
							need1 = 0;
						}
						else if (need2 < 0) {
							need2 = 0;
						}
						JOptionPane.showMessageDialog(null, "Insufficient amount of materials. You need " + need1+ " more log(s) and " + need2 + " more stone", "It's not me...it's you", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			//crafting iron pickaxe
			else if (item.equals("Iron Pickaxe")) {
				int i = JOptionPane.showConfirmDialog(null, "Do you want to craft an Iron Pickaxe? Items needed: 2 logs, 3 iron", "Gold Digger", JOptionPane.YES_NO_OPTION);
				if (i == 0) {
					if (player.getLogs() >= 2 && player.getIron() >= 3) {
						messages.addLast("Iron PickAxe + 1");
						player.setLogs(player.getLogs()-2);
						player.setIron(player.getIron()-3);
						player.setIPick(player.getIPick()+1);
					}
					else {
						int need1 = 2 - player.getLogs();
						int need2 = 3 - player.getIron();
						if (need1 < 0) {
							need1 = 0;
						}
						else if (need2 < 0) {
							need2 = 0;
						}
						JOptionPane.showMessageDialog(null, "Insufficient amount of materials. You need " + need1+ " more log(s) and " + need2 + " more iron", ":P", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			//crafting fireplace
			else if (item.equals("Fireplace")) {
				int i = JOptionPane.showConfirmDialog(null, "Do you want to craft a fireplace? Items needed: 2 logs, 2 stone", "Feeling cold?", JOptionPane.YES_NO_OPTION);
				if (i == 0) {
					if (player.hasFireplace()) {
						JOptionPane.showMessageDialog(null, "You already have one!", "Greedy greedy goat >:(", JOptionPane.PLAIN_MESSAGE);
					}
					else if (player.getLogs() >= 2 && player.getStone() >= 2) {
						messages.addLast("Fireplace + 1");
						player.setLogs(player.getLogs()-2);
						player.setStone(player.getStone()-2);
						player.setFireplace(new Fireplace());
					}
					else {
						int need1 = 2 - player.getLogs();
						int need2 = 2 - player.getStone();
						if (need1 < 0) {
							need1 = 0;
						}
						else if (need2 < 0) {
							need2 = 0;
						}
						JOptionPane.showMessageDialog(null, "Insufficient amount of materials. You need " + need1+ " more log(s) and " + need2 + " more stone", "I hope you freeze to death", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			//crafting special key
			else if (item.equals("Special Key :O")) {
				int i = JOptionPane.showConfirmDialog(null, "Do you want to craft THE Special Key? Items needed: 5 gold, 3 iron, 1 log", "Trying to escape so quickly?", JOptionPane.YES_NO_OPTION);
				if (i == 0) {
					if (player.getKey()) {
						JOptionPane.showMessageDialog(null, "You already have one!", "Greedy greedy goat >:(", JOptionPane.PLAIN_MESSAGE);
					}
					else if (player.getLogs() >= 1 && player.getIron() >= 3 && player.getGold() >= 5) {
						messages.addLast("Special Key + 1");
						player.setLogs(player.getLogs()-1);
						player.setIron(player.getIron()-2);
						player.setGold(player.getGold()-1);
						player.setKey(true);
					}
					else {
						int need1 = 5 - player.getGold();
						int need2 = 3 - player.getIron();
						int need3 = 1- player.getLogs();
						if (need1 < 0) {
							need1 = 0;
						}
						else if (need2 < 0) {
							need2 = 0;
						}
						else if (need3 < 0) {
							need3 = 0;
						}
						JOptionPane.showMessageDialog(null, "Insufficient amount of materials. You need " + need1+ " more gold, " + need2 + " more iron, and " + need3+ " more log(s)", "LOL loser", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}

	}

	public void mouseReleased (MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub		  
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame ("CraftScape");
		movingComponent myPanel = new movingComponent ();
		
		frame.add(entirePanel);
		frame.setVisible(true);
		frame.pack();
		frame.setPreferredSize(new Dimension (1000,620));
		frame.setLocation(270,10);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
	}
}
