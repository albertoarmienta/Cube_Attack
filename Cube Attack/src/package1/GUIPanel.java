package package1;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
//REVERSION

class GUIPanel extends Applet implements ActionListener{
    public static final int WIDTH = 1005, HEIGHT = 829;
    public int gameState = 0;
    public Board b1;
    public int seconds = 0;
    public int mSeconds = 0;
    public Board b2;
    public JLabel banner;
    private JLabel menu;
    private JFrame game = buildFrame();
    JLabel ComboStreakDisplay = new JLabel();
    public GUIPanel() 
    {
        menu = new JLabel();
        menu.setBounds(0,0,WIDTH,HEIGHT);
        menu.setIcon(new ImageIcon(getClass().getResource("MENU.png")));
        game.getContentPane().add(menu);
        setFocusable(true);
        game.addKeyListener(new TAdapter());
        game.setVisible(true);
        game.setLocationRelativeTo(null);
    }
    public JFrame buildFrame() {
        JFrame window = new JFrame("Cube Attack");
        window.setLayout(null);
        window.setPreferredSize(new Dimension( WIDTH, HEIGHT));
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        return window;
    }
    public void startGame1(){
        gameState = 1;
        game.remove(menu);
        b1 = new Board(false);
        //b1.decreaseTime();
        b1.setBounds(0,0,b1.WIDTH,b1.HEIGHT);
        b2 = new Board(true);
        //b2.decreaseTime();
        b2.setBounds(b2.WIDTH + 200,0,b2.WIDTH,b2.HEIGHT);
        banner = new JLabel();
        banner.setBounds(b1.WIDTH, 0, WIDTH-b1.WIDTH-b2.WIDTH,b1.HEIGHT);
        //banner.setIcon(new ImageIcon(getClass().getResource("BANNER.png")));
        game.getContentPane().add(banner);
        game.getContentPane().add(b1);
        game.getContentPane().add(b2);
        ComboStreakDisplay.setBounds(game.getWidth() / 2, game.getHeight() / 2, 50 , 50);
        ComboStreakDisplay.setText(String.valueOf(b1.comboStreak));
        game.getContentPane().add(ComboStreakDisplay);
        game.repaint();
        decreaseTime();
        
    }
    public void startGame2(){
        gameState = 2;
        game.remove(menu);
        b1 = new Board(false);
        //b1.decreaseTime();
        b1.setBounds(0,0,b1.WIDTH,b1.HEIGHT);
        b2 = new Board(false);
        //b2.decreaseTime();
        b2.setBounds(b2.WIDTH + 200,0,b2.WIDTH,b2.HEIGHT);
        banner = new JLabel();
        banner.setBounds(b1.WIDTH, 0, WIDTH-b1.WIDTH-b2.WIDTH,b1.HEIGHT);
        //banner.setIcon(new ImageIcon("src/resources/BANNER.png"));
        game.getContentPane().add(banner);
        game.getContentPane().add(b1);
        game.getContentPane().add(b2);
        ComboStreakDisplay.setBounds(game.getWidth() / 2, game.getHeight() / 2, 50 , 50);
        //game.getContentPane().add(ComboStreakDisplay);
        ComboStreakDisplay.setText(String.valueOf(b1.comboStreak));
        game.getContentPane().add(ComboStreakDisplay);
        game.repaint();
        decreaseTime();
        
    }

    private void decreaseTime() {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                ComboStreakDisplay.setText(String.valueOf(b1.comboStreak));
                //System.out.println("fucskfjasd;lfkjas;d");
                repaint();
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    /*
    @Override
     public void paintComponent(Graphics g) {
        //Whatever is painted last appears on top of everything else
        super.paintComponent(g);
        //g.drawString(String.format("%d:%d",seconds, mSeconds), b2.getX() - (banner.getX()/2), HEIGHT / 2);
        g.setColor(Color.BLUE);
        g.drawString(String.format("%d:%d",seconds, mSeconds), WIDTH / 2, HEIGHT / 2);
        g.drawRect(WIDTH/2, HEIGHT/2, 200, 200);
        
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
                        //game.dispose();
                        //gameState = 0;
                    }
                    break;
            }
            
        }
    }
}
