public class Fireplace {
	private int logs;
	private int timer;
	
	//Constructor: Fireplace
	//return: n/a
	//description: initialize objects
	//parameters: n/a
	public Fireplace () {
		logs = 0;
	}
	
	//method: subtractLogs
	//parameters: n/a
	//Description: subtracts logs in fireplace after certain time
	//return type: void
	public void subtractLogs () {
		timer++;
		if (timer % 250 == 0) {
			this.logs-=1;
			timer=0;
		}
	}
	
	//getters
	public int getLogs () {
		return logs;
	}
	
	//setters
	public void setLogs(int l) {
		this.logs += l ;
	}
	
}
