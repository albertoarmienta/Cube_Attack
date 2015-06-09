
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
        setArray(4,15,new Block("BLUE"));
        setArray(3,15,new Block("RED"));
        setArray(2,15,new Block("BLACK"));
        setArray(1,14,new Block("WHITE"));
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
        for(int x = 0; x<levelArray.length;x++)
            for(int y=0; y<levelArray[0].length;y++)
                levelArray[x][y] = new Block("EMPTY");
            
    }
    public static void setArray(int x,int y, Block a){
        levelArray[x][y] = a;
    }
    public static void moveUp(){
        for(int x = 0;x<levelArray.length;x++)
            for(int y=1; y<levelArray[0].length;y++){
                levelArray[x][y-1] = levelArray[x][y];
            }
    }
}
