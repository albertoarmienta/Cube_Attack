
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
    public static final int WIDTH = 400 , HEIGHT = 800;
    public static Block[][] levelArray = new Block[8][16];
    
     public Board(){
        resetArray();
        JFrame frame = buildFrame(); 
     }
    public JFrame buildFrame(){
        JFrame window = new JFrame("Cube Attack");
        window.setSize(new Dimension (WIDTH,HEIGHT));
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        //window.setLocationRelativeTo(null);
        GUIPanel panel = new GUIPanel();      
        window.getContentPane().add(panel);
        //window.getContentPane().add(label,BorderLayout.CENTER);
        window.pack();
        window.setVisible(true);
        return window;
    }
    public void resetArray(){
        for(int i = 0; i<levelArray.length;i++)
            for(int j=0; j<levelArray[0].length;j++)
                levelArray[i][j] = new Block("EMPTY");
            
    }
}
