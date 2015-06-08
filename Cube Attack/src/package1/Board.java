
package package1;
import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.*;
import javax.swing.*;

public class Board extends Applet {
   
    JFrame frame = buildFrame();
     private BufferedImage img;

     public void paintComponent(Graphics g) {
      //super.paintComponent(g);
      //g.drawImage(Block.image.getImage(), 0, 250, this);
     }
     
    public JFrame buildFrame(){
        JFrame window = new JFrame("Cube Attack");
        window.setPreferredSize(new Dimension (500,500));
        //window.setLayout(new GridLayout());
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        //window.setLocation(250,250);
        //window.setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout());
       // panel.setPreferredSize(new Dimension(5,5));
       // panel.setLocation(0, 0);
        panel.setBackground(Color.blue);
        
        JLabel label = new JLabel();
        label.setIcon(Block.image);
        label.setPreferredSize(new Dimension(50,50));
        
        panel.add(label);
        
        window.getContentPane().add(panel);
        
        
        //window.getContentPane().add(label,BorderLayout.CENTER);
        
        window.pack();
        window.setVisible(true);
        return window;
    }
}
