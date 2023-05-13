import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.util.ArrayList;
public class MazeProjectTemplate extends JPanel implements KeyListener
{
	private JFrame frame;
	private Explorer explorer;
	private int col=0, row=0,dir = 0; //Location and direction of explorer
	//private int size = 15;  		  //Size of grid square in pixels
	private int size = 30;
	private int sizes = 15;
	private boolean debug = true;     //Set to true to print debug info
	private boolean won = false;
	private Location endLoc;         //Finish line of maze
	private boolean is3D = false;
	//private int numRows=31,numCols=64; //Set based on uploaded data
	private int numRows=8,numCols=21;
	private char[][] maze=new char[numRows][numCols];
	private int counter = 0;
	ArrayList<Wall> walls;
	int factor = 50;
	int sprayLimit = 10;
	boolean flashlight = false;
	boolean atStart = false;
    boolean useSpray = false;
    int view = 5;
    int startFOV;
    int endFOV;
    int direction = 0;

	public MazeProjectTemplate()
	{
		setBoard();  //write this method to read board from file
		frame=new JFrame("A-Mazing Program");
		frame.setSize(1000,1000);
		frame.add(this);
		frame.addKeyListener(this);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}
	public void paintComponent(Graphics g)
	{
		//Make Background Blank
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		g2.setColor(Color.BLACK);
		g2.fillRect(0,0,frame.getWidth(),frame.getHeight());

		if(is3D){
			g2.setColor(Color.YELLOW);
			g2.setFont(new Font("Comic Sans", Font.BOLD, 20));
			g2.drawString("3D Maze!",100,930);

			createWalls();
			for(Wall w: walls){
				//g2.setColor(Color.WHITE);
				g2.setPaint(w.getPaint());
				g2.fillPolygon(w.getPolygon());
				g2.setColor(Color.BLACK);
				g2.draw(w.getPolygon());
           	}
			int fontSize = 15;
            Font font = new Font("Arial", Font.BOLD, fontSize);
            g2.setFont(font);
            String on_off = "";
            if(flashlight)
                on_off = "On";
            else
                on_off = "Off";
            g2.setColor(Color.YELLOW);
            g2.drawString("Flashlight: "+on_off, 470, 720);
            g2.drawString("Press 'F' for Flashlight", 440, 740);
            g2.drawString("Total Steps: "+explorer.getSteps(), 100, 725);
            g2.drawString("Total Turns: "+explorer.getTurns(), 275, 725);
            g2.drawString("Sprays: "+sprayLimit, 650, 720);
            g2.drawString("Press 'S' to Spray", 630, 740);
            g2.setColor(Color.GRAY);

            if(atStart){
				g2.setFont(new Font("BANGERS", Font.CENTER_BASELINE, 50));
				g2.setColor(Color.RED);
            	g2.drawString("START", 415, 375);
            	g2.setColor(Color.GRAY);
            }
            if(won){
				g2.setFont(new Font("BANGERS", Font.CENTER_BASELINE, 50));
				g2.setColor(Color.RED);
				g2.drawString("Congratulations! You Won!",165, 375);
				g2.setColor(Color.GRAY);
			}
			for(int c=0;c<maze[0].length;c++)
				for(int r=0;r<maze.length;r++){
					if (explorer != null && explorer.atLocation(r,c)){
						//EXPLORER
						g2.fillRect(315+c*sizes+sizes,775+r*sizes+sizes,sizes,sizes);
						g2.setColor(explorer.getColor());
						g2.fill(explorer.getPolyMini());
						g2.setColor(Color.GRAY);
					}
					else if (maze[r][c] == 'F'){
						//SET FINISH OR GOAL SQUARE
						g2.setColor(Color.GREEN);
						g2.fillRect(315+c*sizes+sizes,775+r*sizes+sizes,sizes,sizes);
						g2.setColor(Color.GRAY);
					}
					else if(maze[r][c]==' '||maze[r][c]=='E') // OPEN SQUARE GRAY
						g2.fillRect(315+c*sizes+sizes,775+r*sizes+sizes,sizes,sizes);
					else if(maze[r][c]=='p'){
						g2.setColor(Color.RED);
						g2.fillRect(315+c*sizes+sizes,775+r*sizes+sizes,sizes,sizes);
						g2.setColor(Color.GRAY);
					}
					else // BLOCKED SQUARE GRAY OUTLINE
						g2.drawRect(315+c*sizes+sizes,775+r*sizes+sizes,sizes,sizes);
				}
		}
		else{
			// DRAW MAZE
			g2.setColor(Color.GRAY);
			for(int c=0;c<maze[0].length;c++)
				for(int r=0;r<maze.length;r++){
					if (explorer != null && explorer.atLocation(r,c)){
						//EXPLORER
						g2.fillRect(c*size+size,r*size+size,size,size);
						g2.setColor(explorer.getColor());
						g2.fill(explorer.getPoly());
						g2.setColor(Color.GRAY);
					}
					else if (maze[r][c] == 'F'){
						//SET FINISH OR GOAL SQUARE
						g2.setColor(Color.GREEN);
						g2.fillRect(c*size+size,r*size+size,size,size);
						g2.setColor(Color.GRAY);
					}
					else if(maze[r][c]==' '||maze[r][c]=='E') // OPEN SQUARE GRAY
						g2.fillRect(c*size+size,r*size+size,size,size);
					else if(maze[r][c]=='p'){
						g2.setColor(Color.RED);
						g2.fillRect(c*size+size,r*size+size,size,size);
						g2.setColor(Color.GRAY);
					}
					else // BLOCKED SQUARE GRAY OUTLINE
						g2.drawRect(c*size+size,r*size+size,size,size);
				}
			if (debug){ // PRINT EXTRA INFO TO HELP DEBUG
				g2.setColor(Color.YELLOW);
				g2.drawString("Dir: "+explorer.getDir()+"     Steps: "+explorer.getSteps()+"     Turns: "+explorer.getTurns(),40, numRows*size+2*size);
				g2.setColor(Color.GRAY);
			}
			if(won){
				g2.setColor(Color.RED);
				g2.drawString("Congratulations! You Won!",40, numRows*size+3*size);
				g2.setColor(Color.GRAY);
			}
		}
		/*int c = 0;
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){
				if(maze[i][j]=='F'){
					c++;
				}
			}
		}*/

	}
	public void createWalls(){

        walls = new ArrayList<>();

        for(int fov=0; fov<view; fov++){

            walls.add(getLeftPathway(fov));
            walls.add(getRightPathway(fov));
            walls.add(getFloor(fov));
            walls.add(getCeiling(fov));
        }
        int rr = explorer.getLoc().getR();
        int cc = explorer.getLoc().getC();
        direction = explorer.getDir();

        switch (direction) {
            case 0: //up
                atStart = false;
                won = false;

                for(int fov=0; fov<view; fov++){
                    try{
						//Start of maze
                        if(maze[rr][cc] == 'E') {
                            atStart = true;
                            startFOV = fov;
                        }
                        //Finish Maze
						if(maze[rr][cc] == 'F'){
							won = true;
							endFOV = fov;
                        }
                        //spray floor
                        if(maze[rr-fov][cc] == 'p'){
                            walls.add(sprayFloor(fov));
                        }
                        //Left walls
                        if(maze[rr-fov][cc-1] == '#'){
                            walls.add(getLeft(fov));
                        }
                        //left triangle walls
                        else{
                            walls.add(getTopLeftTriangle(fov));
                            walls.add(getBotLeftTriangle(fov));
                        }
                        //right walls
                        if(maze[rr-fov][cc+1] == '#'){
                            walls.add(getRight(fov));
                        }
                        //right triangle walls
                        else{
                            walls.add(getTopRightTriangle(fov));
                            walls.add(getBotRightTriangle(fov));
                        }
                        //front wall
                        if(maze[rr-fov][cc] == '#'){
                            walls.add(getInFront(fov));
                            break;
                        }

                    }catch(ArrayIndexOutOfBoundsException e){
                    }
                }
                break;

            case 1:  //right
                atStart = false;
                won = false;

                for(int fov=0; fov<view; fov++){
                    try{
						//Start of maze
						if(maze[rr][cc] == 'E') {
							atStart = true;
							startFOV = fov;
                        }
                        //Finish Maze
						if(maze[rr][cc] == 'F'){
							won = true;
							endFOV = fov;
                        }
                        //spray floor
                        if(maze[rr][cc+fov] == 'p'){
                            walls.add(sprayFloor(fov));
                        }
                        //left wall
                        if(maze[rr-1][cc+fov] == '#'){
                            walls.add(getLeft(fov));
                        }
                        //left triangle walls
                        else{
                            walls.add(getTopLeftTriangle(fov));
                            walls.add(getBotLeftTriangle(fov));
                        }
                        //right wall
                        if(maze[rr+1][cc+fov] == '#'){
                            walls.add(getRight(fov));
                        }
                        //right triangle walls
                        else{
                            walls.add(getTopRightTriangle(fov));
                            walls.add(getBotRightTriangle(fov));
                        }
                        //front walls
                        if(maze[rr][cc+fov] == '#'){
                            walls.add(getInFront(fov));
                            break;
                        }

                    }catch(ArrayIndexOutOfBoundsException e){
                    }
                }

                break;

            case 2:  //down
                atStart = false;
                won = false;
                for(int fov=0; fov<view; fov++){
                    try{
						//Start of maze
						if(maze[rr][cc] == 'E') {
							atStart = true;
							startFOV = fov;
                        }
                        //Finish Maze
                        if(maze[rr][cc] == 'F'){
                            won = true;
                            endFOV = fov;
                        }

                        //spray floor
                        if(maze[rr+fov][cc] == 'p'){
                            walls.add(sprayFloor(fov));
                        }
                        //Left wall
                        if(maze[rr+fov][cc+1] == '#'){
                            walls.add(getLeft(fov));
                        }
                        //left triangle walls
                        else{
                            walls.add(getTopLeftTriangle(fov));
                            walls.add(getBotLeftTriangle(fov));
                        }
                        //right wall
                        if(maze[rr+fov][cc-1] == '#'){
                            walls.add(getRight(fov));
                        }
                        //right triangle walls
                        else{
                            walls.add(getTopRightTriangle(fov));
                            walls.add(getBotRightTriangle(fov));
                        }
                        //front walls
                        if(maze[rr+fov][cc] == '#'){
                            walls.add(getInFront(fov));
                            break;
                        }

                    }catch(ArrayIndexOutOfBoundsException e){
                    }
                }

                break;

            case 3:  //left
                atStart = false;
                won = false;
                for(int fov=0; fov<view; fov++){
                    try{
                        //Start of maze
                        if(maze[rr][cc] == 'E') {
                            atStart = true;
                            startFOV = fov;
                        }
						//Finish Maze
                        if(maze[rr][cc] == 'F'){
                            won = true;
                            endFOV = fov;
                        }
                        //spray floor
                        if(maze[rr][cc-fov] == 'p'){
                            walls.add(sprayFloor(fov));
                        }
                        //left walls
                        if(maze[rr+1][cc-fov] == '#'){
                            walls.add(getLeft(fov));
                        }
                        //left triangle walls
                        else {
                            walls.add(getTopLeftTriangle(fov));
                            walls.add(getBotLeftTriangle(fov));
                        }
                        //right wall
                        if(maze[rr-1][cc-fov] == '#'){
                            walls.add(getRight(fov));
                        }
                        //right triangle walls
                        else{
                            walls.add(getTopRightTriangle(fov));
                            walls.add(getBotRightTriangle(fov));
                        }
                        //front wall
                        if(maze[rr][cc-fov] == '#'){
                            walls.add(getInFront(fov));
                            break;
                        }

                    }catch(ArrayIndexOutOfBoundsException e){
                    }
                }
                break;
        }
    }

    //Left trapezoids
    // n=fov
    public Wall getLeft(int n){
        int num = factor*n;

        int[] rLocs = new int[]{num, 50+num, 650-num, 700-num};
        int[] cLocs = new int[]{100+num, 150+num, 150+num, 100+num};

        if(flashlight) {
            num = 30 * n;
        }
        return new Wall(rLocs, cLocs, 255-num, 255-num, 255-num, "Left", factor);
    }

    //rectangle
    // n = fov
    //Left side
    public Wall getLeftPathway(int n){
        int num = (factor*n);

        int[] rLocs = new int[]{50+num, 50+num, 650-num, 650-num};
        int[] cLocs = new int[]{100+num, 150+num, 150+num, 100+num};
        if(flashlight) {
            num = 30 * n;
        }
        return new Wall(rLocs, cLocs, 255-num, 255-num, 255-num, "LeftPath", factor);
    }

    //triangle
    public Wall getTopLeftTriangle(int n){
        int num = factor*n;

        int[] rLocs = new int[]{num, 50+num, 50+num};
        int[] cLocs = new int[]{100+num, 100+num, 150+num};
        if(flashlight) {
            num = 30 * n;
        }
        return new Wall(rLocs, cLocs, 255-num, 255-num, 255-num, "TopLeft", factor);
    }
    public Wall getBotLeftTriangle(int n){
        int num = factor*n;

        int[] rLocs = new int[]{700-num, 650-num, 650-num};
        int[] cLocs = new int[]{100+num, 100+num, 150+num};
        if(flashlight) {
            num = 30 * n;
        }
        return new Wall(rLocs, cLocs, 255-num, 255-num, 255-num, "BottomLeft", factor);
    }

    //Right Trapezoids       n = fov
    public Wall getRight(int n){
        int num = factor*n;

        int[] rLocs = new int[]{num, 50+num, 650-num, 700-num};
        int[] cLocs = new int[]{900-num, 850-num, 850-num, 900-num};

        if(flashlight) {
            num = 30 * n;
        }
        return new Wall(rLocs, cLocs, 255-num, 255-num, 255-num, "Right", factor);
    }

    //Right Rectangles    n = fov
    public Wall getRightPathway(int n){
        int num = factor*n;

        int[] rLocs = new int[]{50+num, 50+num, 650-num, 650-num};
        int[] cLocs = new int[]{900-num, 850-num, 850-num, 900-num};

        if(flashlight) {
            num = 30 * n;
        }
        return new Wall(rLocs, cLocs, 255-num, 255-num, 255-num, "RightPath", factor);
    }

    //triangles
    public Wall getTopRightTriangle(int n){
        int num = factor*n;

        int[] rLocs = new int[]{num, 50+num, 50+num};
        int[] cLocs = new int[]{900-num, 900-num, 850-num};

        if(flashlight) {
            num = 30 * n;
        }
        return new Wall(rLocs, cLocs, 255-num, 255-num, 255-num, "TopRight", factor);
    }
    public Wall getBotRightTriangle(int n){
        int num = factor*n;

        int[] rLocs = new int[]{700-num, 650-num, 650-num};
        int[] cLocs = new int[]{900-num, 900-num, 850-num};

        if(flashlight) {
            num = 30 * n;
        }
        return new Wall(rLocs, cLocs, 255-num, 255-num, 255-num, "BottomRight", factor);
    }


    public Wall getCeiling(int n){
        int num = factor*n;

        int[] rLocs = new int[]{num, 50+num, 50+num, num};
        int[] cLocs = new int[]{100+num, 150+num, 850-num, 900-num};

        if(flashlight) {
            num = 30 * n;
        }
        return new Wall(rLocs, cLocs, 255-num, 255-num, 255-num, "Ceiling", 20);
    }

    public Wall getFloor(int n){
        int num = factor*n;

        int[] rLocs = new int[]{700-num, 650-num, 650-num, 700-num};
        int[] cLocs = new int[]{100+num, 150+num, 850-num, 900-num};
        if(flashlight) {
            num = 30 * n;
        }

        return new Wall(rLocs, cLocs, 255-num, 255-num, 255-num, "Floor", factor);
    }

    public Wall sprayFloor(int n){
        int num = factor*n;

        int[] rLocs = new int[]{700-num, 650-num, 650-num, 700-num};
        int[] cLocs = new int[]{100+num, 150+num, 850-num, 900-num};
        if(flashlight) {
            num = 30 * n;
        }
        return new Wall(rLocs, cLocs, 255-num,50, 50, "Floor", factor);
    }

    public Wall getInFront(int n){
        int num = factor*n;

        int[] rLocs = new int[]{num, num, 700-num, 700-num};
        int[] cLocs = new int[]{100+num, 900-num, 900-num, 100+num};
        if(flashlight) {
            num = 30 * n;
        }

        return new Wall(rLocs, cLocs, 255-num, 255-num, 255-num, "Front", factor);
    }
	public void keyPressed(KeyEvent e)
	{
		System.out.println(e.getKeyCode());
		if(e.getKeyCode()==32)
			is3D = !is3D;
		explorer.move(e.getKeyCode(),maze);
		if(endLoc.equals(explorer.getLoc()))
			won = true;
		if(e.getKeyCode() == 70){
			flashlight = !flashlight;
		}
		//S for spray
		if(e.getKeyCode() == 83){
			if(sprayLimit > 0) {
				int rLoc = explorer.getLoc().getR();
				int cLoc = explorer.getLoc().getC();
				if(maze[rLoc][cLoc] != 'F'&&maze[rLoc][cLoc]!='p'){
					maze[rLoc][cLoc] = 'p';
					sprayLimit--;
				}
			}
			else{

			}
		}
		if(flashlight)
			view = 6;
		else
			view = 5;
		if(e.getKeyCode() == 38&&maze[explorer.getLoc().getR()][explorer.getLoc().getC()]=='E'){
			maze[explorer.getLoc().getR()][explorer.getLoc().getC()]=' ';
		}
		repaint();
	}
	public void keyReleased(KeyEvent e) //Required for interface, leave empty
	{
	}
	public void keyTyped(KeyEvent e) // Required for interface, leave open
	{
	}
	public void setBoard()
	{
		// Read maze from file and set to maze 2d array
		/*maze[0][0] = 'E';
		maze[0][1] = ' ';
		maze[0][2] = ' ';*/
		try{
			BufferedReader input = new BufferedReader(new FileReader("maze1.txt"));
			String text = "";
			int j = 0;
			while((text=input.readLine())!= null){
				if(debug) System.out.println("len->"+text.length());
				for(int i = 0; i < text.length(); i++){
					maze[j][i]=text.charAt(i);
					if(text.charAt(i)=='E'){
						explorer = new Explorer(new Location(j,i),0,size,Color.RED,sizes);
					}
					if(text.charAt(i)=='F'){
						endLoc = new Location(j,i);
					}
				}
				j++;
			}

		}
		catch (IOException io)
		{
			System.err.println("File does not exist");
		}
	}
	public static void main(String[] args)
	{
		new MazeProjectTemplate();
	}
}