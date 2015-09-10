package package1;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

class GUIPanel extends Applet implements ActionListener{
    //public static final int WIDTH = 1000, HEIGHT = 825;
    
    public static int HEIGHT = getScreenWorkingHeight()*3/4,WIDTH = HEIGHT*6/5;
    public int previousW = WIDTH, previousH = HEIGHT;
    public static final double RATIO = WIDTH/HEIGHT;
    public int gameState = 0;
    public AudioHandler audio = new AudioHandler();
    public Board b1;
    public Board b2;
    public MidColumn b3;
    public JLabel banner;
    private JLabel menu;
    private JFrame game = buildFrame();
    //ComboStreakDisplay.setLineWrap(true);
    public GUIPanel() 
    {
        menu = new JLabel();
        menu.setBounds(0,0,WIDTH,HEIGHT);
        ImageIcon menuIcon = new ImageIcon(getClass().getResource("MENU.png"));
        Image scaledIcon = menuIcon.getImage().getScaledInstance(WIDTH, HEIGHT, java.awt.Image.SCALE_SMOOTH);
        menu.setIcon(new ImageIcon(scaledIcon));
        game.getContentPane().add(menu);
        setFocusable(true);
        game.addKeyListener(new TAdapter());
        game.setVisible(true);
        game.setLocationRelativeTo(null);
        
    }
    //Get the 'working' height and width of users monitor
    public static int getScreenWorkingWidth() {
        return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
}

    public static int getScreenWorkingHeight() {
        return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
}
    public JFrame buildFrame() {
        JFrame window = new JFrame("Cube Attack");
        window.setLayout(null);
        window.getContentPane().setPreferredSize(new Dimension( WIDTH, HEIGHT));
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        return window;
    }
    public void startGame1(){
        gameState = 1;
        game.remove(menu);
        b1 = new Board(false, this,1);
        b2 = new Board(true, this,2);
        b3 = new MidColumn(this,1);
        //SetupDisplay() sets the boards and banners accordingly
        SetupDisplay();
        
    }
    public void startGame2(){
        gameState = 2;
        game.remove(menu);
        b1 = new Board(false, this,1);
        b2 = new Board(false, this,2);
        b3 = new MidColumn(this,2);
        //SetupDisplay() sets the boards and banners accordingly
        SetupDisplay();
    }

    

    /*
     public void paintComponent(Graphics g) {
        //Whatever is painted last appears on top of everything else
        paintComponent(g);
        g.drawImage(new ImageIcon(getClass().getResource("BANNER.png")).getImage(),
                Board.WIDTH,GUIPanel.HEIGHT, GUIPanel.WIDTH - Board.WIDTH * 2, GUIPanel.HEIGHT, this);
     }
    */
    @Override
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
 private class TAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            
        }

        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            switch(key){
                case KeyEvent.VK_1:
                    if(gameState == 1){
                    //b1.moveUp();
                    //b2.moveUp();
                    }
                    else if(gameState==0)
                    {
                        startGame1();
                       
                    }
                    break;
                case KeyEvent.VK_2:
                    if(gameState == 1){
                    //b1.moveUp();
                    //b2.moveUp();
                    }
                    else if(gameState==0)
                    {
                        startGame2();
                       
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if(gameState == 1){
                    b1.swapTargets();
                    audio.playSound1();
                    }
                    else if (gameState == 2)
                        b1.swapTargets();
                    break;
                case KeyEvent.VK_UP:
                    if(gameState == 1)
                        b1.levelCursor.moveUp();
                     else if (gameState == 2)
                         b2.levelCursor.moveUp();
                    break;
                case KeyEvent.VK_DOWN:
                    if(gameState == 1)
                     b1.levelCursor.moveDown();
                    else if (gameState == 2)
                         b2.levelCursor.moveDown();
                    break;
                case KeyEvent.VK_LEFT:
                    if(gameState == 1)
                     b1.levelCursor.moveLeft();
                    else if (gameState == 2)
                         b2.levelCursor.moveLeft();
                    break;
                case KeyEvent.VK_RIGHT:
                    if(gameState == 1)
                     b1.levelCursor.moveRight();
                    else if (gameState == 2)
                         b2.levelCursor.moveRight();
                    break;
                case KeyEvent.VK_W:
                    if (gameState == 2)
                         b1.levelCursor.moveUp();
                    break;
                case KeyEvent.VK_S:
                    if (gameState == 2)
                         b1.levelCursor.moveDown();
                    break;
                case KeyEvent.VK_A:
                    if (gameState == 2)
                         b1.levelCursor.moveLeft();
                    break;
                case KeyEvent.VK_D:
                    if (gameState == 2)
                         b1.levelCursor.moveRight();
                    break;      
                 case KeyEvent.VK_ENTER:
                    if (gameState == 2)
                         b2.swapTargets();
                    break;
                case KeyEvent.VK_0:
                    if(gameState==1)
                    {
                        b1.generateBricks(1);
                        //game.dispose();
                        //gameState = 0;
                    }
                    break;
            }
            
        }
    }

    private void SetupDisplay()
    {
        b1.setBounds(0,0,b1.WIDTH-(b1.OFFSET),b1.HEIGHT);
        b2.setBounds(b2.WIDTH + b3.WIDTH-(b1.OFFSET),0,b2.WIDTH,b2.HEIGHT);
        b3.setBounds(b1.WIDTH-(b1.OFFSET), 0, b3.WIDTH, b1.HEIGHT);
        // If you want text to appear above banner, (..).add() the text before
        // the Banner/picture
        game.getContentPane().add(b1);
        game.getContentPane().add(b2);
        game.getContentPane().add(b3);
        game.repaint();
        
    }
    
    // This function is called within the board class, and it will pass a
    // reference to itself. Always put blocks on opposite board
    public void addBricks(Board whichBoard, int comboSize)
    {
        //System.out.println("yo");
        //b1 is left board
        if(whichBoard == b1)
        {
            b2.generateBricks(comboSize);
        }
        else if(whichBoard == b2)
        {
            b1.generateBricks(comboSize);
        }
        
    }
}
