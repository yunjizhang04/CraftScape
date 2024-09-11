public class Highscore implements Comparable <Highscore>{
	private String name;
	private int score;
	
	//Constructor: Highscore
	//return: n/a
	//description: initialize objects
	//parameters: n/a
	public Highscore (String n, int s) {
		this.name = n;
		this.score = s;
	}
	
	//method: toString
	//parameters: none
	//description: for display purposes
	//return: string
	public String toString () {
		return name + ", " + score;
	}
	
	//name: compareTo
    //parameter: Highscore object s
    //description: compares two word objects
    //return integer
    public int compareTo(Highscore s){
        return s.score - this.score;
    }
}
