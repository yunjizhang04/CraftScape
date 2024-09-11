import java.awt.*;
import java.util.Random;

public class Tree {
	private Rectangle treeRect;
	private int logs;
	private int apples;
	private int logTimer;
	private int appleTimer;

	//Constructor: Tree
	//return: n/a
	//description: initialize objects
	//parameters: n/a
	public Tree (int x, int y, int w, int h) {
		treeRect = new Rectangle (x, y, w, h);
		logs = 5;
		apples = 3;
	}

	//method: replenishTree
	//parameters: n/a
	//Description: replenishes the logs in the tree
	//return type: void
	public void replenishTree () {
		logTimer++;
		if (logTimer == 500) {
			logs = 5;
			logTimer = 0;
		}
	}

	//method: giveApple
	//parameters: n/a
	//Description: gives player apple at random whenever player click
	//				on tree
	//return type: boolean
	public boolean giveApple () {
		Random r = new Random();
		int max = 5;
		int randNum = r.nextInt(max);

		if (randNum == 2) {
			return true;
		}
		else return false;
	}

	//method: replenishApple
	//parameters: n/a
	//Description: replenishes apples in the tree
	//return type: void
	public void replenishApple () {
		appleTimer++;
		if (appleTimer == 500) {
			apples = 3;
			appleTimer = 0;
		}
	}

	//GETTER
	public Rectangle getTreeRect () {
		return treeRect;
	}

	public int getApples () {
		return this.apples;
	}

	public int getLogs () {
		return this.logs;
	}

	//SETTER
	public void setApples (int a) {
		this.apples = a;
	}

	public void setLogs (int l) {
		this.logs = l;
	}

}
