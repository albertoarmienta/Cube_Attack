package package1;

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
//REVERSION

class GUIPanel extends Applet implements ActionListener{
    public final int WIDTH = 1005, HEIGHT = 829;
    public int gameState = 0;
    public Board b1;
    public Board b2;
    public JLabel banner;
    private JLabel menu;
    private JFrame game = buildFrame();
    public GUIPanel() 
    {
        menu = new JLabel();
        menu.setBounds(0,0,WIDTH,HEIGHT);
        menu.setIcon(new ImageIcon("src/resources/MENU.png"));
        game.getContentPane().add(menu);
        setFocusable(true);
        game.addKeyListener(new TAdapter());
        game.setVisible(true);
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
    public void startGame(){
        game.remove(menu);
        b1 = new Board();
        b1.setBounds(0,0,b1.WIDTH,b1.HEIGHT);
        b2 = new Board();
        b2.setBounds(b2.WIDTH + 200,0,b2.WIDTH,b2.HEIGHT);
        banner = new JLabel();
        banner.setBounds(b1.WIDTH, 0, WIDTH-b1.WIDTH-b2.WIDTH,b1.HEIGHT);
        banner.setIcon(new ImageIcon("src/resources/BANNER.png"));
        game.getContentPane().add(banner);
        game.getContentPane().add(b1);
        game.getContentPane().add(b2);
        game.repaint();
        
    }

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
                case KeyEvent.VK_ENTER:
                    if(gameState == 1){
                    b1.moveUp();
                    b2.moveUp();
                    }
                    else if(gameState==0)
                    {
                        startGame();
                        gameState = 1;
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if(gameState == 1){
                    b2.swapTargets();
                    b2.adjacencyCheck( b2.levelCursor.getCursorx(),  b2.levelCursor.getCursory());
                    b2.adjacencyCheck( b2.levelCursor.getCursor2x(),  b2.levelCursor.getCursory());
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(gameState == 1){
                     b2.levelCursor.moveUp();
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(gameState == 1){
                     b2.levelCursor.moveDown();
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if(gameState == 1){
                     b2.levelCursor.moveLeft();
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(gameState == 1){
                     b2.levelCursor.moveRight();
                    }
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
