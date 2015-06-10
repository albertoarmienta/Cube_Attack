
package package1;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Cursor {
    private int target1x;
    private int targety;
    private int target2x;
    private ImageIcon imageIcon;
    private Image image;
    public Cursor(){
        imageIcon = new ImageIcon("src/resources/cursor.png");
        image = imageIcon.getImage();
        target1x = 0;
        target2x = 1;
        targety = 0;
    }
    public int getTarget1x(){
        return target1x;
    }
    public int getTarget2x(){
        return target2x;
    }
    public int getTargety(){
        return targety;
    }
    public void moveLeft(){
        if(target1x>0){
            target1x--;
            target2x--;
        }
    }
    public void moveRight(){
        if(target2x<Board.MAX_X-1){
            target1x++;
            target2x++;
        }
    }
    public void moveUp(){
        if(targety>0){
            targety--;
        }
    }
    public void moveDown(){
        if(targety<Board.MAX_Y-1){
            targety++;
        }
    }
    public void swapTargets(){
        Block temp = Board.levelArray[target1x][targety];
        Board.levelArray[target1x][targety] = Board.levelArray[target2x][targety];
        Board.levelArray[target2x][targety] = temp;
    }
    public Image getImage(){
        return image;
    }
    
}
