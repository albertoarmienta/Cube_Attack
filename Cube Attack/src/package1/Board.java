package package1;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;


public class Board extends JPanel {

    //public static final int WIDTH = 400, HEIGHT = 800;
    public static int WIDTH = (GUIPanel.WIDTH/2) - (MidColumn.WIDTH/2), HEIGHT = GUIPanel.HEIGHT;
    public static final int MAX_X = 8, MAX_Y = 18;
    public static int BLOCK_SIZE = WIDTH/MAX_X;
    public static int OFFSET = WIDTH%BLOCK_SIZE;
    public Block[][] levelArray = new Block[MAX_X][MAX_Y];
    public Cursor levelCursor = new Cursor();
    public boolean moveable = true;
    private final int MAPX_SIZE = WIDTH;
    private final int MAPY_SIZE = HEIGHT;
    private String timeStr = "Time: ";
    private static int timeLeft = 10000;
    private static boolean gameOver = false;
    private static boolean gameStarted = false;
    private ScheduledExecutorService  moveUpThread;
    private ImageIcon backgroundIcon;
    private Image background;

    public GUIPanel guiPanel;
    public int highestBlock = MAX_Y - 1;
    int originalMoveUpTimer = 400;
    public int comboTimer = 0;
    public int DEFAULT_COMBO_TIMER = 200;
    int moveUpTimer = originalMoveUpTimer;
    public boolean canMoveUp = true;
    public int moveUpInterval = 0;
    public int moveUpOffSet = 0;
    public int comboStreak = 0;

    private EnemyAI AIHandler;
    private Board thisBoard;

    public Board(boolean AI, GUIPanel GUI, int side) {
        if(side==1){
            backgroundIcon = new ImageIcon(getClass().getResource("BG1.png"));
        }
        else if(side==2){
            backgroundIcon = new ImageIcon(getClass().getResource("BG2.png"));
        }
        background = backgroundIcon.getImage();
        System.out.println(WIDTH + ":" + HEIGHT);
        guiPanel = GUI;
        thisBoard = this;
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
    public static void resizeBoard(){
        WIDTH = (GUIPanel.WIDTH/2) - (MidColumn.WIDTH/2);
        HEIGHT = GUIPanel.HEIGHT;
        BLOCK_SIZE = WIDTH/MAX_X;
    }
    public void resetArray() {
        for (int x = 0; x < MAX_X; x++) {
            for (int y = 0; y < MAX_Y; y++) {
                levelArray[x][y] = new Block("EMPTY");
            }
        }
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

    public void generateBricks(int comboSize)
    {
        for(int y = 0; y < comboSize; y++)
        {
            for(int x = 0; x < MAX_X; x++)
            {
                levelArray[x][y] = new Block("BRICK");
            }
        }
        
    }
    
    
    public boolean adjacencyCheck(int x, int y) {
        if(levelArray[x][y].color == "BRICK")
            return false;
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
            tempUp = yref - numSameU;
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
            //checking if block above origin is a brick
            if(y - 1 >= 0 && levelArray[x][y - 1].color == "BRICK")
                bricksToBlocks(y - 1);
            if(tempUp - 1 >= 0 && levelArray[x][tempUp - 1].color == "BRICK")
                bricksToBlocks(tempUp - 1);

        }
        return deleteOrigin;
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
                        if (levelArray[x][y].needsRemoval && levelArray[x][y].delayTime <= 0)
                        {
                            Block temp = levelArray[x][y] ;
                            levelArray[x][y] = new Block("EMPTY");
                            temp = null;
                        }
                        else if (levelArray[x][y].needsRemoval)
                        {
                            levelArray[x][y].delayTime--;
                        }
                    }
                }
            }
        }, 0, 50, TimeUnit.MILLISECONDS);
    }
    
    private void bricksToBlocks(int y)
    {
        if(y < 0 || y > MAX_Y)
            return;
        for(int x = 0; x < MAX_X; x++)
        {
            levelArray[x][y] = new Block();
            levelArray[x][y].justSpawned = false;
            adjacencyCheck(x, y);
        }
        
        if(y - 1 >= 0 && levelArray[0][y - 1].color == "BRICK")
            bricksToBlocks(y - 1);
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
                    System.out.println(e);
                }
            }
        }, 0, 150, TimeUnit.MILLISECONDS);
    }
    
    public void shiftDown() {
        boolean moveUp = true;
        for (int x = MAX_X - 1; x >= 0; x--) {
            if(!"EMPTY".equals(levelArray[x][MAX_Y-1].color) && !"BRICK".equals(levelArray[x][MAX_Y-1].color))
                adjacencyCheck(x,MAX_Y-1);
            for (int y = MAX_Y - 2; y >= 0; y--) {
                //If the current block is not EMPTY and the block below it IS EMPTY
                if (!"EMPTY".equals(levelArray[x][y].color) && !"BRICK".equals(levelArray[x][y].color)
                        && "EMPTY".equals(levelArray[x][y + 1].color)) {
                    levelArray[x][y].falling = true;
                    Block temp = levelArray[x][y + 1];
                    levelArray[x][y + 1] = levelArray[x][y];
                    levelArray[x][y] = temp;
                    moveUp = false;
                    //moveUpTimer = originalMoveUpTimer;
                }
                //Else if the current block is not EMPTY and the block below it is not EMPTY and the block is falling
                else if (!"EMPTY".equals(levelArray[x][y+1].color) &&!"EMPTY".equals(levelArray[x][y].color)
                        && levelArray[x][y].falling) {
                    levelArray[x][y].falling = false;
                    if(adjacencyCheck(x, y) == true)
                    {
                        if(comboTimer <= 0)
                            comboStreak = 1;
                        else
                            comboStreak++;
                        comboTimer = DEFAULT_COMBO_TIMER;
                        
                    }
                }
                else if("BRICK".equals(levelArray[x][y].color) && "EMPTY".equals(levelArray[x][y + 1].color) && y < highestBlock - 1)
                {
                    Block temp = levelArray[x][y + 1];
                    levelArray[x][y + 1] = levelArray[x][y];
                    levelArray[x][y] = temp;
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
            //these if statements fix the error when you move a same colored block
            // to 2 same colored block, and it would trigger 3 in a row, but the
            // moved block should fall instead because you moved it to a column that
            // was empty.
            if(y + 1 < MAX_Y && levelArray[x1][y + 1].color != "EMPTY")
                adjacencyCheck(x1, y);
            if(y + 1 < MAX_Y && levelArray[x2][y + 1].color != "EMPTY")
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
                if(comboTimer > 0)
                    comboTimer --;
                else
                {
                    if(comboStreak > 0)
                    {
                        addBricks();
                    }
                    comboStreak = 0;
                }
            }
            catch (Throwable e)
            {
                System.out.println("DecreaseTimeThread:" + e);
            }
            repaint();
        }
        
    }
    private void addBricks()
    {
        guiPanel.addBricks(this, comboStreak);
    }
    public void decreaseTime() {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        //DecreaseTimeThread thread = new DecreaseTimeThread();
        exec.scheduleAtFixedRate(new DecreaseTimeThread(), 0, 10, TimeUnit.MILLISECONDS);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Whatever is painted last appears on top of everything else
        //Draws the background image at (0,0) of this board
        g.drawImage(background, 0, 0, WIDTH, HEIGHT, this);
        //Draws the blocks
        highestBlock = MAX_Y - 1;
        for (int x = 0; x < levelArray.length; x++) {
            for (int y = 0; y < levelArray[0].length; y++) {
                if (levelArray[x][y] instanceof Block) {
                    //g.drawImage(levelArray[x][y].getImage(), BLOCK_SIZE * x, BLOCK_SIZE * y - moveUpOffSet, this);
                    g.drawImage(levelArray[x][y].getImage(), BLOCK_SIZE * x, BLOCK_SIZE * y - moveUpOffSet, BLOCK_SIZE, BLOCK_SIZE, this);
                    //g.drawImage(levelArray[x][y].getImage(), BLOCK_SIZE * x, BLOCK_SIZE * y, this);
                }
                if((levelArray[x][y].color != "EMPTY" && levelArray[x][y].color != "BRICK")
                        && y< highestBlock)
                    highestBlock = y;
                
            }
        }
        g.drawImage(levelCursor.getImage(), levelCursor.getCursorx() * BLOCK_SIZE, levelCursor.getCursory() * BLOCK_SIZE - moveUpOffSet,BLOCK_SIZE*2,BLOCK_SIZE, this);
    }
}
