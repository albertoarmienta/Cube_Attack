
package package1;
import java.applet.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Board extends Applet {
   
    public Board() throws IOException
    {
        Block[][] blockArray = new Block[5][5];
        JFrame window = buildFrame();
       
        
            final BufferedImage image = ImageIO.read(new File("src\\resources\\block.png"));
            JPanel pane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };
            window.add(pane);
            
        
        
        
        
        
        
        
        
    }
    public JFrame buildFrame(){
        JFrame window = new JFrame("Cube Attack");
        JLabel label = new JLabel("BERTOS GAY",SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(300,100)); 
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        window.setPreferredSize(new Dimension (500,750));
        window.getContentPane().add(label,BorderLayout.CENTER);
        window.setLocationRelativeTo(null);
        window.pack();
        window.setVisible(true);
        return window;
    }
}
