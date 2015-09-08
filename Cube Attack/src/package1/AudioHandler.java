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
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author Spiggoingio
 */
public class AudioHandler {
    InputStream in;
    AudioStream as;
    AudioStream as2;
    AudioStream as3;
    AudioStream as4;

    
    public void playSound1() {
        
        try{
            URL sound1 = getClass().getResource("test.wav");
            System.out.println("defaultSound " + sound1);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(sound1);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start( );
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
