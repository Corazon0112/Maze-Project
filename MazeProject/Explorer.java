import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
public class Explorer{
	private Location loc;
	private int dir;
	private int size;
	private int sizes;
	private int steps;
	private int turns;
	private Color color;
	private boolean debug = true;

	//Constructor method
	public Explorer(Location loc, int dir, int size, Color color, int sizes){
		this.loc = loc;
		this.dir = dir;
		this.size = size;
		this.color = color;
		this.sizes = sizes;
	}

	//returns color of explorer
	public Color getColor(){
		return color;
	}

	//returns location of explorer
	public Location getLoc(){
		return loc;
	}

	//returns direction of explorer
	public int getDir(){
		return dir;
	}

	//returns size of maze
	public int getSize(){
		return size;
	}

	//returns number of steps
	public int getSteps(){
		return steps;
	}

	//returns number of turns
	public int getTurns(){
		return turns;
	}

	//Checks if location of row and column is equal to row and column of explorer
	public boolean atLocation(int r, int c){
		return(r==loc.getR()&&c==loc.getC());
	}
	public void move(int key, char[][] maze){
		int r = getLoc().getR();
		int c = getLoc().getC();
		System.out.println(key);

		//forward
		if(key==38)
		{
			//0=up 1=right 2=down 3=left
			//Rotations
			if(dir==0 && r > 0 &&maze[r-1][c] != '#'){
				getLoc().setR(-1);
				steps++;
			}
			if(dir==1 && c<maze[r].length-1 && maze[r][c+1] != '#'){
				getLoc().setC(+1);
				steps++;
			}
			if(dir==2 && r<maze[r].length-1 && maze[r+1][c] != '#'){
				getLoc().setR(+1);
				steps++;
			}
			if(dir==3 && c > 0 &&maze[r][c-1] != '#'){
				getLoc().setC(-1);
				steps++;
			}
		}
		if(key == 37) // Rotate Left
		{
			dir--;
			if(dir==-1)
				dir = 3;
			turns++;
		}
		if(key==39) //Rotate Right
		{
			dir++;
			if(dir==4)
				dir=0;
			turns++;
		}
	}

	// Changes explorer into an arrow
	public Polygon getPoly(){
		int r=getLoc().getR();
		int c=getLoc().getC();
		Polygon arrowHead = new Polygon();
		if (dir == 0){
			arrowHead.addPoint( c*size+size,r*size+2*size);
			arrowHead.addPoint( (int)(c*size+size*1.5), (r*size+size));
			arrowHead.addPoint( c*size+size*2,r*size+size*2);
		}
		if (dir == 1 ){
			arrowHead.addPoint( c*size+size,r*size+size);
			arrowHead.addPoint( c*size+size*2, (int)(r*size+size*1.5));
			arrowHead.addPoint( c*size+size,r*size+size*2);
		}
		if (dir == 2 ){
			arrowHead.addPoint( c*size+size,r*size+size);
			arrowHead.addPoint( (int)(c*size+size*1.5), (r*size+size*2));
			arrowHead.addPoint( c*size+2*size,r*size+size);
		}
		if (dir == 3 ){
			arrowHead.addPoint( c*size+2*size,r*size+size);
			arrowHead.addPoint( c*size+size, (int)(r*size+size*1.5));
			arrowHead.addPoint( c*size+2*size,r*size+2*size);
		}
		return arrowHead;
	}
	public Polygon getPolyMini(){
		int r=getLoc().getR();
		int c=getLoc().getC();
		Polygon arrowHead = new Polygon();
		if (dir == 0){
			arrowHead.addPoint( 315+c*sizes+sizes,775+r*sizes+2*sizes);
			arrowHead.addPoint( (int)(315+c*sizes+sizes*1.5), (775+r*sizes+sizes));
			arrowHead.addPoint( 315+c*sizes+sizes*2,775+r*sizes+sizes*2);
		}
		if (dir == 1 ){
			arrowHead.addPoint( 315+c*sizes+sizes,775+r*sizes+sizes);
			arrowHead.addPoint( 315+c*sizes+sizes*2, (int)(775+r*sizes+sizes*1.5));
			arrowHead.addPoint( 315+c*sizes+sizes,775+r*sizes+sizes*2);
		}
		if (dir == 2 ){
			arrowHead.addPoint( 315+c*sizes+sizes,775+r*sizes+sizes);
			arrowHead.addPoint( (int)(315+c*sizes+sizes*1.5), (775+r*sizes+sizes*2));
			arrowHead.addPoint( 315+c*sizes+2*sizes,775+r*sizes+sizes);
		}
		if (dir == 3 ){
			arrowHead.addPoint( 315+c*sizes+2*sizes,775+r*sizes+sizes);
			arrowHead.addPoint( 315+c*sizes+sizes, (int)(775+r*sizes+sizes*1.5));
			arrowHead.addPoint( 315+c*sizes+2*sizes,775+r*sizes+2*sizes);
		}
		return arrowHead;
	}
}