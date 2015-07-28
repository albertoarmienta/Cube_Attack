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
            weight = calculateYWeight(board.levelCursor.getCursory(), this.y);
                    
            //weight = 0;
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
           for(int j = 0; j < board.MAX_Y - 1 /*&& j < (start_y + 3)*/; j++)
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
       System.out.println(board.levelCursor.getCursory());
       Vector<Vector<Vertex>> results = new Vector<>();
       results.add(green);
       results.add(black);
       results.add(white);
       results.add(blue);
       results.add(red);
       
       for(int i = 0; i < results.size(); i++)
       {
           for(int j = 0; j < results.get(i).size(); j++)
           {
               if((temp = match3Horizontal(results.get(i))) != null)
               {
                   if(Math.abs(temp.weight) < leastWeight)
                   {
                       leastWeight = Math.abs(temp.weight);
                       bestMatch = temp;
                   }
               }
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
        //Vertex [] temp = new Vertex[3];
        if(array.size() < 3)
            return null;
        Vector<Vertex> temp = insertionSort(array);
        boolean matchFound = false;
        Vertex start = null, middle = null, end = null;
        int index = 10000, w = 1000;
        /*
        for(int i = 0; i < array.size(); i++ )
        System.out.print(board.levelArray[array.get(i).x][array.get(i).y].color+ " ");
        System.out.println();
        */
        for(int i = 0; i < temp.size(); i++ )
        {
            if(i + 2 < temp.size() && temp.get(i).y == temp.get(i + 2).y )
            {
                if(Math.abs(temp.get(i).weight) < w)
                {
                    w = Math.abs(temp.get(i).weight);
                    index = i;
                    matchFound = true;
                }
                //we know we have 3 in a horizontalRow
                //System.out.println("[" + start.x + ","+ start.y + "] [" +middle.x + ","+ middle.y + "] ["+ end.x+"," + end.y+ "]");
            }
        }
        if(!matchFound || index == 10000)
            return null;
        else
        {
            start = temp.get(index);
            middle = temp.get(index+1);
            end = temp.get(index + 2);
            if( (start.x + 1) == middle.x)
            {
                end.whichCursor = 'R';
                if(board.levelCursor.getCursor2x() == end.x && board.levelCursor.getCursory() == end.y)
                    end.switchBlocks = true;
                end.weight = calculateYWeight(end);
                return end;
            }
            else if ((end.x - 1) == middle.x)
            {
                System.out.println("end:middle");
                start.whichCursor = 'L';
                if(board.levelCursor.getCursorx() == start.x && board.levelCursor.getCursory() == start.y)
                    start.switchBlocks = true;
                start.weight = calculateYWeight(start);
                return start;
            }
            else if(middle.x < (end.x - 1) && middle.x != (end.x - 1))
            {
                end.whichCursor = 'R';
                if(board.levelCursor.getCursor2x() == end.x && board.levelCursor.getCursory() == end.y)
                    end.switchBlocks = true;
                end.weight = calculateYWeight(end);
                return end;
            }
            else if(middle.x > (start.x + 1) && middle.x != (start.x + 1 ))
            {
                start.whichCursor = 'L';
                if(board.levelCursor.getCursorx() == start.x && board.levelCursor.getCursory() == start.y)
                    start.switchBlocks = true;
                start.weight = calculateYWeight(start);
                return start;
            }
            else return null;//return middle;
        }
        
        //if(Math.abs(start.x - middle.x) != 1)
        // We have a horizontal match, now find best path to get rid of blockes?
        // calculate the number of steps until they are lined up!!, then the AI
        // will make steps based the least number of steps
        // take into account disance from cursor?
        
    }
    
    //this one sorted by Y position
    /*
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
    
    //this one sorts by Y weight
    */
    public Vector<Vertex> insertionSort(Vector<Vertex> array)
    {
        for(int i = 1; i < array.size(); i++)
        {
            Vertex temp = array.get(i);
            int j;
            for(j = i - 1; j >= 0 && temp.weight < array.get(j).weight; j -- )
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
        else if(dest.whichCursor == 'R')
            deltaX = board.levelCursor.getCursor2x() - dest.x;
        else return;
        
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
    
    public int calculateYWeight(Vertex ver)
    {
        return (board.levelCursor.getCursory() - ver.y);
    }
    public int calculateYWeight(int cursorY, int blockY)
    {
        return cursorY - blockY;
    }
    public int calculateWeight(Vertex ver)
    {
        int x = Math.abs(board.levelCursor.getCursorx() - ver.x);
        int y = Math.abs(board.levelCursor.getCursory() - ver.y);
        return x + y;
    }
    
}