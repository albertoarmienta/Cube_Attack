package package1;

import java.applet.*;
import java.awt.*;
import javax.swing.*;

public class Board extends Applet {
	
	public static final int WIDTH = 400, HEIGHT = 800;
	/* SIZE OF THE GRID */
	public static final int MAX_X = 8, MAX_Y = 16;
	public static Block[][] levelArray = new Block[MAX_X][MAX_Y];
	public static Cursor levelCursor = new Cursor();
	
	public Board() {
		resetArray();
		generateRow();
		JFrame frame = buildFrame();
	}
	
	public JFrame buildFrame() {
		JFrame window = new JFrame("Cube Attack");
		window.setSize(new Dimension(WIDTH, HEIGHT));
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		GUIPanel panel = new GUIPanel();
		window.getContentPane().add(panel);
		//window.getContentPane().add(label,BorderLayout.CENTER);
		window.pack();
		window.setVisible(true);
		return window;
	}
	
	public void resetArray() {
		for (int x = 0; x < levelArray.length; x++) {
			for (int y = 0; y < levelArray[0].length; y++) {
				levelArray[x][y] = new Block("EMPTY");
			}
		}
		
	}
	
	public static void setArray(int x, int y, Block a) {
		levelArray[x][y] = a;
	}
	
	public static void moveUp() {
		for (int x = 0; x < levelArray.length; x++) {
			for (int y = 1; y < levelArray[0].length; y++) {
				levelArray[x][y - 1] = levelArray[x][y];
			}
		}
		generateRow();
	}
	
	public static void generateRow() {
		/* This Checks for three in a row when you generate a new row.
		* Looks weird, just checks the last to indicies and if the macth, if they
		* do, it doesn't allow for a block with the same color to be created
		*/
		for (int x = 0; x < MAX_X; x++) {
			if (x >= 2 && (levelArray[x - 1][MAX_Y - 1].color
							== levelArray[x - 2][MAX_Y - 1].color)) {
				while ((levelArray[x][MAX_Y - 1] = new Block()).color
								== levelArray[x - 1][MAX_Y - 1].color) {
					levelArray[x][MAX_Y - 1] = null;
					levelArray[x][MAX_Y - 1] = new Block();
				}
			} else {
				levelArray[x][MAX_Y - 1] = new Block();
			}
		}
	}
	
	public static void adjacencyCheck(int x, int y) {
		int xref = x;
		int yref = y;
		int numSameL = 0;
		int numSameR = 0;
		int numSameU = 0;
		int numSameD = 0;
		int tempUp = 0;
		Boolean deleteOrigin = false;
		while (x > 0) {
			if (Board.levelArray[x][y].color == Board.levelArray[x - 1][y].color && levelArray[x][y].color != "EMPTY") {
				numSameL++;
				x--;
			} else {
				break;
			}
		}
		x=xref;
		while (x < Board.MAX_X - 1) {
			if (Board.levelArray[x][y].color == Board.levelArray[x + 1][y].color && levelArray[x][y].color != "EMPTY") {
				numSameR++;
				x++;
			} else {
				break;
			}
		}
		x=xref;
		while (y > 0) {
			if (Board.levelArray[x][y].color == Board.levelArray[x][y - 1].color && levelArray[x][y].color != "EMPTY") {
				numSameU++;
				y--;
			} else {
				break;
			}
		}
		y=yref;
		while (y < Board.MAX_Y - 1) {
			if (Board.levelArray[x][y].color == Board.levelArray[x][y + 1].color && levelArray[x][y].color != "EMPTY") {
				numSameD++;
				y++;
			} else {
				break;
			}
		}
		y=yref;
		//Removal Time...
		if (numSameL + numSameR >= 2) {
			while (numSameL > 0) {
				Board.levelArray[x - numSameL][y] = null;
				Board.levelArray[x - numSameL][y] = new Block("EMPTY");
				fallingBlocks(x - numSameL, y, 1);
				numSameL--;
			}
			while (numSameR > 0) {
				Board.levelArray[x + numSameR][y] = null;
				Board.levelArray[x + numSameR][y] = new Block("EMPTY");
				fallingBlocks(x + numSameR, y, 1);
				numSameR--;
			}
			deleteOrigin = true;
		}
		if (numSameU + numSameD >= 2) {
			tempUp = yref - numSameU - 1;
			while (numSameU > 0) {
				Board.levelArray[x][y - numSameU] = null;
				Board.levelArray[x][y - numSameU] = new Block("EMPTY");
				numSameU--;
			}
			while (numSameD > 0) {
				Board.levelArray[x][y + numSameD] = null;
				Board.levelArray[x][y + numSameD] = new Block("EMPTY");
				numSameD--;
			}
			
			deleteOrigin = true;
		}
		if(deleteOrigin)
		{
			Board.levelArray[x][y] = null;
			Board.levelArray[x][y] = new Block("EMPTY");
			fallingBlocks(x, tempUp, 1);
			fallingBlocks(x,y,1);
		}
	}
	
	public static void fallingBlocks(int x, int y, int count) {
		int ref_y = y;
		int ref_x = x;
		//for(int i = count; i > 0; i--)
		//for(int j = y; j > 0; j-- )
		//	levelArray[x][j] = levelArray[x][j - 1];
		
		if (y > 0 && levelArray[x][y].color == "EMPTY") {
			while (y < (MAX_Y) && levelArray[x][y].color == "EMPTY") {
				for (int j = y; j > 0; j--) {
					levelArray[x][j] = levelArray[x][j - 1];
					if((j - 1) == 0)
					{
						levelArray[x][0] = null;
						levelArray[x][0] = new Block("EMPTY");

					}
				}
				y += 1;
			}
		} else {
			while (y < (MAX_Y - 1) && levelArray[x][y + 1].color == "EMPTY") {
				for (int j = y; j >= 0; j--) {
					Block temp = levelArray[x][j + 1];
					levelArray[x][j + 1] = levelArray[x][j];
					levelArray[x][j] = temp;
				}
				y += 1;
			}
		}

		
	}
	
	public static void checkAdjacent(int x, int y) {
		//for(int i = x; i < MAX_X && i < (x+3); i++)
	}
}
