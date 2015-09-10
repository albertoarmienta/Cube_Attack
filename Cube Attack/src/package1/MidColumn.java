/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package package1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MidColumn extends JPanel {
    public static int WIDTH = GUIPanel.WIDTH/5,HEIGHT = GUIPanel.HEIGHT;
    private GUIPanel mainPanel;
    public int minutes  = 0;
    public int seconds  = 0;
    public int mSeconds = 0;
    private int DECREASE_TIME_INTERVAL = 100;//milliseconds
    int LComboStreak;
    int RComboStreak;
    private ImageIcon backgroundIcon;
    private Image background;
    public MidColumn(GUIPanel gui, int mode){
        mainPanel = gui;
        if(mode == 1){
            backgroundIcon = new ImageIcon(getClass().getResource("BANNER.png"));
        }else if(mode==2){
            backgroundIcon = new ImageIcon(getClass().getResource("BANNER2.png"));
        }
        WIDTH = GUIPanel.WIDTH/5+(Board.OFFSET*2);
        background = backgroundIcon.getImage();
        decreaseTime();
    }
    private void decreaseTime() {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(mSeconds < 10)
                    mSeconds ++;
                else
                {
                    mSeconds = 0;
                    if(seconds < 59){
                        seconds++;
                        repaint();
                    }
                    else
                    {
                        seconds = 0;
                        minutes ++;
                        repaint();
                    }
                }
            }
        }, 0, DECREASE_TIME_INTERVAL, TimeUnit.MILLISECONDS);
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(background, 0, 0,WIDTH,HEIGHT, this);
        g.setFont(new Font("Courier New", Font.BOLD, 18));
        g.setColor(Color.white);
        String time = "Time: " + minutes + ":";
        if(seconds<10)
            time+= "0";
        time+= seconds;
        g.drawString(time, (WIDTH/2)-55,HEIGHT-50);
        g.drawString("P1 Combo x" + mainPanel.b1.comboStreak,(WIDTH/2)-60,HEIGHT-150);
        g.drawString("P2 Combo x" + mainPanel.b2.comboStreak,(WIDTH/2)-60,HEIGHT-100);
    }
}
