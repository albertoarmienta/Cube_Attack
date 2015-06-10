package package1;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

class GUIPanel extends JPanel implements ActionListener, MouseListener {

    private final int MAPX_SIZE = Board.WIDTH;
    private final int MAPY_SIZE = Board.HEIGHT;
    private final int BLOCK_SIZE = 50;
    private String timeStr = "Time: ";
    private static int timeLeft = 10000;
    private static boolean gameOver = false;
    private static boolean gameStarted = false;

    public GUIPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
        addKeyListener(new TAdapter());
        addMouseListener(this);
        setFocusable(true);
        decreaseTime();

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
        }, 0, 1, TimeUnit.MILLISECONDS);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw Text

        
        for (int i = 0; i < MAPY_SIZE/BLOCK_SIZE; i++) {
            g.drawLine(BLOCK_SIZE * i, 0, BLOCK_SIZE * i, MAPY_SIZE);
            g.drawLine(0, BLOCK_SIZE * i, MAPX_SIZE, BLOCK_SIZE * i);

        }
        for(int x=0;x<Board.levelArray.length;x++)
        {
            for(int y=0;y<Board.levelArray[0].length;y++)
            {
                if(Board.levelArray[x][y] instanceof Block)
                    g.drawImage(Board.levelArray[x][y].getImage(), BLOCK_SIZE*x, BLOCK_SIZE*y, this);
            //block.x = 50*i
            //block.y = 50*j
            //<hittest> : if(blocks[i].x - blocks[i+1].x < Block.WIDTH)
            }
        }
        g.drawImage(Board.levelCursor.getImage(), Board.levelCursor.getTarget1x()*BLOCK_SIZE, Board.levelCursor.getTargety()*BLOCK_SIZE, this);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 24));
        g.setColor(Color.black);
        //g.drawString(this.timeStr + (this.timeLeft / 1000) + "." + (this.timeLeft % 1000 / 100), this.MAPX_SIZE / 2 - 50, this.MAPY_SIZE - 20);
        //Draws a String, centered at the bottom of the window

    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseClicked(MouseEvent me) {
			int x = me.getX() - (me.getX() % BLOCK_SIZE);
			x /= BLOCK_SIZE;
			int y = me.getY() - (me.getX() % BLOCK_SIZE);
			y /= BLOCK_SIZE;
			/** THIS WILL BE USED TO MOVE BLOCKS DOWN, HERE FOR TESTING
			 ** WILL BE PUT ELSEWHERE
			 **/
			Board.levelArray[x][y] = null;
			Board.levelArray[x][y] = new Block("EMPTY");
			Board.levelArray[x][y - 1] = new Block("EMPTY");
			
			for(int i = 2; i > 0; i--)
				for(int j = y; j > 0; j-- )
					Board.levelArray[x][j] = Board.levelArray[x][j - 1];
			
    }

    @Override
    public void mousePressed(MouseEvent me) {
       
    }
    @Override
    public void mouseReleased(MouseEvent me) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent me) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent me) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   

    private class TAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            
        }

        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();
            switch(key){
                case KeyEvent.VK_ENTER:
                    Board.moveUp();
                    break;
                case KeyEvent.VK_SPACE:
                    Board.levelCursor.swapTargets();
                    break;
                case KeyEvent.VK_UP:
                    Board.levelCursor.moveUp();
                    break;
                case KeyEvent.VK_DOWN:
                    Board.levelCursor.moveDown();
                    break;
                case KeyEvent.VK_LEFT:
                    Board.levelCursor.moveLeft();
                    break;
                case KeyEvent.VK_RIGHT:
                    Board.levelCursor.moveRight();
                    break;
            }
            
        }
    }
}
