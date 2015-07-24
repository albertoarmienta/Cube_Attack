/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package package1;

import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author beartoes
 */
public class EnemyAI 
{
    private Block[][] levelArray; 
    private Board board;

    //used for which cursor square to the block
    enum cursorPos{LEFT, RIGHT, UNKNOWN}

    EnemyAI(Block[][] array, Board b)
    {
        levelArray = array;
        board = b;
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new AIThread(), 0, 500, TimeUnit.MILLISECONDS);
    }

    public class AIThread implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                searchForMatch();
            }
            catch(Throwable e)
            {
                System.out.println(e);
            }
        }

    }

    public class Vertex
    {
        public int x;
        public int y;
        // this descides whether to place on left or right cursor
        public char whichCursor;
        // this decides whether to switch blocks
        public boolean switchBlocks;
        // the distance from the current block to the cursor
        public int weight;
        Vertex(int a, int b)
        {
            x = a;
            y = b;
            whichCursor = 'U';
            switchBlocks = false;
            weight = 0;
        }
        //resets swtich blocks and cursor cause only valid for one move
        public void reset()
        {
            whichCursor = 'U';
            switchBlocks = false;
            weight = 0;
        }
    }

    private void searchForMatch()
    {
        //inverse 0 get get the largest possible 32 bit number
       int leastWeight = 1000000;
       Vertex temp;
       Vertex bestMatch = null;
       int start_x = board.levelCursor.getCursorx() - 1; 
       int start_y = board.levelCursor.getCursory() - 2;
       if(start_x < 0)
           start_x = 0;
       if(start_y < 0)
           start_y = 0;

       Vector<Vertex> blue  = new Vector<>();
       Vector<Vertex> red   = new Vector<>();
       Vector<Vertex> green = new Vector<>();
       Vector<Vertex> black = new Vector<>();
       Vector<Vertex> white = new Vector<>();

       for(int i = 0; i < board.MAX_X /*&& i < (start_x + 3)*/; i++)
       {
           for(int j = 0; j < board.MAX_Y /*&& j < (start_y + 3)*/; j++)
           {
               switch(levelArray[i][j].color)
               {
                   case "RED":
                       red.add(new Vertex(i,j));
                       break;
                   case "BLUE":
                       blue.add(new Vertex(i,j));
                       break;
                   case "GREEN":
                       green.add(new Vertex(i,j));
                       break;
                   case "WHITE":
                       white.add(new Vertex(i,j));
                       break;
                   case "BLACK":
                       black.add(new Vertex(i,j));
                       break;
               }
           }
       }
       Vector<Vector<Vertex>> results = new Vector<>();
       results.add(blue);
       results.add(red);
       results.add(green);
       results.add(black);
       results.add(white);
       
       for(int i = 0; i < results.size(); i++)
       {
           if((temp = match3Horizontal(results.get(i))) != null)
           {
                if(temp.weight < leastWeight)
                    bestMatch = temp;
           }
       }
       if(bestMatch != null)
           moveCursorTo(bestMatch);
       /*
       Vertex moveTo = match3Horizontal(black);
       if(moveTo != null)
           moveCursorTo(moveTo);
               */
    }
    
    public Vertex match3Horizontal(Vector<Vertex> array)
    {
        Vertex [] temp = new Vertex[3];
        if(array.size() < 3)
            return null;
        insertionSort(array);
        boolean matchFound = false;
        Vertex start = null, middle = null, end = null;
          for(int i = 0; i < array.size(); i++ )
              System.out.print(board.levelArray[array.get(i).x][array.get(i).y].color+ " ");
              System.out.println();
        for(int i = 0; i < array.size(); i++ )
        {
            if(i + 2 < array.size() && array.get(i).y == array.get(i + 2).y )
            {
                //we know we have 3 in a horizontalRow
                start = array.get(i);
                middle = array.get(i+1);
                end = array.get(i + 2);
                //System.out.println("[" + start.x + ","+ start.y + "] [" +middle.x + ","+ middle.y + "] ["+ end.x+"," + end.y+ "]");
                matchFound = true;
                break;
            }
        }
        if(!matchFound)
            return null;
        else
        {
            if(middle.x < (end.x - 1))
            {
                end.whichCursor = 'R';
                if(board.levelCursor.getCursory() == end.y)
                    end.switchBlocks = true;
                end.weight = calculateWeight(end);
                return end;
            }
            else if(middle.x > (start.x + 1))
            {
                start.whichCursor = 'L';
                if(board.levelCursor.getCursory() == start.y)
                    start.switchBlocks = true;
                start.weight = calculateWeight(start);
                return start;
            }
            else return middle;
        }
        
        //if(Math.abs(start.x - middle.x) != 1)
        // We have a horizontal match, now find best path to get rid of blockes?
        // calculate the number of steps until they are lined up!!, then the AI
        // will make steps based the least number of steps
        // take into account disance from cursor?
        
    }

    public Vector<Vertex> insertionSort(Vector<Vertex> array)
    {
        for(int i = 1; i < array.size(); i++)
        {
            Vertex temp = array.get(i);
            int j;
            for(j = i - 1; j >= 0 && temp.y < array.get(j).y; j -- )
                array.set(j + 1, array.get(j));// = array.get(j);
            array.set(j+1, temp);
        }

        return array;
    }

    void moveCursorTo(Vertex dest)
    {
        int deltaX;
        if(dest.whichCursor == 'L')
            deltaX = board.levelCursor.getCursorx() - dest.x;
        else
            deltaX = board.levelCursor.getCursor2x() - dest.x;

        int deltaY = board.levelCursor.getCursory() - dest.y;

        if(deltaX < 0)
            board.levelCursor.moveRight();
        else if (deltaX > 0)
            board.levelCursor.moveLeft();

        if(deltaY < 0)
            board.levelCursor.moveDown();
        else if (deltaY > 0)
            board.levelCursor.moveUp();

        if(dest.switchBlocks== true)
            board.swapTargets();
        dest.reset();


    }

    public int calculateWeight(Vertex ver)
    {
        int x = Math.abs(board.levelCursor.getCursorx() - ver.x);
        int y = Math.abs(board.levelCursor.getCursory() - ver.y);
        return x + y;
    }

}