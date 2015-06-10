
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
    public int getTarget1x(){
        return cursor_x_left;
    }
    public int getTarget2x(){
        return cursor_x_right;
    }
    public int getTargety(){
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
    public void swapTargets(){
        Block temp = Board.levelArray[cursor_x_left][cursor_y];
        Board.levelArray[cursor_x_left][cursor_y] = Board.levelArray[cursor_x_right][cursor_y];
        Board.levelArray[cursor_x_right][cursor_y] = temp;

				/*Count is one because you only moved horizontal*/
				/*if(Board.levelArray[cursor_x_right][cursor_y].color == "EMPTY" )
					Board.fallingBlocks(cursor_x_right, cursor_y, 1);
				else if(Board.levelArray[cursor_x_left][cursor_y].color == "EMPTY" )
					Board.fallingBlocks(cursor_x_left, cursor_y, 1);

				if(Board.levelArray[cursor_x_right][cursor_y + 1].color == "EMPTY")
				{
					System.out.println("tits right");
					Board.fallingBlocks(cursor_x_right, cursor_y, 1);
				}
				else if(Board.levelArray[cursor_x_left][cursor_y + 1].color == "EMPTY")
				{
					System.out.println("tits left");
					Board.fallingBlocks(cursor_x_left, cursor_y, 1);
				}
				*/
    }
    public Image getImage(){
        return image;
    }
    
}
