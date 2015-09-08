/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package package1;

import java.util.Collections;
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
        exec.scheduleAtFixedRate(new AIThread(), 0, 200, TimeUnit.MILLISECONDS);
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
                System.out.println("AI thread:" + e);
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
       Vector<Vector<Vertex>> results = new Vector<>();
       results.add(green);
       results.add(black);
       results.add(white);
       results.add(blue);
       results.add(red);
       String whichMatchTaken = "NONE";
       for(int i = 0; i < results.size(); i++)
       {
           for(int j = 0; j < results.get(i).size(); j++)
           {
               if((temp = match3Horizontal(results.get(i))) != null)
               {
                   if(Math.abs(temp.weight) < leastWeight)
                   {
                       leastWeight = Math.abs(temp.weight);
                       //System.out.println(temp.x + ", " + temp.y + " : " + temp.weight);
                       bestMatch = temp;
                       whichMatchTaken = "Horizontal";
                   }
               }
               if((temp = matchVertical(results.get(i))) != null)
               {
                   if(Math.abs(temp.weight) < leastWeight)
                   {
                       leastWeight = Math.abs(temp.weight);
                       //System.out.println(temp.x + ", " + temp.y + " : " + temp.weight);
                       bestMatch = temp;
                       whichMatchTaken = "Vertical";
                   }
               }

           }
       }
       
       System.out.println("MATCH TAKEN: " + whichMatchTaken);
       if(bestMatch != null)
       {
           moveCursorTo(bestMatch);
       }
    }
    
    public Vertex match3Horizontal(Vector<Vertex> array)
    {
        if(array.size() < 3)
            return null;
        Vector<Vertex> temp = insertionSort(array);
        boolean horizontalMatch = false, verticalMatch = false;
        Vertex start = null, middle = null, end = null;
        int hIndex = 10000, vIndex = 1000, horizontalWeight = 1000, verticalWeight = 1000;
        for(int i = 0; i < temp.size(); i++ )
        {
            if(i + 2 < temp.size() && temp.get(i).y == temp.get(i + 2).y )
            {
                //we know we have 3 in a horizontalRow
                if(Math.abs(temp.get(i).weight) < horizontalWeight)
                {
                    horizontalWeight = Math.abs(temp.get(i).weight);
                    hIndex = i;
                    horizontalMatch = true;
                }
            }
        }
        if(!horizontalMatch && !verticalMatch)//|| hIndex == 10000)
            return null;
        else if(horizontalMatch && horizontalWeight < verticalWeight)
        {
            start = temp.get(hIndex);
            middle = temp.get(hIndex+1);
            end = temp.get(hIndex + 2);
            if( (start.x + 1) == middle.x)
            {
                end.whichCursor = 'R';
                if(board.levelCursor.getCursor2x() == end.x && board.levelCursor.getCursory() == end.y)
                    end.switchBlocks = true;
                end.weight = calculateWeight(end);
                return end;
            }
            else if ((end.x - 1) == middle.x)
            {
                start.whichCursor = 'L';
                if(board.levelCursor.getCursorx() == start.x && board.levelCursor.getCursory() == start.y)
                    start.switchBlocks = true;
                start.weight = calculateWeight(start);
                return start;
            }
            else if(middle.x < (end.x - 1) && middle.x != (end.x - 1))
            {
                end.whichCursor = 'R';
                if(board.levelCursor.getCursor2x() == end.x && board.levelCursor.getCursory() == end.y)
                    end.switchBlocks = true;
                end.weight = calculateWeight(end);
                return end;
            }
            else if(middle.x > (start.x + 1) && middle.x != (start.x + 1 ))
            {
                start.whichCursor = 'L';
                if(board.levelCursor.getCursorx() == start.x && board.levelCursor.getCursory() == start.y)
                    start.switchBlocks = true;
                start.weight = calculateWeight(start);
                return start;
            }
            else return null;//return middle;
        }
        
        else
            return null;
        
        //if(Math.abs(start.x - middle.x) != 1)
        // We have a horizontal match, now find best path to get rid of blockes?
        // calculate the number of steps until they are lined up!!, then the AI
        // will make steps based the least number of steps
        // take into account disance from cursor?
        
    }
    
    public Vertex matchVertical(Vector<Vertex> array)
    {
        if(array.size() < 3)
            return null;
        array = insertionYSort(array);
        Vector<Vector<Vertex>> results = new Vector<Vector<Vertex>>();
        Vector<Vertex> matches = new Vector<>();
        Vector<Vertex> bestMatch = new Vector<>();
        boolean newBestMatchFound = false;
        int numResults = 0;
        int numMatches = 0;
        int weight = 10000;
        int lowestResult = 0;
        //insert the first one
        matches.add(array.get(0));
        for(int i = 1; i < array.size(); i++)
        {
            //System.out.print("[" + array.get(i).x + ", " + array.get(i).y + "] weight:" + array.get(i).weight);
            //System.out.println(matches.get(numMatches).y + ", " + array.get(i).y);
            //if(array.get(i).y == matches.get(numMatches).y)
            //{
               //continue;
            //}
            if((matches.get(numMatches).y + 1) == (array.get(i).y ))
            {
                //System.out.println(matches.size());
                matches.add(array.get(i));
                numMatches ++;
                /*
                if(i == array.size() - 1 && numMatches > 2)
                {
                    results.add(matches);
                    numResults ++;
                }
                */
                if(array.get(i).weight < weight)
                {
                    lowestResult = numResults;
                }
            }
            else if(matches.get(numMatches).y + 1 != (array.get(i).y))//&& matches.size() < 3)
            {
                //        results.add(matches);
                //       numResults ++;
                //System.out.println(matches.size());//bestMatch.size());
                if(matches.size() < 3)
                {
                    matches.clear();
                    matches.add(array.get(i));
                    numMatches = 0;
                }
                else
                {
                    //if(Math.abs(matches.get(0).weight) < weight)
                    if(calculateWeight(matches.get(0)) < weight)
                    {
                        bestMatch.setSize(matches.size());
                        //bestMatch.addAll(0, matches);
                        Collections.copy(bestMatch, matches);
                        //System.out.println("BestMatch: " + bestMatch.size());//bestMatch.size());
                        matches.clear();
                        matches.add(array.get(i));
                        numMatches = 0;
                    }
                   
                }
                //System.out.println(matches.size());//bestMatch.size());
            }
        }
        //System.out.println("-----------------------------------------");
        Vector<Vertex> temp;
        int resultsIndex = 0;
        int tempWeight = 0;
        
        if(bestMatch.size() >=3)
            temp  = bestMatch;
        else return null;
        Vertex start = null, middle = null, end = null;
        start = temp.get(0);
        middle = temp.get(1);
        end = temp.get(2);
        ///System.out.println(start.x + "," + start.y);
        // the topmost and middle are stacked, so just move the bottom
        if( start.x == middle.x)
        {
            //end is to the left of the blocks
            if(end.x < middle.x)
            {
                end.whichCursor = 'L';
                if(board.levelCursor.getCursorx() == end.x && board.levelCursor.getCursory() == end.y)
                {
                    if(end.x + 1< board.MAX_X && board.levelArray[end.x][end.y].color == board.levelArray[end.x + 1][end.y].color)
                        end.x ++;
                    end.switchBlocks = true;
                }
                //end.switchBlocks = true;
                end.weight = calculateWeight(end);
            }
            else
            {
                end.whichCursor = 'R';
                if(board.levelCursor.getCursor2x() == end.x && board.levelCursor.getCursory() == end.y)
                {
                    if(end.x - 1 >= 0 && board.levelArray[end.x][end.y].color == board.levelArray[end.x - 1][end.y].color)
                        end.x --;
                    end.switchBlocks = true;
                }
                end.weight = calculateWeight(end);
                
            }
            //System.out.println("End: " + end.x + "," + end.y);
            return end;
        }
        else if(end.x == middle.x)
        {
            if(start.x < middle.x)
            {
                start.whichCursor = 'L';
                if(board.levelCursor.getCursorx() == start.x && board.levelCursor.getCursory() == start.y)
                {
                    if(start.x + 1< board.MAX_X && board.levelArray[start.x][start.y].color == board.levelArray[start.x + 1][start.y].color)
                        start.x ++;
                    start.switchBlocks = true;
                }
                start.weight = calculateWeight(start);
            }
            else
            {
                start.whichCursor = 'R';
                if(board.levelCursor.getCursor2x() == start.x && board.levelCursor.getCursory() == start.y)
                {
                    if(start.x - 1 >= 0 && board.levelArray[start.x][start.y].color == board.levelArray[start.x - 1][start.y].color)
                        start.x --;
                    start.switchBlocks = true;
                }
                start.weight = calculateWeight(start);
                
            }
            //System.out.println("start: " + start.x + "," + start.y);
            return start;
        }
        else if(end.x == start.x)
        {
            if(middle.x < start.x)
            {
                middle.whichCursor = 'L';
                if(board.levelCursor.getCursorx() == middle.x && board.levelCursor.getCursory() == middle.y)
                {
                    if(middle.x + 1< board.MAX_X && board.levelArray[middle.x][middle.y].color == board.levelArray[middle.x + 1][middle.y].color)
                        middle.x ++;
                    middle.switchBlocks = true;
                }
                //middle.switchBlocks = true;
                middle.weight = calculateWeight(middle);
                return middle;
            }
            else
            {
                middle.whichCursor = 'R';
                if(board.levelCursor.getCursor2x() == middle.x && board.levelCursor.getCursory() == middle.y)
                {
                    if(middle.x - 1 >= 0 && board.levelArray[middle.x][middle.y].color == board.levelArray[middle.x - 1][middle.y].color)
                        middle.x --;
                    middle.switchBlocks = true;
                }
                middle.weight = calculateWeight(middle);
                return middle;
            }
            
        }
        else
        {
            if(start.x < middle.x)
            {
                start.whichCursor = 'L';
                if(board.levelCursor.getCursorx() == start.x && board.levelCursor.getCursory() == start.y)
                {
                    if(start.x + 1< board.MAX_X && board.levelArray[start.x][start.y].color == board.levelArray[start.x + 1][start.y].color)
                        start.x ++;
                    start.switchBlocks = true;
                }
                //    start.switchBlocks = true;
                start.weight = calculateWeight(start);
            }
            else
            {
                start.whichCursor = 'R';
                if(board.levelCursor.getCursor2x() == start.x && board.levelCursor.getCursory() == start.y)
                {
                    if(start.x - 1 >= 0 && board.levelArray[start.x][start.y].color == board.levelArray[start.x - 1][start.y].color)
                        start.x --;
                    start.switchBlocks = true;
                }
                start.weight = calculateWeight(start);
                
            }
            //System.out.println("start: " + start.x + "," + start.y);
            return start;
        }
        
        
        //return null;
    }
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
    public Vector<Vertex> insertionYSort(Vector<Vertex> array)
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
        int x;
        /*
        if(ver.whichCursor == 'L')
        x = Math.abs(board.levelCursor.getCursorx() - ver.x);
        else //if(ver.whichCursor == 'R')
        x = Math.abs(board.levelCursor.getCursor2x() - ver.x);
        */
        if(ver.x == board.levelCursor.getCursorx() || ver.x == board.levelCursor.getCursor2x())
            x = 0;
        else if(ver.whichCursor == 'L')
            x = Math.abs(board.levelCursor.getCursorx() - ver.x);
        else if(ver.whichCursor == 'R')
            x = Math.abs(board.levelCursor.getCursor2x() - ver.x);
        else
            x = 1000;
        
        int y = Math.abs(board.levelCursor.getCursory() - ver.y);
        return x + y;
    }
    
}