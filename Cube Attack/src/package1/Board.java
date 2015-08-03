package package1;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;


public class Board extends JPanel {

    public static final int WIDTH = 400, HEIGHT = 800;
    //private JLabel background;
    /* SIZE OF THE GRID */
    public static final int MAX_X = 8, MAX_Y = 17;
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
    private ScheduledExecutorService  moveUpThread;
    private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("background.jpg"));
    private Image background = backgroundIcon.getImage();

    int originalMoveUpTimer = 200;
    int moveUpTimer = originalMoveUpTimer;
    public boolean canMoveUp = true;
    public int moveUpInterval = 0;
    public int moveUpOffSet = 0;

    private EnemyAI AIHandler;

    public Board(boolean AI) {
        setBorder(BorderFactory.createLineBorder(Color.black));
        moveUpInterval = (int)Math.ceil((float)originalMoveUpTimer / (float)BLOCK_SIZE);
        resetArray();
        removeBlock();
        moveBlock();
        generateRow();
        decreaseTime();
        if(AI)
        {
            AIHandler = new EnemyAI(levelArray, this);
        }
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
    
    public void generateRow() {
        /* This Checks for three in a row when you generate a new row.
        * Looks weird, just checks the last two indicies and if the macth, if they
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

        for (int x = 0; x < MAX_X; x++) 
            levelArray[x][MAX_Y - 2].justSpawned = false;

        for (int x = 0; x < MAX_X; x++) {
            adjacencyCheck(x, MAX_Y - 2);
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
            if (levelArray[x][y].color.equals(levelArray[x - 1][y].color) && levelArray[x][y].color != "EMPTY" ) {//&& !levelArray[x][y].justSpawned) {
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
            if (levelArray[x][y].color == levelArray[x][y - 1].color && levelArray[x][y].color != "EMPTY" && !levelArray[x][y].justSpawned) {
                numSameU++;
                y--;
            } else {
                break;
            }
        }
        y = yref;
        while (y < MAX_Y - 1) {
            if (levelArray[x][y].color == levelArray[x][y + 1].color && levelArray[x][y].color != "EMPTY"&& !levelArray[x][y + 1].justSpawned) {
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
                levelArray[x - numSameL][y].nextSprite();
                numSameL--;
            }
            while (numSameR > 0) {
                levelArray[x + numSameR][y].nextSprite();
                numSameR--;
            }
            deleteOrigin = true;
        }
        if (numSameU + numSameD >= 2) {
            tempUp = yref - numSameU - 1;
            while (numSameU > 0) {
                levelArray[x][y - numSameU].nextSprite();
                numSameU--;
            }
            while (numSameD > 0) {
                levelArray[x][y + numSameD].nextSprite();
                numSameD--;
            }
            
            deleteOrigin = true;
        }
        if (deleteOrigin) {
            //moveUpTimer = originalMoveUpTimer;
            levelArray[x][y].nextSprite();
        }
    }

    public void moveUp()
    {
        for (int x = 0; x < MAX_X; x++) {
            for (int y = 1; y < MAX_Y; y++) {
                levelArray[x][y - 1] = levelArray[x][y];
            }
        }
        levelCursor.moveUp();
    }
    private void removeBlock() {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            
            @Override
            public void run() {
                for (int x = 0; x < levelArray.length; x++) {
                    for (int y = 0; y < levelArray[0].length; y++) {
                        if (levelArray[x][y].needsRemoval && levelArray[x][y].delayTime <= 0) {
                            Block temp = levelArray[x][y] ;
                            levelArray[x][y] = new Block("EMPTY");
                            temp = null;
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
        boolean moveUp = true;
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
                    moveUp = false;
                    //moveUpTimer = originalMoveUpTimer;
                }
                //Else if the current block is not EMPTY and the block below it is not EMPTY and the block is falling
                else if (!"EMPTY".equals(levelArray[x][y+1].color) &&!"EMPTY".equals(levelArray[x][y].color) && levelArray[x][y].falling) {
                    levelArray[x][y].falling = false;
                    adjacencyCheck(x, y);
                }
            }
        }
        canMoveUp = moveUp;
    }
    
    public void swapTargets() {
        int x1 = levelCursor.getCursorx();
        int x2 = levelCursor.getCursor2x();
        int y = levelCursor.getCursory();
        Block temp = levelArray[x1][y];
        if(!temp.needsRemoval && !levelArray[x2][y].needsRemoval )
        {
            levelArray[x1][y] = levelArray[x2][y];
            levelArray[x2][y] = temp;
            adjacencyCheck(x1, y);
            adjacencyCheck(x2, y);
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
    
    
    public class DecreaseTimeThread implements Runnable
    {
        
            @Override
            public void run() {
                try{
                    if(canMoveUp && moveUpTimer > 0)
                    {
                        if(moveUpTimer % moveUpInterval == 0)
                            moveUpOffSet += 1;
                        moveUpTimer -= 1;
                    }
                    else if (canMoveUp && moveUpTimer <= 0)
                    {
                        moveUpOffSet = 0;
                        moveUpTimer = originalMoveUpTimer;
                        moveUp();
                        generateRow();
                    }
                    
                }
                catch (Throwable e)
                {
                    System.out.println("DecreaseTimeThread:" + e);
                }
            repaint();
            }
            
    }
    //
    public void decreaseTime() {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        //DecreaseTimeThread thread = new DecreaseTimeThread();
        exec.scheduleAtFixedRate(new DecreaseTimeThread(), 0, 10, TimeUnit.MILLISECONDS);
    }
    
    public void paintComponent(Graphics g) {
        //Whatever is painted last appears on top of everything else
        
        super.paintComponent(g);
        
        //Draws the background image at (0,0) of this board
        g.drawImage(background, 0, 0, WIDTH, HEIGHT, this);
        
        //Draws vertica and horizantal lines to create a visual grid
        for (int i = 0; i < MAPY_SIZE / BLOCK_SIZE; i++) {
            g.drawLine(BLOCK_SIZE * i, 0, BLOCK_SIZE * i, MAPY_SIZE);
            g.drawLine(0, BLOCK_SIZE * i, MAPX_SIZE, BLOCK_SIZE * i);
            
        }
        
        //Draws the blocks
        for (int x = 0; x < levelArray.length; x++) {
            for (int y = 0; y < levelArray[0].length; y++) {
                if (levelArray[x][y] instanceof Block) {
                    g.drawImage(levelArray[x][y].getImage(), BLOCK_SIZE * x, BLOCK_SIZE * y - moveUpOffSet, this);
                    //g.drawImage(levelArray[x][y].getImage(), BLOCK_SIZE * x, BLOCK_SIZE * y, this);
                }
                
            }
        }
        //Draws the cursor
        g.drawImage(levelCursor.getImage(), levelCursor.getCursorx() * BLOCK_SIZE, levelCursor.getCursory() * BLOCK_SIZE - moveUpOffSet, this);
        //g.drawImage(levelCursor.getImage(), levelCursor.getCursorx() * BLOCK_SIZE, levelCursor.getCursory() * BLOCK_SIZE, this);
    }
}
