package package1;

import java.applet.*;
import java.awt.*;
import javax.swing.*;

public class Board extends Applet {

    public static final int WIDTH = 400, HEIGHT = 800;
    /* SIZE OF THE GRID */
    public static final int MAX_X = 8, MAX_Y = 16;
    public static Block[][] levelArray = new Block[MAX_X][MAX_Y];

    public Board() {
        resetArray();
        generateRow();
        JFrame frame = buildFrame();
    }

    public JFrame buildFrame() {
        JFrame window = new JFrame("Cube Attack");
        window.setSize(new Dimension(WIDTH, HEIGHT));
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

    public void resetArray() {
        for (int x = 0; x < levelArray.length; x++) {
            for (int y = 0; y < levelArray[0].length; y++) {
                levelArray[x][y] = new Block("EMPTY");
            }
        }

    }

    public static void setArray(int x, int y, Block a) {
        levelArray[x][y] = a;
    }

    public static void moveUp() {
        for (int x = 0; x < levelArray.length; x++) {
            for (int y = 1; y < levelArray[0].length; y++) {
                levelArray[x][y - 1] = levelArray[x][y];
            }
        }
        generateRow();
    }

    public static void generateRow() {
        /* This Checks for three in a row when you generate a new row.
         * Looks weird, just checks the last to indicies and if the macth, if they
         * do, it doesn't allow for a block with the same color to be created
         */
        for (int x = 0; x < MAX_X; x++) {
            if (x >= 2 && (levelArray[x - 1][MAX_Y - 1].color
              == levelArray[x - 2][MAX_Y - 1].color)) {
                while ((levelArray[x][MAX_Y - 1] = new Block()).color
                  == levelArray[x - 1][MAX_Y - 1].color) {
                    levelArray[x][MAX_Y - 1] = null;
                    levelArray[x][MAX_Y - 1] = new Block();
                }
            } else {
                levelArray[x][MAX_Y - 1] = new Block();
            }
        }
    }


		public static void fallingBlocks()
		{
			for(int x = MAX_X - 1; x >= 0; x--)
			{
				for(int y = MAX_Y - 2; x >= 0; y--)
				{
					if(levelArray[x][y].color == "EMPTY")
					{
						;
					}
				}
			}
		}
}
