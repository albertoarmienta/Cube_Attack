package package1;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
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
    public static int WIDTH = 600, HEIGHT = 525;
    public int previousW = WIDTH, previousH = HEIGHT;
    public static final double RATIO = WIDTH/HEIGHT;
    public static int BANNER_WIDTH = WIDTH/6;
    public int gameState = 0;
    public Board b1;
    public int minutes  = 0;
    public int seconds  = 0;
    public int mSeconds = 0;
    public Board b2;
    public JLabel banner;
    private JLabel menu;
    private JFrame game = buildFrame();
    JLabel LComboStreak = new JLabel();
    JLabel RComboStreak = new JLabel();
    JLabel Timer = new JLabel();
    private int DECREASE_TIME_INTERVAL = 100;//milliseconds
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
        game.addComponentListener(new ComponentListener(){
            @Override
            public void componentResized(ComponentEvent e) {
             //System.out.println(game.getContentPane().getHeight());
             //System.out.println(game.getContentPane().getWidth());
                HEIGHT = game.getContentPane().getHeight();
                WIDTH = game.getContentPane().getWidth();
                /*
               if(WIDTH < previousW)
                  WIDTH += (previousW - WIDTH);
               else if (WIDTH > previousW)
                  WIDTH -= (WIDTH - previousW);
                */
                WIDTH = previousW;

               if(HEIGHT < previousH)
                   WIDTH -= (previousH - HEIGHT);
               else if(HEIGHT > previousH)
                   WIDTH += (HEIGHT - previousH);
                //WIDTH =  (int)(HEIGHT*RATIO);
               
                BANNER_WIDTH = WIDTH/6;
                if(b1 != null)
                    b1.setBounds(0,0,(WIDTH/2)-(BANNER_WIDTH/2),HEIGHT);
                if(b2 != null)
                    b2.setBounds(b1.WIDTH + BANNER_WIDTH,0,(WIDTH/2)-(BANNER_WIDTH/2),HEIGHT);
                Board.resizeBoard();
               
                if(banner != null)
                    banner.setBounds(b1.WIDTH, 0, WIDTH - b1.WIDTH - b2.WIDTH, b1.HEIGHT);
                if(LComboStreak != null)
                {
                    LComboStreak.setBounds(b2.WIDTH, b1.HEIGHT / 2, 
                            (int)LComboStreak.getPreferredSize().getWidth(), (int)LComboStreak.getPreferredSize().getHeight());
                }
                if(RComboStreak != null)
                {
                    RComboStreak.setBounds(b2.WIDTH, b1.HEIGHT / 2 + LComboStreak.getHeight(), 
                            (int)RComboStreak.getPreferredSize().getWidth(), (int)RComboStreak.getPreferredSize().getHeight());
                }
                if(Timer != null)
                {
                Timer.setBounds(b1.WIDTH, HEIGHT / 4,
                    (int)Timer.getPreferredSize().getWidth(), (int)Timer.getPreferredSize().getHeight());
                }
               //game.setSize(WIDTH, HEIGHT);
               //previousH = HEIGHT;
               //previousW = WIDTH;
             
            }

            @Override
            public void componentMoved(ComponentEvent e) {
               
            }

            @Override
            public void componentShown(ComponentEvent e) {
              
            }

            @Override
            public void componentHidden(ComponentEvent e) {
               
            }
        });
    }
    public JFrame buildFrame() {
        JFrame window = new JFrame("Cube Attack");
        window.setLayout(null);
        window.getContentPane().setPreferredSize(new Dimension( WIDTH, HEIGHT));
        //window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        return window;
    }
    public void startGame1(){
        gameState = 1;
        game.remove(menu);
        b1 = new Board(false, this);
        b2 = new Board(true, this);
        //SetupDisplay() sets the boards and banners accordingly
        SetupDisplay();
        decreaseTime();
        
    }
    public void startGame2(){
        gameState = 2;
        game.remove(menu);
        b1 = new Board(false, this);
        b2 = new Board(false, this);
        //SetupDisplay() sets the boards and banners accordingly
        SetupDisplay();
        decreaseTime();
        
    }

    private void decreaseTime() {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(mSeconds < 10)
                    mSeconds ++;
                else
                {
                    mSeconds = 0;
                    if(seconds < 59)
                        seconds++;
                    else
                    {
                        seconds = 0;
                        minutes ++;
                    }
                }
                Timer.setText("Timer: " + minutes + " \' " + seconds + " \"");
                Timer.setSize(Timer.getPreferredSize());
                LComboStreak.setText("Left :  \n" + String.valueOf(b1.comboStreak));
                RComboStreak.setText("Right:  \n" + String.valueOf(b2.comboStreak));
                //repaint();
            }
        }, 0, DECREASE_TIME_INTERVAL, TimeUnit.MILLISECONDS);
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
                    if(gameState == 1)
                    b1.swapTargets();
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
        b1.setBounds(0,0,b1.WIDTH,b1.HEIGHT);
        b2.setBounds(b2.WIDTH + BANNER_WIDTH,0,b2.WIDTH,b2.HEIGHT);
        banner = new JLabel();
        // Displays Timer
        //Timer.setText(minutes + " \' " + seconds + " \"");
        Timer.setBounds(b1.WIDTH, this.HEIGHT / 4,
                (int)Timer.getPreferredSize().getWidth(), (int)Timer.getPreferredSize().getHeight());

        //Displays ComboBoxes
        LComboStreak.setText("Left :  \n" + String.valueOf(b1.comboStreak));
        LComboStreak.setBounds(b1.WIDTH, b1.HEIGHT / 2, 
                (int)LComboStreak.getPreferredSize().getWidth(), (int)LComboStreak.getPreferredSize().getHeight());
        RComboStreak.setText("Right :  \n" + String.valueOf(b1.comboStreak));
        RComboStreak.setBounds(b1.WIDTH, b1.HEIGHT / 2 + LComboStreak.getHeight(), 
                (int)RComboStreak.getPreferredSize().getWidth(), (int)RComboStreak.getPreferredSize().getHeight());

        banner.setBounds(b1.WIDTH, 0, this.WIDTH - b1.WIDTH - b2.WIDTH, b1.HEIGHT);
        banner.setIcon(new ImageIcon(getClass().getResource("BANNER.png")));
        // If you want text to appear above banner, (..).add() the text before
        // the Banner/picture
        game.getContentPane().add(b1);
        game.getContentPane().add(b2);
        game.getContentPane().add(Timer);
        game.getContentPane().add(LComboStreak);
        game.getContentPane().add(RComboStreak);
        game.getContentPane().add(banner);
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
