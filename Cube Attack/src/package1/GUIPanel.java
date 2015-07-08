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
    public Board b1;
    public Board b2;
    public JLabel banner;
    public GUIPanel() 
    {
        JFrame game = buildFrame();
        setFocusable(true);
        game.addKeyListener(new TAdapter());
        b1 = new Board();
        b1.setBounds(0,0,b1.WIDTH,b1.HEIGHT);
        b2 = new Board();
        b2.setBounds(b2.WIDTH + 200,0,b2.WIDTH,b2.HEIGHT);
        banner = new JLabel();
        banner.setBounds(b1.WIDTH, 0, WIDTH-b1.WIDTH-b2.WIDTH,b1.HEIGHT);
        banner.setIcon(new ImageIcon("src/resources/BANNER.png"));
        System.out.println(banner.getBounds());
        game.getContentPane().add(banner);
        game.getContentPane().add(b1);
        game.getContentPane().add(b2);
        game.setVisible(true);
    }
    public JFrame buildFrame() {
        JFrame window = new JFrame("Cube Attack");
        window.setLayout(null);
        
       // System.out.println(window.getLayout());
        window.setPreferredSize(new Dimension( WIDTH, HEIGHT));
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //window.setLocationRelativeTo(null);
        
        window.pack();
        //window.setVisible(true);
        return window;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
 private class TAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            
        }

        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            switch(key){
                case KeyEvent.VK_ENTER:
                    b1.moveUp();
                    b2.moveUp();
                    break;
                case KeyEvent.VK_SPACE:
                    b2.swapTargets();
                    b2.adjacencyCheck( b2.levelCursor.getCursorx(),  b2.levelCursor.getCursory());
                    b2.adjacencyCheck( b2.levelCursor.getCursor2x(),  b2.levelCursor.getCursory());
                    break;
                case KeyEvent.VK_UP:
                     b2.levelCursor.moveUp();
                    break;
                case KeyEvent.VK_DOWN:
                     b2.levelCursor.moveDown();
                    break;
                case KeyEvent.VK_LEFT:
                     b2.levelCursor.moveLeft();
                    break;
                case KeyEvent.VK_RIGHT:
                     b2.levelCursor.moveRight();
                    break;
            }
            
        }
    }
}
