import java.util.*;
public class Character {
	
	//stuff
	private int logs, bowl, waterBowl, apples, stone, iron, gold, wPick, sPick, iPick;
	private boolean key = false, house, hasFireplace;
	Fireplace fireplace = new Fireplace ();
	//stats
	int health;
	
	//Constructor: Character
	//return: n/a
	//description: initialize objects
	//parameters: n/a
	public Character () {
		health = 100;
	}
	
	//method: toString
	//parameters: none
	//description: for display purposes
	//return: string
	public String toString () {
		return "HP: " + health + "\nCold: ";
	}
	
	//method: caveReturn
	//parameters: String s
	//Description: random generation of stone, iron, gold or nothing when player click on cave
	//return type: int
	public int caveReturn (String s) {
		Random r = new Random ();
		int [] cave = {0,0,0,0,0,0,0,0,0,0,2,2,2,2,2};
		int [] cave1 = {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2};
		int [] cave2 = {0,0,0,0,0,1,1,1,3, 2, 2, 2, 2, 2, 2};
		int max = 14;
		int randNum = r.nextInt(max);
		
		if (s.equals("stone")) {
			return cave[randNum];
		}
		else if (s.equals("iron")) {
			return cave1[randNum];
		}       
		else {
			return cave2[randNum];
		}
	}
	
	//GETTERS
	public boolean hasFireplace () {
		return hasFireplace;
	}
	
	public Fireplace getFireplace() {
		return this.fireplace;
	}
	
	public int getHealth () {
		return this.health;
	}

	public int getLogs () {
		return this.logs;
	}
	
	public int getApples () {
		return this.apples;
	}
	
	public int getWPick() {
		return this.wPick;
	}
	
	public int getSPick () {
		return this.sPick;
	}
	
	public int getIPick () {
		return this.iPick;
	}
	
	public int getStone () {
		return this.stone;
	}
	
	public int getIron() {
		return this.iron;
	}
	
	public int getBowl () {
		return this.bowl;
	}
	
	public int getWaterBowl() {
		return this.waterBowl;
	}
	
	public int getGold () {
		return this.gold;
	}
	
	public boolean getHouse() {
		return this.house;
	}
	
	public boolean getKey() {
		return this.key;
	}
	
	//SETTERS
	public void setFireplace (Fireplace f) {
		this.fireplace = f;
		hasFireplace = true;
	}
	
	public void setKey(boolean k) {
		this.key = k;
	}
	
	public void setHouse (boolean h) {
		this.house = h;
	}
	
	public void setBowl (int b) {
		this.bowl = b;
	}
	
	public void setWaterBowl (int b) {
		this.waterBowl = b;
	}
	
	public void setGold (int g) {
		this.gold = g;
	}
	
	public void setIron (int i) {
		this.iron = i;
	}
	
	public void setStone(int s) {
		this.stone = s;
	}
	
	public void setWPick(int s) {
		this.wPick = s;
	}
	
	public void setIPick (int s) {
		this.iPick = s;
	}
	
	public void setSPick(int s) {
		this.sPick = s; 
	}
	
	public void setApples (int a) {
		this.apples = a;
	}
	
	public void setLogs(int l) {
		this.logs = l;
	}
	
	public void setHealth(int h) {
		this.health = h;
	}
}
