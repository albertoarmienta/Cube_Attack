package package1;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class Board extends JPanel {
	
	public  static final int WIDTH = 400, HEIGHT = 800;
	/* SIZE OF THE GRID */
	public  static final int MAX_X = 8, MAX_Y = 16;
	public  Block[][] levelArray = new Block[MAX_X][MAX_Y];
	public  Cursor levelCursor = new Cursor();
	public final int BLOCK_SIZE = 50;
	public int delayTime = 200;
	
	private final int MAPX_SIZE = WIDTH;
	private final int MAPY_SIZE = HEIGHT;
	private String timeStr = "Time: ";
	private static int timeLeft = 10000;
	private static boolean gameOver = false;
	private static boolean gameStarted = false;
	
	public Board() {
		setBorder(BorderFactory.createLineBorder(Color.black));
		//setSize(new Dimension(WIDTH,HEIGHT));
		decreaseTime();
		resetArray();
		generateRow();
		//removeBlock();
	}
	
	
	public static class AnimationThread extends JPanel implements Runnable{
		
		private boolean callFallingBlocks = false;
		//m stands for member
		private int m_x;
		private int m_y;
		Board board;
		
		
		public AnimationThread(int x , int y, Board board)
		{
			this.board = board;
			m_x = x;
			m_y = y;
		}
		
		public void run()
		{
			if(this.adjacencyCheck(m_x, m_y))
			{
				repaint();
				try
				{
					Thread.sleep(board.delayTime);
				}catch(InterruptedException e){
				}
				this.adjacencyCheck(m_x, m_y);
				repaint();
			}
		}
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(board.levelArray[m_x][m_y].getImage(), board.BLOCK_SIZE * m_x,
							board.BLOCK_SIZE * m_y, this);
			
		}
		public boolean adjacencyCheck(int x, int y) {
			int xref = x;
			int yref = y;
			int numSameL = 0;
			int numSameR = 0;
			int numSameU = 0;
			int numSameD = 0;
			int tempUp = 0;
			Boolean deleteOrigin = false;
			while (x > 0) {
				if (board.levelArray[x][y].color.equals(board.levelArray[x - 1][y].color) && board.levelArray[x][y].color != "EMPTY") {
					numSameL++;
					x--;
				} else {
					break;
				}
			}
			x = xref;
			while (x < MAX_X - 1) {
				if (board.levelArray[x][y].color == board.levelArray[x + 1][y].color && board.levelArray[x][y].color != "EMPTY") {
					numSameR++;
					x++;
				} else {
					break;
				}
			}
			x = xref;
			while (y > 0) {
				if (board.levelArray[x][y].color == board.levelArray[x][y - 1].color && board.levelArray[x][y].color != "EMPTY") {
					numSameU++;
					y--;
				} else {
					break;
				}
			}
			y = yref;
			while (y < MAX_Y - 1) {
				if (board.levelArray[x][y].color == board.levelArray[x][y + 1].color && board.levelArray[x][y].color != "EMPTY") {
					numSameD++;
					y++;
				} else {
					break;
				}
			}
			y = yref;
			//Removal Time...
			if (numSameL + numSameR >= 2) {
				while (numSameL > 0) {
					if(board.levelArray[x - numSameL][y].nextSprite())
						board.fallingBlocks(x - numSameL, y, 1);
					numSameL--;
				}
				while (numSameR > 0) {
					if(board.levelArray[x + numSameR][y].nextSprite())
						board.fallingBlocks(x + numSameR, y, 1);
					numSameR--;
				}
				deleteOrigin = true;
			}
			if (numSameU + numSameD >= 2) {
				tempUp = yref - numSameU - 1;
				while (numSameU > 0) {
					board.levelArray[x][y - numSameU].nextSprite();
					numSameU--;
				}
				while (numSameD > 0) {
					board.levelArray[x][y + numSameD].nextSprite();
					numSameD--;
				}
				
				deleteOrigin = true;
			}
			if (deleteOrigin) {
				if(board.levelArray[x][y].nextSprite())
				{
					board.fallingBlocks(x, tempUp, 1);
					board.fallingBlocks(x, y, 1);
				}
				return true;
			}
			else
				return false;
		}
	}
	
	public void resetArray() {
		for (int x = 0; x < levelArray.length; x++) {
			for (int y = 0; y < levelArray[0].length; y++) {
				levelArray[x][y] = new Block("EMPTY");
			}
		}
		
	}
	
	public void setArray(int x, int y, Block a) {
		levelArray[x][y] = a;
	}
	
	public void moveUp() {
		for (int x = 0; x < levelArray.length; x++) {
			for (int y = 1; y < levelArray[0].length; y++) {
				levelArray[x][y - 1] = levelArray[x][y];
			}
		}
		generateRow();
	}
	
	public void generateRow() {
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
		
		for(int x = 0; x < MAX_X; x++)
			adjacencyCheck(x, MAX_Y - 1);
	}
	
	public void adjacencyCheck(int x, int y)
	{
		Thread t = new Thread(new AnimationThread(x , y, this));
		t.start();
	}
	
	public void fallingBlocks(int x, int y, int count) {
		int ref_y = y;
		int ref_x = x;
		
		if (y > 0 && levelArray[x][y].color == "EMPTY") {
			while (y < (MAX_Y) && levelArray[x][y].color == "EMPTY")
			{
				for (int j = y; j > 0; j--)
				{
					levelArray[x][j] = levelArray[x][j - 1];
					if ((j - 1) == 0) {
						levelArray[x][0] = null;
						levelArray[x][0] = new Block("EMPTY");
						
					}
				}
				y += 1;
			}
			adjacencyCheck(x, y - 1);
		} else {
			while (y < (MAX_Y - 1) && levelArray[x][y + 1].color == "EMPTY")
			{
				for (int j = y; j >= 0; j--)
				{
					Block temp = levelArray[x][j + 1];
					levelArray[x][j + 1] = levelArray[x][j];
					levelArray[x][j] = temp;
				}
				y += 1;
			}
			adjacencyCheck(x, y );
		}
		
	}
	public void swapTargets(){
		//left block in Cursor
		int x1 = levelCursor.getCursorx();
		//block in Cursor
		int x2 = levelCursor.getCursor2x();
		int y = levelCursor.getCursory();
		Block temp = levelArray[x1][y];
		levelArray[x1][y] = levelArray[x2][y];
		levelArray[x2][y] = temp;
		
		if(levelArray[x2][y].color == "EMPTY" )
			fallingBlocks(x2, y, 1);
		else if(levelArray[x1][y].color == "EMPTY" )
			fallingBlocks(x1, y, 1);
		
		if(y < (Board.MAX_Y - 1) && levelArray[x2][y + 1].color == "EMPTY")
		{
			fallingBlocks(x2, y, 1);
		}
		else if(y < (Board.MAX_Y - 1) && levelArray[x1][y + 1].color == "EMPTY")
		{
			fallingBlocks(x1, y, 1);
		}
	}
	
	public static void addTime(int t) {
		timeLeft += t;
	}
	
	public static int getTimeLeft() {
		return timeLeft;
	}
	
	public static void resetTime() {
		timeLeft = 10000;
	}
	
	public static void setGameStarted(boolean b) {
		gameStarted = b;
	}
	
	public static void setGameOver(boolean b) {
		gameOver = b;
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(MAPX_SIZE, MAPY_SIZE);
	}
	
	//
	private void decreaseTime() {
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				repaint();
			}
		}, 0, 10, TimeUnit.MILLISECONDS);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Draw Text
		
		
		for (int i = 0; i < MAPY_SIZE/BLOCK_SIZE; i++) {
			g.drawLine(BLOCK_SIZE * i, 0, BLOCK_SIZE * i, MAPY_SIZE);
			g.drawLine(0, BLOCK_SIZE * i, MAPX_SIZE, BLOCK_SIZE * i);
			
		}
		for(int x=0;x<levelArray.length;x++)
		{
			for(int y=0;y<levelArray[0].length;y++)
			{
				if(levelArray[x][y] instanceof Block)
					g.drawImage(levelArray[x][y].getImage(), BLOCK_SIZE*x, BLOCK_SIZE*y, this);
			}
		}
		g.drawImage(levelCursor.getImage(), levelCursor.getCursorx()*BLOCK_SIZE, levelCursor.getCursory()*BLOCK_SIZE, this);
		g.setFont(new Font("Times New Roman", Font.ITALIC, 24));
		g.setColor(Color.black);
		//g.drawString(this.timeStr + (this.timeLeft / 1000) + "." + (this.timeLeft % 1000 / 100), this.MAPX_SIZE / 2 - 50, this.MAPY_SIZE - 20);
		//Draws a String, centered at the bottom of the window
		
	}
	
}
