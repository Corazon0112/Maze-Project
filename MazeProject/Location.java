public class Location{
	//Declare variables
	int row;
	int col;

	//Constructor method
	public Location(int row, int col){
		this.row = row;
		this.col = col;
	}

	//Gets Row
	public int getR(){
		return row;
	}

	//Gets Column
	public int getC(){
		return col;
	}

	//Checks if the postions of objects are the same
	public boolean equals(Location other){
		if(row==other.getR()&&col==other.getC())
			return true;
		return false;
	}
	public boolean equals(int row, int col){
		return (this.row == row && this.col == col);
	}

	//Prints row and col into a string
	public String toString(){
		return "("+row+","+col+")";
	}

	//Sets Row and Col
	public void setR(int a){
		row+=a;
	}
	public void setC(int b){
		col+=b;
	}
}