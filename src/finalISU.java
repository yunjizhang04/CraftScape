//Name: Yunji Zhang
//Date: 1/25/2022
//Project: Game ISU
//Description: Walmart version of Minecraft where you craft items and try to escape the area you are
//put inside.

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

@SuppressWarnings("serial")
public class finalISU extends JPanel implements Runnable, KeyListener, MouseListener, ActionListener {
	//GLOBAL VARIABLES
	static JFrame frame = new JFrame ("CraftScape");
	int changeFrame = 1;
	boolean gameFinished = false;
	List<Highscore> score = new ArrayList<>();
	String name = "";
	
	///-------------MAIN PANEL STUFF-------------///
	static JPanel panelMAIN;
	JPanel hsPanelMAIN, panel2MAIN, buttonPanelM;
	JLabel titleM, highscore, character;
	JButton start, exit;
	
	///-------------GAME PANEL STUFF------------///
	//JFrame Stuff
	static JPanel entirePanel, dropboxPanel;
	JComboBox <String> craftItems;
	JButton profile, openReadMe, menu;
	//player stuff
	boolean game = false;
	Character player = new Character ();
	int cold = 0;
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
	int timer = 0;
	int day = 0;
	Image [] background = {Toolkit.getDefaultToolkit().getImage("./gameISU/grass.jpg"),Toolkit.getDefaultToolkit().getImage("grass2.jpg")};
	//moving and screen stuff
	boolean up, down, left, right;
	double speed = 5;
	int screenWidth = 1000;
	int screenHeight = 620;
	Thread thread;
	int FPS = 60;
	//bob image stuff
	Image [] bob = {Toolkit.getDefaultToolkit().getImage("./gameISU/bob.jpg"), Toolkit.getDefaultToolkit().getImage("bobleft.png"), Toolkit.getDefaultToolkit().getImage("bobswim.png"), Toolkit.getDefaultToolkit().getImage("bobswim2.png")};
	Image direction = bob[0];

	//-----------END PANEL "YOU'VE ESCAPED" --------------//
	static JPanel panel;
	JPanel panel2, buttonPanel;
	JLabel title;
	JButton restart, exitEND;
	
	///----------DEAD PANEL ------------------------------//
	static JPanel panelDEAD;
	
	///----------credit panel ------------------------------//
	static JPanel credit;
	
	//Constructor: finalISU
	//return: n/a
	//description: initialize objects
	//parameters: n/a
	public finalISU() {
		//----------- MAIN MENU PANEL -------------//
		panelMAIN = new JPanel();
		panelMAIN.setLayout(new BorderLayout());
		panelMAIN.setBackground(Color.decode("#FF0FFF"));
		
		//higscore panelMAIN
		hsPanelMAIN = new JPanel();
		hsPanelMAIN.setLayout(new GridLayout(1,3));
		hsPanelMAIN.setBackground(Color.decode("#FF0FFF"));

		score.add(new Highscore(name, 0));
		Collections.sort(score);
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
		
		JButton howTo = new JButton ("INSTRUCTIONS AND CREDIT");
		howTo.setActionCommand("credit");
		howTo.addActionListener(this);
		
		//adding onto high score panel
		hsPanelMAIN.add(new JLabel ());
		hsPanelMAIN.add(new JLabel ());
		hsPanelMAIN.add(highscore);
		
		//adding onto button panel
		buttonPanelM.add(start);
		buttonPanelM.add(exit);
		buttonPanelM.add(howTo);
		
		//adding onto panel2
		panel2MAIN.add(titleM);
		panel2MAIN.add(character);
		
		//adding onto main panel
		panelMAIN.add(hsPanelMAIN, BorderLayout.NORTH);
		panelMAIN.add(panel2MAIN, BorderLayout.CENTER);
		panelMAIN.add(buttonPanelM, BorderLayout.SOUTH);

		setBackground(Color.YELLOW);
		
		//----------GAME PANEL--------------------//
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
		
		menu = new JButton("Menu");
		menu.setActionCommand("menu");
		menu.addActionListener(this);
		
		dropboxPanel.add(craftItems);
		dropboxPanel.add(profile);
		dropboxPanel.add(openReadMe);
		dropboxPanel.add(menu);
		entirePanel.add(this, BorderLayout.CENTER);

		//setVisible(true);
		entirePanel.addMouseListener(this);
		entirePanel.addKeyListener (this);
		entirePanel.setFocusable(true);
		thread = new Thread(this);
		thread.start();
		
		///----------------END PANEL--------------------///
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
		restart = new JButton ("MENU");
		restart.setActionCommand("menu");
		restart.addActionListener(this);
		
		exitEND = new JButton ("EXIT");
		exitEND.setActionCommand("exit");
		exitEND.addActionListener(this);
		
		//adding onto button panel
		buttonPanel.add(restart);
		buttonPanel.add(exitEND);
		
		//adding onto panel2
		panel2.add(title);
		
		//adding onto main panel
		panel.add(panel2);
		panel.add(buttonPanel);
		
		///-------------------DEAD------------------------//
		panelDEAD = new JPanel();
		panelDEAD.setLayout(new GridLayout(4,1));
		panelDEAD.setBackground(Color.YELLOW);
		
		//center panel (where title and character is at
		panel2 = new JPanel();
		panel2.setBackground(Color.YELLOW);
		
		title = new JLabel ("You died :(");
		title.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		title.setFont(new Font("Courier New", Font.BOLD, 50));
		
		//panelDEAD for the buttons
		buttonPanel = new JPanel ();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 10));
		buttonPanel.setBackground(Color.YELLOW);
		
		//buttons
		restart = new JButton ("MENU");
		restart.setActionCommand("menu");
		restart.addActionListener(this);
		
		exitEND = new JButton ("EXIT");
		exitEND.setActionCommand("exit");
		exitEND.addActionListener(this);
		
		//adding onto button panel
		buttonPanel.add(restart);
		buttonPanel.add(exitEND);
		
		//adding onto panel2
		panel2.add(title);
		
		//adding onto main panel
		panelDEAD.add(panel2);
		panelDEAD.add(buttonPanel);
		
		//// -----------------credit------------------------//
		credit = new JPanel();
		credit.setLayout(new GridLayout(4,1));
		credit.setBackground(Color.YELLOW);
		
		//center panel (where title and character is at
		panel2 = new JPanel();
		panel2.setBackground(Color.YELLOW);
		
		title = new JLabel ("This game was made by: Yunji Zhang");
		title.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		title.setFont(new Font("Courier New", Font.BOLD, 26));
		
		//panelDEAD for the buttons
		buttonPanel = new JPanel ();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 10));
		buttonPanel.setBackground(Color.YELLOW);
		
		//buttons
		restart = new JButton ("MENU");
		restart.setActionCommand("menu");
		restart.addActionListener(this);
		
		exitEND = new JButton ("EXIT");
		exitEND.setActionCommand("exit");
		exitEND.addActionListener(this);
		
		//adding onto button panel
		buttonPanel.add(restart);
		buttonPanel.add(exitEND);
		buttonPanel.add(openReadMe);
		
		//adding onto panel2
		panel2.add(title);
		
		//adding onto main panel
		credit.add(panel2);
		credit.add(buttonPanel);
	}

	//method: initialize
	//parameters: NA
	//Description: initializes the variables before game starts
	//return type: void
	public void initialize() {
		//setups before the game starts running
		timer = 0;
		cold = 0;
		day = 0;
		player = new Character ();
		check = false;
		fill = true;
		messages.add("Hello");
		
		//fireplace house stuff
		fire = false;
		house = false;
		trees1 = new Tree [5];
		direction = bob[0];
		
		rect = new Rectangle(447, 230, 53, 70);
		border = new Rectangle (447-40, 230-35, 53+60, 70+65);

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

	//method: paintComponent
	//parameters: Graphics g
	//Description: paints graphics
	//return type: void
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
		if (game=true) {
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

	//method: move
	//parameters: none
	//Description: method that makes character move
	//return type: void
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

	//method: keepInBound
	//parameters: none
	//Description: keeps character in bounds
	//return type: void
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

	//method: checkCollision
	//parameters: Rectangle wall
	//Description: checks for collisions between rectangles
	//return type: void
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
	
	//method: run
	//parameters: none
	//Description: running component/actual game part
	//return type: void
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
		score.add(new Highscore(name, timer));
		Collections.sort(score);
		hsPanelMAIN.remove(highscore);
		highscore = new JLabel ("High Score: " + score.get(0));
		hsPanelMAIN.add(highscore);
		repaint();
		
		if (player.health < 1) {
			frame.dispose();
			frame = new JFrame();
			panelDEAD.addKeyListener(this);
			panelDEAD.setFocusable(true); 
			panelDEAD.requestFocus ();
			
			frame.add(panelDEAD);
			frame.pack ();
			frame.setVisible((true));
			frame.setPreferredSize(new Dimension (1000,600));
			frame.setLocation(270,10);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(false);
		}
	}
	
	//method: update
	//parameters: none
	//Description: keeps things updated as the game runs
	//return type: void
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
		if (timer%3500 == 0) {
			if (day == 0) {
				day+=1;
			}
			else {
				day=0;
			}
		}

		//cold bar update
		if (day == 1 && player.hasFireplace() == false || day == 1 && player.getFireplace().getLogs() < 1) {
			cold++;
			if (cold % 10 == 0) {
				player.setHealth(player.getHealth()-1);
				if (player.getHealth() < 50) {
					messages.addLast("I am cold and dying ;-;");
					speed = 2;
				}
			}
		}
		else if ((day == 1 && player.hasFireplace() && player.getFireplace().getLogs() > 1) || day == 0) {
			cold = 0;
			player.setHealth(100);
			speed = 5;
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

	//method: keyPressed
	//parameters: KeyEvent e
	//Description: checks for key pressing
	//return type: void
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

	//method: keyReleased
	//parameters: KeyEvent e
	//Description: checks for when key is released
	//return type: void
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

	//method: mouseClicked
	//parameters: MouseEvent e
	//Description: checks for mouse clicks
	//return type: void
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
							messages.addLast("Roasty Toasty");
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
					game = false;
					frame.dispose();
					frame = new JFrame();
					panel.addKeyListener(this);
					panel.setFocusable(true); 
					panel.requestFocus ();
					
					frame.add(panel);
					frame.pack ();
					frame.setVisible((true));
					frame.setPreferredSize(new Dimension (1000,600));
					frame.setLocation(270,10);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setResizable(false);
				}
				else {
					JOptionPane.showMessageDialog(null, "You don't have access to this yet.", "Be gone", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	//method: actionPerformed
	//parameters: ActionEvent e
	//Description: the action listener
	//return type: void
	@Override
	public void actionPerformed(ActionEvent a) {
		String e = a.getActionCommand();
		entirePanel.requestFocus();
		
		if (e.equals("menu")) {
			frame.dispose();
			frame = new JFrame();
			panelMAIN.addKeyListener(this);
			panelMAIN.setFocusable(true); 
			panelMAIN.requestFocus ();
			game = false;
			
			frame.add(panelMAIN);
			frame.pack ();
			frame.setVisible((true));
			frame.setPreferredSize(new Dimension (1000,620));
			frame.setLocation(270,10);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(false);
		}
		if (e.equals("credit")) {
			frame.dispose();
			frame = new JFrame();
			credit.addKeyListener(this);
			credit.setFocusable(true); 
			credit.requestFocus ();
			game = false;
			
			frame.add(credit);
			frame.pack ();
			frame.setVisible((true));
			frame.setPreferredSize(new Dimension (1000,620));
			frame.setLocation(270,100);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(false);
		}
		if (e.equals("start")) {
			name = JOptionPane.showInputDialog("Enter your name:");
			frame.dispose();
			frame = new JFrame();
			entirePanel.addKeyListener(this);
			entirePanel.setFocusable(true); 
			entirePanel.requestFocus ();
			
			frame.add(entirePanel);
			frame.pack ();
			frame.setVisible((true));
			frame.setPreferredSize(new Dimension (1000,620));
			frame.setLocation(270,10);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(false);
			game= true;
			thread = new Thread(this);
			thread.start();
		}
		if(e.equals("exit")) {
			System.exit(0);
		}

		//view profile
		if (e.equals("profile")) {
			new Character_Profile (player);
		}
		//view read me
		else if (e.equals("textfile")) {
			try {
				Desktop.getDesktop().open(new File ("instructions.txt"));
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

	//method: main
	//parameters: String [] args
	//Description: main method
	//return type: void
	public static void main(String[] args) {
		new finalISU();
		frame.add(panelMAIN);
		frame.setVisible(true);
		frame.pack();
		frame.setPreferredSize(new Dimension (1000,620));
		frame.setLocation(270,10);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
	}
}
