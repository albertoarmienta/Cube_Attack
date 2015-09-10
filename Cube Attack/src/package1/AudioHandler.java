/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package package1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Spiggoingio
 */
public class AudioHandler {
    Clip songClip;

    
    public void playSelectSound() {
        
        try{
            URL sound1 = getClass().getResource("select.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(sound1);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start( );
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void playSwapSound() {
        
        try{
            URL sound1 = getClass().getResource("swap.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(sound1);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start( );
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void playMoveSound() {
        
        try{
            URL sound1 = getClass().getResource("move.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(sound1);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start( );
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public Clip playTitleSong() {
       
        try{
            URL sound1 = getClass().getResource("Thallium.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(sound1);
            songClip = AudioSystem.getClip();
            songClip.open(audioInputStream);
            songClip.loop(-1); //infinite
            songClip.start( );
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
      return songClip;     
    }
    public Clip playGameSong1() {
       
        try{
            URL sound1 = getClass().getResource("Hydrogen.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(sound1);
            songClip = AudioSystem.getClip();
            songClip.open(audioInputStream);
            songClip.loop(-1); //infinite
            songClip.start( );
            
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return songClip;
               
    }
    public void stopSong(Clip c){
        c.stop();
        c.close();
    }
    

}
