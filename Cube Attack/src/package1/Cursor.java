
package package1;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Cursor {
    private int target1x;
    private int target1y;
    private int target2x;
    private int target2y;
    private ImageIcon imageIcon;
    private Image image;
    public Cursor(){
        imageIcon = new ImageIcon("src/resources/cursor.png");
        image = imageIcon.getImage();
    }
    public int getTarget1x(){
        return target1x;
    }
    public int getTarget2x(){
        return target2x;
    }
    public int getTarget1y(){
        return target1y;
    }
    public int getTarget2y(){
        return target2y;
    }
    public void swapTargets(){
        Block temp = Board.levelArray[target1x][target1y];
        Board.levelArray[target1x][target1y] = Board.levelArray[target2x][target2y];
        Board.levelArray[target2x][target2y] = temp;
    }
    
}
