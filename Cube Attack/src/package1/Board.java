package package1;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class Board extends JPanel {

    public static final int WIDTH = 400, HEIGHT = 800;
    /* SIZE OF THE GRID */
    public static final int MAX_X = 8, MAX_Y = 16;
    public Block[][] levelArray = new Block[MAX_X][MAX_Y];
    public Cursor levelCursor = new Cursor();
    public boolean moveable = true;
    private final int MAPX_SIZE = WIDTH;
    private final int MAPY_SIZE = HEIGHT;
    private final int BLOCK_SIZE = 50;
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
        removeBlock();
        moveBlock();
    }

    public void resetArray() {
        for (int x = 0; x < MAX_X; x++) {
            for (int y = 0; y < MAX_Y; y++) {
                levelArray[x][y] = new Block("EMPTY");
            }
        }
    }

    public boolean canFall() {
        for (int x = 0; x < MAX_X; x++) {
            for (int y = 0; y < MAX_Y; y++) {
                if (levelArray[x][y].falling == true) {
                    return false;
                }
                if (levelArray[x][y].needsRemoval) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setArray(int x, int y, Block a) {
        levelArray[x][y] = a;
    }

    public void moveUp() {
        //if (canFall()) {
            for (int x = 0; x < levelArray.length; x++) {
                for (int y = 1; y < levelArray[0].length; y++) {
                    levelArray[x][y - 1] = levelArray[x][y];
                }
            }
            generateRow();
       // }
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

        for (int x = 0; x < MAX_X; x++) {
            adjacencyCheck(x, MAX_Y - 1);
        }
    }

    public void adjacencyCheck(int x, int y) {
        int xref = x;
        int yref = y;
        int numSameL = 0;
        int numSameR = 0;
        int numSameU = 0;
        int numSameD = 0;
        int tempUp = 0;
        Boolean deleteOrigin = false;
            while (x > 0) {
                if (levelArray[x][y].color.equals(levelArray[x - 1][y].color) && levelArray[x][y].color != "EMPTY") {
                    numSameL++;
                    x--;
                } else {
                    break;
                }
            }
            x = xref;
            while (x < MAX_X - 1) {
                if (levelArray[x][y].color == levelArray[x + 1][y].color && levelArray[x][y].color != "EMPTY") {
                    numSameR++;
                    x++;
                } else {
                    break;
                }
            }
            x = xref;
        while (y > 0) {
            if (levelArray[x][y].color == levelArray[x][y - 1].color && levelArray[x][y].color != "EMPTY") {
                numSameU++;
                y--;
            } else {
                break;
            }
        }
        y = yref;
        while (y < MAX_Y - 1) {
            if (levelArray[x][y].color == levelArray[x][y + 1].color && levelArray[x][y].color != "EMPTY") {
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
                //levelArray[x - numSameL][y] = null;
                //levelArray[x - numSameL][y] = new Block("EMPTY");
                levelArray[x - numSameL][y].nextSprite();
                //removeBlock(x - numSameL,y);
                numSameL--;
            }
            while (numSameR > 0) {
                //levelArray[x + numSameR][y] = null;
                //levelArray[x + numSameR][y] = new Block("EMPTY");
                levelArray[x + numSameR][y].nextSprite();
                //removeBlock(x + numSameL,y);
                numSameR--;
            }
            deleteOrigin = true;
        }
        if (numSameU + numSameD >= 2) {
            tempUp = yref - numSameU - 1;
            while (numSameU > 0) {
                //levelArray[x][y - numSameU] = null;
                //levelArray[x][y - numSameU] = new Block("EMPTY");
                levelArray[x][y - numSameU].nextSprite();
                //removeBlock(x,y - numSameU);
                numSameU--;
            }
            while (numSameD > 0) {
                //levelArray[x][y + numSameD] = null;
                //levelArray[x][y + numSameD] = new Block("EMPTY");
                levelArray[x][y + numSameD].nextSprite();
                //removeBlock(x,y + numSameU);
                numSameD--;
            }

            deleteOrigin = true;
        }
        if (deleteOrigin) {
            //levelArray[x][y] = null;
            //levelArray[x][y] = new Block("EMPTY");
            levelArray[x][y].nextSprite();
            //removeBlock(x,y);
        }
    }
    private void removeBlock() {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
           
            @Override
            public void run() {
                for (int x = 0; x < levelArray.length; x++) {
                    for (int y = 0; y < levelArray[0].length; y++) {
                        if (levelArray[x][y].needsRemoval && levelArray[x][y].delayTime <= 0) {
                            levelArray[x][y] = null;
                            levelArray[x][y] = new Block("EMPTY");
                        } else if (levelArray[x][y].needsRemoval) {
                            levelArray[x][y].delayTime--;

                        }
                    }
                }
                //repaint();
            }
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

    private void moveBlock() {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try{
                shiftDown();
                }
                catch(Throwable e)
                {
                    //Main.logger.error("Exception: " + e);
                    System.out.println(e);
                }
            }
        }, 0, 150, TimeUnit.MILLISECONDS);
    }

    public void shiftDown() {
        for (int x = MAX_X - 1; x >= 0; x--) {
            if(!"EMPTY".equals(levelArray[x][MAX_Y-1].color))
                adjacencyCheck(x,MAX_Y-1);
            for (int y = MAX_Y - 2; y >= 0; y--) {
                //If the current block is not EMPTY and the block below it IS EMPTY
                if (!"EMPTY".equals(levelArray[x][y].color) && "EMPTY".equals(levelArray[x][y + 1].color)) {
                    levelArray[x][y].falling = true;
                    Block temp = levelArray[x][y + 1];
                    levelArray[x][y + 1] = levelArray[x][y];
                    levelArray[x][y] = temp;
                } 
                //Else if the current block is not EMPTY and the block below it is not EMPTY and the block is falling
                else if (!"EMPTY".equals(levelArray[x][y+1].color) &&!"EMPTY".equals(levelArray[x][y].color) && levelArray[x][y].falling) {
                    levelArray[x][y].falling = false;
                    adjacencyCheck(x, y);
                }
            }
        }
    }

    public void swapTargets() {
        int x1 = levelCursor.getCursorx();
        int x2 = levelCursor.getCursor2x();
        int y = levelCursor.getCursory();
        Block temp = levelArray[x1][y];
        levelArray[x1][y] = levelArray[x2][y];
        levelArray[x2][y] = temp;
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

        for (int i = 0; i < MAPY_SIZE / BLOCK_SIZE; i++) {
            g.drawLine(BLOCK_SIZE * i, 0, BLOCK_SIZE * i, MAPY_SIZE);
            g.drawLine(0, BLOCK_SIZE * i, MAPX_SIZE, BLOCK_SIZE * i);

        }
        for (int x = 0; x < levelArray.length; x++) {
            for (int y = 0; y < levelArray[0].length; y++) {
                if (levelArray[x][y] instanceof Block) {
                    g.drawImage(levelArray[x][y].getImage(), BLOCK_SIZE * x, BLOCK_SIZE * y, this);
                }
                //block.x = 50*i
                //block.y = 50*j
                //<hittest> : if(blocks[i].x - blocks[i+1].x < Block.WIDTH)
            }
        }
        g.drawImage(levelCursor.getImage(), levelCursor.getCursorx() * BLOCK_SIZE, levelCursor.getCursory() * BLOCK_SIZE, this);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 24));
        g.setColor(Color.black);
        //g.drawString(this.timeStr + (this.timeLeft / 1000) + "." + (this.timeLeft % 1000 / 100), this.MAPX_SIZE / 2 - 50, this.MAPY_SIZE - 20);
        //Draws a String, centered at the bottom of the window

    }
}
