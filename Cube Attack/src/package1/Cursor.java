
package package1;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Cursor {
    private int cursor_x_left;
    private int cursor_x_right;
    private int cursor_y;
    private ImageIcon imageIcon;
    private Image image;
    public Cursor(){
        imageIcon = new ImageIcon("src/resources/cursor.png");
        image = imageIcon.getImage();
        cursor_x_left = 0;
        cursor_x_right = 1;
        cursor_y = Board.MAX_Y - 1;
    }
    public int getCursorx(){
        return cursor_x_left;
    }
    public int getCursor2x(){
        return cursor_x_right;
    }
    public int getCursory(){
        return cursor_y;
    }
    public void moveLeft(){
        if(cursor_x_left>0){
            cursor_x_left--;
            cursor_x_right--;
        }
    }
    public void moveRight(){
        if(cursor_x_right<Board.MAX_X-1){
            cursor_x_left++;
            cursor_x_right++;
        }
    }
    public void moveUp(){
        if(cursor_y>0){
            cursor_y--;
        }
    }
    public void moveDown(){
        if(cursor_y<Board.MAX_Y-1){
            cursor_y++;
        }
    }
    public Image getImage(){
        return image;
    }
    
}
